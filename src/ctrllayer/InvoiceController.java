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
	public boolean createInvoice(Supplier supplier, Employee employee) {
		Invoice invoice = new Invoice();
		invoice.setSupplier(supplier);
		invoice.setPlacedBy(employee);
		
		boolean success = dbInvoice.insertInvoice(invoice) > 0;
		
		return success;
	}
	public boolean confirmInvoice(Invoice invoice) {
		invoice.setDelivered(true);
		boolean success = dbInvoice.confirmInvoice(invoice);
		
		return success;
	}
	public boolean cancelInvoice(Invoice invoice) {
		boolean success = dbInvoice.cancelInvoice(invoice);
		
		return success;
	}
}
