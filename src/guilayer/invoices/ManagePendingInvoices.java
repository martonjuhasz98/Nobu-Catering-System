package guilayer.invoices;

import javax.swing.JPanel;

public class ManagePendingInvoices extends JPanel {
	
	public ManagePendingInvoices() {
		initalize();
	}

	private void initalize() {
		
		setLayout(null);
		
		ConfirmInvoice confirmInvoice = new ConfirmInvoice();
		add(confirmInvoice);
		
		CreateInvoice createInvoice = new CreateInvoice();
		add(createInvoice);	
		
		ShowInvoice showInvoice = new ShowInvoice();
		add(showInvoice);
		
		ListPendingInvoices listInvoice = new ListPendingInvoices(confirmInvoice, createInvoice, showInvoice);
		add(listInvoice);
	}
}
