package demo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created by fmagis on 1/5/16.
 */

@DynamoDBTable(tableName="EddConfig")
public class ConfigObject {
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

    @Override
    public String toString() {
        return "ConfigObject{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", overrideDate='" + overrideDate + '\'' +
                '}';
    }
}
