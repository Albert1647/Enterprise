/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package livescore;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import javax.jms.Topic;

/**
 *
 * @author sarun
 */
public class Main {
    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String destType = null;
        Connection connection = null;
        Session session = null;
        Destination dest = null;
        TextMessage message = null;
        InputStreamReader inputStreamReader = null;
        String answer = "";
        BufferedReader input = null;

        if (args.length != 1) {
            System.err.println("Program takes one argument: <dest_type>");
            System.exit(1);
        }

        destType = args[0];
        System.out.println("Destination type is " + destType);

        if (!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or \"topic\"");
            System.exit(1);
        }

        try {
            if (destType.equals("queue")) {
                dest = (Destination) queue;
            } else {
                dest = (Destination) topic;
            }
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            inputStreamReader = new InputStreamReader(System.in);
            input = new BufferedReader(inputStreamReader);
            String currentScore = "Thailand 1-0 Spain";
            MessageProducer producer = session.createProducer(dest);
            while (true) {
                try {
                    System.out.println(currentScore);
                    System.out.println("Enter Live Score ");
                    answer = input.readLine();
                    currentScore = answer;
                    message = session.createTextMessage();
                    message.setText(currentScore);
                    System.out.println("Sending message: " + message.getText());
                    producer.send(message);
                    producer.send(session.createMessage());
                } catch (IOException e) {
                    System.err.println("I/O exception: " + e.toString());
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
