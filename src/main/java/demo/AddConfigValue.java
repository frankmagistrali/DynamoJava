package demo;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created by fmagis on 1/5/16.
 */
public class AddConfigValue {
    static AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());

    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);

        ConfigObject configObject = new ConfigObject();
        configObject.setId("Feb28Disable");
        configObject.setType("Disable");
        configObject.setStartDate("2016-01-28");
        configObject.setEndDate("2016-01-28");
        configObject.setCountryCode("US");
        mapper.save(configObject);
    }
}
