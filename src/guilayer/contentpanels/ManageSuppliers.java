package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import guilayer.inventory.*;
import guilayer.suppliers.EditSupplier;
import guilayer.suppliers.ListSuppliers;

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
