/**
 * This class  contains Utility methods specific to the framework architechture and
 * will be used to perform various actions across the framework
 *
 * @author Subrato
 */
package com.core_engine.utilities;
import java.io.*;

import com.core_engine.reports.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.fluttercode.datafactory.impl.DataFactory;

import java.security.SecureRandom;
import java.util.*;


public class CommonUtils {
	static String path = System.getProperty("user.dir") + "/EnvironmentDetails/config.properties";
	static String authPath = System.getProperty("user.dir") + "/EnvironmentDetails/auth.properties";
	static ArrayList<String> myArray=new ArrayList<>();

	/**
	 *
	 * @param strVal
	 * @return
	 */
	public static String getProperty(String strVal) {
		String val;
		FileInputStream fis =null;
		try {
			Properties prop = new Properties();
			if (!strVal.equals("APIGEE_TOKEN")) {
				fis = new FileInputStream(path);
				prop.load(fis);
			}else {
				fis = new FileInputStream(authPath);
				prop.load(fis);
			}
			val = prop.getProperty(strVal).trim();
		} catch (Throwable t) {
			val = null;
		}
		return val;
	}



	/**
	 *
	 * @return
	 */
	public static String htmlReportPathGenerated() {
		try {
			return ExtentManager.dynamicHtmlReportPath;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	public static String getRandomString(int len) {
		String CHARACTER_SET="0123456789abcdefghijklmnopqrstuvwxyz";
		SecureRandom random = new SecureRandom();
		StringBuffer buff = new StringBuffer(len);
		for(int i = 0; i < len; i++) {
			int offset = random.nextInt(CHARACTER_SET.length());
			buff.append(CHARACTER_SET.substring(offset,offset+1));
		}
		return buff.toString();
	}

	/**
	 *
	 * @param dir
	 */
	public static void deleteDirectory (String dir) {
		try {
			FileUtils.deleteDirectory(new File(dir));
		} catch (Throwable t) {

		}
	}


public static String getRandomNumber() {
		return null;
	}

	/**
	 *
	 * @param key,value
	 * @return
	 */
	public static void setProperty(String key,String value) {
		try {
			FileReader reader = null;
			FileWriter writer = null;
			File file = new File(authPath);
			reader = new FileReader(file);
			writer = new FileWriter(file);
			Properties prop = new Properties();
			prop.load(reader);
			prop.put(key,value);
			prop.store(writer,"write a file");
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	//Generate Random Names
	public static void RandomNames(String Name) {
		DataFactory df = new DataFactory();
		Random rand = new Random();
		String Value = null;
		for (int i = 0; i < 2000; i++) {
			switch (Name.toUpperCase()) {
				case "FIRSTNAME":
					Value = df.getFirstName();
					break;
				case "MIDDLENAME":
					Value = df.getFirstName();
					break;
				case "LASTNAME":
					Value = df.getLastName();
					break;
				case "ACCOUNTNAME":
					Value = df.getBusinessName();
					break;
				case "NUMBER":
					int num = rand.nextInt(9999);
					num += 1;
					String val = Integer.toString(num);
					Value = val + df.getFirstName();
					break;
			}
			myArray.add(Value);
		}
	}
	public static String getRandomName(String Name) {
		RandomNames(Name);
		Random generator = new Random();
		int randomIndex = generator.nextInt(myArray.size());
		String value = myArray.get(randomIndex);
		myArray.clear();
		return value;
	}


}