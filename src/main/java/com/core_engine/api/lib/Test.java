package com.core_engine.api.lib;

import com.core_engine.reports.ReportUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class Test {
    public static void main (String[] a) {
            try {

                String [] s="class testscripts.CPP.TC001_POST_AddNewCPPRecord".split("_");
                System.out.println(s[0]);
                System.out.println(s[0].split("\\.")[1]);
                System.out.println(s[0].substring(22,27));



                String []values = "TC001,TC002,TC003,TC004,TC005,TC006,TC007,TC008,TC009".split(",");
                String  val="";
                    for(int i=0;i<values.length;i++){
                          val=values[i].toString();
                        Thread th=new Thread();
                        th.setName(val);
                    }
                System.out.println(val);




                Response response=null;
                JSONParser parser = new JSONParser();
                File file =new File("C:\\Users\\RXS214\\OneDrive - American Family\\Documents\\newworkspace\\relate-mulesoft-interationtesting\\Json_Templates\\AccountMatch.json");
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(file));
              //  JSONObject parentObject =  (JSONObject)jsonObject.get("account");
               // JSONObject childObject =  (JSONObject)parentObject.get("phone");

                JSONArray jsonArray = (JSONArray)jsonObject.get("accountMatch");
               JsonPath jsonPath = response.jsonPath();
                    for (int i = 0; i <= jsonArray.size(); i++) {
                    String producerAssociationExists = jsonPath.getString("accountMatch[" + i + "].producerAssociationExists");
                    ReportUtil.markPassed("CDHID is : " + jsonPath.getString("accountMatch[" + i + "].systemId") + "  , and Version is : " + jsonPath.getString("accountMatch[" + i + "].version"));
                    String accountMatchType = jsonPath.getString("accountMatch[" + i + "].accountMatchType");
                    String socialSecurityNumber = jsonPath.getString("accountMatch[" + i + "].socialSecurityNumber");
                    String employerIdentificationNumber = jsonPath.getString("accountMatch[" + i + "].employerIdentificationNumber");
                    String driversLicenseNumber = jsonPath.getString("accountMatch[" + i + "].driversLicenseNumber");
                    String driversLicenseState = jsonPath.getString("accountMatch[" + i + "].driversLicenseState");
                    ReportUtil.markPassed("Validated Required details are : " + "ProducerAssociationExists is :" + producerAssociationExists + " , AccountMatchType is : " + accountMatchType);
                    ReportUtil.markPassed("Matched record fields  :  " + " SocialSecurityNumber : " + socialSecurityNumber + " , EmployerIdentificationNumber : " + employerIdentificationNumber + " , DriversLicenseNumber is: " + driversLicenseNumber + " , DriversLicenseState is: " + driversLicenseState);
                    String addressline1 = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].addressLine1");
                    String city = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].city");
                    String state = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].state");
                    String purposeType = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].purposeType");
                    String usageType = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].usageType");
                    if (jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].country").equalsIgnoreCase("USA")) {
                        String postalCode = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].postalCode");
                        ReportUtil.markPassed("Matched Account Address details are :" + "AddressLine1 :" + addressline1 + " ,City : " + city + " ,State : " + state + " ,Postcode : " + postalCode + " ,PurposeType is: " + purposeType + " ,UsageType is: " + usageType);
                        break;
                    } else {
                        String provinceTerritory = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].provinceTerritory");
                        String foreignPostalCode = jsonPath.getString("accountMatch[" + i + "].address.records[" + i + "].foreignPostalCode");
                        ReportUtil.markPassed("Matched Account Address details are :" + "AddressLine1 : " + addressline1 + " ,City : "  + city + " ,State is: " + state + " ,ProvinceTerritory is: " + provinceTerritory + " ,ForeignPostalCode: " + foreignPostalCode + " ,PurposeType : " + purposeType + " ,UsageType is: " + usageType);
                        break;
                    }
                }

            }catch (Throwable t){
                System.out.println(t.getMessage());

            }


    }
}
