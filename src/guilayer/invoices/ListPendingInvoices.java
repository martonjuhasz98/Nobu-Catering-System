package guilayer.invoices;

import javax.swing.JPanel;

import ctrllayer.InvoiceController;
import guilayer.MainWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.PerformListener;
import guilayer.inventory.ListInventory;
import modlayer.Invoice;
import modlayer.Item;
import modlayer.Unit;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.AbstractTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListPendingInvoices extends JPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private InvoiceController invoiceCtrl;
	private CreateInvoice createInvoice;
	private ShowInvoice showInvoice;
	private ConfirmInvoice confirmInvoice;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private JTable table;
	private InvoiceTableModel model;
	private boolean searching;
	
	public ListPendingInvoices(ConfirmInvoice confirmInvoice, CreateInvoice createInvoice, ShowInvoice showInvoice) {
		this.confirmInvoice = confirmInvoice;
		this.createInvoice = createInvoice;
		this.showInvoice = showInvoice;
		invoiceCtrl = new InvoiceController();
		searching = false;
		
		showInvoice.addPerformListener(this);
		createInvoice.addPerformListener(this);
		confirmInvoice.addPerformListener(this);
		
		initialize();
	}
	
	private void initialize() {
		setLayout(null);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
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
		scrollPane.setBounds(10, 42, 780, 447);
		add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		model.setInvoices(invoiceCtrl.getPendingInvoices());
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		AbstractAction confirm = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRowIndex = Integer.valueOf(e.getActionCommand());
		        Invoice invoice = model.getInvoiceAt(modelRowIndex);

				setVisible(false);
				confirmInvoice.confirm(invoice);
		    }
		};
		AbstractAction cancel = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (JOptionPane.showConfirmDialog(ListPendingInvoices.this, "Are you sure?") != JOptionPane.YES_OPTION) {
		        	return;
		        }
		    	
		        JTable table = (JTable)e.getSource();
		        int modelRowIndex = Integer.valueOf(e.getActionCommand());
		        Invoice invoice = model.getInvoiceAt(modelRowIndex);
		        
				if (!invoiceCtrl.cancelInvoice(invoice)) {
					JOptionPane.showMessageDialog(ListPendingInvoices.this,
						    "An error occured while confirmed the Invoice!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(ListPendingInvoices.this,
					"The Invoice was successfully confirmed!",
				    "Success!",
				    JOptionPane.INFORMATION_MESSAGE);
				model.setInvoices(invoiceCtrl.getPendingInvoices());
		    }
		};
		
		ButtonColumn btnColumn = new ButtonColumn(table, confirm, model.getColumnCount()-2);
		btnColumn.setMnemonic(KeyEvent.VK_ACCEPT);
		btnColumn = new ButtonColumn(table, cancel, model.getColumnCount()-1);
		btnColumn.setMnemonic(KeyEvent.VK_CANCEL);
		
		confirmInvoice.addPerformListener(this);
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	private void searchInventory() {
		if (searching) return;
		searching = true;
		
		String keyword = txt_search.getText().trim();
		model.setInvoices(invoiceCtrl.searchPendingInvoices(keyword));
		
		searching = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			searchInventory();
		} if (e.getSource() == btn_create) {
			setVisible(false);
			createInvoice.create();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
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
	public void performed() {
		model.setInvoices(invoiceCtrl.getPendingInvoices());
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	private class InvoiceTableModel extends AbstractTableModel {
		
		private String[] columns = new String[] { "Supplier", "Ordered", "Placed by", "", "" };
		private ArrayList<Invoice> invoices;
		
		public InvoiceTableModel() {
			this(new ArrayList<Invoice>());
		}
		public InvoiceTableModel(ArrayList<Invoice> invoices) {
			this.invoices = invoices;
			update();
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
					return invoice.getPlacedBy().getName();
				case 3:
					return "Confirm";
				case 4:
					return "Cancel";
			}
		
			return null;
		}
		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		@Override
		public Class getColumnClass(int columnIndex) {
			switch(columnIndex) {
				case 0:
					return String.class;
				case 1:
					return Date.class;
				case 2:
					return String.class;
				case 3:
					return JButton.class;
				case 4:
					return JButton.class;
		}
		
		return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= getColumnCount() - 2;
		}
		public void update() {
			fireTableDataChanged();
		}
		public Invoice getInvoiceAt(int rowIndex) {
			return invoices.get(rowIndex);
		}
		public void setInvoices(ArrayList<Invoice> invoices) {
			this.invoices = invoices;
			update();
		}
	}
}
