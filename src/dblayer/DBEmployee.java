package dblayer;

import java.sql.Connection;
import java.util.ArrayList;

import dblayer.interfaces.IFDBEmployee;
import modlayer.Employee;

public class DBEmployee implements IFDBEmployee{

	private Connection con;
	
	public DBEmployee() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Employee> getEmployees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Employee> searchEmployees(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee selectEmployee(String cpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

}
