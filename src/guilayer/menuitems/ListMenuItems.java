package guilayer.menuitems;

import ctrllayer.MenuItemController;
import guilayer.ManagerWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import modlayer.MenuItem;

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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListMenuItems extends NavigationPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditMenuItem editMenuItem;
	private MenuItemController menuItemCtrl;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private JTable table;
	private MenuItemTableModel model;
	private ButtonColumn btn_delete;
	private boolean fetchingData;
	private String lastKeyword;

	public ListMenuItems(EditMenuItem editMenuItem) {
		super();
		
		this.editMenuItem = editMenuItem;
		
		menuItemCtrl = new MenuItemController();
		
		editMenuItem.addPerformListener(this);
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);

		model = new MenuItemTableModel();

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

		btn_delete = new ButtonColumn(table, model.getColumnCount() - 1, this);
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
		lastKeyword = "";
		fetchingData = false;
	}
	@Override
	public void performed() {
		prepare();
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	//Functionalities
	private void createMenuItem() {
		editMenuItem.openToCreate();
		setVisible(false);
	}
	private void updateMenuItem(MenuItem menuItem) {
		editMenuItem.openToUpdate(menuItem);
		setVisible(false);
	}
	private void deleteMenuItem(MenuItem menuItem) {
		String message, title;
		int messageType;
		
		if (!menuItemCtrl.deleteMenuItem(menuItem)) {
			message = "An error occured while deleting the Menu Item!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Menu Item was successfully deleted!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			prepare();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void searchMenuItems() {
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
		final Object source = e.getSource();
		if (source == btn_search) {
			searchMenuItems();
		} else if (source == btn_delete) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Deleting Menu Item",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}

			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			MenuItem menuItem = model.getItem(modelRowIndex);

			deleteMenuItem(menuItem);
		} else if (source == btn_create) {
			createMenuItem();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			MenuItem menuItem = model.getItem(modelRowIndex);

			updateMenuItem(menuItem);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchMenuItems();
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
	private class MenuItemTableModel extends ItemTableModel<MenuItem> {

		public MenuItemTableModel() {
			super();

			columns = new String[] { "ID", "Name", "Category", "Price", "" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem menuItem = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return menuItem.getId();
			case 1:
				return menuItem.getName();
			case 2:
				return menuItem.getCategory().getName();
			case 3:
				return new DecimalFormat("#0.00").format(menuItem.getPrice());
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
	private class FetchWorker extends SwingWorker<ArrayList<MenuItem>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<MenuItem> doInBackground() throws Exception {
			return keyword.isEmpty()
					? menuItemCtrl.getMenuItems()
					: menuItemCtrl.searchMenuItems(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchMenuItems();
		}
	}
}
