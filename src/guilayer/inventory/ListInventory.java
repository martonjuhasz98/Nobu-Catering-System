package guilayer.inventory;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.EditListener;
import guilayer.inventory.ListInventory;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListInventory extends JPanel implements ActionListener, MouseListener, EditListener, CaretListener {

	private EditItem itemEditor;
	private ItemController itemCtrl;
	private JTable table;
	private ItemTableModel model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;
	
	public ListInventory(EditItem editInv) {
		this.itemEditor = editInv;
		itemCtrl = new ItemController();
		
		editInv.addEditListener(this);
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setBounds(0, 0, MainWindow.contentWidth, MainWindow.totalHeight);
		
		model = new ItemTableModel();
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);
		
		btn_search = new JButton("Search");
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);
		
		btn_create = new JButton("Create");
		btn_create.setBounds(517, 3, 73, 23);
		add(btn_create);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 580, 473);
		add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoCreateRowSorter(true);
		model.setItems(itemCtrl.getItems());
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		AbstractAction delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRowIndex = Integer.valueOf(e.getActionCommand());
		        Item item = model.getItemAt(modelRowIndex);
		        
				if (!itemCtrl.deleteItem(item)) {
					JOptionPane.showMessageDialog(ListInventory.this,
						    "An error occured while deleting the Item!",
						    "Error!",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JOptionPane.showMessageDialog(ListInventory.this,
					"The Item was successfully deleted!",
				    "Success!",
				    JOptionPane.INFORMATION_MESSAGE);
				model.setItems(itemCtrl.getItems());
		    }
		};
		
		ButtonColumn btnColumn = new ButtonColumn(table, delete, model.getColumnCount()-1);
		btnColumn.setMnemonic(KeyEvent.VK_D);
		
		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);
	}
	private void searchInventory() {

		String keyword = txt_search.getText().trim();
		model.setItems(itemCtrl.searchItems(keyword));
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchInventory();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			searchInventory();
		} if (e.getSource() == btn_create) {
			setVisible(false);
			itemEditor.createItem();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Item item = model.getItemAt(modelRowIndex);
			
			setVisible(false);
			itemEditor.updateItem(item);
		}
	}
	@Override
	public void created() {
		model.setItems(itemCtrl.getItems());
		setVisible(true);
	}
	@Override
	public void updated() {
		model.setItems(itemCtrl.getItems());
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
	
	
	private class ItemTableModel extends AbstractTableModel {
		
		private String[] columns = new String[] { "Name", "Quantity", "Unit", "Category", "" };
		private ArrayList<Item> items;
		
		public ItemTableModel() {
			this(new ArrayList<Item>());
		}
		public ItemTableModel(ArrayList<Item> items) {
			this.items = items;
			update();
		}

		@Override
		public int getRowCount() {
			return items.size();
		}
		@Override
		public int getColumnCount() {
			return columns.length;
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
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}
		@Override
		public Class getColumnClass(int columnIndex) {
			switch(columnIndex) {
				case 0:
					return String.class;
				case 1:
					return double.class;
				case 2:
					return Unit.class;
				case 3:
					return ItemCategory.class;
				case 4:
					return JButton.class;
		}
		
		return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == getColumnCount() - 1;
		}
		public void update() {
			fireTableDataChanged();
		}
		public Item getItemAt(int rowIndex) {
			return items.get(rowIndex);
		}
		public void setItems(ArrayList<Item> items) {
			this.items = items;
			update();
		}
	}
}
