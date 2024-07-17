package testscripts.CPE;

import com.core_engine.api.lib.ApiReports;
import com.core_engine.api.lib.ApiUtils;
import com.core_engine.api.lib.HttpUtils;
import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.ResponseCodeFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TC003_DELETE_DeleteCPERecord extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String emailValues;
	static String requestBody;
	static JsonPath jsonPath;
	static String testCaseDescription="";
	@Test
	public void deleteCpeRecord() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		tcId="TC006";
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			emailValues =xls.getCellData(testSheetName, "Email_Data", rowNum);
			requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, emailValues);
			Response response = deleteRequest(endpoint, requestBody);
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ApiUtils.writeResponseData(response);
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ApiReports.validateContactPointData(response);
				ReportUtil.markPassed("Record has deleted and the records count is:  "+ApiReports.getRecordsCount());
				ReportUtil.markPassed("Record Array is Emapty:  "+jsonPath.getString("account.email.records"));
				ReportUtil.markPassed("Check the Response for Deleted record:  \n"+response.prettyPrint());
				ReportUtil.markPassed("************************* TestCase End **********************");
				} else {
				ApiReports.errorResponse(response);
			}
		}
	}
}