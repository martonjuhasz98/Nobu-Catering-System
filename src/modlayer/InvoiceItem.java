package modlayer;

public class InvoiceItem {

	private Invoice invoice;
	private Item item;
	private int quantity;
	private double unitPrice;
	
	public InvoiceItem() {
		quantity = 0;
		unitPrice = 0.0;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	public Item getItem() {
		return item;
	}
	public int getQuantity() {
		return quantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof InvoiceItem)) {
	      return false;
	    }
	    InvoiceItem i = (InvoiceItem)o;
	    return i.invoice.equals(invoice) && i.item.equals(item);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
