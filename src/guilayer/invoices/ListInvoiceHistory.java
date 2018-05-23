package guilayer.invoices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.InvoiceController;
import guilayer.MainWindow;
import guilayer.interfaces.EditListener;
import modlayer.Invoice;

public class ListInvoiceHistory extends JPanel implements ActionListener, MouseListener,EditListener{
	
	private JTable table;
	private JButton btn_create;
	private HistoryTableModel model;
	private InvoiceController invoiceCtrl;
	private ShowInvoice showInvoice;
	
	/**
	 * Create the panel.
	 */
	public ListInvoiceHistory(ShowInvoice showInvoice) {
		this.showInvoice = showInvoice;
		invoiceCtrl = new InvoiceController();
		
		showInvoice.addEditListener(this);
		
		initialize();
	}
	
	private void initialize() {
		setLayout(null);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
		model = new HistoryTableModel();
		
		btn_create = new JButton("Create");
		btn_create.setBounds(10, 11, 73, 23);
		add(btn_create);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 760, 400);
		add(scrollPane);
		
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		model.setInvoices(invoiceCtrl.getInvoiceHistory());
		scrollPane.setViewportView(table);
		
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	
	private class HistoryTableModel extends AbstractTableModel {

		private String[] columns = new String[] { "Supplier", "Date Ordered", "Date Delivered", "Placed by" };
		private ArrayList<Invoice> invoices;
		
		public HistoryTableModel() {
			this(new ArrayList<Invoice>());
		}

		public HistoryTableModel(ArrayList<Invoice> invoices) {
			this.invoices = invoices;
			update();
		}

		@Override
		public Class getColumnClass(int columnIndex) {
			try {
			return getValueAt(0, columnIndex).getClass();
			} catch (Exception ignored) {
				return new Object().getClass();
			}
		}

		@Override
		public int getRowCount() {
			return invoices.size();
		}
		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Invoice invoice = invoices.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return invoice.getSupplier().getName();
				case 1:
					return invoice.getTimestamp();
				case 2:
					return invoice.getDateDelivered();
				case 3:
					return invoice.getPlacedBy();
			}
		
			return null;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
		public Invoice getInvoiceAt(int rowIndex) {
			return invoices.get(rowIndex);
		}
		
		public void setInvoices(ArrayList<Invoice> invoices) {
			this.invoices = invoices;
			update();
		}
		
		public void update() {
			fireTableDataChanged();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Invoice invoice = model.getInvoiceAt(modelRowIndex);
			
			setVisible(false);
			showInvoice.details(invoice);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_create) {
			//showInvoice.createInvoice();
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

	
	@Override
	public void created() {
		model.setInvoices(invoiceCtrl.getInvoiceHistory());
		setVisible(true);
	}
	@Override
	public void updated() {
		model.setInvoices(invoiceCtrl.getInvoiceHistory());
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
}
