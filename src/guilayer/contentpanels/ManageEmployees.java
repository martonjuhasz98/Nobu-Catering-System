package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import guilayer.inventory.*;
import guilayer.employees.EditEmployee;
import guilayer.employees.ListEmployees;

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
