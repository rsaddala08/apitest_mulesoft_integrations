package testscripts.CPP;

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

public class TC02_SMOKE_PUT_EditCPPRecord extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String phoneValues;
	static String requestBody;
	static JsonPath jsonPath;
	static String testCaseDescription="";
	@Test
	public void updateMultipleCppRecord() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			phoneValues =xls.getCellData(testSheetName, "Phone_Data", rowNum);
			requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, phoneValues);
			Response response = putRequest(endpoint, requestBody);
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ApiUtils.writeResponseData(response);
				ApiReports.validateContactPointData(response);
				ApiReports.validateMultipleCppRecord(response,Constants.PHONE_NUMBER);
				ReportUtil.markPassed("************************* TestCase End **********************");
			} else {
				ApiReports.errorResponse(response);
			}
		}
	}
}