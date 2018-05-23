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
		
		setLayout(null);
		
		add(new ListPendingInvoices(), "Pending");
		add(new ListInvoiceHistory(), "History");
	}
}
