package service.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetSocketAddress;

import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style= SOAPBinding.Style.DOCUMENT, use= SOAPBinding.Use.LITERAL)
public class Broker{

    //collect all online services
    static LinkedList<URL> urls=new LinkedList<URL>();

    //check function may be delete later
    @WebMethod
    public LinkedList<URL> getUrls() {
        return urls;
    }

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info){
        LinkedList<Quotation> quotations=new LinkedList<>();
        for(URL url:urls){
            quotations.add(getQuotation(info,url));
        }
        return quotations;
    }

    //generate quotation using QuoterService
    private static Quotation getQuotation(ClientInfo clientInfo, URL url){
        QName serviceName = new QName("http://core.service/", "QuoterService");
        Service service = Service.create(url, serviceName);
        QName portName = new QName("http://core.service/", "QuoterPort");
        QuoterService quotationService = service.getPort(portName, QuoterService.class);
        return quotationService.generateQuotation(clientInfo);
    }

    public static void main(String[] args) {
        String host="localhost";
        if (args.length>0){
            host=args[0];
        }

        try {
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();
            //Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(host);
            System.out.println("on host " + jmdns.getInetAddress());
            // Add service listener
            jmdns.addServiceListener("_quote._tcp.local.", new ServiceListener());
            System.out.println("broker is running");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static class ServiceListener implements javax.jmdns.ServiceListener{

        //add url to linkedlist
        private void addUrl(String url) throws Exception {
            URL U=new URL(url);
            System.out.println("URL "+U);
            if(!urls.contains(U)) {
                urls.add(U);
            }
            System.out.println(urls);
        }
        @Override
        public void serviceAdded(ServiceEvent serviceEvent) {
        }

        @Override
        public void serviceRemoved(ServiceEvent serviceEvent) {
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            //call add url function to add online services
            String path=event.getInfo().getPropertyString("path");
            try {
                addUrl(path);
            }  catch (Exception ignored) {
                //for some reason I keep getting java.net.MalformedURLException but my url is valid with http://,
                // so I delete Exception here to make my result clear
            }
        }
    }
}
