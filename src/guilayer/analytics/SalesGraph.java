package guilayer.analytics;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.JDatePicker;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;

import ctrllayer.AnalyticsController;
import ctrllayer.ItemController;
import java.awt.BorderLayout;

public class SalesGraph extends JPanel {
	private ItemController ic = new ItemController(); // TODO: replace with MenuItemController
	private AnalyticsController ac = new AnalyticsController();
	// private String datePattern = "yyyy-MM-dd";
//	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	/**
	 * Create the panel.
	 */
	public SalesGraph() {

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

		JPanel chartLayoutPanel = new JPanel();
		chartLayoutPanel.setBounds(0, 30, 778, 425);
		add(chartLayoutPanel);
		chartLayoutPanel.setLayout(new BorderLayout(0, 0));

		final CategoryChart chart = new CategoryChartBuilder().width(778).height(42).xAxisTitle("Date")
				.yAxisTitle("% wasted").build();
		chart.addSeries("a", new double[] { 0, 0, 0, 0, 0 }, new double[] { 0, 0, 0, 0, 0 });
		chart.getStyler().setLegendVisible(false);
		// chart.addSeries("b", new double[] { 0, 2, 4, 6, 9 }, new double[] { -1, 6, 4,
		// 0, 4 });
		JPanel chartPanel = new XChartPanel<CategoryChart>(chart);
		chartLayoutPanel.add(chartPanel, BorderLayout.CENTER);

		// chart.addSeries("c", new double[] { 0, 1, 3, 8, 9 }, new double[] { -2, -1,
		// 1, 0, 1 });
		JButton fetch = new JButton("Fetch");
		fetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
				try {
					String[][] data = ac.getSales(
							new java.sql.Date(format.parse(fromDatePicker.getFormattedTextField().getText()).getTime()),
							new java.sql.Date(format.parse(toDatePicker.getFormattedTextField().getText()).getTime()));
					
					ArrayList<java.util.Date> xData = new ArrayList();
					for (String s : data[0])
						xData.add(format.parse(s));
					
					ArrayList<Double> revenue = new ArrayList();
					for (String s : data[1])
						revenue.add(Double.valueOf((s.equals("NULL"))?"0":s));
					chart.addSeries("Revenue", xData,revenue );
					
					ArrayList<Double> costs = new ArrayList();
					for (String s : data[1])
						costs.add(Double.valueOf((s.equals("NULL"))?"0":s));
					chart.addSeries("Costs", xData,costs );
					
					ArrayList<Double> profit = new ArrayList();
					for (String s : data[1])
						profit.add(Double.valueOf((s.equals("NULL"))?"0":s));
					chart.addSeries("Profit", xData,profit );
					

					 chartPanel.revalidate();
//					results.getString("date"),
//					results.getString("revenue"),
//					results.getString("costs"),
//					results.getString("profit")
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Please pick dates");
				}

			}
		});
		fetch.setBounds(660, 0, 117, 29);
		add(fetch);

	}

}