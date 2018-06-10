package guilayer.contentpanels;

import guilayer.employees.*;
import guilayer.essentials.NavigationPanel;

public class ManageEmployees extends NavigationPanel {
	
	private ListEmployees listEmployee;
	private EditEmployee employeeEditor;
	
	public ManageEmployees() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		employeeEditor = new EditEmployee();
		add(employeeEditor);
		
		listEmployee = new ListEmployees(employeeEditor);
		add(listEmployee);
	}
	@Override
	public void prepare() {
		listEmployee.prepare();
		employeeEditor.prepare();
	}
	@Override
	public void reset() {
		listEmployee.reset();
		employeeEditor.reset();
	}
}
