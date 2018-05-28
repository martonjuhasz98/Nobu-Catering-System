package ctrllayer;

import modlayer.Employee;

public class SessionSingleton {

	private static final int ACCESSLEVEL = 3;
	private static SessionSingleton instance;
	Employee loggedIn;
    
	private SessionSingleton() {
	}

	public static SessionSingleton getInstance() {
		if (instance == null)
			instance = new SessionSingleton();
		return instance;
	}

	public boolean logIn(String username, String password) {
		loggedIn = new EmployeeController().getEmployee(username, password);
		return (loggedIn != null && loggedIn.getAccessLevel() <= ACCESSLEVEL) ? true : false;
	}
	
	public Employee getEmployee() {
		return loggedIn;
	}
}
