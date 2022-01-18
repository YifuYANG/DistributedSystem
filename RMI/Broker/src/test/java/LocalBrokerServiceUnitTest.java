import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocalBrokerServiceUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        BrokerService bkrService = new LocalBrokerService();
        try {
            //create and connect to registry
            registry = LocateRegistry.createRegistry(1099);
            //create remote object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(bkrService, 0);
            //register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brokerService);
            //first object
            QuotationService afqService= new AFQService();
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService, 0);
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
            //second object
            QuotationService ddqService= new DDQService();
            QuotationService quotationService2 = (QuotationService) UnicastRemoteObject.exportObject(ddqService, 0);
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService2);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
    @Test
    public void connectionTest() throws Exception {
        //looking for the object with BROKER_SERVICE
        BrokerService service = (BrokerService)
                registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void GenerateClientInfo() throws RemoteException {
        //create a clientinfo class
        ClientInfo info = new ClientInfo("Mingqi",'m',100,20,20,"dashabi");
        LocalBrokerService brokerService = new LocalBrokerService();
        //call broker service function and looking for all the object that connect to RMI registry
        List<Quotation> quotationList = brokerService.getQuotations(info);
        Quotation quotation1=quotationList.get(0);
        Quotation quotation2=quotationList.get(1);
        assertEquals(quotation1.company,"Auld Fellas Ltd.");
        assertEquals(quotation2.company,"Dodgy Drivers Corp.");
    }
}
