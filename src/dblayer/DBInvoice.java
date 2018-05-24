package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBInvoice;
import modlayer.Supplier;
import modlayer.Transaction;
import modlayer.TransactionType;
import modlayer.Employee;
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
			ps.setBoolean(1, delivered);
			
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
			  "SELECT i.* "
			+ "FROM [Invoice] AS i "
			+ "INNER JOIN [Employee] AS e "
			+ "ON i.employee_cpr = e.cpr "
			+ "INNER JOIN [Supplier] AS s "
			+ "ON i.supplier_cvr = s.cvr "
			+ "WHERE i.is_delivered = ? "
			+ "AND (i.id LIKE ? "
			+ "OR e.name LIKE ? "
			+ "OR s.name LIKE ?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(1, delivered);
			ps.setString(2, "%" + keyword + "%");
			ps.setString(3, "%" + keyword + "%");
			ps.setString(4, "%" + keyword + "%");
			
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
		Invoice invoice = null;
		
		String query = "SELECT * FROM [Invoice] WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				invoice = buildInvoice(results);
			}
		} catch (SQLException e) {
			System.out.println("Invoice was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return invoice;
	}
	
	@Override
	public int insertInvoice(Invoice invoice) {
		int id = -1;
		
		String query = "INSERT INTO [Invoice] (employee_cpr, supplier_cvr) VALUES (?, ?)";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setString(1, invoice.getPlacedBy().getCpr());
			ps.setString(2, invoice.getSupplier().getCvr());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            invoice.setId(id);
	            }
			}
			ps.close();
			
			//InvoiceItems
			double totalPrice = 0;
			String itemBarcode;
			double unitPrice;
			double quantity;
			
			for (InvoiceItem item : invoice.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				unitPrice = item.getUnitPrice();
				quantity = item.getQuantity();
				totalPrice += quantity * unitPrice;
				
				query =   "INSERT INTO [Invoice_Item] "
						+ "(item_barcode, invoice_id, quantity, unit_price) "
						+ "VALUES (?, ?, ?, ?)";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setString(1, itemBarcode);
					ps.setInt(2, id);
					ps.setDouble(3, quantity);
					ps.setDouble(4, unitPrice);
					
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("InvoiceItem was not inserted!");
					
					throw e;
				}
			}
			
			//Transaction
			Transaction transaction = new Transaction();
			transaction.setId(id);
			transaction.setAmount(totalPrice);
			transaction.setType(TransactionType.ACCOUNT);
			DBTransaction dbTransaction = new DBTransaction();
			if (dbTransaction.insertTransaction(transaction) < 0) {
				throw new SQLException("Transaction was not inserted!");
			}
			ps.close();
			
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
		boolean success = false;
		
		String query =
				"UPDATE [Invoice] "
			  + "SET is_delivered = ?, "
			  + "WHERE id = ?";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(1, true);
			ps.setInt(1, invoice.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			//Update Item quantity
			String itemBarcode;
			double quantity;
			
			for (InvoiceItem item : invoice.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				quantity = item.getQuantity();
				
				query =   "UPDATE [Item] "
						+ "SET quantity = quantity + ? "
						+ "WHERE barcode = ?))";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setDouble(1, quantity);
					ps.setString(2, itemBarcode);
					
					if (ps.executeUpdate() < 1) {
						throw new SQLException();
					}
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("InvoiceItem was not inserted!");
					
					throw e;
				}
			}
		
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Item was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
		}
		
		return success;
	}
	
	@Override
	public boolean cancelInvoice(Invoice invoice) {
		boolean success = false;
		
		String query = "DELETE FROM [Invoice] WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, invoice.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Item was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private Invoice buildInvoice(ResultSet results) throws SQLException {
		Invoice invoice = null;
		
		String query = "";
		try {
			invoice = new Invoice();
			
			//Employee
			DBEmployee dbEmployee = new DBEmployee();
			Employee employee = dbEmployee.selectEmployee(results.getString("employee_cpr"));
			
			//Supplier
			DBSupplier dbSupplier = new DBSupplier();
			Supplier supplier = dbSupplier.selectSupplier(results.getString("supplier_cvr"));
				
			//Transaction
			DBTransaction dbTransaction = new DBTransaction();
			Transaction transaction = dbTransaction.selectTransaction(results.getInt("id"));
			
			//Invoice
			invoice.setId(results.getInt("id"));
			invoice.setDelivered(results.getBoolean("is_delivered"));
			invoice.setTimestamp(results.getDate("timestamp"));
			invoice.setDateDelivered(results.getDate("date_delivered"));
			invoice.setPlacedBy(employee);
			invoice.setSupplier(supplier);
			invoice.setTransaction(transaction);
			
			//InvoiceItem
			ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();
			query =   "SELECT item_barcode, quantity, unit_price "
					+ "FROM [Invoice_item] "
					+ "WHERE invoice_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, invoice.getId());
			
			DBItem dbItem = new DBItem();
			InvoiceItem item;
			results = ps.executeQuery();
			while (results.next()) {
				item = new InvoiceItem();
				item.setInvoice(invoice);
				item.setItem(dbItem.selectItem(results.getString("item_barcode")));
				item.setQuantity(results.getInt("quantity"));
				item.setUnitPrice(results.getDouble("unit_price"));
				items.add(item);
			}
			ps.close();
			invoice.setItems(items);
		}
		catch (SQLException e) {
			System.out.println("Invoice was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return invoice;
	}
}
