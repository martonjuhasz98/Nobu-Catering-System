package modlayer;

public enum TransactionType {

	CASH(0), CREDITCARD(1), VOUCHER(2);
	
	private int id;
	
	private TransactionType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
