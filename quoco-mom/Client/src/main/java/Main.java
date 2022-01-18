import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
	static Map<Long, ClientInfo> cache = new HashMap<>();
	public static void main(String[] args)  {
		String host = args.length > 0 ? args[0] : "localhost";

		//create ConnectionFactory set ip address and port number
		ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
		try{
			//create connection using factory
			Connection connection = factory.createConnection();
			//start connection
			connection.start();
			//create session using connection
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			//create destination
			Queue queue = session.createQueue("ClientApplication");
			Queue clientInfo = session.createQueue("ClientInfo");
			//create producer and consumer
			MessageProducer producer = session.createProducer(clientInfo);
			MessageConsumer consumer = session.createConsumer(queue);


			for (ClientInfo client : clients) {
				QuotationRequestMessage quotationRequest = new QuotationRequestMessage(new Random().nextLong(), client);
				Message request = session.createObjectMessage(quotationRequest);
				cache.put(quotationRequest.id, quotationRequest.info);
				//sent the request to ClientInfo queue
				producer.send(request);
				// receive from ClientApplication queue
				boolean check = true;
				//keep searching message that contain correct seed id
				while(check){
					Message message = consumer.receive();
					if (message instanceof ObjectMessage) {
						Object content = ((ObjectMessage) message).getObject();
						if (content instanceof ClientApplicationMessage) {
							ClientApplicationMessage response = (ClientApplicationMessage) content;
							if(quotationRequest.id== response.ID){
								displayProfile(response.info);
								check=false;
								for(Quotation quotation: response.quotations){
									displayQuotation(quotation);
								}
								System.out.println("\n");
							}
						}
						message.acknowledge();
					} else {
						System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
					}
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
				" | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
	};


}
