package guilayer.invoices;

import guilayer.essentials.NavigationPanel;

public class ManageInvoiceHistory extends NavigationPanel {
	
	private ListInvoiceHistory listInvoice;
	private ShowInvoice showInvoice;
	private CreateInvoice createInvoice;
	
	public ManageInvoiceHistory() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		createInvoice = new CreateInvoice();
		add(createInvoice);	
		
		showInvoice = new ShowInvoice();
		add(showInvoice);
		
		listInvoice = new ListInvoiceHistory(createInvoice, showInvoice);
		add(listInvoice);
	}
	@Override
	public void prepare() {
		listInvoice.prepare();
		showInvoice.prepare();
		createInvoice.prepare();
	}
	@Override
	public void reset() {
		listInvoice.reset();
		showInvoice.reset();
		createInvoice.reset();
	}
}
