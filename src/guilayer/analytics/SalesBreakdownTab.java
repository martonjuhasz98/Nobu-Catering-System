package guilayer.analytics;

import javax.swing.JPanel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.JDatePicker;

import ctrllayer.AnalyticsController;
import ctrllayer.ItemController;

import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SalesBreakdownTab extends JPanel {
	private JTable table;
	private AnalyticsController ac = new AnalyticsController();
	private SalesTableModel model = new SalesTableModel();

	/**
	 * Create the panel.
	 */
	public SalesBreakdownTab() {

		setToolTipText("from");
		setBounds(new Rectangle(0, 0, 800, 450));
		setLayout(null);
		// UtilDateModel model = new UtilDateModel();
		JDatePicker fromDatePicker = new JDatePicker();
		fromDatePicker.getFormattedTextField().setText("from");
		fromDatePicker.setSize(150, 30);
		fromDatePicker.setLocation(10, 0);

		add(fromDatePicker);

		JLabel dash = new JLabel("-");
		dash.setBounds(170, 6, 61, 16);
		add(dash);

		JDatePicker toDatePicker = new JDatePicker();
		toDatePicker.getFormattedTextField().setText("to");
		toDatePicker.setBounds(195, 0, 150, 30);
		add(toDatePicker);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 30, 778, 425);
		add(scrollPane);

		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);

		JButton fetch = new JButton("Fetch");
		fetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// java.util.Date dd = new
				// java.util.Date(fromDatePicker.getFormattedTextField().getText());
				DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
				try {
					model.setData(ac.getSalesBbreakdown(
							new java.sql.Date(format.parse(fromDatePicker.getFormattedTextField().getText()).getTime()),
							new java.sql.Date(format.parse(toDatePicker.getFormattedTextField().getText()).getTime())));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block

					JOptionPane.showMessageDialog(null, "Please pick dates");
				}

			}
		});
		fetch.setBounds(660, 0, 117, 29);
		add(fetch);

	}

	private class SalesTableModel extends AbstractTableModel {

		private String[] columns = new String[] { "id", "name", "category", "price","sold" };
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
			this.data = data;
			update();
		}
	}
}
