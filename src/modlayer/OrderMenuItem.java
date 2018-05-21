package modlayer;

public class OrderMenuItem {

	private Order order;
	private MenuItem menuItem;
	private int quantity;
	private boolean isFinished;
	
	public OrderMenuItem() {
		quantity = 0;
	}

	public Order getOrder() {
		return order;
	}
	public MenuItem getMenuItem() {
		return menuItem;
	}
	public int getQuantity() {
		return quantity;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setFinished(boolean finished) {
		this.isFinished = finished;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this) {
	      return true;
	    }
	    if (!(o instanceof OrderMenuItem)) {
	      return false;
	    }
	    OrderMenuItem i = (OrderMenuItem)o;
	    return i.order.equals(order) && i.menuItem.equals(menuItem);
	}
	@Override
	public int hashCode() {
	    return 0;
	}
}
