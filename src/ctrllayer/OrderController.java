package ctrllayer;

import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.IFDBOrder;
import dblayer.interfaces.IFDBOrderMenuItem;
import modlayer.*;

public class OrderController {

	private IFDBOrder dbOrder;
	private IFDBOrderMenuItem dbItem;

	public OrderController() {
		dbOrder = new DBOrder();
		dbItem = new DBOrderMenuItem();
	}

	//Orders
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
	public int createOrder(int tableNo) {
		Employee employee = SessionSingleton.getInstance().getUser();
		
		Order order = new Order();
		order.setTableNo(tableNo);
		order.setEmployee(employee);
		
		int id = dbOrder.insertOrder(order);
		
		return id;
	}
	public boolean updateOrder(Order order) {
		boolean success = dbOrder.updateOrder(order);
		
		return success;
	}
	public boolean payOrder(TransactionType payment, Order order) {
		Transaction transaction = new Transaction();
		transaction.setType(payment);
		order.setTransaction(transaction);
		
		boolean success = dbOrder.payOrder(order);
		
		return success;
	}
	public boolean cancelOrder(Order order) {
		boolean success = dbOrder.cancelOrder(order);
		
		return success;
	}
	
	//OrderMenuItems
	public boolean canAddOrderMenuItem(OrderMenuItem item) {
		boolean canCreate = dbItem.canCreateOrderMenuItem(item);
		
		return canCreate;
	}
	public boolean hasOrderMenuItem(OrderMenuItem item) {
		boolean success = dbItem.hasOrderMenuItem(item);
		
		return success;
	}
	public boolean addOrderMenuItem(OrderMenuItem item, boolean force) {
		boolean success = dbItem.insertOrderMenuItem(item, force);
		
		return success;
	}
	public boolean editOrderMenuItem(OrderMenuItem item, boolean force) {
		boolean success = dbItem.updateOrderMenuItem(item, force);
		
		return success;
	}
	public boolean confirmOrderMenuItem(OrderMenuItem item) {
		boolean success = dbItem.confirmOrderMenuItem(item);
		
		return success;
	}
	public boolean removeOrderMenuItem(OrderMenuItem item) {
		boolean success = dbItem.deleteOrderMenuItem(item);
		
		return success;
	}
}
