package com.core_engine.api.lib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.CommonUtils;
import com.core_engine.utilities.Constants;
import com.core_engine.utilities.ResponseCodeFactory;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtils extends HttpUtils {

    public static File requestData = null;
    public static FileWriter fileWriter = null;
    static JSONParser parser = new JSONParser();
    public static JSONObject jsonObject;
    public static String requestBody = "";
    public static JSONObject accountObject = null;
    public static JSONObject contactObject = null;
    public static JSONObject householdObject = null;
    public static String effectiveDate = "";
    public static int responseCode = 0;
    public static Response responseBody;
   // public static String requestFilePath = System.getProperty("user.dir") + "/Json_Templates/" + testSheetName + ".json";

    public static String requestFilePath = "";
    public static String accountMatchPath = System.getProperty("user.dir") + "/Json_Templates/AccountMatch.json";

    /**
     * @param URI
     * @param response
     */
    public static void validateResponseHeader(String URI, Response response) {
        if (response.header("content-type").contains("application/json")) {
            ReportUtil.markPassed("Response header is correctly validated");
        } else {
            ReportUtil.markFailed("Reponse header is not validated");
        }
    }

    /**
     * @param response
     * @return
     */
    public static long getResponseTime(Response response) {
        return response.getTime();
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static JSONObject generateRequestBody() {
        JSONObject json = new JSONObject();
        String random = CommonUtils.getRandomString(6);
        json.put("name", random);
        json.put("gender", "male");
        json.put("email", random + "@email.com");
        json.put("status", "active");
        return json;
    }

    /**
     * @return
     */
    public static JSONObject updateRequestBody(JSONObject requestBody) {
        JSONObject json = new JSONObject();
        requestBody.forEach((key, value) -> {
            if (key.equals("name")) {
                json.put(key, value + "_UPDATED");
            }
        });
        return json;
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String generateAssociationRequestBody() {
        JSONObject json = new JSONObject();
        String System_Id = "";
        String jsonRequest = "";
        if (testCaseId.equals("TC001")) {
            System_Id = CommonUtils.getRandomNumber();
            CommonUtils.setProperty("SYSTEM_ID", System_Id);
            json.put("userId", xls.getCellData(testSheetName, "USER_ID", rowNum));
            json.put("systemId", System_Id);
            json.put("producerIdentifier", xls.getCellData(testSheetName, "Producer_Identifier", rowNum));
            json.put("assosciationReason", xls.getCellData(testSheetName, "Assosciation_Reason", rowNum));
        } else {
            json.put("userId", xls.getCellData(testSheetName, "USER_ID", rowNum));
            json.put("systemId", xls.getCellData(testSheetName, "System_ID", rowNum));
            json.put("producerIdentifier", xls.getCellData(testSheetName, "Producer_Identifier", rowNum));
            json.put("assosciationReason", xls.getCellData(testSheetName, "Assosciation_Reason", rowNum));
        }
        return jsonRequest;
    }

    public static String jsonFilePath(){
        if (DriverScript.testSheetName.equalsIgnoreCase("CPP")||DriverScript.testSheetName.equalsIgnoreCase("CPE")
            ||DriverScript.testSheetName.equalsIgnoreCase("ACCOUNT")) {
            requestFilePath = System.getProperty("user.dir") + "/Json_Templates/" + testSheetName + ".json";
        }
        return requestFilePath;
    }

    @SuppressWarnings("unchecked")
	public static String contactPointJsonRequest(String userData, String accountData, String contactData) {
        try {
            requestFilePath=jsonFilePath();
            requestData = new File(requestFilePath);
            Object obj = parser.parse(new FileReader(requestData));
            jsonObject = (JSONObject) obj;
            accountObject = (JSONObject) jsonObject.get("account");
            requestBody = jsonObject.toString();
            effectiveDate = generateDateValue("effectiveDate");
            Constants.PARTY_VESRION = getCDHVersion(SYSTEM_ID);

            //To Pass UserData
            if (!userData.isEmpty()) {
                String[] userValues = userData.split(",");
                jsonObject.put("userId", userValues[0]);
                jsonObject.put("updateDate", userValues[1]);
            }
            //To Pass Account Data
            if (!accountData.isEmpty()) {
                accountObject.put("recordType", accountData);
            }
            accountObject.put("systemId", SYSTEM_ID);
            accountObject.put("version", Constants.PARTY_VESRION);
            accountObject.put("externalId", Constants.ACCOUNT_ID);
            accountObject.put("effectiveDate", effectiveDate);
            //To Pass HouseholdData
            householdObject = (JSONObject) accountObject.get("household");
            householdObject.put("externalId", Constants.ACCOUNT_ID);
            householdObject.put("id", Constants.ACCOUNT_ID);
            //To Pass PhoneData
            String[] contactValues = contactData.split(",");
            if (DriverScript.testSheetName.equals("CPP")) {
                contactObject = (JSONObject) accountObject.get("phone");
                contactObject.put("phoneNumber", contactValues[2]);
                Constants.PHONE_NUMBER = contactValues[2].toString();
            } else {
				contactObject = (JSONObject) accountObject.get("email");
                System.out.println(contactValues[2]);
                contactObject.put("email", contactValues[2]);
                Constants.EMAIL = contactValues[2].toString();
            }
            contactObject.put("id", contactValues[0]);
            Constants.ID = contactValues[0].toString();

            contactObject.put("isPrimary", Boolean.valueOf(contactValues[1]));
            contactObject.put("usage", contactValues[3]);
            Constants.USAGE_TYPE = contactValues[3].toString();
            contactObject.put("specialInstructions", contactValues[4]);
            Constants.SPECIALINSTRUCTIONS = contactValues[4].toString();
            if (contactValues[3].equalsIgnoreCase("OTHER")) {
                contactObject.put("comment", contactValues[5]);
                Constants.COMMENTS = contactValues[5].toString();
            } else {
                contactObject.put("comment", contactValues[5]);
                Constants.COMMENTS = contactValues[5].toString();
            }
            if (!contactValues[6].isEmpty()) {
                contactObject.put("specialInstructionsManaged", Boolean.valueOf(contactValues[6]));
            } else {
                contactObject.put("specialInstructionsManaged", false);
            }
            try {
                if (contactObject.get("records").toString().equalsIgnoreCase(null)) ;
            } catch (NullPointerException n) {
                JSONArray array = new JSONArray();
                contactObject.put("records", new JSONArray());
            }

            if (!requestBody.contains("\"effectiveDate\"")) {
                accountObject.put("effectiveDate", effectiveDate);
            }
            if (requestBody.contains("\"updated\"")) {
                requestBody = requestBody.replace(",\"updated\":true", "");
                requestBody = requestBody.replace(",\"updated\":false", "");
            }
            requestBody = jsonObject.toString();
            fileWriter = new FileWriter(requestData);
            fileWriter.write(requestBody);
            fileWriter.flush();
            fileWriter.close();

        } catch (Throwable t) {
            ReportUtil.markFailed("RequestCreationisFailedwith:" + t.getMessage());
            ReportUtil.markException(t.getMessage());

        }
        return requestBody;
    }
	public static String outboundAccountJsonRequest(String userData,String accountType, String accountData) {
		try {
            requestFilePath=jsonFilePath();
			requestData = new File(requestFilePath);
			Object obj = parser.parse(new FileReader(requestData));
			jsonObject = (JSONObject) obj;
			accountObject = (JSONObject) jsonObject.get("account");
			requestBody = jsonObject.toString();
			effectiveDate = generateDateValue("effectiveDate");
			Constants.PARTY_VESRION = getCDHVersion(SYSTEM_ID);

			//To Pass UserData
			if (!userData.isEmpty()) {
				String[] userValues = userData.split(",");
				jsonObject.put("userId", userValues[0]);
				jsonObject.put("updateDate", userValues[1]);
				jsonObject.put("applyAccountMatch",Boolean.valueOf(userValues[2]));
			}
			//To Pass Account Data
			accountObject.put("recordType", accountType);
			accountObject.put("systemId", SYSTEM_ID);
			accountObject.put("version", Constants.PARTY_VESRION);
			accountObject.put("externalId", Constants.ACCOUNT_ID);
			accountObject.put("effectiveDate", effectiveDate);

			if(accountData.contains(",")) {
				String accValues[] =accountData.split(",");
				for (int i=0;i<accValues.length;i++){
                    if(accValues[i].equalsIgnoreCase("firstName")||accValues[i].equalsIgnoreCase("middleName")||
                            accValues[i].equalsIgnoreCase("lastName")||accValues[i].equalsIgnoreCase("accountName")){
                      String name = CommonUtils.getRandomName(accValues[i]);
                        accountObject.put(accValues[i], name);
                    }else{
				String[] account=accValues[i].split("_");
                if(account[0].equalsIgnoreCase("deceasedFlag")||account[0].equalsIgnoreCase("dissolvedFlag")){
                    accountObject.put(account[0], Boolean.valueOf(account[1]));
                }else {
                    accountObject.put(account[0], account[1]);
                }
				}}
			}else {
				String[] account=accountData.split("_");
				accountObject.put(account[0],account[1]);
			}
			requestBody = jsonObject.toString();
			fileWriter = new FileWriter(requestData);
			fileWriter.write(requestBody);
			fileWriter.flush();
			fileWriter.close();

		} catch (Throwable t) {
			ReportUtil.markFailed("RequestCreationisFailedwith:" + t.getMessage());
            ReportUtil.markException(t.getMessage());

		}
		return requestBody;
	}


	public static Response deleteMultipleRecords(Response response) {
        try {
            if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
                ApiReports.validateContactPointData(response);
                writeResponseData(response);
                JsonPath jsonPath = response.jsonPath();
                int count = ApiReports.getRecordsCount();
                int i;
                for (i = 0; i <= count; i++) {
                    if (count == i) {
                        System.out.println("Iteration completed");
                        break;
                    }
                    requestData = new File(requestFilePath);
                    Object obj = parser.parse(new FileReader(requestData));
                    jsonObject = (JSONObject) obj;
                    accountObject = (JSONObject) jsonObject.get("account");
					if (DriverScript.testSheetName.equals("CPP")) {
						contactObject = (JSONObject) accountObject.get("phone");
						contactObject.put("id", jsonPath.getString("account.phone.records[" + i + "].id"));
						contactObject.put("phoneNumber", jsonPath.getString("account.phone.records[" + i + "].phoneNumber"));
						contactObject.put("usage", jsonPath.getString("account.phone.records[" + i + "].usage"));
						contactObject.put("specialInstructions", jsonPath.getString("account.phone.records[" + i + "].specialInstructions"));
						contactObject.put("isPrimary", Boolean.valueOf(jsonPath.getString("account.phone.records[" + i + "].isPrimary")));
						contactObject.put("comment", jsonPath.getString("account.phone.records[" + i + "].comment"));
						contactObject.put("specialInstructionsManaged", false);
						Constants.PHONE_NUMBER = jsonPath.getString("account.phone.records[" + i + "].phoneNumber").toString();
						Constants.USAGE_TYPE = jsonPath.getString("account.phone.records[" + i + "].usage");
					} else {
						contactObject = (JSONObject) accountObject.get("email");
						contactObject.put("id", jsonPath.getString("account.email.records[" + i + "].id"));
						contactObject.put("email", jsonPath.getString("account.email.records[" + i + "].email"));
						contactObject.put("usage", jsonPath.getString("account.email.records[" + i + "].usage"));
						contactObject.put("specialInstructions", jsonPath.getString("account.email.records[" + i + "].specialInstructions"));
						contactObject.put("isPrimary", Boolean.valueOf(jsonPath.getString("account.email.records[" + i + "].isPrimary")));
						contactObject.put("comment", jsonPath.getString("account.email.records[" + i + "].comment"));
						contactObject.put("specialInstructionsManaged", false);
						Constants.EMAIL = jsonPath.getString("account.email.records[" + i + "].email").toString();
						Constants.USAGE_TYPE = jsonPath.getString("account.email.records[" + i + "].usage");
					}

                    //To Pass Account Data
                    accountObject = (JSONObject) jsonObject.get("account");
                    effectiveDate = generateDateValue("effectiveDate");
                    accountObject.put("effectiveDate", effectiveDate);
                    Constants.PARTY_VESRION = getCDHVersion(SYSTEM_ID);
                    accountObject.put("version", Constants.PARTY_VESRION);

                    requestBody = jsonObject.toString();
                    response = deleteRequest(endpoint, requestBody);
                    response.prettyPrint();
                    if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
                        writeResponseData(response);
                        ApiReports.validateContactPointData(response);
                    }
                }
            }
        } catch (Throwable t) {
            ReportUtil.markFailed(response.prettyPrint());
			ReportUtil.markException(t.getMessage());
        }
        return response;
    }

    public static void writeResponseData(Response response) {
        try {
            Constants.RESPONSE_BODY = response.asString();
            requestData = new File(requestFilePath);
            fileWriter = new FileWriter(requestData);
            fileWriter.write(Constants.RESPONSE_BODY);
            fileWriter.flush();
            fileWriter.close();

        } catch (Throwable t) {
            t.printStackTrace();
            ReportUtil.markFailed("Write Response Body is Failed with: " + t.getMessage());
        }
    }
    public static void writeAccMatchedResponseData(Response response) {
        try {
            Constants.RESPONSE_BODY = response.asString();
            requestData = new File(accountMatchPath);
            fileWriter = new FileWriter(requestData);
            fileWriter.write(Constants.RESPONSE_BODY);
            fileWriter.flush();
            fileWriter.close();

        } catch (Throwable t) {
            t.printStackTrace();
            ReportUtil.markFailed("Write Response Body is Failed with: " + t.getMessage());
        }
    }
    public static int addMultipleRequestData(String userValues, String accountValues, String inputData) {
        try {
            switch (testSheetName) {
                case "CPP":
                    responseCode = multipleRecordsData(userValues, accountValues, inputData);
                    break;
                case "CPE":
                    responseCode = multipleRecordsData(userValues, accountValues, inputData);
                    break;
                case "ADDRESS":
                    responseCode = multipleRecordsData(userValues, accountValues, inputData);
                    break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return responseCode;
    }

    public static int multipleRecordsData(String userVal, String accVal, String inputRequestData) {
        try {
            if (inputRequestData.contains("|")) {
                String[] records = inputRequestData.split("\\|");
                for (int i = 0; i <= records.length - 1; i++) {
                    requestBody = contactPointJsonRequest(userVal, accVal, records[i]);
                    Response response = postRequest(endpoint, requestBody);
                    responseCode = response.getStatusCode();
                    if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
                        ApiReports.validateContactPointData(response);
                        responseBody = response;
                        writeResponseData(response);
                    } else {
                        ReportUtil.markFailed("Request is failed with:  " + response.getStatusCode() + ":" + response.getStatusLine());
                        ReportUtil.markFailed(response.prettyPrint());
                    }
                }
            }
        } catch (Throwable t) {
            ReportUtil.markFailed("Exception:" + t.getMessage());
			ReportUtil.markException(t.getMessage());
        }
        return responseCode;
    }

    public static String getCDHVersion(String CDHID) {
        String cdhEndpoint = "";
        try {
            RequestSpecification request = RestAssured.given();
            String apexEnv = CommonUtils.getProperty("APEX_ENV");
            if (apexEnv.equalsIgnoreCase("INT1")) {
                cdhEndpoint = ("https://intbj25.amfam.com/partyrestv03Service/party/v3/parties/" + CDHID + "?authId=macsrvt&levelOfDetails=EMAILS&levelOfDetails=PHONES&levelOfDetails=CONFIDENTIAL&levelOfDetails=DEMOGRAPHIC&levelOfDetails=ADDRESSES").replace("customerIdNumber", CDHID);
            } else {
                cdhEndpoint = ("https://intbj25.amfam.com/partyrestv03Service-01/party/v3/parties/" + CDHID + "?authId=macsrvt&levelOfDetails=EMAILS&levelOfDetails=PHONES&levelOfDetails=CONFIDENTIAL&levelOfDetails=DEMOGRAPHIC&levelOfDetails=ADDRESSES").replace("customerIdNumber", CDHID);
            }
            Response response = request
                    .header("AFI-UserId", "macsrvt")
                    .header("AFI-AppName", "CDH")
                    .header("Authorization", "Basic cWFsNDAxOnFhbDQwMQ==")
                    .get(cdhEndpoint);
            response.prettyPrint();
            if (response.getStatusCode() == 200) {
                JsonPath jsonPath = response.jsonPath();
                Constants.PARTY_VESRION = jsonPath.getString("party.partyVersion");
                System.out.println("Party Version is:  " + Constants.PARTY_VESRION);
            }
        } catch (Throwable t) {
            ReportUtil.markFailed(t.getMessage());
            ReportUtil.markException(t.getMessage());
        }
        return Constants.PARTY_VESRION;
    }

    public static String generateDateValue(String newDate) {
        try {
            switch (newDate) {
                case "updateDate":
                    break;
                case "effectiveDate":
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    newDate = dateFormat.format(date);
                    break;
                default:
                    System.out.println("No Date");
                    break;
            }

        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        return newDate;
    }


}