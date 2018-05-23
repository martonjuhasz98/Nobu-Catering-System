package guilayer.invoices;

import javax.swing.JPanel;

public class InvoicePaneManager extends JPanel {
	
	public InvoicePaneManager() {

		
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
