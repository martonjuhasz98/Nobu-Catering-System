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
import modlayer.City;
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
		
		String query = "SELECT * FROM [Invoice_View] "
					+ "WHERE invoiceDateDelivered IS " + (delivered ? "NOT" : "") + " NULL";
		try {
			
			Statement ps = con.createStatement();
			ps.setQueryTimeout(5);
			
			Invoice invoice;
			ResultSet results = ps.executeQuery(query);
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

		String query = "SELECT * FROM [Invoice_View] "
						+ "WHERE invoiceDateDelivered IS " + (delivered ? "NOT" : "") + " NULL "
						+ "AND (invoiceId LIKE ? "
						+ "OR employeeName LIKE ? "
						+ "OR supplierName LIKE ?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ps.setString(3, "%" + keyword + "%");
			
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
		
		String query = "SELECT * FROM [Invoice_View] "
						+ "WHERE invoiceId = ?";
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
		String query = "";
		
		try {
			DBConnection.startTransaction();
			
			query = "INSERT INTO [Invoice] (employee_cpr, supplier_cvr) VALUES (?, ?)";
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
					
					boolean success = ps.executeUpdate() > 0;
					ps.close();
					if (!success) {
						throw new SQLException();
					}
				}
				catch (SQLException e) {
					System.out.println("InvoiceItem was not inserted!");
					
					throw e;
				}
			}
			
			//Transaction
			Transaction transaction = new Transaction();
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
			return -1;
		}
		
		return id;
	}
	
	@Override
	public boolean confirmInvoice(Invoice invoice) {
		boolean success = false;
		
		String query =
				"UPDATE [Invoice] "
			  + "SET date_delivered = GETDATE() "
			  + "WHERE id = ?";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, invoice.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			//Update Item quantity
			String itemBarcode;
			double quantity;
			
			for (InvoiceItem item : invoice.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				quantity = item.getQuantity();
				if (quantity == 0) continue;
				
				query =   "UPDATE [Item] "
						+ "SET quantity = quantity + ? "
						+ "WHERE barcode = ?";
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
		String query = "";
		
		try {
			PreparedStatement ps;
			
			//Invoice
			query = "DELETE FROM [Invoice] WHERE id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, invoice.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			//Transaction
			query = "DELETE FROM [Transaction] WHERE id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, invoice.getTransaction().getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			if (!success) {
				throw new SQLException("Transaction was not deleted!");
			}
		}
		catch (SQLException e) {
			System.out.println("Invoice was not deleted!");
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
			
			//City
			City city = new City();
			city.setZipCode(results.getString("employeeCityZipCode"));
			city.setName(results.getString("employeeCityName"));
			
			//Employee
			Employee employee = new Employee();
			employee.setCpr(results.getString("employeeCpr"));
			employee.setName(results.getString("employeeName"));
			employee.setUsername(results.getString("employeeUsername"));
			employee.setPassword(results.getString("employeePassword"));
			employee.setAddress(results.getString("employeeAddress"));
			employee.setCity(city);
			employee.setPhone(results.getString("employeePhone"));
			employee.setEmail(results.getString("employeeEmail"));
			employee.setAccessLevel(results.getInt("employeeAccessLevel"));
			
			//City
			city = new City();
			city.setZipCode(results.getString("supplierCityZipCode"));
			city.setName(results.getString("supplierCityName"));

			//Supplier
			Supplier supplier = new Supplier();
			supplier.setCvr(results.getString("supplierCvr"));
			supplier.setName(results.getString("supplierName"));
			supplier.setAddress(results.getString("supplierAddress"));
			supplier.setCity(city);
			supplier.setPhone(results.getString("supplierPhone"));
			supplier.setEmail(results.getString("supplierEmail"));
				
			//Transaction
			Transaction transaction = new Transaction();
			transaction.setId(results.getInt("invoiceId"));
			transaction.setAmount(results.getDouble("transactionAmount"));
			transaction.setType(TransactionType.getType(results.getInt("transactionTypeId")));
			transaction.setTimestamp(results.getDate("transactionTimestamp"));
			
			//Invoice
			invoice.setId(results.getInt("invoiceId"));
			invoice.setTimestamp(results.getDate("invoiceTimestamp"));
			invoice.setDateDelivered(results.getDate("invoiceDateDelivered"));
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
