package service.core;

import java.util.List;

public class ClientApplication {
    private int id;
    private ClientInfo info;
    private List<Quotation> quotations;

    public ClientApplication(int id, ClientInfo info, List<Quotation> quotations) {
        this.id = id;
        this.info = info;
        this.quotations = quotations;
    }

    public ClientApplication() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClientInfo getInfo() {
        return info;
    }

    public void setInfo(ClientInfo info) {
        this.info = info;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }
}
