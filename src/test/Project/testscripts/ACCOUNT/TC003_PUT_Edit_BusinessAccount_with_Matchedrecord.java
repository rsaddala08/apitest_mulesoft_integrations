package testscripts.ACCOUNT;

import com.core_engine.api.lib.ApiReports;
import com.core_engine.api.lib.ApiUtils;
import com.core_engine.api.lib.HttpUtils;
import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.ResponseCodeFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TC003_PUT_Edit_BusinessAccount_with_Matchedrecord extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String accountType;
	static String requestBody;
	static JsonPath jsonPath;
	static String testCaseDescription="";
	@Test
	public void editBusinessAccountMatchedRecord() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		tcId="TC009";
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountType =xls.getCellData(testSheetName, "Account_Type", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			requestBody = ApiUtils.outboundAccountJsonRequest(userValues, accountType, accountValues);
			Response response = putRequest(endpoint, requestBody);
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ReportUtil.markPassed("Request should success and the Status code is : " + response.getStatusCode());
				ReportUtil.markPassed("Total Response Time : " + response.getTime());
				ReportUtil.markPassed("Validate Code : "+jsonPath.getString("validationCode")+" , and Validation Message : "+jsonPath.getString("validationMessage"));
				ReportUtil.markPassed("MatchFlag should be :" +jsonPath.getString("matchFlag"));
				ApiUtils.writeAccMatchedResponseData(response);
				ApiReports.validateAccountMatchedDetails(response);
				ReportUtil.markPassed("************************* TestCase End **********************");
			} else {
				ApiReports.errorResponse(response);
			}
		}
	}
}