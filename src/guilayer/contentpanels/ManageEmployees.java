package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.employees.*;

public class ManageEmployees extends JPanel {
	
	public ManageEmployees() {
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		EditEmployee EmployeeEditor = new EditEmployee();
		add(EmployeeEditor);
		
		ListEmployees listSup = new ListEmployees(EmployeeEditor);
		add(listSup);
	}
}
