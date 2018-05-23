package guilayer.contentpanels;

import java.awt.CardLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import guilayer.invoices.*;

public class ManageInvoices extends JTabbedPane {
	
	public ManageInvoices() {

		
		initalize();
	}

	private void initalize() {
		
		
		add(new ListPendingInvoices(), "Pending");
		add(new InvoicePaneManager(), "History");

	}
}
