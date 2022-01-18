import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BrokerServer {
    public static void main(String[] args) {
        //creating the registry is the responsibility of the broker
        Registry registry = null;
        try{
            //String host = args[0];
            //create registry and remote object for each service
            if (args.length == 0) {
                try {
                    registry = LocateRegistry.createRegistry(1099);
                } catch (Exception e) {
                    System.out.println("BROKER IS TRYING TO CONNECT TO EXISTING REGISTRY");
                    registry = LocateRegistry.getRegistry(1099);
                }
            } else {
                registry  = LocateRegistry.getRegistry(args[0],1099);
            }
            BrokerService broker=new LocalBrokerService();
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(broker,0);
            // Register the objects with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE,brokerService);
            System.out.println("Broker Connecting");
            for (String name : registry.list()){
                System.out.println("name"+name);
            }
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}