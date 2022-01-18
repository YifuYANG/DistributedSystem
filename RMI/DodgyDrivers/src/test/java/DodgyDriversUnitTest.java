import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
public class DodgyDriversUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService dgyService = new DDQService();
        try {
            //create and connect to registry
            registry = LocateRegistry.createRegistry(1099);
            //create remote object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(dgyService, 0);
            //register the object with the RMI Registry
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
    @Test
    public void connectionTest() throws Exception {
        //looking for the object with DODGY_DRIVERS_SERVICE
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        //create a clientinfo class
        ClientInfo info = new ClientInfo("Mingqi",'m',100,20,20,"dashabi");
        DDQService service = new DDQService();
        //call the function from DDQService and get the info
        String company=service.generateQuotation(info).company;
        String reference=service.generateQuotation(info).reference;
        Double price=service.generateQuotation(info).price;
        System.out.println(company+" "+reference+" "+price);
        assertNotNull(service.generateQuotation(info));
    }
}