package guilayer.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Menu extends JPanel {
	
	private int itemHeight;
	private int textIndent;
	private Font font;
	private Color fontColour;
	private Color currentBackgroundColour;
	private ArrayList<MenuItemListener> listeners;
	private ArrayList<MenuItem> items;
	private int currentIndex;
	
	public Menu() {
		super();
		
		itemHeight = 32;
		textIndent = 24;
		font = super.getFont();
		fontColour = Color.WHITE;
		currentBackgroundColour = Color.BLUE;
		listeners = new ArrayList<>();
		items = new ArrayList<>();
		currentIndex = -1;
		
		initialize();
	}

	private void initialize() {
		this.setLayout(null);
	}
	public Color getCurrentColour() {
		return currentBackgroundColour;
	}
	@Override
	public Color getForeground() {
		return fontColour;
	}
	public int getTextIndent() {
		return textIndent;
	}
	public int getItemHeight() {
		return itemHeight;
	}
	@Override
	public Font getFont() {
		return font;
	}
	public void setCurrentColour(Color colour) {
		this.currentBackgroundColour = colour;
		for (MenuItem item : items) {
			item.setCurrentColour(colour);
		}
	}
	@Override
	public void setBackground(Color colour) {
		super.setBackground(colour);
		if (items == null) {
			super.setBackground(colour);
			return;
		}
		
		for (MenuItem item : items) {
			item.setBackground(colour);
		}
	}
	@Override
	public void setForeground(Color colour) {
		this.fontColour = colour;
		if (items == null) {
			super.setForeground(colour);
			return;
		}
		
		for (MenuItem item : items) {
			item.setForeground(colour);
		}
	}
	public void setTextIndent(int textIndent) {
		this.textIndent = textIndent;
		for (MenuItem item : items) {
			item.setTextIndent(textIndent);
		}
	}
	public void setItemHeight(int height) {
		this.itemHeight = height;
		for (MenuItem item : items) {
			item.setSize(item.getWidth(), height);
		}
	}
	@Override
	public void setFont(Font font) {
		this.font = font;
		if (items == null) {
			super.setFont(font);
			return;
		}
		
		for (MenuItem item : items) {
			item.setFont(font);
		}
	}
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		calculateLayout();
	}

	public void add(String name) {
		MenuItem item = new MenuItem(name);
		item.setTextIndent(textIndent);
		item.setFont(font);
		item.setForeground(fontColour);
		item.setBackground(this.getBackground());
		item.setCurrentColour(currentBackgroundColour);
		this.add(item);
		
		int currentIndex = items.size();
		if (items.isEmpty()) {
			this.currentIndex = currentIndex;
			item.setActive(true);
		}
		items.add(item);
		
		calculateLayout();
		
		item.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Menu.this.currentIndex > -1) {
					items.get(Menu.this.currentIndex).setActive(false);
				}
				
				Menu.this.currentIndex = currentIndex;
				item.setActive(true);
				
				calculateLayout();
				
				fireMenuItemListeners(currentIndex);
			}
		});			
	}

	public boolean addMenuItemListener(MenuItemListener l) {
		return listeners.add(l);
	}
	public boolean removeMenuItemListener(MenuItemListener l) {
		return listeners.remove(l);
	}
	private void fireMenuItemListeners(int index) {
		for (MenuItemListener l : listeners) {
			l.menuItemClicked(index);
		}
	}

	private void calculateLayout() {
		if (items == null || items.isEmpty())
			return;
		
		int menuWidth = this.getWidth();
		int offset = (this.getHeight() - items.size() * itemHeight) / 2;
		for (MenuItem item : items) {
			item.setBounds(0, offset, menuWidth, itemHeight);
			
			offset += itemHeight;
		}
	}
}
