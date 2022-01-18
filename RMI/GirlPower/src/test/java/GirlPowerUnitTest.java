import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import org.junit.*;
import static org.junit.Assert.assertNotNull;
public class GirlPowerUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService gpqService = new GPQService();
        try {
            //create and connect to registry
            registry = LocateRegistry.createRegistry(1099);
            //create remote object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(gpqService, 0);
            //register the object with the RMI Registry
            registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
    @Test
    public void connectionTest() throws Exception {
        //looking for the object with AULD_FELLAS_SERVICE
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRL_POWER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        //create a clientinfo class
        ClientInfo info = new ClientInfo("Mingqi",'m',100,20,20,"dashabi");
        GPQService service = new GPQService();
        //call the function from GPQService and get the info
        String company=service.generateQuotation(info).company;
        String reference=service.generateQuotation(info).reference;
        Double price=service.generateQuotation(info).price;
        System.out.println(company+" "+reference+" "+price);
        assertNotNull(service.generateQuotation(info));
    }
}