package com.core_engine.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.core_engine.console.Logging;
import com.core_engine.reports.ExtentManager;
import com.core_engine.reports.ReportUtil;
import com.core_engine.support.Xls_Reader;
import com.core_engine.utilities.CommonUtils;
import com.core_engine.utilities.Constants;
import com.core_engine.utilities.SendEmail;
import com.core_engine.utilities.Zip;
import org.testng.annotations.*;

public class DriverScript {

	//public static String TEST_DATA_PATH = Constants.FRAMEWORK_ROOT_DIRECTORY + "/src/test/java/resources/testData";
	public static String TEST_DATA_PATH = Constants.FRAMEWORK_ROOT_DIRECTORY + "/src/test/Project/resources/testData";
	public static Xls_Reader xls = null, xlsController = new Xls_Reader(TEST_DATA_PATH + Constants.FILE_SEPARATOR_KEY + "controller.xlsx");
	public static int rowNum = 2, rowNumController = 2;
	public static int rowNumExecutableTC = 2;
	public static int count = 0;
	public static String testCaseName;
	public static String testSheetName;
	public static String testCaseId;
	public static boolean continueRun = false;
	public static String endpoint = null;
	public static String BASE_URL = "";
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmSS");
	static String timeStamp = dateFormat.format(new Date());
	public static String toEmails="";
	public static String ccEmails="";
	public static String TEST_REPORT_NAME="";
	public static String TEST_RESOURCE_NAME="";
	public static String METHOD_NAME="";
	public static String SYSTEM_ID="";
	public static int noOfTCs=0;
	public static String countOfEnableTCs="";
	public static String BatchRun="";

	/**
	 *
	 * @return
	 */
	public static int getRowNumForExecutableTestCases() {
		while (rowNumExecutableTC <= xlsController.getRowCount(Constants.TEST_DATA)) {
			if (xlsController.getCellData(Constants.TEST_DATA, Constants.TEST_CASE_RUNMODE, rowNumExecutableTC).toUpperCase().equals(Constants.TEST_CASE_RUNMODE_YES)) {
				count++;
			}
			rowNumExecutableTC++;
		}
		rowNumExecutableTC = 2;
		return count;
	}

	public  boolean isRunnable(String tcId) {
		boolean isRunnable = false;
		continueRun = false;
		rowNumController = xlsController.getCellRowNum(Constants.TEST_DATA, Constants.TEST_CASE_ID, tcId);
		rowNum = rowNumController;
		testCaseId = tcId;
		countOfEnableTCs=xlsController.getNoOfTCsEnabled(Constants.TEST_DATA);
		testCaseName = xlsController.getCellData(Constants.TEST_DATA, Constants.TEST_CASE_NAME, rowNum);
		testSheetName = xlsController.getCellData(Constants.TEST_DATA, Constants.TEST_SHEET_NAME, rowNum);
		if (xlsController.getCellData(Constants.TEST_DATA, Constants.TEST_CASE_RUNMODE, rowNum).equalsIgnoreCase(Constants.TEST_CASE_RUNMODE_YES)) {
			noOfTCs++;
			TEST_REPORT_NAME= xlsController.getCellData(Constants.TEST_DATA,"SuiteType", rowNum);
			xls = new Xls_Reader(TEST_DATA_PATH + Constants.FILE_SEPARATOR_KEY + "Testdata.xlsx");
			continueRun = true;
			TEST_RESOURCE_NAME=xls.getCellData(testSheetName, "ResourceName", rowNum);
			METHOD_NAME=xls.getCellData(testSheetName, "MethodType", rowNum);
			SYSTEM_ID=xls.getCellData(testSheetName, CommonUtils.getProperty("ENV").toString(), rowNum);
			getReportName();
			endpoint=getEnvironmentDateils();
			isRunnable = true;
			Logging.info("Test scenario started:==== " + testCaseId + ": " + testCaseName);
		} else {
			Constants.EXECUTION_REPORT_FILE_NAME="BatchResults"+ "_" +testSheetName.toString()+ "_" + timeStamp.toString();
			Logging.info("Please check the runmode of TestCaseID '" + testCaseId + "'");
			isRunnable = false;
		}
		return isRunnable;
	}

