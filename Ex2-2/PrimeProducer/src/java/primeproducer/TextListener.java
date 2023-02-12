/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primeproducer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    private MessageProducer replyProducer;
    private Session session;

    public TextListener(Session session) {

        this.session = session;
        try {
            replyProducer = session.createProducer(null);
        } catch (JMSException ex) {
            Logger.getLogger(TextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText() + " "
                        + msg.getJMSCorrelationID());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            String responseMessage = "";
            String[] range = msg.getText().split(",");
            if (range.length != 2) {
                System.out.println("Invalid Range Request");
                responseMessage = "Invalid Range Request";
                TextMessage response = session.createTextMessage(responseMessage);
                System.out.println("sending message " + response.getText());
                replyProducer.send(message.getJMSReplyTo(), response);
                return;
            }
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            if (start > end) {
                System.out.println("Invalid Range Order a > b");
                responseMessage = "Invalid Range Order a > b";
                TextMessage response = session.createTextMessage(responseMessage);
                System.out.println("sending message " + response.getText());
                replyProducer.send(message.getJMSReplyTo(), response);
                return;
            }
            if (start < 0 || end < 0) {
                System.out.println("Invalid Range Minus");
                responseMessage = "Invalid Range Minus";
                TextMessage response = session.createTextMessage(responseMessage);
                System.out.println("sending message " + response.getText());
                replyProducer.send(message.getJMSReplyTo(), response);
                return;
            }
            
            int primeCount = getPrimeCount(start, end);
            responseMessage = "The number of primes between " + range[0] + " and " + range[1] + " is " + primeCount;
            TextMessage response = session.createTextMessage(responseMessage);
            System.out.println("sending message " + response.getText());
            replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }

    }

    public int getPrimeCount(int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    public boolean isPrime(int n) {
        for (int i = 2; i * i <= n; i++) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }
    
}
