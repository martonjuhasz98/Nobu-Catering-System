package modlayer;

public class Discrepancy {
	
	private Item item;
	private double quantity;
	private Stocktaking stocktaking;
	
	public Discrepancy() {
		quantity = 0.0;
	}

	public Item getItem() {
		return item;
	}
	public double getQuantity() {
		return quantity;
	}
	public Stocktaking getStocktaking() {
		return stocktaking;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public void setStocktaking(Stocktaking stocktaking) {
		this.stocktaking = stocktaking;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof Discrepancy)) {
	      return false;
	    }
	    Discrepancy d = (Discrepancy)o;
	    return d.item.equals(item) && d.stocktaking.equals(stocktaking);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
