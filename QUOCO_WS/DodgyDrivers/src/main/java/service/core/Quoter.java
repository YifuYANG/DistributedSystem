package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style= SOAPBinding.Style.RPC, use= SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService{
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";

	/**
	 * Quote generation:
	 * 5% discount per penalty point (3 points required for qualification)
	 * 50% penalty for <= 3 penalty points
	 * 10% discount per year no claims
	 */
	@WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 800 and 1000
		double price = generatePrice(800, 200);

		// 5% discount per penalty point (3 points required for qualification)
		int discount = (info.points > 3) ? 5*info.points:-50;

		// Add a no claims discount
		discount += getNoClaimsDiscount(info);

		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 10*info.noClaims;
	}

	//advertise the service
	private static void Advertise(String host){
		try {
			String config = "path=http://"+host+":9002/quotation?wsdl";
			// Create a JmDNS instance
			JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
			jmdns = JmDNS.create(InetAddress.getByName("192.168.0.220"), "Yifu");
			System.out.println("on host " + InetAddress.getLocalHost());
			// Register a service
			ServiceInfo serviceInfo = ServiceInfo.create("_quote._tcp.local.", "sqs", 9002, config);
			jmdns.registerService(serviceInfo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		Thread.sleep(5000);
		String host="localhost";
		if (args.length>0){
			host=args[0];
		}
		try {
			Endpoint endpoint = Endpoint.create(new Quoter());
			HttpServer server = HttpServer.create(new InetSocketAddress(9002), 5);
			server.setExecutor(Executors.newFixedThreadPool(5));
			HttpContext context = server.createContext("/quotation");
			endpoint.publish(context);
			server.start();
			Advertise(host);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
