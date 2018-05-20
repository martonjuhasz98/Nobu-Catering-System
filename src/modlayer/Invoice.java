package modlayer;

import java.util.Date;

public class Invoice {

	private int id;
	private InvoiceStatus status;
	private Date timestamp;
	private Date dateDelivered;
	private Employee createdBy;
	private Supplier supplier;
	private Payment payment;
	
	public Invoice() {
		
	}

	public int getId() {
		return id;
	}
	public InvoiceStatus getStatus() {
		return status;
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
	public Payment getPayment() {
		return payment;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setStatus(InvoiceStatus status) {
		this.status = status;
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
	public void setPayment(Payment payment) {
		this.payment = payment;
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
