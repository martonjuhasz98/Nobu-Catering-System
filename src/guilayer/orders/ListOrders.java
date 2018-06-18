package guilayer.orders;


import ctrllayer.OrderController;
import guilayer.WaiterWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.NavigationPanel;
import guilayer.essentials.PerformListener;
import guilayer.essentials.PlaceholderTextField;
import modlayer.Order;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import javax.swing.SwingConstants;

public class ListOrders extends NavigationPanel
		implements ActionListener, MouseListener, PerformListener, CaretListener {

	private EditOrder editOrder;
	private OrderController orderCtrl;
	private PlaceholderTextField txt_search;
	private JTable table;
	private OrderTableModel model;
	private ButtonColumn btn_cancel;
	private boolean fetchingData;
	private String lastKeyword;

	public ListOrders(EditOrder editOrder) {
		super();
		
		this.editOrder = editOrder;
		
		orderCtrl = new OrderController();
		
		editOrder.addPerformListener(this);

		initialize();
	}
	//Layout
	private void initialize() {

		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);
		Font fontAwesome = importFont("fontawesome.otf");

		model = new OrderTableModel();
		
		JPanel pnl_search = new JPanel();
		pnl_search.setSize(400, 52);
		FlowLayout fl_pnl_search = (FlowLayout) pnl_search.getLayout();
		fl_pnl_search.setVgap(0);
		fl_pnl_search.setHgap(0);
		fl_pnl_search.setAlignment(FlowLayout.LEFT);
		pnl_search.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_search.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnl_search.setOpaque(false);
		pnl_search.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnl_search.setPreferredSize(new Dimension(400, 52));
		add(pnl_search);
		
		txt_search = new PlaceholderTextField();
		txt_search.setPreferredSize(new Dimension(348, 32));
		txt_search.setAlignmentX(Component.LEFT_ALIGNMENT);
		txt_search.setAlignmentY(Component.TOP_ALIGNMENT);
		txt_search.setFont(getFont().deriveFont(Font.PLAIN, 14f));
		txt_search.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 0, (Color) new Color(160, 160, 160)), new EmptyBorder(4, 8, 4, 0)));
		txt_search.setCaretColor(SystemColor.textInactiveText);
		txt_search.setForeground(SystemColor.textInactiveText);
		txt_search.setMargin(new Insets(0, 0, 0, 0));
		txt_search.setPlaceholder("Search");
		pnl_search.add(txt_search);
		
		JLabel ico_search = new JLabel("\uf002");
		ico_search.setPreferredSize(new Dimension(32, 32));
		ico_search.setAlignmentY(Component.TOP_ALIGNMENT);
		ico_search.setAlignmentX(Component.RIGHT_ALIGNMENT);
		ico_search.setHorizontalAlignment(SwingConstants.LEFT);
		ico_search.setForeground(SystemColor.controlShadow);
		ico_search.setBorder(new CompoundBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(160, 160, 160)), new EmptyBorder(8, 8, 8, 8)));
		ico_search.setFont(fontAwesome.deriveFont(Font.PLAIN, 24f));
		pnl_search.add(ico_search);


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 57, 780, 432);
		add(scrollPane);

		table = new JTable();
		table.setRowHeight(32);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setPreferredSize(new Dimension(0, 32));
		table.setAutoCreateRowSorter(true);
		table.setModel(model);
		scrollPane.setViewportView(table);

		btn_cancel = new ButtonColumn(table, model.getColumnCount() - 1, this);
		btn_cancel.setMnemonic(KeyEvent.VK_CANCEL);

		reset();
		
		txt_search.addCaretListener(this);
		table.addMouseListener(this);
	}
	private Font importFont(String fontName) {
		try {
			InputStream is = EditOrder.class.getResourceAsStream("/font/" + fontName);
	        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
	        return font;
		} catch (Exception e) {
			System.out.println(fontName + " could not be imported!");
			e.printStackTrace();
	    }
		return null;
	}
	@Override
	public void prepare() {
		new FetchWorker().execute();
	}
	@Override
	public void reset() {
		setVisible(true);
		
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
	private void createOrder() {
		editOrder.openToCreate();
		setVisible(false);
	}
	private void editOrder(Order order) {
		editOrder.openToUpdate(order);
		setVisible(false);
	}
	private void cancelOrder(Order order) {
		String message, title;
		int messageType;
		
		if (!orderCtrl.cancelOrder(order)) {
			message = "An error occured while cancelling the Order!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Order was successfully cancelled!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			prepare();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void searchOrders() {
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
		if (source == btn_cancel) {
			if (JOptionPane.showConfirmDialog(ListOrders.this, "Are you sure?", "Cancelling order",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}

			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			Order order = model.getItem(modelRowIndex);

			cancelOrder(order);
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchOrders();
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getSource() == table) {
			int viewRowIndex = table.getSelectedRow();
			int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
			Order order = model.getItem(modelRowIndex);

			editOrder(order);
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
	private class OrderTableModel extends ItemTableModel<Order> {

		public OrderTableModel() {
			super();

			columns = new String[] { "Id", "Table No.", "Menu items", "" };
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Order order = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return order.getId();
			case 1:
				return order.getTableNo();
			case 2:
				return order.getItems().size();
			case 3:
				return "Cancel";
			}

			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= getColumnCount() - 1;
		}
	}
	private class FetchWorker extends SwingWorker<ArrayList<Order>, Void> {
		
		private String keyword;

		public FetchWorker() {
			this("");
		}
		public FetchWorker(String keyword) {
			super();
			this.keyword = keyword;
		}

		@Override
		protected ArrayList<Order> doInBackground() throws Exception {
			return keyword.isEmpty()
					? orderCtrl.getUnpaidOrders()
					: orderCtrl.searchUnpaidOrders(keyword);
		}
		@Override
		protected void done() {
			try {
				model.setItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchOrders();
		}
	}
}
