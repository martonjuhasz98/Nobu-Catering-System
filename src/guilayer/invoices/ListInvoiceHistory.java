package guilayer.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.InvoiceController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
import modlayer.Invoice;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ListInvoiceHistory extends JPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private InvoiceController invoiceCtrl;
	private CreateInvoice createInvoice;
	private ShowInvoice showInvoice;
	private JTable table;
	private HistoryTableModel model;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private boolean isSearching;
	private String lastKeyword;

	public ListInvoiceHistory(CreateInvoice createInvoice, ShowInvoice showInvoice) {
		this.createInvoice = createInvoice;
		this.showInvoice = showInvoice;
		invoiceCtrl = new InvoiceController();
		lastKeyword = "";
		isSearching = false;
		createInvoice.addPerformListener(this);
		showInvoice.addPerformListener(this);

		initialize();
	}

	private void initialize() {

		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		model = new HistoryTableModel();

		txt_search = new JTextField();
		txt_search.setColumns(10);
		txt_search.setBounds(10, 12, 142, 20);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(160, 11, 65, 23);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(717, 11, 73, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 780, 418);
		add(scrollPane);

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);

		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);

		reset();

	}

	private void reset() {
		model.setItems(invoiceCtrl.getInvoiceHistory());
		txt_search.setText("");
	}

	private void search() {
		if (isSearching)
			return;
		isSearching = true;
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) {
			isSearching = false;
			return;
		}
		lastKeyword = keyword;
		// model.setItems(invoiceCtrl.searchInvoiceHistory(keyword));
		new SearchWorker(keyword).execute();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			search();
		}
		if (e.getSource() == btn_create) {
			createInvoice.create();
			setVisible(false);
		}
	}

	@Override
	public void performed() {
		model.setItems(invoiceCtrl.getInvoiceHistory());
		setVisible(true);
	}

	@Override
	public void cancelled() {
		setVisible(true);
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			search();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Invoice invoice = model.getItem(modelRowIndex);

			showInvoice.show(invoice);
			setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

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

	public class SearchWorker extends SwingWorker<ArrayList<Invoice>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Invoice> doInBackground() throws Exception {
			// Start
			return invoiceCtrl.searchInvoiceHistory(keyword);
		}

		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			search();
		}
	}
}
