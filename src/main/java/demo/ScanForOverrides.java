package demo;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

import java.util.List;

/**
 * Created by fmagis on 1/5/16.
 */
public class ScanForOverrides {
    static AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String configTableName = "EddConfig";

    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        DynamoDBScanExpression expression = new DynamoDBScanExpression();

        expression.addFilterCondition("ConfigType",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withS("Override")));

        expression.addFilterCondition("CountryCode",
                new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ)
                    .withAttributeValueList(new AttributeValue().withS("US")));

        final List<ConfigObject> queryResults = mapper.scan(ConfigObject.class, expression);

        for (ConfigObject config : queryResults) {
            System.out.println(config);
        }
    }
}
