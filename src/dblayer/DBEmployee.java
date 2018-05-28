package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBEmployee;
import modlayer.City;
import modlayer.Employee;

public class DBEmployee implements IFDBEmployee{

	private Connection con;
	
	public DBEmployee() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Employee> getEmployees() {
		ArrayList<Employee> employees = new ArrayList<>();
		
		String query =
			  "SELECT * FROM [Employee]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			Employee employee;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				employee = buildEmployee(results);
				employees.add(employee);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Employees were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return employees;
	}

	@Override
	public ArrayList<Employee> searchEmployees(String keyword) {
		ArrayList<Employee> employees = new ArrayList<Employee>();

		String query =
			    "SELECT * FROM [Employee]"
			  + "WHERE cpr LIKE ? "
			  + "OR name LIKE ? "
			  + "OR username LIKE ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ps.setString(3, "%" + keyword + "%");
			
			Employee employee;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				employee = buildEmployee(results);
				employees.add(employee);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Employees were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return employees;
	}

	@Override
	public Employee selectEmployee(String cpr) {
		Employee employee = null;
		
		String query =
				  "SELECT * FROM [Employee]"
				+ "WHERE cpr = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, cpr);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				employee = buildEmployee(results);
			}
		} catch (SQLException e) {
			System.out.println("Employee was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return employee;
	}
	
	@Override
	public Employee selectEmployee(String username,String password) {
		Employee employee = null;
		
		String query =
				  "SELECT * FROM [Employee]"
				+ "WHERE username = ? AND password = ? ";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				employee = buildEmployee(results);
			}
		} catch (SQLException e) {
			System.out.println("Employee was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return employee;
	}

	@Override
	public String insertEmployee(Employee employee) {
		String cpr = null;
		
		String query =
				  "INSERT INTO [Employee] "
				+ "(cpr, name, username, password, address, zip_code, phone, email, access_level) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, employee.getCpr());
			ps.setString(2, employee.getName());
			ps.setString(3, employee.getUsername());
			ps.setString(4, employee.getPassword());
			ps.setString(5, employee.getAddress());
			ps.setString(6, employee.getCity().getZipCode());
			ps.setString(7, employee.getPhone());
			ps.setString(8, employee.getEmail());
			ps.setInt(9, employee.getAccessLevel());
			
			if (ps.executeUpdate() > 0) {
				cpr = employee.getCpr();
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Employee was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return null;
		}
		
		return cpr;
	}

	@Override
	public boolean updateEmployee(Employee employee) {
		boolean success = false;
		
		String query =
				"UPDATE [Employee] "
			  + "SET name = ?, "
			  + "address = ?, "
			  + "username = ?, "
			  + "password = ?, "
			  + "zip_code = ?, "
			  + "phone = ?, "
			  + "email = ?,"
			  + "access_level = ? "
			  + "WHERE cpr = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, employee.getName());
			ps.setString(2, employee.getAddress());
			ps.setString(3, employee.getUsername());
			ps.setString(4, employee.getPassword());
			ps.setString(5, employee.getCity().getZipCode());
			ps.setString(6, employee.getPhone());
			ps.setString(7, employee.getEmail());
			ps.setInt(8, employee.getAccessLevel());
			ps.setString(9, employee.getCpr());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Employee was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}

	@Override
	public boolean deleteEmployee(Employee employee) {
		boolean success = false;
		
		String query = "DELETE FROM [Employee] WHERE cpr = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, employee.getCpr());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Employee was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private Employee buildEmployee(ResultSet results) throws SQLException {
		Employee employee = null;
		
		String query = "";
		try {
			
			//City
			DBCity dbCity = new DBCity();
			City city = dbCity.selectCity(results.getString("zip_code"));
			
			//Employee
			employee = new Employee();
			employee.setCpr(results.getString("cpr"));
			employee.setName(results.getString("name"));
			employee.setUsername(results.getString("username"));
			employee.setPassword(results.getString("password"));
			employee.setAddress(results.getString("address"));
			employee.setCity(city);
			employee.setPhone(results.getString("phone"));
			employee.setEmail(results.getString("email"));
			employee.setAccessLevel(results.getInt("access_level"));
		}
		catch (SQLException e) {
			System.out.println("Employee was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return employee;
	}
	
}
