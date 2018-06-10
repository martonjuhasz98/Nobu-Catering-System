package guilayer.contentpanels;

import guilayer.essentials.NavigationTabbedPane;
import guilayer.invoices.*;

public class ManageInvoices extends NavigationTabbedPane {
	
	private ManagePendingInvoices mngPendingInvoices;
	private ManageInvoiceHistory mngInvoiceHistory;
	
	public ManageInvoices() {
		super();
		
		initalize();
	}

	private void initalize() {
		
		mngPendingInvoices = new ManagePendingInvoices();
		add(mngPendingInvoices, "Pending");
		
		mngInvoiceHistory = new ManageInvoiceHistory();
		add(mngInvoiceHistory, "History");

	}
	@Override
	public void prepare() {
		mngPendingInvoices.prepare();
		mngInvoiceHistory.prepare();
	}
	@Override
	public void reset() {
		mngPendingInvoices.reset();
		mngInvoiceHistory.reset();
	}
}
