package guilayer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import guilayer.contentpanels.*;
import guilayer.menu.*;
import modlayer.Employee;

public class WaiterWindow extends JFrame {
	
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
	private Employee user;
	
	public WaiterWindow(Employee user) {
		super();
		this.user = user;
		
		initialize();
	}

	private void initialize() {
		
		setBounds(100, 100, totalWidth + 4, totalHeight + 28);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Nobu order system");
		setFont(contentFont);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, totalWidth, totalHeight);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		Menu menu = new Menu();
		menu.setLayout(null);
		menu.setBounds(0, 0, menuWidth, totalHeight);
		menu.setItemHeight(menuItemHeight);
		menu.setTextIndent(24);
		menu.setBackground(menuBackgroundColour);
		menu.setForeground(menuFontColour);
		menu.setFont(menuFont);
		menu.setCurrentColour(activeMenuItemSignColour);
		menu.add("Create");
		menu.add("Pay");
		contentPane.add(menu);
		
		JPanel content = new JPanel();
		CardLayout layout = new CardLayout(0, 0);
		content.setLayout(layout);
		content.setBounds(200, 0, contentWidth, totalHeight);
		content.setBackground(contentBackgroundColour);
		content.setForeground(contentFontColour);
		content.setFont(contentFont);
		contentPane.add(content);
		
		content.add(new CreateOrders(), "0");
		content.add(new ManageOrders(), "1");
		
		menu.addMenuItemListener(new MenuItemListener() {
			@Override
			public void menuItemClicked(int clickedIndex) {
				switch(clickedIndex) {
					case 0:
						layout.show(content, "0");
						break;
					case 1:
						layout.show(content, "1");
						break;
				}
			}
		});
		
		setVisible(true);
	}
}
