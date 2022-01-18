package service.core;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@WebService
public interface BrokerService {

    @WebMethod
    LinkedList<Quotation> getQuotations(ClientInfo info);

}
