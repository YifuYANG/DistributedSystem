import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
public class AuldFellasUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService afqService = new AFQService();
        try {
            //create and connect to registry
            registry = LocateRegistry.createRegistry(1099);
            //create remote object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService, 0);
            //register the object with the RMI Registry
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
    @Test
    public void connectionTest() throws Exception {
        //looking for the object with AULD_FELLAS_SERVICE
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        //create a clientinfo class
        ClientInfo info = new ClientInfo("Mingqi",'m',100,20,20,"dashabi");
        AFQService service = new AFQService();
        //call the function from AFQService and get the info
        String company=service.generateQuotation(info).company;
        String reference=service.generateQuotation(info).reference;
        Double price=service.generateQuotation(info).price;
        System.out.println(company+" "+reference+" "+price);
        assertNotNull(service.generateQuotation(info));
    }
}