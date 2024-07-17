package com.core_engine.reports;
import java.io.File;

import com.core_engine.base.DriverScript;
import com.core_engine.utilities.CommonUtils;
import com.core_engine.utilities.Constants;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import static com.core_engine.base.DriverScript.rowNum;
import static com.core_engine.base.DriverScript.testSheetName;

public class ExtentManager {
	private static ExtentReports extent;
	public static String dynamicHtmlReportPath;
	public static String reportFolderPath = null;

	static String ENV = CommonUtils.getProperty("ENV");

	public static ExtentReports getInstance() {
		try {
			if (extent == null) {

				dynamicHtmlReportPath = htmlReportPath();
				extent = new ExtentReports();
				ExtentSparkReporter spark = new ExtentSparkReporter(dynamicHtmlReportPath);
				spark.loadXMLConfig(new File(System.getProperty("user.dir") + "/report-config.xml"));
				spark.viewConfigurer().viewOrder().as(new ViewName[]{
						ViewName.TEST,
						ViewName.EXCEPTION,
						ViewName.CATEGORY,
						ViewName.DEVICE,
						ViewName.AUTHOR,
						ViewName.DASHBOARD
				}).apply();
				extent.attachReporter(spark);
				extent.setSystemInfo("User Name", System.getProperty("user.name"));
				extent.setSystemInfo("OS", System.getProperty("os.name"));
				//extent.setSystemInfo("Host Name",System.getProperty("hostname"));
				extent.setSystemInfo("Environment", ENV);
			    //extent.setSystemInfo("Component", testSheetName);
				extent.setSystemInfo("baseURI", DriverScript.BASE_URL);
				//extent.setSystemInfo("endPoint", DriverScript.xls.getCellData(testSheetName, Constants.ENDPOINT, rowNum));
				}
		}catch (Throwable t){
			System.out.println("File Not Found : "+ t.getMessage());
		}
		return extent;
	}


	public static String htmlReportPath() {
		String fileName = Constants.EXECUTION_REPORT_FILE_NAME + ".html";
		reportFolderPath = System.getProperty("user.dir") + Constants.FILE_SEPARATOR_KEY + "Results";
		File dir = new File(reportFolderPath);
		dir.mkdir();
		//extent = new ExtentReports(reportFolderPath + Constants.FILE_SEPARATOR_KEY + fileName);
		//Desktop.getDesktop().browse(new File(fileName).toURI());
		return (reportFolderPath + File.separator + fileName);
	}

}