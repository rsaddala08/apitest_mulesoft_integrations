package com.core_engine.api.lib;

import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.Constants;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class ApiReports {
    public static String phoneNumber = "";
    public static String UsageType = "";
    public static String SpecialInstructions = "";
    public static String IsPrimary = "";
    public static String Comment = "";
    public static String ID = "";
    public static String UpdateFlag = "";

    public static JsonPath jsonPath ;
    public static String allRecords="";

    static JSONParser parser = new JSONParser();

    public static int getRecordsCount(){
        int recordsCount=0;

        try{
            JSONParser parser = new JSONParser();
            JSONObject childObject=null;
            File file =new File(ApiUtils.requestFilePath);
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            JSONObject parentObject =  (JSONObject)jsonObject.get("account");
            if(DriverScript.testSheetName.equalsIgnoreCase("CPP")) {
                childObject  = (JSONObject) parentObject.get("phone");
            }
            else if(DriverScript.testSheetName.equalsIgnoreCase("CPE")) {
                childObject  = (JSONObject) parentObject.get("email");
            }
            else{
                childObject  = (JSONObject) parentObject.get("address");
            }
            JSONArray jsonArray = (JSONArray)childObject.get("records");
            recordsCount=jsonArray.size();
            ReportUtil.markInfo("No.of records are available in the object :  "+ recordsCount);
        }catch (Throwable t){
            t.getMessage();
        }
        return recordsCount;
    }
    public static void validateMultipleCppRecord(Response response, String inputValue) {
        jsonPath = response.jsonPath();
        allRecords = jsonPath.getString("account.phone.records");
        int count = getRecordsCount();
        int i;
        for (i = 0; i <= allRecords.length(); i++) {
            if (count == i) {
                System.out.println("Iteration completed");
                break;
            }
            ID = jsonPath.getString("account.phone.records[" + i + "].id");
            phoneNumber = jsonPath.getString("account.phone.records[" + i + "].phoneNumber");
            UsageType = jsonPath.getString("account.phone.records[" + i + "].usage");
            SpecialInstructions = jsonPath.getString("account.phone.records[" + i + "].specialInstructions");
            IsPrimary = jsonPath.getString("account.phone.records[" + i + "].isPrimary");
            Comment = jsonPath.getString("account.phone.records[" + i + "].comment");
            UpdateFlag = jsonPath.getString("account.phone.records[" + i + "].updated");
            if (inputValue.equalsIgnoreCase(phoneNumber)) {
                ReportUtil.markPassed("Validated CHD response should shows that: "+"ID:"+ID+" ,PhoneNumber:"+phoneNumber+" ,UsageType:"+UsageType+"," +
                        "IsPrimary:  " +IsPrimary+" ,SpecialInstructions :  " +SpecialInstructions+" ,Comment: " +Comment+" ,Update flag is: "+UpdateFlag);
             }else {
                ReportUtil.markInfo("Existing CPP record details should shows the phone number: " + phoneNumber + ", Usage type is:  " + UsageType + "," +
                        "IsPrimary flag is:  " + IsPrimary+",SpecialInstructions is :  " + SpecialInstructions+",Comment Value is: " + Comment+", Update Flag Value is:  " + UpdateFlag);     }
            continue;
        }

    }

    public static void validateMultipleCpeRecords(Response response, String inputValue) {
        jsonPath = response.jsonPath();
        allRecords = jsonPath.getString("account.email.records");
        int count = getRecordsCount();
        int i;
        for (i = 0; i <= allRecords.length(); i++) {
            if (count == i) {
                System.out.println("Iteration completed");
                break;
            }
            ID = jsonPath.getString("account.email.records[" + i + "].id");
            String emailAddress = jsonPath.getString("account.email.records[" + i + "].email");
            UsageType = jsonPath.getString("account.email.records[" + i + "].usage");
            SpecialInstructions = jsonPath.getString("account.email.records[" + i + "].specialInstructions");
            IsPrimary = jsonPath.getString("account.email.records[" + i + "].isPrimary");
            Comment = jsonPath.getString("account.email.records[" + i + "].comment");
            UpdateFlag = jsonPath.getString("account.email.records[" + i + "].updated");
            if (inputValue.equalsIgnoreCase(emailAddress)) {
                ReportUtil.markPassed("Validated CHD response should shows that: "+"ID:"+ID+" ,Email Address : "+emailAddress+" ,UsageType :  "+UsageType+"," +
                        "IsPrimary:  " +IsPrimary+" ,SpecialInstructions :  " +SpecialInstructions+" ,Comment : " +Comment+" ,Update flag is : "+UpdateFlag);
            }else {
                ReportUtil.markInfo("Existing CPP record details should shows the Email Address : " + emailAddress + ", Usage type is:  " + UsageType + "," +
                        "IsPrimary flag is:  " + IsPrimary+",SpecialInstructions is :  " + SpecialInstructions+",Comment Value is: " + Comment+", Update Flag Value is:  " + UpdateFlag);     }
            continue;
        }

    }

    public static void validateContactPointData(Response response){
        try{
            jsonPath = response.jsonPath();
            if (DriverScript.METHOD_NAME.equalsIgnoreCase("POST")){
                ReportUtil.markPassed("Added New Contact Point Phone Record Successfully!!");
                ReportUtil.markPassed("Request should success and the Status code is : " + response.getStatusCode());
                ReportUtil.markPassed("Total Response Time : " + response.getTime());
                ReportUtil.markPassed("CDHID is : " + jsonPath.getString("account.systemId")+"  , and Version is : "+ jsonPath.getString("account.version"));
                if(DriverScript.testSheetName.equalsIgnoreCase("CPP")) {
                    ReportUtil.markInfo("Added New Contact Point Phone Record: "+"ID : "+ Constants.ID+" ,PhoneNumber : "+Constants.PHONE_NUMBER+" ,UsageType : "+Constants.USAGE_TYPE+"," +
                            "IsPrimary :  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                    }
                else if(DriverScript.testSheetName.equalsIgnoreCase("CPE")) {
                    ReportUtil.markInfo("Added New Contact Point Email Record: "+"ID:"+ Constants.ID+" ,Email address : "+Constants.EMAIL+" ,UsageType : "+Constants.USAGE_TYPE+"," +
                            "IsPrimary:  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                    }
                else{
                    ReportUtil.markInfo("Added New Address Record: "+"ID:"+ Constants.ID+" ,PhoneNumber:"+Constants.PHONE_NUMBER+" ,UsageType:"+Constants.USAGE_TYPE+"," +
                            "IsPrimary:  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                }

            } else if (DriverScript.METHOD_NAME.equalsIgnoreCase("PUT")) {
                ReportUtil.markPassed("Existing contact point phone record has updated Successfully!!");
                ReportUtil.markPassed("Request should success and the Status code is : " + response.getStatusCode());
                ReportUtil.markPassed("Total Response Time : " + response.getTime());
                ReportUtil.markPassed("CDHID id : " + jsonPath.getString("account.systemId")+"  , and Version is : "+ jsonPath.getString("account.version"));
                if(DriverScript.testSheetName.equalsIgnoreCase("CPP")) {
                    ReportUtil.markInfo("Edited Existing Contact Point Phone Record: "+"ID:"+Constants.ID+" ,PhoneNumber : "+Constants.PHONE_NUMBER+" ,UsageType:"+Constants.USAGE_TYPE+"," +
                            "IsPrimary:  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                }
                else if(DriverScript.testSheetName.equalsIgnoreCase("CPE")) {
                    ReportUtil.markInfo("Edited Existing Contact Point Email Record: "+"ID:"+Constants.ID+" ,Email address : "+Constants.EMAIL+" ,UsageType:"+Constants.USAGE_TYPE+"," +
                            "IsPrimary:  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                }
                else{
                    ReportUtil.markInfo("Edited Existing Address Record: "+"ID:"+Constants.ID+" ,Address : "+Constants.PHONE_NUMBER+" ,UsageType:"+Constants.USAGE_TYPE+"," +
                            "Purpose:  " +Constants.IS_PRIMARY+" ,SpecialInstructions :  " +Constants.SPECIALINSTRUCTIONS+" ,Comment: " +Constants.COMMENTS);
                }

            }else {
                ReportUtil.markInfo("Deleted Existing Contact Point Phone Record: PhoneNumber:"+Constants.PHONE_NUMBER+" ,UsageType:"+Constants.USAGE_TYPE);
                ReportUtil.markPassed("Existing contact point phone record has deleted Successfully!!");
                ReportUtil.markPassed("Request should success and the Status code is : " + response.getStatusCode());
                ReportUtil.markPassed("Total Response Time : " + response.getTime());
                ReportUtil.markPassed("CDHID is : " + jsonPath.getString("account.systemId")+"  , and Version is : "+ jsonPath.getString("account.version"));

                if(DriverScript.testSheetName.equalsIgnoreCase("CPP")) {
                    ReportUtil.markPassed("Deleted Contact Point Phone is: " + Constants.PHONE_NUMBER+",  with Usage Type :  "+Constants.USAGE_TYPE);
                }
                else if(DriverScript.testSheetName.equalsIgnoreCase("CPE")) {
                    ReportUtil.markPassed("Deleted Contact Point Email is: " + Constants.EMAIL+",  with Usage Type :  "+Constants.USAGE_TYPE);
                }
                else{
                    ReportUtil.markPassed("Deleted Address is: " + Constants.PHONE_NUMBER+",  with Usage Type :  "+Constants.USAGE_TYPE);
                }

            }

        }catch (Throwable t){
            ReportUtil.markFailed(t.getMessage());
            ReportUtil.markException(t.getMessage());
        }

    }

    public static void validateAccountMatchedDetails(Response response) {
        try {
            File file =new File(ApiUtils.accountMatchPath);
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray)jsonObject.get("accountMatch");
            jsonPath = response.jsonPath();
            for (int i = 0; i <= jsonArray.size() - 1; i++) {
                String producerAssociationExists = jsonPath.getString("accountMatch[" + i + "].producerAssociationExists");
                ReportUtil.markInfo("CDHID is : " + jsonPath.getString("accountMatch[" + i + "].systemId") + "  , and Version is : " + jsonPath.getString("accountMatch[" + i + "].version"));
                String accountMatchType = jsonPath.getString("accountMatch[" + i + "].accountMatchType");
                String socialSecurityNumber = jsonPath.getString("accountMatch[" + i + "].socialSecurityNumber");
                String employerIdentificationNumber = jsonPath.getString("accountMatch[" + i + "].employerIdentificationNumber");
                String driversLicenseNumber = jsonPath.getString("accountMatch[" + i + "].driversLicenseNumber");
                String driversLicenseState = jsonPath.getString("accountMatch[" + i + "].driversLicenseState");
                ReportUtil.markPassed("Validated Required details are : " + "ProducerAssociationExists is :" + producerAssociationExists + " , AccountMatchType is : " + accountMatchType);
                ReportUtil.markPassed("Matched record fields  :  " + " SocialSecurityNumber : " + socialSecurityNumber + " , EmployerIdentificationNumber : " + employerIdentificationNumber + " , DriversLicenseNumber is: " + driversLicenseNumber + " , DriversLicenseState is: " + driversLicenseState);
                String addressline1 = jsonPath.getString("accountMatch[" + i + "].address.records[0].addressLine1");
                String city = jsonPath.getString("accountMatch[" + i + "].address.records[0].city");
                String state = jsonPath.getString("accountMatch[" + i + "].address.records[0].state");
                String purposeType = jsonPath.getString("accountMatch[" + i + "].address.records[0].purposeType");
                String usageType = jsonPath.getString("accountMatch[" + i + "].address.records[0].usageType");
                if (jsonPath.getString("accountMatch[" + i + "].address.records[0].country").equalsIgnoreCase("USA")) {
                    String postalCode = jsonPath.getString("accountMatch[" + i + "].address.records[0].postalCode");
                    ReportUtil.markPassed("Matched Account Address details are :" + "AddressLine1 :" + addressline1 + " ,City : " + city + " ,State : " + state + " ,Postcode : " + postalCode + " ,PurposeType is: " + purposeType + " ,UsageType is: " + usageType);
              } else {
                    String provinceTerritory = jsonPath.getString("accountMatch[" + i + "].address.records[0].provinceTerritory");
                    String foreignPostalCode = jsonPath.getString("accountMatch[" + i + "].address.records[0].foreignPostalCode");
                    ReportUtil.markPassed("Matched Account Address details are :" + "AddressLine1 : " + addressline1 + " ,City : "  + city + " ,State is: " + state + " ,ProvinceTerritory is: " + provinceTerritory + " ,ForeignPostalCode: " + foreignPostalCode + " ,PurposeType : " + purposeType + " ,UsageType is: " + usageType);
                }
            }

        }catch (Throwable t){
            System.out.println(t.getMessage());
             }
        }
    public static void validateNotMatchedAccountDetails(Response response, String inputValue) {
        jsonPath = response.jsonPath();
        allRecords = jsonPath.getString("account");
        ReportUtil.markPassed("CDHID is : " + jsonPath.getString("account.systemId")+"  , and Version is : "+ jsonPath.getString("account.version"));
        if(inputValue.contains(",")){
        String accValues[] =inputValue.split(",");
        for (int i=0;i<accValues.length;i++) {
            ReportUtil.markPassed("Validated Account details and "+  accValues[i]+" field should updated with : "+jsonPath.getString("account."+accValues[i]));
        }
        }else {
            ReportUtil.markPassed("Validated Account details and " + inputValue + " field should updated with : " + jsonPath.getString("account." + inputValue));
        }
    }

    public static void errorResponse(Response response){
        jsonPath=response.jsonPath();
        ReportUtil.markFailed("Request is failed with:  "  + response.getStatusCode()+ " :::: " +response.getStatusLine());
        ReportUtil.markFailed("Error Type is: "+ jsonPath.getString("errorType")+" ,  and Error message is: " + jsonPath.getString("message"));
        ReportUtil.markFailed("Error Response Body Is:  " + response.prettyPrint());
    }

    }



