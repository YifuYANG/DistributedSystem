import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AFQServer {
    public static void main(String[] args) {
        QuotationService afqService = new AFQService();
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            //String host = args[0];
            Registry registry = null;
            if (args.length == 0) {
                try {
                    registry = LocateRegistry.createRegistry(1099);
                } catch (Exception e) {
                    System.out.println("AULDFELLAS IS TRYING TO CONNECT TO EXISTING REGISTRY");
                    registry = LocateRegistry.getRegistry(1099);
                }
            } else {
                registry  = LocateRegistry.getRegistry(args[0],1099);
            }
            // Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService,0);
            // Register the object with the RMI Registry
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
            for (String name : registry.list()){
                System.out.println("name"+name);
            }
            System.out.println("AuldFellas Connecting");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}