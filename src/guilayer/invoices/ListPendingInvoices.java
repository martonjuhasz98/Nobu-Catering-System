package guilayer.invoices;

import ctrllayer.InvoiceController;
import guilayer.ManagerWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import modlayer.Invoice;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListPendingInvoices extends NavigationPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private InvoiceController invoiceCtrl;
	private CreateInvoice createInvoice;
	private ShowInvoice showInvoice;
	private ConfirmInvoice confirmInvoice;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable table;
	private InvoiceTableModel model;
	private ButtonColumn btn_confirm;
	private ButtonColumn btn_cancel;
	private JButton btn_create;
	private boolean fetchingData;
	private String lastKeyword;

	public ListPendingInvoices(ConfirmInvoice confirmInvoice, CreateInvoice createInvoice, ShowInvoice showInvoice) {
		super();
		
		this.confirmInvoice = confirmInvoice;
		this.createInvoice = createInvoice;
		this.showInvoice = showInvoice;
		
		invoiceCtrl = new InvoiceController();
		
		showInvoice.addPerformListener(this);
		createInvoice.addPerformListener(this);
		confirmInvoice.addPerformListener(this);

		initialize();
	}
	//Layout
	private void initialize() {
		
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		model = new InvoiceTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 12, 142, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(160, 11, 73, 23);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(697, 11, 73, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 760, 400);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		btn_confirm = new ButtonColumn(table, model.getColumnCount() - 2, this);
		btn_confirm.setMnemonic(KeyEvent.VK_ACCEPT);
		btn_cancel = new ButtonColumn(table, model.getColumnCount() - 1, this);
		btn_cancel.setMnemonic(KeyEvent.VK_CANCEL);

		reset();
		
		confirmInvoice.addPerformListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
	}
	@Override
	public void reset() {
		txt_search.setText("");
		lastKeyword = "";
		fetchingData = false;
	}
	@Override
	public void performed() {
		setVisible(true);
		new FetchWorker().execute();
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	//Functionalities
	private void showInvoice(Invoice invoice) {
		showInvoice.openToShow(invoice);
		setVisible(false);
	}
	private void confirmInvoice(Invoice invoice) {
		confirmInvoice.openToConfirm(invoice);
		setVisible(false);
	}
	private void cancelInvoice(Invoice invoice) {
		String message, title;
		int messageType;
		
		if (!invoiceCtrl.cancelInvoice(invoice)) {
			message = "An error occured while cancelling the Invoice!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Invoice was successfully cancelled!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			new FetchWorker().execute();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void createInvoice() {
		createInvoice.openToCreate();
		setVisible(false);
	}
	private void searchPendingInvoices() {
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchPendingInvoices();
		} else if (source == btn_confirm) {
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Invoice invoice = model.getItem(modelRowIndex);

			confirmInvoice(invoice);
		} else if (source == btn_cancel) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Canceling invoice",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}

			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Invoice invoice = model.getItem(modelRowIndex);
			
			cancelInvoice(invoice);
		} else if (source == btn_create) {
			createInvoice();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchPendingInvoices();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Invoice invoice = model.getItem(modelRowIndex);

			showInvoice(invoice);
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	//Classes
	private class InvoiceTableModel extends ItemTableModel<Invoice> {

		public InvoiceTableModel() {
			super();

			columns = new String[] { "Supplier", "Ordered", "Placed by", "", "" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Invoice invoice = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return invoice.getSupplier().getName();
			case 1:
				return invoice.getTimestamp();
			case 2:
				return invoice.getPlacedBy().getName();
			case 3:
				return "Confirm";
			case 4:
				return "Cancel";
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= getColumnCount() - 2;
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Invoice>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Invoice> doInBackground() throws Exception {
			return keyword.isEmpty()
					? invoiceCtrl.getPendingInvoices()
					: invoiceCtrl.searchPendingInvoices(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchPendingInvoices();
		}
	}
}
