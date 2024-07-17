package com.core_engine.utilities;

import com.core_engine.base.DriverScript;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendEmail {
    static String projectPath = System.getProperty("user.dir");
    String reportName="";
    public void email(String testCaseName, String emailTo, String emailCC) {
        String[] toemails = new String[0];
        String[] ccemails = new String[0];
        String from = "noreply-testautomation@amfam.com";
        String host = "mr2.amfam.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            if (emailTo.contains(",")) {
                toemails = emailTo.split(",");
                for (int i = 0; i < toemails.length; i++) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(toemails[i]));
                }
            } else {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            }
            if (emailCC.contains(",")) {
                ccemails = emailCC.split(",");
                for (int i = 0; i < ccemails.length; i++) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccemails[i]));
                }
            } else {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCC));
            }
            //message.setSubject(DriverScript.testSheetName+" API Test Results for " + testCaseName + "");

            message.setSubject( "Mulesoft Test Execution Report");
            if(!Constants.EXECUTION_REPORT_FILE_NAME.isEmpty()) {
                reportName=Constants.EXECUTION_REPORT_FILE_NAME+ " " + "Test".toString();
            }else{
                reportName = DriverScript.testSheetName + " " + DriverScript.TEST_REPORT_NAME + " " + "Test";
                 }
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlbody = "<h1 style=\"color:rgb(122, 122, 122);text-decoration: underline;\">Api Test Execution Report</h1>\r\n"
                    + "\r\n"
                    + "<p>\r\n"
                    + "The test run completed for " + reportName + " and the result is attached with this email.\r\n"
                    + "</p>"
                    + "<br>\r\n"
                    + "<br>\r\n"
                    + "<br>\r\n"
                    + "<br>\r\n"
                    + "<p style=\"color:rgb(10, 43, 228);\">\r\n"
                    + "Thank You,<br>\r\n"
                    + "<font color='orange' face='Cambria'><b>"+"Mulesoft Integration Team"+"</b></font><br>\r\n"
                    + "American Family Insurance<br>\r\n"
                    + "\r\n"
                    + "</p>";

            messageBodyPart.setContent(htmlbody, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            String fileName = testCaseName + ".html";

            // File attachment
            messageBodyPart = new MimeBodyPart();
            String filename = projectPath + "/Results/" + Constants.EXECUTION_REPORT_FILE_NAME+".html";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}