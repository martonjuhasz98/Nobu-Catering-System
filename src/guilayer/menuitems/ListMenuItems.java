package guilayer.menuitems;

import javax.swing.JPanel;

import ctrllayer.MenuItemController;
import guilayer.ManagerWindow;
import guilayer.interfaces.ButtonColumn;
import guilayer.interfaces.ItemTableModel;
import guilayer.interfaces.PerformListener;
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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListMenuItems extends JPanel implements ActionListener, MouseListener, PerformListener, CaretListener {

	private MenuItemController menuItemCtrl;
	private EditMenuItem editMenuItem;
	private JTextField txt_search;
	private JButton btn_search;
	private JButton btn_create;
	private JTable table;
	private MenuItemTableModel model;
	private boolean isSearching;
	private String lastKeyword;

	public ListMenuItems(EditMenuItem editMenuItem) {
		this.editMenuItem = editMenuItem;
		menuItemCtrl = new MenuItemController();
		lastKeyword = "";
		isSearching = false;
		editMenuItem.addPerformListener(this);
		initialize();
	}

	private void initialize() {
		setLayout(null);
		setBounds(0, 0, 800, 487);

		model = new MenuItemTableModel();

		txt_search = new JTextField();
		txt_search.setBounds(10, 4, 179, 20);
		txt_search.setColumns(10);
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

		AbstractAction delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ListMenuItems.this, "Are you sure?", "Deliting Menu Item",
						JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}

				int modelRowIndex = Integer.valueOf(e.getActionCommand());
				MenuItem menuItem = model.getItem(modelRowIndex);

				if (!menuItemCtrl.deleteMenuItem(menuItem)) {
					JOptionPane.showMessageDialog(ListMenuItems.this, "An error occured while deleting the Menu Item!",
							"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(ListMenuItems.this, "The MenuItem was successfully deleted!", "Success!",
						JOptionPane.INFORMATION_MESSAGE);
				reset();
			}
		};

		
		ButtonColumn deleteColumn = new ButtonColumn(table, delete, model.getColumnCount() - 1);
		deleteColumn.setMnemonic(KeyEvent.VK_DELETE);

		txt_search.addCaretListener(this);
		btn_search.addActionListener(this);
		btn_create.addActionListener(this);
		table.addMouseListener(this);

		reset();
	}

	private void reset() {
		model.setItems(menuItemCtrl.getMenuItems());
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
			editMenuItem.create();
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
			MenuItem menuItem = model.getItem(modelRowIndex);

			editMenuItem.update(menuItem);
			setVisible(false);
		}
	}

	@Override
	public void performed() {
		model.setItems(menuItemCtrl.getMenuItems());
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

	public class SearchWorker extends SwingWorker<ArrayList<MenuItem>, Void> {
		private String keyword;

		public SearchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<MenuItem> doInBackground() throws Exception {
			// Start
			return menuItemCtrl.searchMenuItems(keyword);
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
