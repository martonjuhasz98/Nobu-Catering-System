package guilayer.invoices;

import javax.swing.JPanel;

public class ManagePendingInvoices extends JPanel {
	
	public ManagePendingInvoices() {

		
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		ShowInvoice showInvoice = new ShowInvoice();
		add(showInvoice);
		
		ConfirmInvoice confirmInvoice = new ConfirmInvoice();
		add(confirmInvoice);
		
		ListPendingInvoices listInvoice = new ListPendingInvoices(showInvoice, confirmInvoice);
		add(listInvoice);
	}
}
