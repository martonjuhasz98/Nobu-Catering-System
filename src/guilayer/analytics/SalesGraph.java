package guilayer.analytics;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdatepicker.JDatePicker;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;

import ctrllayer.AnalyticsController;
import ctrllayer.ItemController;
import java.awt.BorderLayout;

public class SalesGraph extends JPanel {
	private AnalyticsController ac = new AnalyticsController();
	private XChartPanel<CategoryChart> chartPanel;
	private final CategoryChart chart;
	private JDatePicker toDatePicker;
	private JDatePicker fromDatePicker;
	// private String datePattern = "yyyy-MM-dd";
	// private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	/**
	 * Create the panel.
	 */
	public SalesGraph() {

		setToolTipText("from");
		setBounds(new Rectangle(0, 0, 800, 450));
		setLayout(null);
		// UtilDateModel model = new UtilDateModel();
		fromDatePicker = new JDatePicker();
		fromDatePicker.getFormattedTextField().setText("from");
		fromDatePicker.setSize(150, 30);
		fromDatePicker.setLocation(10, 0);

		add(fromDatePicker);

		JLabel dash = new JLabel("-");
		dash.setBounds(170, 6, 61, 16);
		add(dash);

		toDatePicker = new JDatePicker();
		toDatePicker.getFormattedTextField().setText("to");
		toDatePicker.setBounds(195, 0, 150, 30);
		add(toDatePicker);

		JPanel chartLayoutPanel = new JPanel();
		chartLayoutPanel.setBounds(0, 30, 778, 425);
		add(chartLayoutPanel);
		chartLayoutPanel.setLayout(new BorderLayout(0, 0));

		chart = new CategoryChartBuilder().width(778).height(42).xAxisTitle("Date")
				.yAxisTitle("% wasted").build();
		DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
		chart.getStyler().setLegendVisible(false);
		chart.getStyler().setDatePattern("yyyy-MM-dd");
		ArrayList<java.util.Date> xData = new ArrayList<>();
		ArrayList<Double> yData = new ArrayList<>();
		xData.add(new java.util.Date());
		yData.add(0.00);
		
		chart.addSeries("a", xData, yData);
		// chart.addSeries("b", new double[] { 0, 2, 4, 6, 9 }, new double[] { -1, 6, 4,
		// 0, 4 });
		chartPanel = new XChartPanel<CategoryChart>(chart);
		chartLayoutPanel.add(chartPanel, BorderLayout.CENTER);

		// chart.addSeries("c", new double[] { 0, 1, 3, 8, 9 }, new double[] { -2, -1,
		// 1, 0, 1 });
		JButton fetch = new JButton("Fetch");
		fetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fetch();
			}
		});
		fetch.setBounds(660, 0, 117, 29);
		add(fetch);

	}
	private void fetch() {
		DateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		try {
			
			String[][] data = ac.getSales(
					new java.sql.Date(format.parse(fromDatePicker.getFormattedTextField().getText()).getTime()), 
					new java.sql.Date(format.parse(toDatePicker.getFormattedTextField().getText()).getTime()));

			chart.removeSeries("a");
			chart.getStyler().setLegendVisible(true);

			System.out.println(Arrays.deepToString(data));
			format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			ArrayList<java.util.Date> xData = new ArrayList<java.util.Date>();
			for (String[] row : data)
				xData.add(format.parse(row[0]));

			ArrayList<Double> revenue = new ArrayList<Double>();
			for (String[] row : data)
				revenue.add(Double.valueOf((row[1].equals("NULL")) ? "0" : row[1]));
			chart.addSeries("Revenue", xData, revenue);

			ArrayList<Double> costs = new ArrayList<Double>();
			for (String[] row : data)
				costs.add(Double.valueOf((row[2].equals("NULL")) ? "0" : row[2]));
			chart.addSeries("Costs", xData, costs);

			ArrayList<Double> profit = new ArrayList<Double>();
			for (String[] row : data)
				profit.add(Double.valueOf((row[3].equals("NULL")) ? "0" : row[3]));
			chart.addSeries("Profit", xData, profit);

			chartPanel.revalidate();
			chartPanel.repaint();
			// results.getString("date"),
			// results.getString("revenue"),
			// results.getString("costs"),
			// results.getString("profit")
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}