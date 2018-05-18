package modlayer;

public class InvoiceItem {

	private Item item;
	private int quantity;
	private double unitPrice;
	
	public InvoiceItem() {
		
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
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
