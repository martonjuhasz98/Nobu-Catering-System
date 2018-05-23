package dblayer.interfaces;

import java.util.ArrayList;
import modlayer.*;

public interface IFDBInvoice {

    public ArrayList<Invoice> getInvoices(boolean delivered);
    public ArrayList<Invoice> searchInvoices(String keyword, boolean delivered);
    public Invoice selectInvoice(int id);
    public int insertInvoice(Invoice invoice);
    public boolean confirmInvoice(Invoice invoice);
    public boolean cancelInvoice(Invoice invoice);
}