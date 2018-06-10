package ctrllayer;

import modlayer.Employee;

public class SessionSingleton {

	private static final int ACCESSLEVEL = 3;
	private static SessionSingleton instance;
	private Employee user;
    
	private SessionSingleton() {
	}
	
	public static SessionSingleton getInstance() {
		if (instance == null)
			instance = new SessionSingleton();
		return instance;
	}

	public boolean logIn(String username, String password) {
		if (user != null) return true;
		
		user = new EmployeeController().getEmployee(username, password);
		if (user == null || user.getAccessLevel() > ACCESSLEVEL) {
			user = null;
			return false;
		}
		
		return true;
	}
	public Employee getUser() {
		return user;
	}
}
