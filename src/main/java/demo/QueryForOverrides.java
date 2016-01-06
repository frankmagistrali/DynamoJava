package demo;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;

import java.util.List;

/**
 * Created by fmagis on 1/5/16.
 */
public class QueryForOverrides {
    static AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String configTableName = "EddConfig";

    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        final Table table = dynamoDB.getTable(configTableName);
        DynamoDBQueryExpression<Config> expression = new DynamoDBQueryExpression<Config>().withIndexName("TypeAndCountryIndex");
        Config configKey = new Config();
        configKey.setType("Override");
        expression.withHashKeyValues(configKey);
        expression.setConsistentRead(false);

        final List<Config> queryResults = mapper.query(Config.class, expression);

        System.out.println(queryResults);
    }

//    public static void main(String[] args) {
//        final Table table = dynamoDB.getTable(configTableName);
//        QueryRequest queryRequest = new QueryRequest(configTableName);
//        DynamoDBQueryExpression expression = new DynamoDBQueryExpression().withIndexName()
//        queryRequest.setIndexName("TypeAndCountryIndex");
//        queryRequest.set();
//        ScanRequest scanRequest = new ScanRequest().withTableName(configTableName);
//        scanRequest.
//                client.scan()
//        final ItemCollection<ScanOutcome> scan = table.scan("CountryCode=US", null, null);
////        final ItemCollection<QueryOutcome> query = table.query("Type", "Override");
//        System.out.println(scan.firstPage().iterator());
//    }

    @DynamoDBTable(tableName="EddConfig")
    public static class Config {
        private String id;
        private String type;
        private String countryCode;
        private String startDate;
        private String endDate;
        private String overrideDate;

        //Partition key
        @DynamoDBHashKey(attributeName="Id")
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

//        //Range key
//        @DynamoDBRangeKey(attributeName="ConfigType")
//        public String getReplyDateTime() { return replyDateTime; }
//        public void setReplyDateTime(String replyDateTime) { this.replyDateTime = replyDateTime; }

        @DynamoDBIndexHashKey(attributeName="ConfigType", globalSecondaryIndexName = "TypeAndCountryIndex")
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        @DynamoDBAttribute(attributeName="CountryCode")
        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode;}

        @DynamoDBAttribute(attributeName="StartDate")
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate;}

        @DynamoDBAttribute(attributeName="EndDate")
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate;}

        @DynamoDBAttribute(attributeName="OverrideDate")
        public String getOverrideDate() { return overrideDate; }
        public void setOverrideDate(String overrideDate) { this.overrideDate = overrideDate;}
    }
}
