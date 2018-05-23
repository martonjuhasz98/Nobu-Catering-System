package modlayer;

public enum TransactionType {

	CASH(1), CREDITCARD(2), VOUCHER(3), ACCOUNT(4);
	
	private int id;
	
	private TransactionType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
