package guilayer.inventory;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.ItemController;
import guilayer.ManagerWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import guilayer.inventory.ListInventory;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListInventory extends NavigationPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditItem itemEditor;
	private ItemController itemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JTable table;
	private InventoryTableModel model;
	private ButtonColumn btn_delete;
	private JButton btn_create;
	private boolean fetchingData;
	private String lastKeyword;
	
	public ListInventory(EditItem editInv) {
		super();
		
		this.itemEditor = editInv;
		
		itemCtrl = new ItemController();
		
		editInv.addPerformListener(this);
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		model = new InventoryTableModel();
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);
		
		btn_create = new JButton("Create");
		btn_create.setBounds(707, 3, 73, 23);
		add(btn_create);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 770, 450);
		add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		btn_delete = new ButtonColumn(table, model.getColumnCount()-1, this);
		btn_delete.setMnemonic(KeyEvent.VK_DELETE);
		
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
		txt_search.setText("");
		fetchingData = false;
		lastKeyword = "";
	}
	@Override
	public void performed() {
		setVisible(true);
		prepare();
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	//Functionalities
	private void searchInventory() {	
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	private void createItem() {
		itemEditor.openToCreate();
		setVisible(false);
	}
	private void editItem(Item item) {
		itemEditor.openToUpdate(item);
		setVisible(false);
	}
	private void deleteItem(Item item) {
		String message, title;
		int messageType;
		
		if (!itemCtrl.deleteItem(item)) {
			message = "An error occured while deleting the Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Item was successfully deleted!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			prepare();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_search) {
			searchInventory();
		} else if (source == btn_delete) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Deleting item", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
	    	
	        int modelRowIndex = Integer.valueOf(e.getActionCommand());
	        Item item = (Item)model.getItem(modelRowIndex);
	        
	        deleteItem(item);
		} else if (source == btn_create) {
			createItem();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Item item = (Item)model.getItem(modelRowIndex);
			
			editItem(item);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
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
	private class InventoryTableModel extends ItemTableModel<Item> {
		
		public InventoryTableModel() {
			super();
			
			columns = new String[] { "Name", "Quantity", "Unit", "Category", "" };
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			
			Item item = items.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return item.getName();
				case 1:
					return item.getQuantity();
				case 2:
					return item.getUnit().getAbbr();
				case 3:
					return item.getCategory().getName();
				case 4:
					return "Delete";
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == getColumnCount() - 1;
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Item>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Item> doInBackground() throws Exception {
			return keyword.isEmpty()
					? itemCtrl.getItems()
					: itemCtrl.searchItems(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchInventory();
		}
	}
}
