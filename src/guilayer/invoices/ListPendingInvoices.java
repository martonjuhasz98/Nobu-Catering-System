package guilayer.invoices;

import javax.swing.JPanel;

import ctrllayer.InvoiceController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
import guilayer.invoices.ListInvoiceHistory.SearchWorker;
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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListPendingInvoices extends JPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private InvoiceController invoiceCtrl;
	private CreateInvoice createInvoice;
	private ShowInvoice showInvoice;
	private ConfirmInvoice confirmInvoice;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private JTable table;
	private InvoiceTableModel model;
	private boolean isSearching;
	private String lastKeyword;

	public ListPendingInvoices(ConfirmInvoice confirmInvoice, CreateInvoice createInvoice, ShowInvoice showInvoice) {
		this.confirmInvoice = confirmInvoice;
		this.createInvoice = createInvoice;
		this.showInvoice = showInvoice;
		invoiceCtrl = new InvoiceController();
		lastKeyword = "";
		isSearching = false;
		showInvoice.addPerformListener(this);
		createInvoice.addPerformListener(this);
		confirmInvoice.addPerformListener(this);

		initialize();
	}

	private void initialize() {
		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight - 30);

		model = new InvoiceTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 11, 142, 20);
		txt_search.setColumns(10);
		add(txt_search);

		btn_search = new JButton("Search");
		btn_search.setBounds(162, 10, 65, 23);
		add(btn_search);

		btn_create = new JButton("Create");
		btn_create.setBounds(725, 10, 65, 23);
		add(btn_create);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 780, 417);
		add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		AbstractAction confirm = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Invoice invoice = model.getItem(modelRowIndex);

				setVisible(false);
				confirmInvoice.confirm(invoice);
			}
		};
		AbstractAction cancel = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ListPendingInvoices.this, "Are you sure?", "Canceling invoice",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}

				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				Invoice invoice = model.getItem(modelRowIndex);

				if (!invoiceCtrl.cancelInvoice(invoice)) {
					JOptionPane.showMessageDialog(ListPendingInvoices.this,
							"An error occured while canceled the Invoice!", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListPendingInvoices.this, "The Invoice was successfully canceled!",
						"Success!", JOptionPane.INFORMATION_MESSAGE);
				reset();
			}
		};

		ButtonColumn confirmColumn = new ButtonColumn(table, confirm, model.getColumnCount() - 2);
		confirmColumn.setMnemonic(KeyEvent.VK_ACCEPT);
		ButtonColumn cancelColumn = new ButtonColumn(table, cancel, model.getColumnCount() - 1);
		cancelColumn.setMnemonic(KeyEvent.VK_CANCEL);

		confirmInvoice.addPerformListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);

		reset();
	}

	private void reset() {
		model.setItems(invoiceCtrl.getPendingInvoices());
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
	public void performed() {
		model.setItems(invoiceCtrl.getPendingInvoices());
		setVisible(true);
	}

	@Override
	public void cancelled() {
		setVisible(true);
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

	public class SearchWorker extends SwingWorker<ArrayList<Invoice>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Invoice> doInBackground() throws Exception {
			// Start
			return invoiceCtrl.searchPendingInvoices(keyword);
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
