package ctrllayer;

import modlayer.Employee;

public class SessionSingleton {

	private static final int ACCESSLEVEL = 0;
	private static SessionSingleton instance;
	Employee loggedIn;

	private SessionSingleton() {
		instance = new SessionSingleton();
	}

	public static SessionSingleton getInstance() {
		if (instance == null)
			new SessionSingleton();
		return instance;
	}

	public boolean logIn(String username, String password) {
		loggedIn = new EmployeeController().getEmployee(username, password);
		return (loggedIn != null && loggedIn.getAccessLevel() <= ACCESSLEVEL) ? true : false;
	}
}
