package guilayer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import guilayer.contentpanels.*;
import guilayer.essentials.Navigatable;
import guilayer.menu.*;

public class WaiterWindow extends JFrame implements MenuItemListener {
	
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
	private Menu menu;
	private JPanel content;
	private CardLayout layout;
	private JComponent[] contentPanels;
	private boolean loading;
	private int lastIndex;
	private int prevIndex;
	
	public WaiterWindow() {
		super();
		
		lastIndex = 0;
		prevIndex = 0;
		loading = false;
		
		initialize();
	}

	private void initialize() {
		
		setVisible(false);
		setBounds(100, 100, totalWidth + 4, totalHeight + 28);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Nobu order system");
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
		menu.add("Create Order");
		menu.add("List Orders");
		contentPane.add(menu);
		
		content = new JPanel();
		layout = new CardLayout(0, 0);
		content.setLayout(layout);
		content.setBounds(200, 0, contentWidth, totalHeight);
		content.setBackground(contentBackgroundColour);
		content.setForeground(contentFontColour);
		content.setFont(contentFont);
		contentPane.add(content);
		
		contentPanels = new JComponent[2];
		contentPanels[0] = new CreateOrders();
		contentPanels[1] = new ManageOrders();
		for (int i = 0; i < contentPanels.length; i++) {
			content.add(contentPanels[i], Integer.toString(i));
		}
		
		menu.addMenuItemListener(this);
	}
	@Override
	public void menuItemClicked(int clickedIndex) {
		if (clickedIndex < 0 || clickedIndex >= contentPanels.length || clickedIndex == prevIndex) return;
		layout.show(content, Integer.toString(clickedIndex));
		lastIndex = clickedIndex;
		
		if (loading == true) return;
		loading = true;

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
