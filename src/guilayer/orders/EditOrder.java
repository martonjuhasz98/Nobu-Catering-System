package guilayer.orders;

import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.Document;
import javax.swing.text.NumberFormatter;

import ctrllayer.OrderController;
import ctrllayer.SessionSingleton;
import ctrllayer.MenuItemController;
import guilayer.WaiterWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformListener;
import guilayer.essentials.PerformPanel;
import guilayer.essentials.PlaceholderTextField;
import modlayer.OrderMenuItem;
import modlayer.TransactionType;
import modlayer.MenuItem;
import modlayer.MenuItemCategory;
import modlayer.Order;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.border.MatteBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class EditOrder extends PerformPanel implements PerformListener, ActionListener, CaretListener, TableModelListener, ItemListener {
	
	private SelectTable selectTable;
	private OrderController orderCtrl;
	private MenuItemController itemCtrl;
	private Order order;
	private PlaceholderTextField txt_search;
	private boolean fetchingData;
	private String lastKeyword;
	private JTable tbl_order;
	private OrderTableModel mdl_order;
	private boolean isCreating;
	private ButtonColumn btn_remove;
	private JLabel grandTotal;
	private JLabel tax;
	private JLabel discount;
	private JLabel subtotal;
	private JLabel lbl_title;
	private JLabel lbl_table;
	private JPanel pnl_menu;
	private JCheckBox chk_tax;
	private JPanel btn_print;
	private JPanel btn_card;
	private JPanel btn_cash;
	private JPanel btn_cancel;
	
	public EditOrder(SelectTable selectTable) {
		super();
		
		this.selectTable = selectTable;
		
		setSize(new Dimension(800, 500));
		setPreferredSize(new Dimension(800, 500));
		setBackground(SystemColor.window);
		
		itemCtrl = new MenuItemController();
		orderCtrl = new OrderController();
		
		selectTable.addPerformListener(this);
		
		initialize();
	}
	//Layout
	private void initialize() {
		
		setVisible(false);
		setBounds(0, 0, WaiterWindow.contentWidth, WaiterWindow.totalHeight);
		Font fontAwesome = importFont("fontawesome.otf");
		Font helvetica = importFont("helvetica.otf");
		
		mdl_order = new OrderTableModel();
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		JPanel pnl_top = new JPanel();
		pnl_top.setPreferredSize(new Dimension(800, 32));
		FlowLayout fl_pnl_top = (FlowLayout) pnl_top.getLayout();
		fl_pnl_top.setAlignment(FlowLayout.RIGHT);
		fl_pnl_top.setVgap(0);
		fl_pnl_top.setHgap(0);
		add(pnl_top);
		
		lbl_title = new JLabel("Order#1 for Table 00");
		lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_title.setPreferredSize(new Dimension(620, 32));
		lbl_title.setForeground(SystemColor.textInactiveText);
		lbl_title.setFont(getFont().deriveFont(Font.BOLD, 14));
		pnl_top.add(lbl_title);
		
		lbl_table = new JLabel("Change table");
		lbl_table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl_table.setForeground(new Color(70, 130, 180));
		lbl_table.setPreferredSize(new Dimension(90, 32));
		lbl_table.setFont(getFont().deriveFont(Font.PLAIN, 14));
		pnl_top.add(lbl_table);
		
		JPanel pnl_left = new JPanel();
		FlowLayout fl_pnl_left = (FlowLayout) pnl_left.getLayout();
		fl_pnl_left.setAlignment(FlowLayout.LEFT);
		fl_pnl_left.setVgap(0);
		fl_pnl_left.setHgap(0);
		pnl_left.setBorder(null);
		pnl_left.setPreferredSize(new Dimension(400, 468));
		pnl_left.setOpaque(false);
		add(pnl_left);
		
		JPanel pnl_search = new JPanel();
		FlowLayout fl_pnl_search = (FlowLayout) pnl_search.getLayout();
		fl_pnl_search.setVgap(0);
		fl_pnl_search.setHgap(0);
		fl_pnl_search.setAlignment(FlowLayout.LEFT);
		pnl_search.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_search.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnl_search.setOpaque(false);
		pnl_search.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnl_search.setPreferredSize(new Dimension(400, 52));
		pnl_left.add(pnl_search);
		
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
		
		
		JScrollPane scr_menu = new JScrollPane();
		scr_menu.setAlignmentY(Component.TOP_ALIGNMENT);
		scr_menu.setAlignmentX(Component.LEFT_ALIGNMENT);
		scr_menu.setBackground(SystemColor.window);
		scr_menu.setOpaque(false);
		scr_menu.setBorder(null);
		scr_menu.setPreferredSize(new Dimension(390, 416));
		scr_menu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnl_left.add(scr_menu);
		
		FlowLayout fl_pnl_menu = new FlowLayout(FlowLayout.LEFT, 0, 0);
		pnl_menu = new JPanel(fl_pnl_menu);
		pnl_menu.setPreferredSize(new Dimension(390, 0));
		pnl_menu.setBackground(SystemColor.window);
		pnl_menu.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_menu.setAlignmentX(Component.LEFT_ALIGNMENT);
		scr_menu.setViewportView(pnl_menu);
		
		txt_search.addCaretListener(this);
		
		NumberFormat format = NumberFormat.getCurrencyInstance();
		format.setMaximumFractionDigits(0);

		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setMinimum(0.0);
		formatter.setMaximum(10000000.0);
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		
		JPanel pnl_right = new JPanel();
		pnl_right.setBorder(null);
		pnl_right.setPreferredSize(new Dimension(400, 468));
		FlowLayout fl_pnl_right = (FlowLayout) pnl_right.getLayout();
		fl_pnl_right.setVgap(0);
		fl_pnl_right.setHgap(0);
		fl_pnl_right.setAlignment(FlowLayout.LEFT);
		add(pnl_right);
		
		JScrollPane scr_order = new JScrollPane(tbl_order);
		scr_order.setBackground(Color.WHITE);
		scr_order.setPreferredSize(new Dimension(400, 268));
		scr_order.setBorder(BorderFactory.createEmptyBorder());
		pnl_right.add(scr_order);
		
		tbl_order = new JTable();
		tbl_order.setShowHorizontalLines(true);
		tbl_order.setShowVerticalLines(false);
		tbl_order.setRowHeight(32);
		tbl_order.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tbl_order.getTableHeader().setReorderingAllowed(false);
		tbl_order.getTableHeader().setPreferredSize(new Dimension(400, 32));
		tbl_order.setAutoCreateRowSorter(true);
		tbl_order.setModel(mdl_order);
		scr_order.setViewportView(tbl_order);
		btn_remove = new ButtonColumn(tbl_order, mdl_order.getColumnCount() - 1, this);
		
		JPanel pnl_pay = new JPanel();
		FlowLayout fl_pnl_pay = (FlowLayout) pnl_pay.getLayout();
		fl_pnl_pay.setAlignment(FlowLayout.LEFT);
		fl_pnl_pay.setVgap(10);
		fl_pnl_pay.setHgap(10);
		pnl_pay.setPreferredSize(new Dimension(400, 200));
		pnl_right.add(pnl_pay);
		
		JPanel pnl_calculations = new JPanel();
		FlowLayout fl_pnl_calculations = (FlowLayout) pnl_calculations.getLayout();
		fl_pnl_calculations.setVgap(8);
		fl_pnl_calculations.setHgap(12);
		fl_pnl_calculations.setAlignment(FlowLayout.LEFT);
		pnl_calculations.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_calculations.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnl_calculations.setPreferredSize(new Dimension(380, 90));
		pnl_pay.add(pnl_calculations);
		JPanel pnl_subtotal = new JPanel();
		pnl_calculations.add(pnl_subtotal);
		pnl_subtotal.setBorder(null);
		pnl_subtotal.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_subtotal.setAlignmentX(Component.LEFT_ALIGNMENT);
		FormLayout fl_pnl_subtotal = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:20px"),
				ColumnSpec.decode("left:80px"),
				ColumnSpec.decode("right:40px"),},
			new RowSpec[] {
				RowSpec.decode("bottom:24px"),
				RowSpec.decode("bottom:24px"),
				RowSpec.decode("bottom:24px:grow"),});
		pnl_subtotal.setLayout(fl_pnl_subtotal);
		
		JLabel lbl_subtotal = new JLabel("SUBTOTAL");
		lbl_subtotal.setForeground(SystemColor.textInactiveText);
		lbl_subtotal.setAlignmentY(Component.TOP_ALIGNMENT);
		pnl_subtotal.add(lbl_subtotal, "2, 1");
		
		subtotal = new JLabel("22,29");
		subtotal.setForeground(SystemColor.windowBorder);
		subtotal.setFont(getFont().deriveFont(Font.BOLD, 14));
		pnl_subtotal.add(subtotal, "3, 1");
		
		JLabel lbl_discount = new JLabel("DISCOUNT");
		lbl_discount.setForeground(SystemColor.textInactiveText);
		lbl_discount.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		pnl_subtotal.add(lbl_discount, "2, 2");
		
		discount = new JLabel("4,99");
		discount.setForeground(SystemColor.windowBorder);
		discount.setFont(getFont().deriveFont(Font.BOLD, 14));
		pnl_subtotal.add(discount, "3, 2");
		
		chk_tax = new JCheckBox("");
		chk_tax.setSelected(true);
		chk_tax.setMargin(new Insets(0, 0, 0, 0));
		chk_tax.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		pnl_subtotal.add(chk_tax, "1, 3");
		
		JLabel lbl_tax = new JLabel("TAX");
		lbl_tax.setForeground(SystemColor.textInactiveText);
		lbl_tax.setAlignmentY(1.0f);
		pnl_subtotal.add(lbl_tax, "2, 3");
		
		tax = new JLabel("1,48");
		tax.setForeground(SystemColor.windowBorder);
		tax.setFont(getFont().deriveFont(Font.BOLD, 14));
		pnl_subtotal.add(tax, "3, 3");
		
		JPanel pnl_grandTotal = new JPanel();
		pnl_grandTotal.setPreferredSize(new Dimension(204, 72));
		FlowLayout fl_pnl_grandTotal = (FlowLayout) pnl_grandTotal.getLayout();
		fl_pnl_grandTotal.setVgap(0);
		fl_pnl_grandTotal.setHgap(0);
		fl_pnl_grandTotal.setAlignment(FlowLayout.RIGHT);
		pnl_calculations.add(pnl_grandTotal);
		
		JLabel lbl_grandTotal = new JLabel("GRAND TOTAL");
		lbl_grandTotal.setForeground(SystemColor.textInactiveText);
		lbl_grandTotal.setVerticalAlignment(SwingConstants.BOTTOM);
		lbl_grandTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_grandTotal.setPreferredSize(new Dimension(204, 24));
		pnl_grandTotal.add(lbl_grandTotal);
		
		grandTotal = new JLabel("19.64");
		grandTotal.setForeground(new Color(70, 130, 180));
		grandTotal.setAlignmentY(Component.TOP_ALIGNMENT);
		grandTotal.setVerticalAlignment(SwingConstants.TOP);
		grandTotal.setPreferredSize(new Dimension(204, 48));
		grandTotal.setMinimumSize(new Dimension(204, 14));
		grandTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		grandTotal.setFont(getFont().deriveFont(Font.BOLD, 41f));
		pnl_grandTotal.add(grandTotal);
		
		JPanel pnl_payment = new JPanel();
		pnl_pay.add(pnl_payment);
		pnl_payment.setBorder(null);
		pnl_payment.setPreferredSize(new Dimension(380, 80));
		pnl_payment.setLayout(new GridLayout(2, 2, 5, 5));
		
		btn_cash = new JPanel();
		btn_cash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		FlowLayout fl_btn_cash = (FlowLayout) btn_cash.getLayout();
		fl_btn_cash.setVgap(0);
		fl_btn_cash.setHgap(0);
		pnl_payment.add(btn_cash);
		btn_cash.setBackground(new Color(70, 130, 180));
		btn_cash.setPreferredSize(new Dimension(100, 34));
		
		JLabel ico_cash = new JLabel("\uf3d1");
		ico_cash.setAlignmentY(Component.TOP_ALIGNMENT);
		ico_cash.setPreferredSize(new Dimension(24, 36));
		ico_cash.setForeground(new Color(255, 255, 255));
		ico_cash.setFont(fontAwesome.deriveFont(Font.PLAIN, 24f));
		btn_cash.add(ico_cash);
		
		JLabel lbl_cash = new JLabel("Cash");
		lbl_cash.setFont(getFont().deriveFont(Font.PLAIN, 14));
		lbl_cash.setForeground(new Color(255, 255, 255));
		btn_cash.add(lbl_cash);
		
		btn_card = new JPanel();
		btn_card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		FlowLayout fl_btn_card = (FlowLayout) btn_card.getLayout();
		fl_btn_card.setVgap(0);
		fl_btn_card.setHgap(0);
		btn_card.setPreferredSize(new Dimension(100, 32));
		btn_card.setBackground(new Color(70, 130, 180));
		pnl_payment.add(btn_card);
		
		JLabel ico_card = new JLabel("\uf09d");
		ico_card.setPreferredSize(new Dimension(24, 36));
		ico_card.setForeground(Color.WHITE);
		ico_card.setFont(fontAwesome.deriveFont(Font.PLAIN, 24f));
		btn_card.add(ico_card);
		
		JLabel lbl_card = new JLabel("Card");
		lbl_card.setForeground(Color.WHITE);
		lbl_card.setFont(getFont().deriveFont(Font.PLAIN, 14));
		btn_card.add(lbl_card);
		
		btn_print = new JPanel();
		btn_print.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		FlowLayout flowLayout = (FlowLayout) btn_print.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		btn_print.setPreferredSize(new Dimension(100, 32));
		btn_print.setBackground(new Color(72, 209, 204));
		pnl_payment.add(btn_print);
		
		JLabel ico_print = new JLabel("\uf02f");
		ico_print.setPreferredSize(new Dimension(24, 36));
		ico_print.setForeground(Color.WHITE);
		ico_print.setFont(fontAwesome.deriveFont(Font.PLAIN, 24f));
		btn_print.add(ico_print);
		
		JLabel lbl_print = new JLabel("Print");
		lbl_print.setForeground(Color.WHITE);
		lbl_print.setFont(lbl_print.getFont().deriveFont(14f));
		btn_print.add(lbl_print);
		
		btn_cancel = new JPanel();
		btn_cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		FlowLayout fl_btn_cancel = (FlowLayout) btn_cancel.getLayout();
		fl_btn_cancel.setHgap(0);
		fl_btn_cancel.setVgap(0);
		btn_cancel.setPreferredSize(new Dimension(100, 32));
		btn_cancel.setBackground(new Color(72, 209, 204));
		pnl_payment.add(btn_cancel);
		
		JLabel lbl_cancel = new JLabel("Cancel");
		lbl_cancel.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_cancel.setFont(getFont().deriveFont(Font.PLAIN, 14));
		lbl_cancel.setPreferredSize(new Dimension(185, 36));
		lbl_cancel.setForeground(Color.WHITE);
		btn_cancel.add(lbl_cancel);
		
		btn_remove = new ButtonColumn(tbl_order, mdl_order.getColumnCount() - 1, this);
		btn_remove.setMnemonic(KeyEvent.VK_DELETE);
		
		reset();
		
		chk_tax.addItemListener(this);
		mdl_order.addTableModelListener(this);
		lbl_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EditOrder.this.setVisible(false);
				selectTable.openToChange(order);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lbl_table.setText("<html><u>Change table</u><html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lbl_table.setText("Change table");
			}
		});
		btn_cash.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!btn_cash.isEnabled()) return;
				if (JOptionPane.showConfirmDialog(EditOrder.this, "Are you sure?", "Paying by Cash", JOptionPane.YES_NO_OPTION)
						!= JOptionPane.YES_OPTION) {
					return;
				}
				
				payOrder(TransactionType.CASH, order);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!btn_cash.isEnabled()) return;
				btn_cash.setBackground(new Color(51, 102, 153));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!btn_cash.isEnabled()) return;
				btn_cash.setBackground(new Color(70, 130, 180));
			}
		});
		btn_card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!btn_card.isEnabled()) return;
				if (JOptionPane.showConfirmDialog(EditOrder.this, "Are you sure?", "Paying by Card", JOptionPane.YES_NO_OPTION)
						!= JOptionPane.YES_OPTION) {
					return;
				}
				
				payOrder(TransactionType.CREDITCARD, order);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!btn_card.isEnabled()) return;
				btn_card.setBackground(new Color(51, 102, 153));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!btn_card.isEnabled()) return;
				btn_card.setBackground(new Color(70, 130, 180));
			}
		});
		btn_print.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!btn_print.isEnabled()) return;

				order.setItems(mdl_order.getItems());
				
				printOrder(order);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!btn_print.isEnabled()) return;
				btn_print.setBackground(new Color(32, 178, 170));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!btn_print.isEnabled()) return;
				btn_print.setBackground(new Color(72, 209, 204));
			}
		});
		btn_cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancel();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_cancel.setBackground(new Color(32, 178, 170));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_cancel.setBackground(new Color(72, 209, 204));
			}
		});
		
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
		setVisible(false);
		
		order = new Order();
		isCreating = true;
		lastKeyword = "";
		fetchingData = false;
		
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		updatePrices();
		
		txt_search.setText("");
	}
	public void openToCreate() {
		open();
		
		order = new Order();
		
		setVisible(false);
		selectTable.openToSelect(order);
	}
	public void openToUpdate(Order order) {
		open();
		
		this.order = order;
		isCreating = false;
		
		lbl_title.setText(String.format("Order#%d for Table %02d", order.getId(), order.getTableNo()));
		mdl_order.setItems(order.getItems());
		updatePrices();
	}
	@Override
	public void performed() {
		if (isCreating && order.getId() < 1)
			createOrder(order.getTableNo());
		else if (order.getId() > 0)
			updateOrder(order);
		
		lbl_title.setText(String.format("Order#%d for Table %02d", order.getId(), order.getTableNo()));
		setVisible(true);
	}
	@Override
	public void cancelled() {
		setVisible(true);
	}
	private void categorizeMenuItems(ArrayList<MenuItem> items) {
		//Empty container
		pnl_menu.removeAll();
		pnl_menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		int totalHeight = 0;
		if (items.isEmpty()) {
			JLabel lbl_empty = new JLabel();
			lbl_empty.setText("No results.");
			lbl_empty.setBorder(new EmptyBorder(10, 10, 10, 10));
			lbl_empty.setForeground(SystemColor.textInactiveText);
			lbl_empty.setFont(getFont().deriveFont(Font.BOLD, 14));
			lbl_empty.setPreferredSize(new Dimension(400, 32));
			pnl_menu.add(lbl_empty);
			
			totalHeight += lbl_empty.getPreferredSize().getHeight();
		} else {
			FlowLayout flt_group, flt_items, flt_item;
			JPanel pnl_group, pnl_items;
			MenuItemPanel pnl_item;
			JLabel lbl_groupName, lbl_itemId, lbl_itemName;
			
			flt_group = new FlowLayout(FlowLayout.LEFT, 0, 0);
			flt_items = new FlowLayout(FlowLayout.LEFT, 10, 10);
			flt_item = new FlowLayout(FlowLayout.CENTER, 0, 0);
			pnl_group = null;
			pnl_items = null;
			
			pnl_menu.removeAll();
			pnl_menu.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
			totalHeight = 0;
			MenuItemCategory category = new MenuItemCategory();
			for (MenuItem item : items) {
				if (!item.getCategory().equals(category)) {
					category = item.getCategory();
					
					pnl_group = new JPanel(flt_group);
					pnl_group.setBackground(SystemColor.window);
					pnl_menu.add(pnl_group);
					
					lbl_groupName = new JLabel(category.getName());
					lbl_groupName.setForeground(SystemColor.textInactiveText);
					lbl_groupName.setFont(getFont().deriveFont(Font.BOLD, 14));
					lbl_groupName.setBorder(new EmptyBorder(10, 10, 10, 10));
					lbl_groupName.setPreferredSize(new Dimension(400, 32));
					pnl_group.add(lbl_groupName);
					
					pnl_items = new JPanel(flt_items);
					pnl_items.setBackground(SystemColor.menu);
					pnl_items.setOpaque(false);
					pnl_items.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
					pnl_items.setPreferredSize(new Dimension(400, flt_items.getVgap()));
					pnl_group.add(pnl_items);
					
					pnl_group.setPreferredSize(new Dimension(400, (int)lbl_groupName.getPreferredSize().getHeight()
							+ (int)pnl_items.getPreferredSize().getHeight()));
					totalHeight += (int)pnl_group.getPreferredSize().getHeight();
				}
				
				pnl_item = new MenuItemPanel(flt_item);
				pnl_item.setPreferredSize(new Dimension(80, 80));
				pnl_item.setBorder(new EmptyBorder(8, 8, 8, 8));
				pnl_item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				pnl_item.setMenuItem(item);
				pnl_items.add(pnl_item);
				pnl_item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						JPanel btn = (JPanel)e.getSource();
						btn.setBackground(SystemColor.scrollbar);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						JPanel btn = (JPanel)e.getSource();
						btn.setBackground(SystemColor.menu);
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						MenuItemPanel btn = (MenuItemPanel)e.getSource();
						OrderMenuItem orderItem = new OrderMenuItem();
						orderItem.setMenuItem(btn.getMenuItem());
						orderItem.setOrder(order);
						orderItem.setQuantity(1);
						
						addOrderMenuItem(orderItem);
					}
				});
				
				lbl_itemId = new JLabel(Integer.toString(item.getId()));
				lbl_itemId.setForeground(SystemColor.textInactiveText);
				lbl_itemId.setFont(getFont().deriveFont(Font.PLAIN, 32));
				lbl_itemId.setVerticalAlignment(SwingConstants.BOTTOM);
				lbl_itemId.setHorizontalAlignment(SwingConstants.CENTER);
				lbl_itemId.setPreferredSize(new Dimension(62, 38));
				pnl_item.add(lbl_itemId);
				
				lbl_itemName = new JLabel("<html><p text-align:'center'>" + item.getName() + "</p></html>");
				lbl_itemName.setForeground(SystemColor.textInactiveText);
				lbl_itemName.setFont(getFont().deriveFont(Font.PLAIN, 11));
				lbl_itemName.setHorizontalTextPosition(SwingConstants.CENTER);
				lbl_itemName.setHorizontalAlignment(SwingConstants.CENTER);
				lbl_itemName.setVerticalAlignment(SwingConstants.TOP);
				lbl_itemName.setPreferredSize(new Dimension(62, 28));
				pnl_item.add(lbl_itemName);
				
				if (pnl_items.getComponentCount() % 5 == 1) {
					int newRowHeight = (int)pnl_item.getPreferredSize().getHeight() + flt_items.getVgap();
					Dimension dim = pnl_items.getPreferredSize();
					dim.height += newRowHeight;
					pnl_items.setPreferredSize(dim);
					dim = pnl_group.getPreferredSize();
					dim.height += newRowHeight;
					pnl_group.setPreferredSize(dim);
					
					totalHeight += newRowHeight;
				}
			}
		}
		
		Dimension dim = pnl_menu.getPreferredSize();
		dim.height = totalHeight;
		pnl_menu.setPreferredSize(dim);
		pnl_menu.revalidate();
		pnl_menu.repaint();
	}
	private void updatePrices() {
		double subtotal = 0, tax = 0, total = 0;
		for (OrderMenuItem item : mdl_order.getItems()) {
			subtotal += item.getQuantity() * item.getMenuItem().getPrice();
		}
		total = subtotal * 1.25;
		tax = total - subtotal;
		if (!chk_tax.isSelected()) {
			total -= tax;
		}
		
		this.subtotal.setText(Double.toString(subtotal));
		this.discount.setText(Double.toString(0d));
		this.tax.setText(Double.toString(tax));
		this.grandTotal.setText(Double.toString(total));
	}
	//Functionalities
	private void createOrder(int tableNo) {
		order.setTableNo(tableNo);
		order.setId(orderCtrl.createOrder(tableNo));
	}
	private void updateOrder(Order order) {
		orderCtrl.updateOrder(order);
	}
	private void payOrder(TransactionType payment, Order order) {
		String message, title;
		int messageType;
		
		if (!orderCtrl.payOrder(payment, order)) {
			message = "An error occured while paying the Order!";
			title = "Error!";
			messageType = JOptionPane.ERROR_MESSAGE;
		} else {
			message = "The Order was successfully payed!";
			title = "Success!";
			messageType = JOptionPane.INFORMATION_MESSAGE;
			
			close();
			triggerPerformListeners();
		}
		
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
	private void printOrder(Order order) {
		Font monaco = importFont("monaco.ttf");
		
		String text = ""
			+ "\r\n" + "\t" 
			+ "\r\n" + "Jernbanegade 2, 9000 Aalborg"
			+ "\r\n" + "+45 25 75 03 11"
			+ "\r\n" + "www.nobustreetfood.dk"
			+ "\r\n" + "kundenservice@nobustreetfood.dk"
			+ "\r\n" + "CVR: 28288662"
			+ "\r\n"
			+ "\r\n" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date())
			+ "\r\n"
			+ "\r\n"
			+ "\r\n" + "          " +"Invoice no. #" + order.getId()
			+ "\r\n" + "-------------------------------------";
		
		double total = 0.0;
		for (OrderMenuItem orderItem : order.getItems()) {
			MenuItem menuItem = orderItem.getMenuItem();
			text += String.format("\r\n%-22.22s%-5sDKK %4.1f", menuItem.getName(), orderItem.getQuantity() + "x",
					orderItem.getQuantity() * menuItem.getPrice());
			total += menuItem.getPrice() * orderItem.getQuantity();
		}
		text += "\r\n" + "-------------------------------------"
		    + "\r\n" + String.format("%-27sDKK %4.1f", "Subtotal", total)
		    + "\r\n" + String.format("%-27sDKK %4.1f", "VAT (25%)", total*0.25)
		    + "\r\n"
		    + "\r\n" + String.format("%-27sDKK %4.1f", "Total", total*1.25);
		
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(monaco.deriveFont(Font.PLAIN, 10f));
		textPane.setText(text);
		
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(OrientationRequested.PORTRAIT);
		aset.add(new MediaPrintableArea((float)0.0,(float)0.0,100, 185, MediaPrintableArea.MM));


		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
		
		try {
			textPane.print(
					new MessageFormat("NOBU STREET FOOD"),
					new MessageFormat("“Let’s give them all a 12”"),
					false, service, aset, false);
		} catch (PrinterException e) {
			e.printStackTrace();
		}
	}
	private void cancelOrder(Order order) {
		orderCtrl.cancelOrder(order);
	}
	private void addOrderMenuItem(OrderMenuItem orderItem) {
		if (orderCtrl.hasOrderMenuItem(orderItem)) return;
		
		boolean forced = false;
		if (!orderCtrl.canAddOrderMenuItem(orderItem)) {
			if (JOptionPane.showConfirmDialog(this, 
					"There are not enough ingredients for this Menu item in the inventory!\nDo you still want to add it to the Order?", 
					"Adding Menu Item", JOptionPane.YES_NO_OPTION)
					!= JOptionPane.YES_OPTION) {
				return;
			}
			forced = true;
		}
		
		if (!orderCtrl.addOrderMenuItem(orderItem, forced)) {
			JOptionPane.showMessageDialog(this,
				    "An error occured while adding the Menu Item to the Order!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		mdl_order.addItem(orderItem);
	}
	private void editOrderMenuItem(OrderMenuItem orderItem, int newQuantity) {
		if (!orderCtrl.hasOrderMenuItem(orderItem)) return;
		int prevQuantity = orderItem.getQuantity();
		
		boolean forced = false;
		if (newQuantity > prevQuantity) {
			orderItem.setQuantity(newQuantity - prevQuantity);
			if (!orderCtrl.canAddOrderMenuItem(orderItem)) {
				if (JOptionPane.showConfirmDialog(EditOrder.this, 
						"There are not enough ingredients for this Menu orderItem in the inventory!\nDo you still want to update the newQuantity?", 
						"Update Menu orderItem", JOptionPane.YES_NO_OPTION)
						!= JOptionPane.YES_OPTION) {
					orderItem.setQuantity(prevQuantity);
					return;
				}
				forced = true;
			}
		}
		orderItem.setQuantity(newQuantity);
			
		if (!orderCtrl.editOrderMenuItem(orderItem, forced)) {
			JOptionPane.showMessageDialog(EditOrder.this,
				    "The Menu orderItem was not updated!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			orderItem.setQuantity(prevQuantity);
			return;
		}

		updatePrices();
	}
	private void removeOrderMenuItem(OrderMenuItem orderItem) {
		if (!orderCtrl.removeOrderMenuItem(orderItem)) {
			JOptionPane.showMessageDialog(this,
				    "The Menu item was not removed!",
				    "Error!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		mdl_order.removeItem(orderItem);
	}
	private void cancel() {
		if (isCreating && order.getId() > 0) {
			cancelOrder(order);
		}
		
		close();
		triggerCancelListeners();
	}
	private void searchMenuItems() {
		if (fetchingData) return;
		
		String keyword = txt_search.getText().trim();
		if (lastKeyword.equals(keyword)) return;
		
		fetchingData = true;
		lastKeyword = keyword;
		
		new FetchWorker(keyword).execute();
	}
	private boolean isFilled() {
		if (mdl_order.getItems().isEmpty())
			return false;
		
		return true;
	}
	//EventListeners
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == btn_remove) {
			int modelRowIndex = Integer.valueOf(e.getActionCommand());
			OrderMenuItem orderItem = mdl_order.getItem(modelRowIndex);
			
			removeOrderMenuItem(orderItem);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == chk_tax) {
			updatePrices();
		}
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		if (e.getSource() == txt_search) {
			searchMenuItems();
		}
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getSource() == mdl_order) {
			updatePrices();
			btn_cash.setEnabled(isFilled());
			btn_card.setEnabled(isFilled());
			btn_print.setEnabled(isFilled());
		}
	}
	//Classes
	private class OrderTableModel extends ItemTableModel<OrderMenuItem> {

		public OrderTableModel() {
			super();
			
			columns = new String[] { "Item No.", "Name", "Price", "Quantity", "Total", ""};
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			OrderMenuItem orderItem = getItem(rowIndex);
			MenuItem item = orderItem.getMenuItem();
			
			switch(columnIndex) {
				case 0:
					return item.getId();
				case 1:
					return item.getName();
				case 2:
					return item.getPrice();
				case 3:
					return orderItem.getQuantity();
				case 4:
					return item.getPrice() * orderItem.getQuantity();
				case 5:
					return "x";
			}
			
			return null;
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return (columnIndex == 3 || columnIndex == 5);
		}
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 3) {
				int quantity = (int)value;
				if (quantity <= 0) return;
				OrderMenuItem item = getItem(rowIndex);
				
				editOrderMenuItem(item, quantity);
				
				update();
			}
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
					? itemCtrl.getOrderedMenuItems()
					: itemCtrl.searchOrderedMenuItems(keyword);
		}
		@Override
		protected void done() {
			try {
				categorizeMenuItems(get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			fetchingData = false;
			searchMenuItems();
		}
	}
	private class MenuItemPanel extends JPanel {
		
		private MenuItem item;
		
		public MenuItemPanel() {
			super();
			item = null;
		}
		public MenuItemPanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
			item = null;
		}
		public MenuItemPanel(LayoutManager lm) {
			super(lm);
			item = null;
		}
		public MenuItemPanel(LayoutManager lm, boolean isDoubleBuffered) {
			super(lm, isDoubleBuffered);
			item = null;
		}
		
		public MenuItem getMenuItem() {
			return item;
		}
		public void setMenuItem(MenuItem item) {
			this.item = item;
		}
	}
}
