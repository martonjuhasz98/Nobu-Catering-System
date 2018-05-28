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
		user = new EmployeeController().getEmployee(username, password);
		return (user != null && user.getAccessLevel() <= ACCESSLEVEL) ? true : false;
	}
	
//	public Employee getEmployee() {
//		return user;
//	}
	public Employee getUser() {
		return user;
	}
}
