package modlayer;

import java.util.Date;

public class Transaction {

	private int id;
	private TransactionType type;
	private double amount;
	private Date timestamp;
	
	public Transaction() {
		id = -1;
	}

	public int getId() {
		return id;
	}
	public TransactionType getType() {
		return type;
	}
	public double getAmount() {
		return amount;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Transaction)) {
	      return false;
	    }
	    Transaction i = (Transaction)o;
	    return i.id == id;
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
