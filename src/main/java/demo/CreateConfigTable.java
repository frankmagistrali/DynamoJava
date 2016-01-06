package demo;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateTableSpec;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;

/**
 * Created by fmagis on 1/5/16.
 */
public class CreateConfigTable {

    static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
            new ProfileCredentialsProvider()));

    static String configTableName = "EddConfig";

    public static void main(String[] args) {
        deleteTable(configTableName);

        // Parameter1: table name // Parameter2: reads per second //
        // Parameter3: writes per second // Parameter4/5: partition key and data type
        // Parameter6/7: sort key and data type (if applicable)

        createTable(configTableName, 10L, 5L, "Id", "S");
        addSecondaryIndex(configTableName);

    }

    private static void addSecondaryIndex(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            // define a global secondary index
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("ConfigType")
                    .withAttributeType("S"));

            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("CountryCode")
                    .withAttributeType("S"));

            CreateGlobalSecondaryIndexAction createGlobal = new CreateGlobalSecondaryIndexAction().withIndexName("TypeAndCountryIndex")
                    .withKeySchema(
                            new KeySchemaElement().withAttributeName("ConfigType").withKeyType(KeyType.HASH),  //Partition key
                            new KeySchemaElement().withAttributeName("CountryCode").withKeyType(KeyType.RANGE))  //Sort key
                    .withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY))
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(10L)
                            .withWriteCapacityUnits(5L));

            GlobalSecondaryIndexUpdate update = new GlobalSecondaryIndexUpdate().withCreate(createGlobal);
            ArrayList<GlobalSecondaryIndexUpdate> updates = new ArrayList<GlobalSecondaryIndexUpdate>();
            updates.add(update);

            final UpdateTableSpec updateTableSpec = new UpdateTableSpec().withGlobalSecondaryIndexUpdates(updates);
            updateTableSpec.withAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing UpdateTable request for " + tableName);
            table.updateTable(updateTableSpec);
            System.out.println("Waiting for " + tableName
                    + " to be updated...this may take a while...");
            table.waitForActive();

        } catch (Exception e) {
            System.err.println("UpdateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }

    }

    private static void createTable(
            String tableName, long readCapacityUnits, long writeCapacityUnits,
            String partitionKeyName, String partitionKeyType) {

        createTable(tableName, readCapacityUnits, writeCapacityUnits,
                partitionKeyName, partitionKeyType, null, null);
    }

    private static void createTable(
            String tableName, long readCapacityUnits, long writeCapacityUnits,
            String partitionKeyName, String partitionKeyType,
            String sortKeyName, String sortKeyType) {

        try {

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement()
                    .withAttributeName(partitionKeyName)
                    .withKeyType(KeyType.HASH)); //Partition key

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName(partitionKeyName)
                    .withAttributeType(partitionKeyType));

            if (sortKeyName != null) {
                keySchema.add(new KeySchemaElement()
                        .withAttributeName(sortKeyName)
                        .withKeyType(KeyType.RANGE)); //Sort key
                attributeDefinitions.add(new AttributeDefinition()
                        .withAttributeName(sortKeyName)
                        .withAttributeType(sortKeyType));
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput( new ProvisionedThroughput()
                            .withReadCapacityUnits(readCapacityUnits)
                            .withWriteCapacityUnits(writeCapacityUnits));

            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable request for " + tableName);
            Table table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName
                    + " to be created...this may take a while...");
            table.waitForActive();

        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    private static void deleteTable(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();
            System.out.println("Waiting for " + tableName
                    + " to be deleted...this may take a while...");
            table.waitForDelete();

        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
}
