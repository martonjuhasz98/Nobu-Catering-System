package modlayer;

public class Discrepancy {
	
	private Item item;
	private int quantity;
	private Session session;
	
	public Discrepancy() {
		
	}

	public Item getItem() {
		return item;
	}
	public int getQuantity() {
		return quantity;
	}
	public Session getSession() {
		return session;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}
