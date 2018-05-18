package modlayer;

public enum PaymentType {

	CASH(0), CREDITCARD(1), VOUCHER(2);
	
	private int id;
	
	private PaymentType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
