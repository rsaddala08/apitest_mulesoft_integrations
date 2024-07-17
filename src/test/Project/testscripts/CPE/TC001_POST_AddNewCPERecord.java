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

import java.util.HashSet;
import java.util.Set;

public class TC001_POST_AddNewCPERecord extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String emailValues;
	static String requestBody;
	static JsonPath jsonPath;
	static String testCaseDescription="";
	@Test
	public void addCpeRecord() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		tcId="TC004";
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			emailValues =xls.getCellData(testSheetName, "Email_Data", rowNum);
			requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, emailValues);
			Response response = postRequest(endpoint, requestBody);
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ApiReports.validateContactPointData(response);
				ApiUtils.writeResponseData(response);
				ApiReports.validateMultipleCpeRecords(response,Constants.EMAIL);
				ReportUtil.markPassed("************************* TestCase End **********************");
			} else {
				ApiReports.errorResponse(response);
			}
		}
		}
}