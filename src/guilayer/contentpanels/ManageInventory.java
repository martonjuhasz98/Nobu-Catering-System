package guilayer.contentpanels;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.ItemController;
import modlayer.Item;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class ManageInventory extends JPanel {

	private ItemController itemCtrl;
	private JTable table;
	private ItemTableModel model;
	private int selectedId;
	private JButton btn_delete;
	private JTextField txt_search;
	
	public ManageInventory() {
		itemCtrl = new ItemController();
		selectedId = -1;
		
		setLayout(null);
		model = new ItemTableModel();
		
		btn_delete = new JButton("Delete");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		
		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		add(txt_search);
		txt_search.setColumns(10);
		
		JButton btn_search = new JButton("Search");
		btn_search.setEnabled(false);
		btn_search.setBounds(199, 4, 73, 20);
		add(btn_search);
		
		JButton btn_create = new JButton("Create");
		btn_create.setEnabled(false);
		btn_create.setBounds(436, 0, 73, 23);
		add(btn_create);
		btn_delete.setEnabled(false);
		btn_delete.setBounds(517, 0, 73, 23);
		add(btn_delete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 28, 580, 473);
		add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		model.setItems(itemCtrl.getItems());
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
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
			
		});
	}
	private class ItemTableModel extends AbstractTableModel {
		
		private String[] columns = new String[] { "Name", "Quantity", "Unit", "Category" };
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
