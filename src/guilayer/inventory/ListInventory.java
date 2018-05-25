package guilayer.inventory;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ctrllayer.ItemController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
import guilayer.inventory.ListInventory;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class ListInventory extends JPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditItem itemEditor;
	private ItemController itemCtrl;
	private JTable table;
	private InventoryTableModel<Item> model;
	private JButton btn_search;
	private JButton btn_create;
	private JTextField txt_search;
	private boolean searching;
	
	public ListInventory(EditItem editInv) {
		this.itemEditor = editInv;
		itemCtrl = new ItemController();
		searching = false;
		
		editInv.addPerformListener(this);
		
		initialize();
	}
	
	private void initialize() {
		
		setLayout(null);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		
		model = new InventoryTableModel<Item>();
		
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
		        if (JOptionPane.showConfirmDialog(ListInventory.this, "Are you sure?") != JOptionPane.YES_OPTION) {
		        	return;
		        }
		    	
		        int modelRowIndex = Integer.valueOf(e.getActionCommand());
		        Item item = (Item)model.getItem(modelRowIndex);
		        
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
		if (searching) return;
		searching = true;
		
		String keyword = txt_search.getText().trim();
		model.setItems(itemCtrl.searchItems(keyword));
		
		searching = false;
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
			itemEditor.create();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Item item = (Item)model.getItem(modelRowIndex);
			
			setVisible(false);
			itemEditor.update(item);
		}
	}
	@Override
	public void performed() {
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
	
	private class InventoryTableModel<T extends Item> extends ItemTableModel<T> {
		
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
}
