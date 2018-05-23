package guilayer.invoices;

import javax.swing.JPanel;

public class ManageInventoryHistory extends JPanel {
	
	public ManageInventoryHistory() {

		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		ShowInvoice showInvoice = new ShowInvoice();
		add(showInvoice);
		
		ListInvoiceHistory listInvoiceHistory = new ListInvoiceHistory(showInvoice);
		add(listInvoiceHistory);
	}
}
