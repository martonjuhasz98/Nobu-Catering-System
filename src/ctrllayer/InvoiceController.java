package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.IFDBInvoice;
import modlayer.*;

public class InvoiceController {

	private IFDBInvoice dbInvoice;

	public InvoiceController() {
		dbInvoice = new DBInvoice();
	}

	public ArrayList<Invoice> getPendingInvoices() {
		return dbInvoice.getInvoices(false);
	}
	public ArrayList<Invoice> getInvoiceHistory() {
		return dbInvoice.getInvoices(true);
	}
	public ArrayList<Invoice> searchPendingInvoices(String keyword) {
		return dbInvoice.searchInvoices(keyword, false);
	}
	public ArrayList<Invoice> searchInvoiceHistory(String keyword) {
		return dbInvoice.searchInvoices(keyword, true);
	}
	public Invoice getInvoice(int id) {
		return dbInvoice.selectInvoice(id);
	}
	public boolean createInvoice(Supplier supplier, ArrayList<InvoiceItem> items) {
		Invoice invoice = new Invoice();
		invoice.setSupplier(supplier);
		invoice.setPlacedBy(SessionSingleton.getInstance().getUser());
		invoice.setItems(items);
		
		boolean success = dbInvoice.insertInvoice(invoice) > 0;
		
		return success;
	}
	public boolean confirmInvoice(Invoice invoice) {
		boolean success = dbInvoice.confirmInvoice(invoice);
		
		return success;
	}
	public boolean cancelInvoice(Invoice invoice) {
		boolean success = dbInvoice.cancelInvoice(invoice);
		
		return success;
	}
}
