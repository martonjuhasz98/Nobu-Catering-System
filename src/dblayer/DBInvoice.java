package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBInvoice;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;
import modlayer.Invoice;
import modlayer.InvoiceItem;

public class DBInvoice implements IFDBInvoice {
	
	private Connection con;
	
	public DBInvoice() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Invoice> getInvoices(boolean delivered) {
		ArrayList<Invoice> invoices = new ArrayList<>();
		
		String query = "SELECT * FROM [Invoice] WHERE is_delivered = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(0, delivered);
			
			Invoice invoice;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				invoice = buildInvoice(results);
				invoices.add(invoice);
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("Invoices were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return invoices;
	}
	
	@Override
	public ArrayList<Invoice> searchInvoices(String keyword, boolean delivered) {
		ArrayList<Invoice> invoices = new ArrayList<Invoice>();

		String query =
			  "SELECT i.barcode AS invoiceBarcode, "
			+ "i.name AS invoiceName, "
			+ "i.quantity AS invoiceQuantity, "
			+ "u.abbreviation AS unitAbbrevation, "
			+ "u.name AS unitName, "
			+ "c.id AS categoryId, "
			+ "c.name AS categoryName "
			+ "FROM [Invoice] AS i "
			+ "INNER JOIN [Unit] AS u "
			+ "ON i.unit = u.abbreviation "
			+ "INNER JOIN [Invoice_Category] AS c "
			+ "ON i.category_id = c.id "
			+ "WHERE i.barcode LIKE ? "
			+ "OR i.name LIKE ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			
			Invoice invoice;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				invoice = buildInvoice(results);
				invoices.add(invoice);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Invoices were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return invoices;
	}
	
	@Override
	public Invoice selectInvoice(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int insertInvoice(Invoice invoice) {
		int id = -1;
		
		String query = "INSERT INTO [Invoice] (employee_cpr) VALUES (?)";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            invoice.setId(id);
	            }
			}
			ps.close();
			
			String itemBarcode;
			double unitPrice;
			double quantity;
			
			//Discrepancies
			for (InvoiceItem item : invoice.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				unitPrice = item.getUnitPrice();
				quantity = item.getQuantity();
				
				query =   "INSERT INTO [Item] "
						+ "(invoice_id, item_barcode, quantity) "
						+ "VALUES (?, ?, ? - ("
						+ "	SELECT quantity"
						+ "	FROM [Item]"
						+ "	WHERE barcode = ?))";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setInt(1, id);
					ps.setString(2, itemBarcode);
					ps.setDouble(3, quantity);
					ps.setString(4, itemBarcode);
					
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("Item was not inserted!");
					
					throw e;
				}
			}
			
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Invoice was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
		}
		
		return id;
	}
	
	@Override
	public boolean confirmInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean cancelInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private Invoice buildInvoice(ResultSet results) throws SQLException {
		Invoice invoice = null;
		
		String query = "";
		try {
			
			//Unit
			Unit unit = new Unit();
			unit.setAbbr(results.getString("unitAbbrevation"));
			unit.setName(results.getString("unitName"));
			
			//InvoiceCategory
			InvoiceCategory category = new InvoiceCategory();
			category.setId(results.getInt("categoryId"));
			category.setName(results.getString("categoryName"));
			
			//Invoice
			invoice = new Invoice();
			invoice.setBarcode(results.getString("invoiceBarcode"));
			invoice.setName(results.getString("invoiceName"));
			invoice.setQuantity(results.getDouble("invoiceQuantity"));
			invoice.setUnit(unit);
			invoice.setCategory(category);
		}
		catch (SQLException e) {
			System.out.println("Invoice was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return invoice;
	}
}