	public void emailReceipts(){
		toEmails=CommonUtils.getProperty("TO_EMails");
		ccEmails=CommonUtils.getProperty("CC_EMails");
	}

	public static String getReportName() {
		if (CommonUtils.getProperty("BatchRun").equalsIgnoreCase("YES")) {
			Constants.EXECUTION_REPORT_FILE_NAME="MulesoftAPIs_Execution_Reports"+"_" + timeStamp.toString();
		}
		else if(TEST_REPORT_NAME.equalsIgnoreCase("SMOKE")||TEST_REPORT_NAME.equalsIgnoreCase("REGRESSION")) {
			Constants.EXECUTION_REPORT_FILE_NAME = testSheetName + "_" + TEST_REPORT_NAME + "_Test Results" + "_" + "_" + timeStamp.toString();
		}else{
			Constants.EXECUTION_REPORT_FILE_NAME=testCaseId+ "_" +testSheetName.toString();
		}
		return Constants.EXECUTION_REPORT_FILE_NAME;
	}

	public static final int countOfExecutableTestCases = getRowNumForExecutableTestCases();

	public static String getTestDataSheetName() {
		String testDataSheet = xlsController.getCellData(Constants.TEST_DATA, Constants.TEST_DATA_FILE_NAME, rowNum);
		return testDataSheet;
	}

	public static String getEnvironmentDateils() {
		String env = CommonUtils.getProperty("ENV");
		Constants.ACCOUNT_ID = xls.getCellData(testSheetName, "AccountID", rowNum);
		String envType=env+"_"+testSheetName.toUpperCase();
		switch (envType){
			case "DEV_CPP":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "APIGEE_CPP":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "QA_CPP":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "DEV_CPE":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "APIGEE_CPE":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "QA_CPE":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
				break;
			case "DEV_ACCOUNT":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum)+"/"+Constants.ACCOUNT_ID;
				break;
			case "QA_ACCOUNT":
				BASE_URL = CommonUtils.getProperty(envType);
				endpoint = BASE_URL +Constants.ACCOUNT_ID+ xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum);
			default:
				break;

		}
		return endpoint;
	}



	public static String getTestCaseId(String tcName){
		tcName= String.valueOf(tcName);
		String [] tName=tcName.split("_");
		String tcID=tName[0].split("\\.")[1];
		tcID=tName[0].substring(22,27);
		System.out.println(tcID);
		return tcID;
	}

	@BeforeMethod
	public void beforeMethod() {

	}

	@AfterMethod
	public void afterMethod() {
		ReportUtil.test = null;
	}

	@BeforeSuite
	public void init () {
	//System.out.println("Before Suite - " + this.getClass().getSuperclass().getSimpleName());
    //CommonUtils.deleteDirectory(Constants.FRAMEWORK_ROOT_DIRECTORY + "/Results");
		}


	@AfterSuite
	public void afterSuite() throws IOException {
			Zip.zipFile();
			if (CommonUtils.getProperty("EmailAction").equalsIgnoreCase("YES")) {
				SendEmail emailReport = new SendEmail();
				this.emailReceipts();
				if (CommonUtils.getProperty("BatchRun").equalsIgnoreCase("YES")) {
					if (noOfTCs == Integer.parseInt(countOfEnableTCs)) {
						emailReport.email(Constants.EXECUTION_REPORT_FILE_NAME, toEmails, ccEmails);
					} } else {
					emailReport.email(Constants.EXECUTION_REPORT_FILE_NAME, toEmails, ccEmails);
				}
			} else {
				System.out.println("Not Required to send an email");
			}
		}
}