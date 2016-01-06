package demo;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

/**
 * Created by fmagis on 1/5/16.
 */
public class PopulateConfigTable {
    static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(
            new ProfileCredentialsProvider()));

    static String configTableName = "EddConfig";

    public static void main(String[] args) {
        loadInitialConfigs(configTableName);

    }

    private static void loadInitialConfigs(String tableName) {

        Table table = dynamoDB.getTable(tableName);

        try {

            System.out.println("Adding data to " + tableName);

            Item item = new Item()
                    .withPrimaryKey("Id", "Jan10Override")
                    .withString("ConfigType", "Override")
                    .withString("CountryCode", "US")
                    .withString("StartDate", "2016-01-01")
                    .withString("EndDate", "2016-01-10")
                    .withString("OverrideDate", "2016-01-10");
            table.putItem(item);

            item = new Item()
                    .withPrimaryKey("Id", "Jan12Disable")
                    .withString("ConfigType", "Disable")
                    .withString("CountryCode", "US")
                    .withString("StartDate", "2016-01-12")
                    .withString("EndDate", "2016-01-14");
            table.putItem(item);

            item = new Item()
                    .withPrimaryKey("Id", "Jan15Override")
                    .withString("ConfigType", "Override")
                    .withString("CountryCode", "US")
                    .withString("StartDate", "2016-01-13")
                    .withString("EndDate", "2016-01-15")
                    .withString("OverrideDate", "2016-01-17");
            table.putItem(item);

            item = new Item()
                    .withPrimaryKey("Id", "Jan15OverrideChina")
                    .withString("ConfigType", "Override")
                    .withString("CountryCode", "CN")
                    .withString("StartDate", "2016-01-13")
                    .withString("EndDate", "2016-01-15")
                    .withString("OverrideDate", "2016-01-17");
            table.putItem(item);

        } catch (Exception e) {
            System.err.println("Failed to create item in " + tableName);
            System.err.println(e.getMessage());
        }

    }
}
