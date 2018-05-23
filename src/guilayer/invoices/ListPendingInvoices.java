package guilayer.invoices;

import javax.swing.JPanel;

import ctrllayer.InvoiceController;
import guilayer.interfaces.PerformListener;

public class ListPendingInvoices extends JPanel implements PerformListener{

	private InvoiceController invoiceCtrl;
	private ShowInvoice showInvoice;
	private ConfirmInvoice confirmInvoice;
	
	public ListPendingInvoices(ShowInvoice showInvoice, ConfirmInvoice confirmInvoice) {
		this.showInvoice = showInvoice;
		this.confirmInvoice = confirmInvoice;
		invoiceCtrl = new InvoiceController();
		
		showInvoice.addPerformListener(this);
		confirmInvoice.addPerformListener(this);
		
		initialize();
	}
	
	private void initialize() {
		
	}

	@Override
	public void performed() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		
	}
}
