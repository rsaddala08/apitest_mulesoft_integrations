package testscripts.CPP;

import com.core_engine.api.lib.ApiReports;
import com.core_engine.api.lib.ApiUtils;
import com.core_engine.api.lib.HttpUtils;
import com.core_engine.base.DriverScript;
import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.ResponseCodeFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.core_engine.base.DriverScript.*;

public class TS003_DELETE_MultipleCPPRecords extends HttpUtils {
	static String userValues;
	static String accountValues;
	static String phoneValues;
	static String requestBody;
	static JsonPath jsonPath;
	static Response response=null;
	static String testCaseDescription="";
	@Test
	public void deleteMultipleCppRecords() {
		String tcId= DriverScript.getTestCaseId(String.valueOf(this.getClass()));
		if (isRunnable(tcId)) {
			ReportUtil.markPassed("************************* TestCase Start **********************");
			testCaseDescription =xls.getCellData(testSheetName, "TestCaseDescription", rowNum);
			userValues =xls.getCellData(testSheetName, "User_Data", rowNum);
			accountValues =xls.getCellData(testSheetName, "Account_Data", rowNum);
			phoneValues =xls.getCellData(testSheetName, "Phone_Data", rowNum);
			if(phoneValues.contains("|")){
				requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, phoneValues);
				response=deleteRequest(endpoint,requestBody);
				ApiUtils.responseBody=ApiUtils.deleteMultipleRecords(response);
				response=ApiUtils.responseBody;
			}else{
				requestBody = ApiUtils.contactPointJsonRequest(userValues, accountValues, phoneValues);
				response=deleteRequest(endpoint,requestBody);
			}
			response.prettyPrint();
			jsonPath = response.jsonPath();
			if (response.getStatusCode() == ResponseCodeFactory.RESPONSE_CODE_200) {
				ReportUtil.markInfo("TestCase Description :  "+"  "+testCaseDescription);
				ApiUtils.writeResponseData(response);
				ReportUtil.markPassed("Record has deleted and the records count is:  "+ ApiReports.getRecordsCount());
				ReportUtil.markPassed("Record Array is Emapty:  "+jsonPath.getString("account.phone.records"));
				ReportUtil.markPassed("Check the Response for Deleted record:  \n"+response.prettyPrint());
				ReportUtil.markPassed("All Records has been deleted successfully!!");
				ReportUtil.markPassed("************************* TestCase End **********************");
			} else {
				ApiReports.errorResponse(response);
			}
		}
	}
}