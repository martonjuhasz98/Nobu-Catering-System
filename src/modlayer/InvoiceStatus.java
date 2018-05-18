package modlayer;

public enum InvoiceStatus {

	ORDERED(0), IN_TRANSIT(1), DELIVERED(2);
	
	private int id;
	
	private InvoiceStatus(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
