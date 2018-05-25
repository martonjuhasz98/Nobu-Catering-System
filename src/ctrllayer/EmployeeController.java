package ctrllayer;

import java.nio.charset.Charset;
import java.util.ArrayList;

import dblayer.*;
import dblayer.interfaces.IFDBCity;
import dblayer.interfaces.IFDBEmployee;
import modlayer.*;

public class EmployeeController {

	private IFDBEmployee dbEmployee;
	private IFDBCity dbCity = new DBCity();

	public EmployeeController() {
		dbEmployee = new DBEmployee();
	}

	public ArrayList<Employee> getEmployees() {
		return dbEmployee.getEmployees();
	}

	public ArrayList<Employee> searchEmployees(String keyword) {
		return dbEmployee.searchEmployees(keyword);
	}

	public Employee getEmployee(String cpr) {
		return dbEmployee.selectEmployee(cpr);
	}

	public Employee getEmployee(String username, String password) {
		return dbEmployee.selectEmployee(username, hash(password));
	}

	public boolean createEmployee(String cpr, String name, String username, String password, String address, City city,
			String phone, String email, int accessLevel) {
		Employee employee = new Employee();
		employee.setCpr(cpr);
		employee.setName(name);
		employee.setUsername(username);
		employee.setPassword(password);
		employee.setAddress(address);
		employee.setCity(city);
		employee.setPhone(phone);
		employee.setEmail(email);
		employee.setAccessLevel(accessLevel);

		boolean success = dbEmployee.insertEmployee(employee) != null;

		return success;
	}

	public boolean updateEmployee(Employee employee) {
		boolean success = dbEmployee.updateEmployee(employee);

		return success;
	}

	public boolean deleteEmployee(Employee employee) {
		boolean success = dbEmployee.deleteEmployee(employee);

		return success;
	}
	
	public City getCity(String zipCode) {
		return dbCity.selectCity(zipCode);
	}

	public String hash(String text) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(text.getBytes(Charset.forName("UTF8")));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			return null;
		}
	}
}
