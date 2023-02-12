/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primeproducer;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
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
        TextListener listener = null;
        MessageConsumer consumer = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Create Temp Queue Listener
            listener = new TextListener(session);
            consumer = session.createConsumer(queue);
            
//            Listen to Question From
            consumer.setMessageListener(listener);
            
            connection.start();
            String ch = "";
            Scanner inp = new Scanner(System.in);
            System.out.print("Press q to quit");
            
            while (true) {
                ch = inp.nextLine();
                if (ch.equals("q")) {
                    break;
                }
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
