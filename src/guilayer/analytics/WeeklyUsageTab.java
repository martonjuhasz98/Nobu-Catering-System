package guilayer.analytics;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import ctrllayer.AnalyticsController;
import ctrllayer.ItemController;

public class WeeklyUsageTab extends JPanel {
	private JTable table;
	private ItemController ic = new ItemController();
	private AnalyticsController ac = new AnalyticsController();
	private SalesTableModel model = new SalesTableModel(); 
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	/**
	 * Create the panel.
	 */
	public WeeklyUsageTab() {
		
		setToolTipText("from");
		setBounds(new Rectangle(0, 0, 800, 450));
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 30, 778, 425);
		add(scrollPane);
		
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		
		JButton fetch = new JButton("Fetch");
		fetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//java.util.Date dd = new java.util.Date(fromDatePicker.getFormattedTextField().getText());
				DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
				try {
					model.setData(ac.getWeeklyAverage());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"Please pick dates");
					}
			}
		});
		fetch.setBounds(660, 0, 117, 29);
		add(fetch);
	}

	private class SalesTableModel extends AbstractTableModel {

		private String[] columns = new String[] { "Barcode", "Name", "unit", "Average","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday" };
		private String[][] data = new String[0][0];

		
		public SalesTableModel() {
		}

		public SalesTableModel(String[][] data) {
			this.data = data;
			update();
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columns[columnIndex];
		}

		@Override
		public Class getColumnClass(int columnIndex) {
			try {
			return getValueAt(0, columnIndex).getClass();
			} catch (Exception ignored) {
				return new Object().getClass();
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void update() {
			fireTableDataChanged();
		}
		public void setData(String[][] data) {
			this.data =data;
			update();
		}
	}

}
