package modlayer;

public class Adjustment {
	
	private Item item;
	private int quantity;
	private Session date;
	
	public Adjustment() {
		
	}

	public Item getItem() {
		return item;
	}
	public int getQuantity() {
		return quantity;
	}
	public Session getDate() {
		return date;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setDate(Session date) {
		this.date = date;
	}
}
