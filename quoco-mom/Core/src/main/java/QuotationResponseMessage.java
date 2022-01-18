
public class QuotationResponseMessage implements java.io.Serializable {
    public long id;
    public Quotation quotation;
    private static final long serialVersionUID =1L;
    public QuotationResponseMessage(long id, Quotation quotation) {
        this.id = id;
        this.quotation=quotation;
    }
}
