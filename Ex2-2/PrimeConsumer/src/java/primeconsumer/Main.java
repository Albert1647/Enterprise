/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primeconsumer;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class Main {

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/TempQueue")
    private static Queue queue;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        TextMessage message = null;
        TextListener listener = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            listener = new TextListener(session);
            
            Queue tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            responseConsumer.setMessageListener(listener);
            
            MessageProducer producer = session.createProducer(queue);
//            Create Consumer
            connection.start();
            String ch = "";
            Scanner inp = new Scanner(System.in);
//          create producer
            System.out.println("Press enter to quit");
            System.out.println("Enter two numbers, Use ',' to separate each number. To end the program press enter");
            while (true) {
                ch = inp.nextLine();
                if (ch.isEmpty()) {
                    break;
                }
                message = session.createTextMessage();
                message.setText(ch);
                message.setJMSReplyTo(tempDest);
                String correlationId = "12345";
                message.setJMSCorrelationID(correlationId);
                System.out.println("Sending message: " + message.getText());
                producer.send(message);
            }
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }

    }
}
