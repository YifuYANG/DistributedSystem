import java.util.List;

public class ClientApplicationMessage implements java.io.Serializable {
    public long ID;
    public ClientInfo info;
    public List<Quotation> quotations;


    public ClientApplicationMessage(long id,ClientInfo info, List<Quotation> quotations) {
        this.ID = id;
        this.info = info;
        this.quotations=quotations;
    }
}
