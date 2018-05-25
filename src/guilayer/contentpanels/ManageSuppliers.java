package guilayer.contentpanels;

import javax.swing.JPanel;

import guilayer.suppliers.*;

public class ManageSuppliers extends JPanel {
	
	public ManageSuppliers() {
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		EditSupplier SupplierEditor = new EditSupplier();
		add(SupplierEditor);
		
		ListSuppliers listSup = new ListSuppliers(SupplierEditor);
		add(listSup);
	}
}
