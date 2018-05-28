package modlayer;

import java.util.ArrayList;
import java.util.Date;

public class Invoice {

	private int id;
	private Date timestamp;
	private Date dateDelivered;
	private Employee createdBy;
	private Supplier supplier;
	private Transaction transaction;
	private ArrayList<InvoiceItem> items;
	
	public Invoice() {
		id = -1;
	}

	public int getId() {
		return id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public Date getDateDelivered() {
		return dateDelivered;
	}
	public Employee getPlacedBy() {
		return createdBy;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public ArrayList<InvoiceItem> getItems() {
		return items;
	} 
	public void setId(int id) {
		this.id = id;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setDateDelivered(Date dateDelivered) {
		this.dateDelivered = dateDelivered;
	}
	public void setPlacedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	public void setItems(ArrayList<InvoiceItem> items) {
		this.items = items;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Invoice)) {
	      return false;
	    }
	    Invoice i = (Invoice)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
