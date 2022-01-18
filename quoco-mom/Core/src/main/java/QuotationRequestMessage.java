public class QuotationRequestMessage implements java.io.Serializable {
    public long id;
    public ClientInfo info;
    public QuotationRequestMessage(long id, ClientInfo info) {
        this.id = id;
        this.info = info;
    }
}