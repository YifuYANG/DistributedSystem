import java.util.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Queue;

public class Broker{


    public static List<ClientID> infos=new LinkedList<>();
    public static List<Quotation> quotations=new LinkedList<>();
    public static Map<Long,List<Quotation>> cache=new HashMap<>();
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        Connection connection = factory.createConnection();
        connection.setClientID("Broker");
        connection.start();
        System.out.println("------Broker is online and connected------");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue ClientApplication = session.createQueue("ClientApplication");
        MessageProducer ClientApplicationProducer = session.createProducer(ClientApplication);

        //new thread to call first consumer's message listener
        Thread first = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Topic topic = session.createTopic("APPLICATIONS");
                    Queue ClientInfoQueue = session.createQueue("ClientInfo");
                    MessageProducer producer = session.createProducer(topic);
                    MessageConsumer first_consumer = session.createConsumer(ClientInfoQueue);
                    first_consumer.setMessageListener(new MessageListener() {
                        @Override
                        public void onMessage(Message ClientInfoMessage) {
                            try {
                                // Get the next message from the ClientInfo queue
                                // Check it is the right type of message
                                if (ClientInfoMessage instanceof ObjectMessage) {
                                    // It’s an Object Message
                                    Object content = ((ObjectMessage) ClientInfoMessage).getObject();
                                    if (content instanceof QuotationRequestMessage) {
                                        // It’s a Quotation Request Message
                                        QuotationRequestMessage requestMessage = (QuotationRequestMessage) content;
                                        Message request = session.createObjectMessage(requestMessage);
                                        producer.send(request);
                                        //add client id and client info
                                        infos.add(new ClientID(requestMessage.id, requestMessage.info));
                                    }
                                    ClientInfoMessage.acknowledge();
                                } else {
                                    System.out.println("Unknown message type: " + ClientInfoMessage.getClass().getCanonicalName());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        first.start();

        //new thread to call second consumer's message listener
        Thread second = new Thread(() -> {
            try {
                Queue QuotationsSQueue = session.createQueue("Quotations");
                MessageConsumer second_consumer = session.createConsumer(QuotationsSQueue);
                second_consumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message QuotationsMessage) {
                        try{
                            // Get the next message from the Quotations queue
                            // Check it is the right type of message
                            if (QuotationsMessage instanceof ObjectMessage){
                                // It’s an Object Message
                                Object content_2 = ((ObjectMessage) QuotationsMessage).getObject();
                                // It’s a Quotation Response Message
                                if(content_2 instanceof QuotationResponseMessage){
                                    //add quotation in to the list
                                    QuotationResponseMessage response = (QuotationResponseMessage) content_2;
                                    //clear the quotation list for new client
                                    if (!cache.containsKey(response.id)) {
                                        quotations.clear();
                                    }
                                    quotations.add(response.quotation);
                                    cache.put(response.id, quotations);
                                }
                            }
                            QuotationsMessage.acknowledge();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        second.start();

        while (true){
            //wait a bit for services
            Thread.sleep(5000);
            for(ClientID id: infos){
                if(cache.containsKey(id.id)){
                    //match seed id
                    ClientApplicationMessage clientApplicationMessage = new ClientApplicationMessage(id.id,id.info,cache.get(id.id));
                    //create message
                    Message clientMessage=session.createObjectMessage(clientApplicationMessage);
                    //send message
                    ClientApplicationProducer.send(clientMessage);
                    System.out.println(clientApplicationMessage.info.name);
                    System.out.println("-------------------------------");
                }
            }
        }

    }
}

