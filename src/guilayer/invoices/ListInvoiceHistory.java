package guilayer.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.InvoiceController;
import guilayer.ManagerWindow;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import modlayer.Invoice;

import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ListInvoiceHistory extends NavigationPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private InvoiceController invoiceCtrl;
	private CreateInvoice createInvoice;
	private ShowInvoice showInvoice;
	private JTable table;
	private HistoryTableModel model;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private boolean fetchingData;
	private String lastKeyword;

	public ListInvoiceHistory(CreateInvoice createInvoice, ShowInvoice showInvoice) {
		super();
		
		this.createInvoice = createInvoice;
		this.showInvoice = showInvoice;
		
		invoiceCtrl = new InvoiceController();
		
		createInvoice.addPerformListener(this);
		showInvoice.addPerformListener(this);

		initialize();
	}

	private void initialize() {

		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		model = new HistoryTableModel();

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
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		reset();
		
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
		fetchingData = false;
		lastKeyword = "";
		
		txt_search.setText("");
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
	private void searchInvoiceHistory() {	
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
		if (e.getSource() == btn_search) {
			searchInvoiceHistory();
		}
		if (e.getSource() == btn_create) {
			createInvoice.openToCreate();
			setVisible(false);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInvoiceHistory();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Invoice invoice = model.getItem(modelRowIndex);

			showInvoice.openToShow(invoice);
			setVisible(false);
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
	private class HistoryTableModel extends ItemTableModel<Invoice> {

		public HistoryTableModel() {
			super();

			columns = new String[] { "Supplier", "Date Ordered", "Date Delivered", "Placed by" };
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
				return invoice.getDateDelivered();
			case 3:
				return invoice.getPlacedBy().getName();
			}

			return null;
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
					? invoiceCtrl.getInvoiceHistory()
					: invoiceCtrl.searchInvoiceHistory(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchInvoiceHistory();
		}
	}
}
