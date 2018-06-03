package TestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import ctrllayer.ItemController;
import ctrllayer.SessionSingleton;
import dblayer.DBConnection;
import modlayer.Item;

class TestStocktaking {
	ItemController ic = new ItemController();
	
	private Item item;
	double previous = 0;
	private DBConnection dbCon = DBConnection.getInstance();
	private Connection con = dbCon.getConnection();
	
	@BeforeClass
	void build() {
		SessionSingleton.getInstance().logIn("a", "a");
	}
	
	
	@Test
	void test() {
		build();
		
		ArrayList<Item> items = new ArrayList<>();
		
		item = ic.getItem("121667");
		previous = item.getQuantity();
		
		item.setQuantity(previous + 1);
		items.add(item);
		ic.createStocktaking(items);
		
		assertTrue(ic.getItem("121667").getQuantity() == (previous + 1));
		//somehow @AfterClass isnt working
		clearChanges();
	}
	
	@AfterClass
	void clearChanges() {
		build();
		
		ArrayList<Item> items = new ArrayList<>();
		
		item = ic.getItem("121667");
		item.setQuantity(previous);
		
		items.add(item);
		ic.createStocktaking(items);
		
		//CLEAR DB ENTRYS
		boolean success = false;
		String query = "DELETE FROM [Stocktaking] "
				+ "WHERE employee_cpr = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "1234567890");
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		assertTrue(success);
	}
}
