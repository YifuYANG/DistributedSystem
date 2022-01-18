import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AFQReceiver {
    static AFQService afqService = new AFQService();
    public static void main(String[] args) throws JMSException {
        String host = args.length > 0 ? args[0] : "localhost";

        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
        Connection connection = factory.createConnection();
        connection.setClientID("AuldFellas");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("Quotations");
        Topic topic = session.createTopic("APPLICATIONS");
        MessageConsumer consumer = session.createConsumer(topic);
        MessageProducer producer = session.createProducer(queue);

        connection.start();
        System.out.println("------------------AuldFellas is up and running-------------------");
        while (true) {
            // Get the next message from the APPLICATION topic
            Message message = consumer.receive();
            // Check it is the right type of message
            if (message instanceof ObjectMessage) {
                // It’s an Object Message
                Object content = ((ObjectMessage) message).getObject();
                if (content instanceof QuotationRequestMessage) {
                    // It’s a Quotation Request Message
                    QuotationRequestMessage request = (QuotationRequestMessage) content;

                    // Generate a quotation and send a quotation response message…
                    Quotation quotation = afqService.generateQuotation(request.info);
                    Message response = session.createObjectMessage(new QuotationResponseMessage(request.id, quotation));
                    producer.send(response);
                }
            } else {
                System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
            }
        }

    }
}
