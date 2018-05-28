package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.IFDBOrder;
import modlayer.*;

public class OrderController {

	private IFDBOrder dbOrder;

	public OrderController() {
		dbOrder = new DBOrder();
	}

	public ArrayList<Order> getUnpaidOrders() {
		return dbOrder.getOrders(false);
	}
	public ArrayList<Order> getOrderHistory() {
		return dbOrder.getOrders(true);
	}
	public ArrayList<Order> searchUnpaidOrders(String keyword) {
		return dbOrder.searchOrders(keyword, false);
	}
	public ArrayList<Order> searchOrderHistory(String keyword) {
		return dbOrder.searchOrders(keyword, true);
	}
	public Order getOrder(int id) {
		return dbOrder.selectOrder(id);
	}
	
	public boolean createOrder(int tableNo, ArrayList<OrderMenuItem> items) {
		Employee employee = SessionSingleton.getInstance().getUser();
		
		Order order = new Order();
		order.setTableNo(tableNo);
		order.setEmployee(employee);
		order.setItems(items);
		
		boolean success = dbOrder.insertOrder(order) > 0;
		
		return success;
	}
	public boolean payOrder(TransactionType payment, Order order) {
		double totalPrice = 0;
		for (OrderMenuItem item : order.getItems()) {
			totalPrice += item.getQuantity() * item.getMenuItem().getPrice();
		}
		
		Transaction transaction = new Transaction();
		transaction.setAmount(totalPrice);
		transaction.setType(payment);
		order.setTransaction(transaction);
		
		boolean success = dbOrder.payOrder(order);
		
		return success;
	}
	public boolean cancelOrder(Order order) {
		boolean success = dbOrder.cancelOrder(order);
		
		return success;
	}
}
