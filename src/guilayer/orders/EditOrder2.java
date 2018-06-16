package guilayer.orders;

import java.awt.RenderingHints;

import javax.swing.JTextField;
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
import ctrllayer.MenuItemController;
import guilayer.WaiterWindow;
import guilayer.essentials.ButtonColumn;
import guilayer.essentials.ItemTableModel;
import guilayer.essentials.PerformPanel;
import modlayer.OrderMenuItem;
import modlayer.MenuItem;
import modlayer.MenuItemCategory;
import modlayer.Order;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class EditOrder2 extends PerformPanel implements ActionListener, CaretListener, TableModelListener, ItemListener {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JFrame window = new JFrame();
					
					EditOrder2 editor = new EditOrder2();
					window.getContentPane().add(editor);
					
					editor.openToCreate();
					window.pack();
					window.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
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
	private JLabel lblOrder;
	private JLabel lbl_table;
	private JPanel pnl_menu;
	private JCheckBox chk_tax;
	
	public EditOrder2() {
		super();
		setSize(new Dimension(800, 500));
		setPreferredSize(new Dimension(800, 500));
		setBackground(SystemColor.window);
		
		itemCtrl = new MenuItemController();
		orderCtrl = new OrderController();
		
		order = null;
		isCreating = true;
		lastKeyword = "";
		fetchingData = false;
		
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
		
		lblOrder = new JLabel("Order#32 for Table 10");
		lblOrder.setHorizontalAlignment(SwingConstants.CENTER);
		lblOrder.setPreferredSize(new Dimension(620, 32));
		lblOrder.setForeground(SystemColor.textInactiveText);
		lblOrder.setFont(getFont().deriveFont(Font.BOLD, 14));
		pnl_top.add(lblOrder);
		
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
		
		JPanel group_1 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) group_1.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		group_1.setBackground(SystemColor.window);
		group_1.setPreferredSize(new Dimension(400, 222));
		pnl_menu.add(group_1);
		
		JLabel lbl_groupName_1 = new JLabel("Menu Category");
		lbl_groupName_1.setForeground(SystemColor.textInactiveText);
		lbl_groupName_1.setFont(getFont().deriveFont(Font.BOLD, 14));
		lbl_groupName_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		lbl_groupName_1.setPreferredSize(new Dimension(400, 32));
		group_1.add(lbl_groupName_1);
		
		JPanel itemsContainer = new JPanel();
		itemsContainer.setBackground(SystemColor.window);
		itemsContainer.setOpaque(false);
		itemsContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		itemsContainer.setPreferredSize(new Dimension(400, 190));
		group_1.add(itemsContainer);
		itemsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		JPanel item_1 = new JPanel();
		item_1.setBackground(SystemColor.scrollbar);
		item_1.setBorder(new EmptyBorder(8, 8, 8, 8));
		item_1.setPreferredSize(new Dimension(80, 80));
		FlowLayout flowLayout_2 = (FlowLayout) item_1.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		itemsContainer.add(item_1);
		
		JLabel label = new JLabel("12");
		label.setForeground(SystemColor.textInactiveText);
		label.setFont(getFont().deriveFont(Font.PLAIN, 32));
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(62, 38));
		item_1.add(label);
		
		JLabel lblRoastedChickenWith = new JLabel("<html><p text-align:'center'>Menu item name</p></html>");
		lblRoastedChickenWith.setForeground(SystemColor.textInactiveText);
		lblRoastedChickenWith.setFont(getFont().deriveFont(Font.PLAIN, 11));
		lblRoastedChickenWith.setHorizontalTextPosition(SwingConstants.CENTER);
		lblRoastedChickenWith.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoastedChickenWith.setVerticalAlignment(SwingConstants.TOP);
		lblRoastedChickenWith.setPreferredSize(new Dimension(62, 28));
		item_1.add(lblRoastedChickenWith);
		
		JPanel item_2 = new JPanel();
		item_2.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_2);
		
		JPanel item_3 = new JPanel();
		item_3.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_3);
		
		JPanel item_4 = new JPanel();
		item_4.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_4);
		
		JPanel item_5 = new JPanel();
		item_5.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_5);
		
		JPanel item_6 = new JPanel();
		item_6.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_6);
		
		JPanel item_7 = new JPanel();
		item_7.setPreferredSize(new Dimension(80, 80));
		itemsContainer.add(item_7);
		
		JPanel group_2 = new JPanel();
		group_2.setPreferredSize(new Dimension(400, 142));
		group_2.setBackground(Color.WHITE);
		pnl_menu.add(group_2);
		
		JLabel lbl_groupName_2 = new JLabel("Menu Category");
		lbl_groupName_2.setPreferredSize(new Dimension(400, 32));
		lbl_groupName_2.setForeground(SystemColor.textInactiveText);
		lbl_groupName_2.setFont(getFont().deriveFont(Font.BOLD, 14));
		lbl_groupName_2.setBorder(new EmptyBorder(10, 10, 10, 10));
		group_2.add(lbl_groupName_2);
		
		JPanel itemsContainer_2 = new JPanel();
		itemsContainer_2.setPreferredSize(new Dimension(400, 100));
		itemsContainer_2.setOpaque(false);
		itemsContainer_2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		itemsContainer_2.setBackground(Color.WHITE);
		group_2.add(itemsContainer_2);
		itemsContainer_2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		JPanel item_8 = new JPanel();
		item_8.setPreferredSize(new Dimension(80, 80));
		itemsContainer_2.add(item_8);
		
		JPanel item_9 = new JPanel();
		item_9.setPreferredSize(new Dimension(80, 80));
		itemsContainer_2.add(item_9);
		
		JPanel item_10 = new JPanel();
		item_10.setPreferredSize(new Dimension(80, 80));
		itemsContainer_2.add(item_10);
		
		JPanel item_11 = new JPanel();
		item_11.setPreferredSize(new Dimension(80, 80));
		itemsContainer_2.add(item_11);
		
		JPanel group_3 = new JPanel();
		group_3.setPreferredSize(new Dimension(400, 222));
		group_3.setBackground(Color.WHITE);
		pnl_menu.add(group_3);
		
		JLabel lbl_groupName_3 = new JLabel("Menu Category");
		lbl_groupName_3.setPreferredSize(new Dimension(400, 32));
		lbl_groupName_3.setForeground(SystemColor.textInactiveText);
		lbl_groupName_3.setFont(getFont().deriveFont(Font.BOLD, 14));
		lbl_groupName_3.setBorder(new EmptyBorder(10, 10, 10, 10));
		group_3.add(lbl_groupName_3);
		
		JPanel itemsContainer_3 = new JPanel();
		itemsContainer_3.setPreferredSize(new Dimension(400, 190));
		itemsContainer_3.setOpaque(false);
		itemsContainer_3.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		itemsContainer_3.setBackground(Color.WHITE);
		group_3.add(itemsContainer_3);
		itemsContainer_3.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		JPanel item_12 = new JPanel();
		item_12.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_12);
		
		JPanel item_13 = new JPanel();
		item_13.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_13);
		
		JPanel item_14 = new JPanel();
		item_14.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_14);
		
		JPanel item_15 = new JPanel();
		item_15.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_15);
		
		JPanel item_16 = new JPanel();
		item_16.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_16);
		
		JPanel item_17 = new JPanel();
		item_17.setPreferredSize(new Dimension(80, 80));
		itemsContainer_3.add(item_17);
		
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
		scr_order.setPreferredSize(new Dimension(400, 268));
		scr_order.setBorder(BorderFactory.createEmptyBorder());
		pnl_right.add(scr_order);
		
		tbl_order = new JTable();
		tbl_order.setShowHorizontalLines(false);
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
		
		JPanel btn_cash = new JPanel();
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
		
		JPanel btn_card = new JPanel();
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
		
		JPanel btn_scan = new JPanel();
		FlowLayout fl_btn_scan = (FlowLayout) btn_scan.getLayout();
		fl_btn_scan.setVgap(0);
		fl_btn_scan.setHgap(0);
		btn_scan.setPreferredSize(new Dimension(100, 32));
		btn_scan.setBackground(new Color(72, 209, 204));
		pnl_payment.add(btn_scan);
		
		
		JLabel lbl_scan = new JLabel("Scan");
		lbl_scan.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_scan.setFont(getFont().deriveFont(Font.PLAIN, 14));
		lbl_scan.setPreferredSize(new Dimension(185, 36));
		lbl_scan.setForeground(Color.WHITE);
		btn_scan.add(lbl_scan);
		
		JPanel btn_other = new JPanel();
		FlowLayout fl_btn_other = (FlowLayout) btn_other.getLayout();
		fl_btn_other.setHgap(0);
		fl_btn_other.setVgap(0);
		btn_other.setPreferredSize(new Dimension(100, 32));
		btn_other.setBackground(new Color(72, 209, 204));
		pnl_payment.add(btn_other);
		
		JLabel lbl_other = new JLabel("Other");
		lbl_other.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_other.setFont(getFont().deriveFont(Font.PLAIN, 14));
		lbl_other.setPreferredSize(new Dimension(185, 36));
		lbl_other.setForeground(Color.WHITE);
		btn_other.add(lbl_other);
		btn_remove.setMnemonic(KeyEvent.VK_DELETE);
		mdl_order.addTableModelListener(this);
	}
	private Font importFont(String fontName) {
		try {
			InputStream is = EditOrder2.class.getResourceAsStream("/font/" + fontName);
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
		order = null;
		isCreating = true;
		
		mdl_order.setItems(new ArrayList<OrderMenuItem>());
		updatePrices();
		
		txt_search.setText("");
	}
	public void openToCreate() {
		open();
	}
	public void openToUpdate(Order order) {
		open();
		
		this.order = order;
		isCreating = false;
		
		mdl_order.setItems(order.getItems());
		updatePrices();
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
		pnl_menu.validate();
		pnl_menu.repaint();
	}
	private void updatePrices() {
		double subtotal = 0, tax = 0, total = 0;
		for (OrderMenuItem item : mdl_order.getItems()) {
			subtotal += item.getQuantity() * item.getMenuItem().getPrice();
		}
		total = subtotal * (chk_tax.isSelected() ? 1.25 : 1);
		tax = total - subtotal;
		
		this.subtotal.setText(Double.toString(subtotal));
		this.discount.setText("0");
		this.tax.setText(Double.toString(tax));
		this.grandTotal.setText(Double.toString(total));
	}
	//Functionalities
	private void createOrder(int tableNo) {
		order = new Order();
		order.setTableNo(tableNo);
		order.setId(orderCtrl.createOrder(tableNo));
	}
	private void updateOrder(Order order) {
		orderCtrl.updateOrder(order);
	}
	private void cancelOrder(Order order) {
		orderCtrl.cancelOrder(order);
	}
	private void addOrderMenuItem(OrderMenuItem orderItem) {
		if (orderCtrl.hasOrderMenuItem(orderItem)) {
			for (OrderMenuItem item : mdl_order.getItems()) {
				if (item.equals(orderItem)) {
					editOrderMenuItem(orderItem, item.getQuantity()+orderItem.getQuantity());
					break;
				}
			}
			return;
		}
		
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
				if (JOptionPane.showConfirmDialog(EditOrder2.this, 
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
			JOptionPane.showMessageDialog(EditOrder2.this,
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
	private void submit() {
		String message = (isCreating ? "Creating" : "Updating") + " an Order";
		String title = "Are you sure?";
		int messageType = JOptionPane.YES_NO_OPTION;

		if (JOptionPane.showConfirmDialog(this, message, title, messageType) != JOptionPane.YES_OPTION) {
			return;
		}
		
		message = "The Order was successfully " + (isCreating ? "created" : "updated") + "!";
		title = "Success!";
		messageType = JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(this, message, title, messageType);

		close();
		triggerPerformListeners();
	}
	private void cancel() {
		if (isCreating && order != null) {
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
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
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
					return "Remove";
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
	private class PlaceholderTextField extends JTextField {

	    private String placeholder;

	    public PlaceholderTextField() {
	    }

	    public PlaceholderTextField(
	        final Document pDoc,
	        final String pText,
	        final int pColumns)
	    {
	        super(pDoc, pText, pColumns);
	    }

	    public PlaceholderTextField(final int pColumns) {
	        super(pColumns);
	    }

	    public PlaceholderTextField(final String pText) {
	        super(pText);
	    }

	    public PlaceholderTextField(final String pText, final int pColumns) {
	        super(pText, pColumns);
	    }

	    public String getPlaceholder() {
	        return placeholder;
	    }

	    @Override
	    protected void paintComponent(final Graphics pG) {
	        super.paintComponent(pG);

	        if (placeholder.length() == 0 || getText().length() > 0) {
	            return;
	        }

	        final Graphics2D g = (Graphics2D) pG;
	        g.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setColor(getDisabledTextColor());
	        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
	            .getMaxAscent() + getInsets().top);
	    }

	    public void setPlaceholder(final String s) {
	        placeholder = s;
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
