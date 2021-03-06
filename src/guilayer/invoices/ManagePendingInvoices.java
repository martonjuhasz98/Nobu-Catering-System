package guilayer.invoices;

import guilayer.essentials.NavigationPanel;

public class ManagePendingInvoices extends NavigationPanel {
	
	private ListPendingInvoices listInvoice;
	private ShowInvoice showInvoice;
	private CreateInvoice createInvoice;
	private ConfirmInvoice confirmInvoice;
	
	public ManagePendingInvoices() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		confirmInvoice = new ConfirmInvoice();
		add(confirmInvoice);
		
		createInvoice = new CreateInvoice();
		add(createInvoice);	
		
		showInvoice = new ShowInvoice();
		add(showInvoice);
		
		listInvoice = new ListPendingInvoices(confirmInvoice, createInvoice, showInvoice);
		add(listInvoice);
	}
	@Override
	public void prepare() {
		listInvoice.prepare();
		showInvoice.prepare();
		createInvoice.prepare();
		confirmInvoice.prepare();
	}
	@Override
	public void reset() {
		listInvoice.reset();
		showInvoice.reset();
		createInvoice.reset();
		confirmInvoice.reset();
	}
}
