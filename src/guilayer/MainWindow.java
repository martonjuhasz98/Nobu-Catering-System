package guilayer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.EventQueue;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseEvent;

import guilayer.contentpanels.*;
import guilayer.menu.Menu;
import guilayer.menu.MenuItemListener;

public class MainWindow {
	
	private static final int totalWidth = 1000;
	private static final int totalHeight = 500;
	private static final int menuWidth = 200;
	private static final int contentWidth = 800;
	private static final int menuItemHeight = 48;
	private static final Color menuFontColour = Color.WHITE;
	private static final Color menuBackgroundColour = Color.DARK_GRAY;
	private static final Color activeMenuItemBackgroundColour = Color.GRAY;
	private static final Color activeMenuItemSignColour = Color.LIGHT_GRAY;
	private static final Color contentFontColour = Color.DARK_GRAY;
	private static final Color contentBackgroundColour = Color.WHITE;
	private static final Font menuFont = new Font("Segoe UI", Font.BOLD, 16);
	private static final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
	private JFrame frame;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainWindow() {		
		initialize();
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, totalWidth + 4, totalHeight + 28);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Western Style Ltd.");
		frame.setFont(contentFont);
		frame.setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, totalWidth, totalHeight);
		contentPane.setLayout(null);
		frame.setContentPane(contentPane);
		
		Menu menu = new Menu();
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
		menu.add("Suppliers");
		menu.add("Menu Items");
		menu.add("Employees");
		contentPane.add(menu);
		
		JPanel content = new JPanel();
		CardLayout layout = new CardLayout(0, 0);
		content.setLayout(layout);
		content.setBounds(200, 0, contentWidth, totalHeight);
		content.setBackground(contentBackgroundColour);
		content.setForeground(contentFontColour);
		content.setFont(contentFont);
		contentPane.add(content);
		
		content.add(new ManageInventory(), "1");
		
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
					case 2:
						layout.show(content, "2");
						break;
					case 3:
						layout.show(content, "3");
						break;
					case 4:
						layout.show(content, "4");
						break;
				}
			}
		});
	}
}
