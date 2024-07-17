package com.core_engine.reports;
import com.core_engine.base.DriverScript;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ReportUtil extends ExtentManager {
	static ExtentReports report = getInstance();

	public static ExtentTest test;
	static int countOfCallingEndTest = 0;
	static int countOfCallingStartTest = 0;


	public static String reportStepFailed(String comment) {
		String reportStep = null;
		try {
			reportStep = "<font color='red' face='Cambria'><b>" + comment + "</b></font>";
		} catch (Throwable t) {
			reportStep = null;
		}
		return reportStep;
	}


	public static String reportStepPassed(String comment) {
		String reportStep = null;
		try {
			reportStep = "<font color='black' face='Cambria'><b>" + comment + "</b></font>";
		} catch (Throwable t) {
			reportStep = null;
		}
		return reportStep;
	}


	public static String reportStepInfo(String comment) {
		try {
			String reportStep = "<font color='blue' face='Cambria'><i>" + comment + "</i></font>";
			return reportStep;
		} catch (Throwable t) {
			return null;
		}
	}


	public static String reportStepWarning(String comment) {
		String reportStep = null;
		try {
			reportStep = "<font color='orange' face='Cambria'><b>" + comment + "</b></font>";
		} catch (Throwable t) {
			reportStep = null;
		}
		return reportStep;
	}


	public static String reportStepSkip(String comment) {
		try {
			String reportStep = "<font color='sky blue' face='Cambria'><b>" + comment + "</b></font>";
			return reportStep;
		} catch (Throwable t) {
			return null;
		}
	}


	public static void markPassed(String comment) {
		if (DriverScript.continueRun) {
			if (test == null) {

				test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).assignAuthor(DriverScript.TEST_RESOURCE_NAME).assignCategory(DriverScript.TEST_REPORT_NAME);
			}
			try {
				test.log(Status.PASS, comment);
				} finally {
				if (report != null) {
					//report.endTest(test);
					report.flush();
				}
			}
		}
	}

	public static void markFailed(String comment) {
		if (DriverScript.continueRun) {
			if (test == null) {
				test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).assignAuthor(DriverScript.TEST_RESOURCE_NAME).assignCategory(DriverScript.TEST_REPORT_NAME);
			}
			try {
				test.log(Status.FAIL, comment);
			}catch (Throwable t){
				report.createTest("Failed Exception").fail(t.getMessage());
			}
			finally {
				if (report != null) {
					report.flush();
				}
			}
		}
	}


	public static void markInfo(String comment) {
		if (test == null) {
			test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).assignAuthor(DriverScript.TEST_RESOURCE_NAME).assignCategory(DriverScript.TEST_REPORT_NAME);
		}
		try {
			if (comment.toUpperCase().equals("START")) {
				comment = "Starting the test";
				test.log(Status.INFO, comment);
				countOfCallingStartTest++;
			} else if (comment.toUpperCase().equals("END")) {
				comment = "Ending the test";
				test.log(Status.INFO, comment);
				test = null;
				countOfCallingEndTest++;
			} else {
				test.log(Status.INFO, comment);
			}
		} finally {
			if (report != null) {
				//report.endTest(test);
				report.flush();
			}
		}
	}


	public static void markWarning(String comment) {
		if (DriverScript.continueRun) {
			if (test == null) {
				test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).assignAuthor(DriverScript.TEST_RESOURCE_NAME).assignCategory(DriverScript.TEST_REPORT_NAME);
			}
			try {
				test.log(Status.WARNING, reportStepWarning(comment));
			} finally {
				if (report != null) {
					//report.endTest(test);
					report.flush();
				}
			}
		}
	}


	public static void markSkip(String comment) {
		if (test == null) {
			test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).assignAuthor(DriverScript.TEST_RESOURCE_NAME).assignCategory(DriverScript.TEST_REPORT_NAME);
		}
		try {
			test.log(Status.SKIP, reportStepSkip(comment));
		} finally {
			if (report != null) {
				//report.endTest(test);
				report.flush();
			}
		}
	}


	public static void markStart() {
		if (test == null) {
			test = report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName);
		}
		try {
			test = null;
		} finally {
			if (report != null) {
				//report.endTest(test);
				report.flush();
			}
		}
	}
	public static void markException(String comment) {
		if (DriverScript.continueRun) {
			try {
				test.log(Status.FAIL, comment);
			}catch (Throwable t) {
				report.createTest(DriverScript.testCaseId +": " + DriverScript.testCaseName).fail(t);
				if (report != null) {
					report.flush();
				}
				}
		}
	}

}