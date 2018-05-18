package modlayer;

public class Item {
	
	private String barcode;
	private String name;
	private int quantity;
	private Unit unit;
	
	public Item() {
		
	}

	public String getBarcode() {
		return barcode;
	}
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
	public Unit getUnit() {
		return unit;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
}
