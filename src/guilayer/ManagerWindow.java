package guilayer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import guilayer.contentpanels.*;
import guilayer.essentials.Navigatable;
import guilayer.menu.Menu;
import guilayer.menu.MenuItemListener;
import java.awt.Component;

public class ManagerWindow extends JFrame implements MenuItemListener {
	
	public static final int totalWidth = 1000;
	public static final int totalHeight = 500;
	public static final int menuWidth = 200;
	public static final int contentWidth = 800;
	public static final int menuItemHeight = 48;
	public static final Color menuFontColour = Color.WHITE;
	public static final Color menuBackgroundColour = Color.DARK_GRAY;
	public static final Color activeMenuItemBackgroundColour = Color.GRAY;
	public static final Color activeMenuItemSignColour = Color.LIGHT_GRAY;
	public static final Color contentFontColour = Color.DARK_GRAY;
	public static final Color contentBackgroundColour = Color.WHITE;
	public static final Font menuFont = new Font("Segoe UI", Font.BOLD, 16);
	public static final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
	private JPanel contentPane;
	private CardLayout layout;
	private JPanel content;
	private Menu menu;
	private JComponent[] contentPanels;
	private boolean loading;
	private int lastIndex;
	private int prevIndex;
	
	public ManagerWindow() {
		super();
		
		lastIndex = 0;
		prevIndex = 0;
		loading = false;
		
		initialize();
	}

	private void initialize() {
		
		setBounds(100, 100, totalWidth + 4, totalHeight + 28);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Nobu inventory system");
		setFont(contentFont);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, totalWidth, totalHeight);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		menu = new Menu();
		menu.setLayout(null);
		menu.setBounds(0, 0, menuWidth, totalHeight);
		menu.setItemHeight(menuItemHeight);
		menu.setTextIndent(24);
		menu.setBackground(menuBackgroundColour);
		menu.setForeground(menuFontColour);
		menu.setFont(menuFont);
		menu.setCurrentColour(activeMenuItemSignColour);
		menu.add("Analytics");
		menu.add("Inventory");
		menu.add("Stocktaking");
		menu.add("Invoices");
		menu.add("Suppliers");
		menu.add("Menu Items");
		menu.add("Employees");
		contentPane.add(menu);
		
		content = new JPanel();
		layout = new CardLayout(0, 0);
		content.setLayout(layout);
		content.setBounds(200, 0, contentWidth, totalHeight);
		content.setBackground(contentBackgroundColour);
		content.setForeground(contentFontColour);
		content.setFont(contentFont);
		contentPane.add(content);
		
		contentPanels = new JComponent[7];
		contentPanels[0] = new Analytics();
		contentPanels[1] = new ManageInventory();
		contentPanels[2] = new Stocktaking();
		contentPanels[3] = new ManageInvoices();
		contentPanels[4] = new ManageSuppliers();
		contentPanels[5] = new ManageMenuItems();
		contentPanels[6] = new ManageEmployees();
		for (int i = 0; i < contentPanels.length; i++) {
			content.add(contentPanels[i], Integer.toString(i));
		}
		
		menu.addMenuItemListener(this);
	}
	@Override
	public void menuItemClicked(int clickedIndex) {
		lastIndex = clickedIndex;
		if (loading || clickedIndex < 0 || clickedIndex >= contentPanels.length || clickedIndex == prevIndex) return;
		loading = true;

		layout.show(content, Integer.toString(clickedIndex));

		new Thread() {
			public void run() {
				//Load stuff in background
				((Navigatable)contentPanels[clickedIndex]).prepare();
				((Navigatable)contentPanels[prevIndex]).reset();
				//Release lock
				prevIndex = clickedIndex;
				loading = false;
				//If this is not the last one clicked
				if (lastIndex != prevIndex) {
					menuItemClicked(lastIndex);
				}
			}
		}.start();
	}
}
