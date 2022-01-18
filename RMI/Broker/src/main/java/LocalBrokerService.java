import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;


/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {
	public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
		List<Quotation> quotations = new LinkedList<Quotation>();
		Registry registry = null;
		//connect to registry
		try {
			registry  = LocateRegistry.getRegistry(1099);
		} catch (Exception e){
			System.out.println(e);
		}
		try {
			for (String name : registry.list()) {
				//looking for company's services and add result Quotations to the list
				if (name.startsWith("qs-")) {
					QuotationService service = (QuotationService) registry.lookup(name);
					quotations.add(service.generateQuotation(info));
				}
			}
		} catch (Exception e){
			System.out.println(e);
		}
		return quotations;
	}
}