package modlayer;

import java.util.Date;

public class Payment {

	private int id;
	private PaymentType type;
	private double amount;
	private Date timestamp;
	
	public Payment() {
		
	}

	public int getId() {
		return id;
	}
	public PaymentType getType() {
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
	public void setType(PaymentType type) {
		this.type = type;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
