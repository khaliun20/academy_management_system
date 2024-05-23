package edu.duke.projectTeam8;

import services.Courier;
import services.SendService;
import models.SendEnhancedRequestBody;
import models.SendEnhancedResponseBody;
import models.SendRequestMessage;
import models.SendRequestMessageRouting;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;

/**
 * This class is used to send emails to users
 */
public class EmailSender {

    /**
     * This is a constructor that initializes the EmailSender object with an API key
     * 
     * @param apiKey the API key to be used to send emails
     */
    public EmailSender(String apiKey) {
        Courier.init(apiKey);
    }

    /**
     * This is a constructor that initializes the EmailSender object with a default
     * API key
     */
    public EmailSender() {
        this("pk_prod_B9FKTNQNS9M1PCQSFTJN4WC8NF9S");
    }

    /**
     * This method creates a SendRequestMessage object with the recipient's email
     * address
     * 
     * @param recipientAddress the recipient's email address
     * @return the SendRequestMessage object
     */
    private SendRequestMessage makeEmail(String recipientAddress) {
        SendRequestMessage message = new SendRequestMessage();
        Map<String, String> to = new HashMap<>();
        to.put("email", recipientAddress);
        message.setTo(to);
        return message;
    }

    /**
     * This method sets the title and body of the email
     * 
     * @param message the SendRequestMessage object
     * @param title   the title of the email
     * @param body    the body of the email
     */
    private void setEmailContent(SendRequestMessage message, String title, String body) {
        HashMap<String, Object> content = new HashMap<>();
        content.put("title", title);
        content.put("body", body);
        message.setContent(content);
    }

    /**
     * This method sends an email to the recipient with the specified title and body
     * 
     * @param recipientAddress the recipient's email address
     * @param title            the title of the email
     * @param body             the body of the email
     * @throws IOException if the email cannot be sent
     */
    public void sendEmail(String recipientAddress, String title, String body)
            throws IOException {
        SendRequestMessage message = makeEmail(recipientAddress);
        setEmailContent(message, title, body);

        SendRequestMessageRouting routing = new SendRequestMessageRouting();
        routing.setMethod("single");
        List<Object> channelList = new ArrayList<>();
        channelList.add("email");
        routing.setChannels(channelList);
        message.setRouting(routing);

        SendEnhancedRequestBody request = new SendEnhancedRequestBody();
        request.setMessage(message);
        SendEnhancedResponseBody response = new SendService().sendEnhancedMessage(request);
        if (response == null)
            System.err.println("Message submission failed: missing info");
        else
            System.out.println("Message submitted to mail server");
    }

}
