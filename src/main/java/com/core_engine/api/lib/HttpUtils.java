package com.core_engine.api.lib;

import com.core_engine.reports.ReportUtil;
import com.core_engine.utilities.CommonUtils;
import com.core_engine.base.DriverScript;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class HttpUtils extends AuthFactory {

    static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    static final String AUTHORIZATION = "Authorization";
    public static  Map<String, String> hm = new HashMap<String, String>();
    static String path = System.getProperty("user.dir") + "/EnvironmentDetails/auth.properties";

    /**
     * @param endpoint
     * @return
     */
    public static Response get(String endpoint) {
        try {
            RequestSpecification request = RestAssured.given();
            Response response = request
                    .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .get(endpoint);
            return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("GET command is failed for the testCaseID: " + DriverScript.testCaseId);
            t.printStackTrace();
            return null;
        }
    }
    public static Response apigeeTokenGenerate(String endpoint) {
        try {
            RequestSpecification request = RestAssured.given();
            String Username ="0oa1fju2iijDXQusZ0h8";
            String Password ="y67o0P5QUMO3RTZY2rPL_s0ZshiUo2ANIE86_WuR";
            Response response = request.
                    auth().preemptive().basic(Username,Password)
                    .header("scope", "api_scope ")
                    .header("grant_type","client_credentials")
                    .get(endpoint);
            return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("GET command is failed for the testCaseID: " + DriverScript.testCaseId);
            ReportUtil.markException(t.getMessage());
            t.printStackTrace();
            return null;
        }
    }
    /**
     * @return
     */
    public static RequestSpecification request() {
        RequestSpecification request = RestAssured.given();
        return request.header(AUTHORIZATION, "Bearer " + BEARER_TOKEN);
    }

    /**
     * @param endpoint
     * @param jsonRequest
     * @return
     */
    public static Response postRequest(String endpoint, String jsonRequest) {
        try {
                hm=getHeadersMap();
            BEARER_TOKEN= CommonUtils.getProperty("APIGEE_TOKEN");
           Response response = given()
                    .headers(hm)
                    .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .accept("application/json")
                    .contentType("application/json")
                    .log()
                    .all()
                    .body(jsonRequest)
                    .post(endpoint);
            if(response.getStatusCode()==401||response.getStatusCode()==403&&response.jsonPath().getString("message").contains("Token is not valid")) {
                 BEARER_TOKEN = BasicAuthenticationToken();
                CommonUtils.setProperty("APIGEE_TOKEN", BEARER_TOKEN.toString());
                    response = given()
                            .headers(hm)
                            .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                            .accept(APPLICATION_JSON_CONTENT_TYPE)
                            .contentType(APPLICATION_JSON_CONTENT_TYPE)
                            .log()
                            .all()
                            .body(jsonRequest)
                            .post(endpoint);
            }
                return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("POST command is failed for the testCaseID: " + DriverScript.testCaseId);
            t.printStackTrace();
            ReportUtil.markException(t.getMessage());
            return null;
        }
    }

    public static Response putRequest(String endpoint, String jsonRequest) {
        try {
            hm=getHeadersMap();
            BEARER_TOKEN= CommonUtils.getProperty("APIGEE_TOKEN");
            Response response = given()
                    .headers(hm)
                    .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .accept("application/json")
                    .contentType("application/json")
                    .log()
                    .all()
                    .body(jsonRequest)
                    .put(endpoint);
            if(response.getStatusCode()==401||response.getStatusCode()==403&&response.jsonPath().getString("message").contains("Token is not valid")) {
                BEARER_TOKEN = BasicAuthenticationToken();
                CommonUtils.setProperty("APIGEE_TOKEN", BEARER_TOKEN.toString());
                response = given()
                        .headers(hm)
                        .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                        .accept(APPLICATION_JSON_CONTENT_TYPE)
                        .contentType(APPLICATION_JSON_CONTENT_TYPE)
                        .log()
                        .all()
                        .body(jsonRequest)
                        .put(endpoint);
            }
            return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("PUT command is failed for the testCaseID: " + DriverScript.testCaseId);
            t.printStackTrace();
            ReportUtil.markException(t.getMessage());
            return null;
        }    }
    /**
     * @param endpoint
     * @return
     */
    public static Response deleteRequest(String endpoint, String jsonRequest) {
        try {
            hm=getHeadersMap();
            BEARER_TOKEN= CommonUtils.getProperty("APIGEE_TOKEN");
            Response response = given()
                    .headers(hm)
                    .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .accept("application/json")
                    .contentType("application/json")
                    .log()
                    .all()
                    .body(jsonRequest)
                    .post(endpoint);
            if(response.getStatusCode()==401||response.getStatusCode()==403&&response.jsonPath().getString("message").contains("Token is not valid")) {
                BEARER_TOKEN = BasicAuthenticationToken();
                CommonUtils.setProperty("APIGEE_TOKEN", BEARER_TOKEN.toString());
                response = given()
                        .headers(hm)
                        .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                        .accept(APPLICATION_JSON_CONTENT_TYPE)
                        .contentType(APPLICATION_JSON_CONTENT_TYPE)
                        .log()
                        .all()
                        .body(jsonRequest)
                        .post(endpoint);
            }
            return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("DELETE command is failed for the testCaseID: " + DriverScript.testCaseId);
            t.printStackTrace();
            ReportUtil.markException(t.getMessage());
            return null;
        }
    }

    /**
     * @param endpoint
     * @param json
     * @return
     */
    public static Response patch(String endpoint, JSONObject json) {
        try {
            Response response = given()
                    .header(AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .accept(APPLICATION_JSON_CONTENT_TYPE)
                    .contentType(APPLICATION_JSON_CONTENT_TYPE)
                    .body(json)
                    .when()
                    .patch(endpoint);
            return response;
        } catch (Throwable t) {
            ReportUtil.markFailed("PATCH command is failed for the testCaseID: " + DriverScript.testCaseId);
            t.printStackTrace();
            return null;
        }
    }

     static void doTrustToCertificates() throws Exception {
       Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
 // connecting to URL
     static void connectToUrl(String URL) throws Exception {
        doTrustToCertificates();//
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        System.out.println("ResponseCode ="+conn.getResponseCode());
    }

    public static Map<String, String> getHeadersMap() {
        hm.put("afe-interaction-id", "12345");
        hm.put("afe-experience-id", "12345");
        hm.put("afe-session-id", "12345");
        hm.put("afe-request-id", "12345");
        hm.put("afe-product-line", "12345");
        hm.put("afe-source-id", "RELATE");
        if(CommonUtils.getProperty("ENV").equalsIgnoreCase("DEV")) {
            hm.put("afe-api-key", "xbo8JcKuhuQr6v5IIMZaeGKDh6IwsDXhkApqrPEAb4uAZUAO");
        }else {
            hm.put("afe-api-key", "fVTLbGwFAQTArC6ZEuJg11rmMbSJBTEmwL0RQsbwVtpEGhZZ");
        }
        hm.put("afe-trace-id", "12345");
        hm.put("X-CORRELATION-ID", "12345");
        return hm;
    }

    public static String BasicAuthenticationToken(){
        Response response = RestAssured.given().with().auth().preemptive()
                .basic(CommonUtils.getProperty("ApigeeAuth_UserName"), CommonUtils.getProperty("ApigeeAuth_Password"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("grant_type",CommonUtils.getProperty("Apigee_GrantType") )
                .formParam("scope", CommonUtils.getProperty("Apigee_Scope"))
                .formParam("username", CommonUtils.getProperty("ApigeeAuth_UserName"))
                .formParam("password", CommonUtils.getProperty("ApigeeAuth_Password")).when()
                .post(CommonUtils.getProperty("ApigeeAuthToken"));
        JsonPath path = response.jsonPath();
        BEARER_TOKEN = path.get("access_token");
        return BEARER_TOKEN;
    }

    public static void setProperty(String key,String value) {
        try {
            FileReader reader = null;
            FileWriter writer = null;
            File file = new File(path);
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



}