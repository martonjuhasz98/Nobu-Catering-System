package guilayer.orders;

import guilayer.ManagerWindow;
import guilayer.essentials.PerformPanel;
import modlayer.Order;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;

import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class SelectTable extends PerformPanel {

	private int noOfTables;
	private TablePanel selectedTable;
	private Order order;
	private JLabel btn_confirm;
	private JLabel btn_cancel;
	private JPanel pnl_tables;
	private JLabel lbl_title;
	private JPanel center;
	
	public SelectTable(int noOfTables) {
		super();

		this.noOfTables = noOfTables;
		
		initialize();
	}
	//Layout
	private void initialize() {

		setVisible(false);
		setBounds(0, 0, ManagerWindow.contentWidth, ManagerWindow.totalHeight);
		setBackground(Color.WHITE);
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		int totalHeight = 0;
		totalHeight += 32;
		totalHeight += 10 + noOfTables / 5 * 60;
		totalHeight += 40;
		
		int borderHeight = ManagerWindow.totalHeight;
		borderHeight -= totalHeight;
		borderHeight /= 2;
		
		center = new JPanel();
		center.setBackground(SystemColor.window);
		center.setBorder(new CompoundBorder(new EmptyBorder(borderHeight, 0, borderHeight, 0), new LineBorder(new Color(109, 109, 109))));
		add(center);
		center.setPreferredSize(new Dimension(310, 500));
		center.setLayout(new BorderLayout(0, 0));
		
		lbl_title = new JLabel("Select table");
		lbl_title.setOpaque(true);
		lbl_title.setBackground(SystemColor.menu);
		center.add(lbl_title, BorderLayout.NORTH);
		lbl_title.setForeground(SystemColor.textInactiveText);
		lbl_title.setPreferredSize(new Dimension(310, 32));
		lbl_title.setBorder(null);
		lbl_title.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnl_tables = new JPanel();
		center.add(pnl_tables, BorderLayout.CENTER);
		pnl_tables.setPreferredSize(new Dimension(310, 310));
		pnl_tables.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) pnl_tables.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JPanel pnl_buttons = new JPanel();
		pnl_buttons.setBackground(SystemColor.window);
		center.add(pnl_buttons, BorderLayout.SOUTH);
		FlowLayout flowLayout_1 = (FlowLayout) pnl_buttons.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		
		btn_confirm = new JLabel("Confirm");
		btn_confirm.setForeground(SystemColor.controlDkShadow);
		btn_confirm.setBackground(SystemColor.scrollbar);
		btn_confirm.setOpaque(true);
		btn_confirm.setHorizontalAlignment(SwingConstants.CENTER);
		btn_confirm.setPreferredSize(new Dimension(96, 32));
		btn_confirm.setFont(getFont().deriveFont(Font.PLAIN, 14f));
		btn_confirm.setBorder(null);
		btn_confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pnl_buttons.add(btn_confirm);
		
		btn_cancel = new JLabel("Cancel");
		btn_cancel.setVisible(false);
		btn_cancel.setForeground(SystemColor.controlDkShadow);
		btn_cancel.setBackground(SystemColor.scrollbar);
		btn_cancel.setOpaque(true);
		btn_cancel.setHorizontalAlignment(SwingConstants.CENTER);
		btn_cancel.setPreferredSize(new Dimension(96, 32));
		btn_cancel.setFont(getFont().deriveFont(Font.PLAIN, 14f));
		btn_cancel.setBorder(null);
		btn_cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		pnl_buttons.add(btn_cancel);
		
		btn_confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!btn_confirm.isEnabled()) return;
				btn_confirm.setBackground(SystemColor.DARK_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!btn_confirm.isEnabled()) return;
				btn_confirm.setBackground(SystemColor.scrollbar);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!btn_confirm.isEnabled()) return;
				
				selectedTable.setBackground(SystemColor.menu);
				
				order.setTableNo(selectedTable.getTableNo());
				
				triggerPerformListeners();
				close();
			}
		});
		btn_cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_cancel.setBackground(SystemColor.DARK_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_cancel.setBackground(SystemColor.scrollbar);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				cancel();
			}
		});
		
		generateTablePanels();
		reset();
	}
	private void generateTablePanels() {
		TablePanel pnl_table;
		JLabel lbl_no, lbl_table;
		
		for (int i = 1; i < noOfTables+1; i++) {
			pnl_table = new TablePanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			pnl_table.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnl_table.setPreferredSize(new Dimension(50, 50));
			pnl_table.setBackground(SystemColor.menu);
			pnl_table.setTableNo(i);
			pnl_table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			pnl_tables.add(pnl_table);
			
			lbl_no = new JLabel(String.format("%02d", i));
			lbl_no.setForeground(SystemColor.textInactiveText);
			lbl_no.setVerticalAlignment(SwingConstants.BOTTOM);
			lbl_no.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_no.setPreferredSize(new Dimension(40, 28));
			lbl_no.setFont(getFont().deriveFont(Font.PLAIN, 24));
			pnl_table.add(lbl_no);
			
			lbl_table = new JLabel("Table");
			lbl_table.setForeground(SystemColor.textInactiveText);
			lbl_table.setFont(getFont().deriveFont(Font.PLAIN, 11));
			lbl_table.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_table.setVerticalAlignment(SwingConstants.TOP);
			lbl_table.setPreferredSize(new Dimension(40, 12));
			pnl_table.add(lbl_table);
			
			pnl_table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					TablePanel btn = (TablePanel)e.getSource();
					if (btn != selectedTable)
						btn.setBackground(SystemColor.scrollbar);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					TablePanel btn = (TablePanel)e.getSource();
					if (btn != selectedTable)
						btn.setBackground(SystemColor.menu);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if (selectedTable != null)
						selectedTable.setBackground(SystemColor.menu);
					
					TablePanel btn = (TablePanel)e.getSource();
					btn.setBackground(SystemColor.DARK_GRAY);
					
					selectedTable = btn;
					btn_confirm.setEnabled(isFilled());
				}
			});
		}
	}
	@Override
	public void prepare() {
		reset();
	}
	@Override
	public void reset() {
		if (selectedTable != null) {
			selectedTable.setBackground(SystemColor.menu);
			selectedTable = null;
		}
		
		btn_confirm.setBackground(SystemColor.scrollbar);
		btn_confirm.setEnabled(false);
		btn_cancel.setBackground(SystemColor.scrollbar);
		btn_cancel.setVisible(true);
	}
	public void openToSelect(Order order) {
		open();
		
		this.order = order;
		
		btn_cancel.setVisible(false);
	}
	public void openToChange(Order order) {
		open();
		
		this.order = order;
		
		selectedTable = (TablePanel)pnl_tables.getComponent(order.getTableNo()-1);
		selectedTable.setBackground(SystemColor.DARK_GRAY);
		btn_confirm.setEnabled(true);
	}
	private void cancel() {
		triggerCancelListeners();
		close();
	}
	//Functionalities
	private boolean isFilled() {
		if (selectedTable.getTableNo() < 1)
			return false;
		
		return true;
	}
	//Classes
	private class TablePanel extends JPanel {
		
		private int tableNo;
		
		public TablePanel() {
			super();
			tableNo = -1;
		}
		public TablePanel(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
			tableNo = -1;
		}
		public TablePanel(LayoutManager lm) {
			super(lm);
			tableNo = -1;
		}
		public TablePanel(LayoutManager lm, boolean isDoubleBuffered) {
			super(lm, isDoubleBuffered);
			tableNo = -1;
		}
		
		public int getTableNo() {
			return tableNo;
		}
		public void setTableNo(int tableNo) {
			this.tableNo = tableNo;
		}
	}
}
