package service.broker;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */

@RestController
public class LocalBrokerService{

    int application_id=0;

    private Map<Integer, ClientApplication> cache=new HashMap<>();
    @RequestMapping(value="/application",method= RequestMethod.POST)
    public ResponseEntity<ClientApplication> generateQuotations(@RequestBody ClientInfo info) throws Exception {
        List<Quotation> quotations=new LinkedList<>();
        //use rest Template to visit the url
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ClientInfo> request = new HttpEntity<>(info);
        //set url for each service
        URL AFQ = new URL("http://localhost:8080/quotations");
        URL DDQ = new URL("http://localhost:8081/quotations");
        URL GPQ = new URL("http://localhost:8082/quotations");
        //set the response number
        int AFQResponse=0;
        int DDQResponse=0;
        int GPQResponse=0;
        try {
            //start connection
            HttpURLConnection AFQConnection = (HttpURLConnection) AFQ.openConnection();
            HttpURLConnection DDQConnection = (HttpURLConnection) DDQ.openConnection();
            HttpURLConnection GPQConnection = (HttpURLConnection) GPQ.openConnection();
            //set the response number
            AFQResponse = AFQConnection.getResponseCode();
            DDQResponse = DDQConnection.getResponseCode();
            GPQResponse = GPQConnection.getResponseCode();
        } catch (IOException ignored) {
        }
        //if number is 405 means the service is on
        if(HttpURLConnection.HTTP_BAD_METHOD == AFQResponse){
            //send request to service
            quotations.add(restTemplate.postForObject("http://localhost:8080/quotations", request, Quotation.class));
        }

        if(HttpURLConnection.HTTP_BAD_METHOD == DDQResponse){
            //send request to service
            quotations.add(restTemplate.postForObject("http://localhost:8081/quotations", request, Quotation.class));
        }

        if(HttpURLConnection.HTTP_BAD_METHOD == GPQResponse){
            //send request to service
            quotations.add(restTemplate.postForObject("http://localhost:8082/quotations", request, Quotation.class));
        }

        ClientApplication clientAppliation=new ClientApplication(application_id,info,quotations);
        cache.put(application_id,clientAppliation);
        application_id++;
        return new ResponseEntity<>(clientAppliation, HttpStatus.CREATED);
    }

    @RequestMapping(value="/application/{id}",method= RequestMethod.GET)
    public ResponseEntity<ClientApplication> getApplication(@PathVariable int id){
        ClientApplication clientApplication=new ClientApplication();
        //loop the cache to find target client application
        for(int i=0;i<cache.size();i++){
            if(cache.containsKey(id)){
                clientApplication=cache.get(id);
            }
        }
        return new ResponseEntity<>(clientApplication, HttpStatus.CREATED);
    }

    //add all application to list
    @RequestMapping(value="/application",method= RequestMethod.GET)
    public ResponseEntity<List<ClientApplication>> getApplications(){
        List<ClientApplication> clientApplications=new ArrayList<>();

        for(int i=0;i<cache.size();i++){
            clientApplications.add(cache.get(i));
        }
        return new ResponseEntity<>(clientApplications, HttpStatus.CREATED);
    }
}
