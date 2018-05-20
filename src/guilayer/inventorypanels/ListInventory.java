package guilayer.inventorypanels;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.ItemController;
import guilayer.MainWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.CreateListener;
import guilayer.interfaces.EditListener;
import guilayer.inventorypanels.ListInventory;
import modlayer.Item;

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

public class ListInventory extends JPanel implements ActionListener, ListSelectionListener, MouseListener, CreateListener, EditListener {

	private CreateInventory createInv;
	private EditInventory editInv;
	private ItemController itemCtrl;
	private JTable table;
	private ItemTableModel model;
	private int selectedId;
	private JButton btn_search;
	private JButton btn_create;
	private JButton btn_delete;
	private JTextField txt_search;
	
	public ListInventory(CreateInventory createInv, EditInventory editInv) {
		this.createInv = createInv;
		this.editInv = editInv;
		itemCtrl = new ItemController();
		selectedId = -1;
		
		createInv.addCreateListener(this);
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
		btn_create.setBounds(436, 0, 73, 23);
		add(btn_create);
		
		btn_delete = new JButton("Delete");
		btn_delete.setBounds(517, 0, 73, 23);
		btn_delete.setEnabled(false);
		add(btn_delete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 580, 473);
		add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(true);
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
		        
		        //Delete Item
		    }
		};
		
		ButtonColumn btnColumn = new ButtonColumn(table, delete, model.getColumnCount()-1);
		btnColumn.setMnemonic(KeyEvent.VK_D);
		
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		btn_delete.addActionListener(this);
		table.addMouseListener(this);
		table.getSelectionModel().addListSelectionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_search) {
			String keyword = txt_search.getText().trim();
			model.setItems(itemCtrl.searchItems(keyword));
		} if (e.getSource() == btn_create) {
			setVisible(false);
			createInv.open();
		} else if (e.getSource() == btn_delete) {
			boolean success = itemCtrl.deleteItem(model.getItemAt(selectedId));
			if (!success) {
				JOptionPane.showMessageDialog(null,
					    "An error occured while deleting the Customer!",
					    "Error!",
					    JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			JOptionPane.showMessageDialog(null,
				"The Customer was successfully deleted!",
			    "Success!",
			    JOptionPane.INFORMATION_MESSAGE);
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == table.getSelectionModel()) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	
	        int firstIndex = e.getFirstIndex();
	        int lastIndex = e.getLastIndex();
	
	        if (lsm.isSelectionEmpty()) {
	            selectedId = -1;
	            btn_delete.setEnabled(false);
	        } else {
	            int minIndex = lsm.getMinSelectionIndex();
	            int maxIndex = lsm.getMaxSelectionIndex();
	            for (int i = minIndex; i <= maxIndex; i++) {
	                if (lsm.isSelectedIndex(i)) {
	                    selectedId = i;
	                    break;
	                }
	            }
	            btn_delete.setEnabled(true);
	        }
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Item item = model.getItemAt(modelRowIndex);
			
			editInv.open(item);
		}
	}
	@Override
	public void edited(boolean success) {
		if (success) {
			model.setItems(itemCtrl.getItems());
		}
		setVisible(true);
	}
	@Override
	public void created(boolean success) {
		if (success) {
			model.setItems(itemCtrl.getItems());
		}
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
					return item.getUnit();
				case 3:
					return item.getCategory();
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
			return getValueAt(0, columnIndex).getClass();
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
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
