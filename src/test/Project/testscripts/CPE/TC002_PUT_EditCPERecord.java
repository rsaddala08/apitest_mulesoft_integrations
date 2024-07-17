package testscripts.CPE;

import com.core_engine.api.lib.ApiReports;
import com.core_engine.api.lib.ApiUtils;
import com.core_engine.api.lib.HttpUtils;
import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.Constants;
import com.core_engine.utilities.ResponseCodeFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TC002_PUT_EditCPERecord extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String emailValues;
	static String requestBody;
	static JsonPath jsonPath;
	static String testCaseDescription="";
	@Test
	public void editCpeRecord() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		tcId="TC005";
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			emailValues =xls.getCellData(testSheetName, "Email_Data", rowNum);
			requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, emailValues);
			Response response = putRequest(endpoint, requestBody);
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ApiUtils.writeResponseData(response);
				ApiReports.validateContactPointData(response);
				ApiReports.validateMultipleCpeRecords(response,Constants.EMAIL);
				ReportUtil.markPassed("************************* TestCase End **********************");
			} else {
				ApiReports.errorResponse(response);
			}
		}
	}
}