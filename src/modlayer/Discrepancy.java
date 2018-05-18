package modlayer;

public class Discrepancy {
	
	private Item item;
	private double quantity;
	private Session session;
	
	public Discrepancy() {
		
	}

	public Item getItem() {
		return item;
	}
	public double getQuantity() {
		return quantity;
	}
	public Session getSession() {
		return session;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}
