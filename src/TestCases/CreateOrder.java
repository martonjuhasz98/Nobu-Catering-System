package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.jupiter.api.Test;

import ctrllayer.MenuItemController;
import ctrllayer.OrderController;
import ctrllayer.SessionSingleton;
import dblayer.DBConnection;
import modlayer.Order;
import modlayer.OrderMenuItem;

class CreateOrder {
	private DBConnection dbCon = DBConnection.getInstance();
	private Connection con = dbCon.getConnection();
	
	OrderController oc = new OrderController();
	MenuItemController mic = new MenuItemController();
	
	OrderMenuItem omi = new OrderMenuItem();
	private int OrderID = 0;

	@Test
	void test() {
		Order testOrder = new Order();
		SessionSingleton.getInstance().logIn("a", "a");
		ArrayList<OrderMenuItem> menuItems = new ArrayList<>();
		
		omi.setMenuItem(mic.getMenuItem(23));
		omi.setQuantity(1);
		omi.setFinished(true);
		omi.setOrder(testOrder);
		menuItems.add(omi);
		testOrder.setTableNo(1);
		testOrder.setItems(menuItems);
		testOrder.setId(oc.createOrder(0));
		OrderID = testOrder.getId();
		
		
		oc.updateOrder(testOrder);
		oc.addOrderMenuItem(omi,true);
		
		assertTrue(!oc.getOrder(testOrder.getId()).getItems().isEmpty());
		assertTrue(oc.getOrder(testOrder.getId()).getTransaction().getAmount() == 0 );
		clearDB();
	}
	
	@AfterClass
	void clearDB() {
		boolean success = false;
		String query = "DELETE FROM [Order] "
				+ "WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, OrderID);
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(success);
	}

}
