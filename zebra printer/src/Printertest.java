import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.Sides;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.MessageFormat;

public class Printertest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Printertest frame = new Printertest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	JTextPane textPane = new JTextPane();

	public static final String NEW_LINE = "\r\n";
	public static final String TAB = "\t";

	public Printertest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JButton btnPrintSpasser = new JButton("PRINT SPASSER");
		btnPrintSpasser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				textPane.setText(
				NEW_LINE + TAB 
				+ NEW_LINE + "Jernbanegade 2, 9000 Aalborg"
				+ NEW_LINE + "+45 25 75 03 11"
				+ NEW_LINE + "www.nobustreetfood.dk"
				+ NEW_LINE + "kundenservice@nobustreetfood.dk"
				+ NEW_LINE + "CVR-nr.: 454323452"
				+ NEW_LINE
				+ NEW_LINE + "Saturday 16th June 2018, 20.26"
				+ NEW_LINE
				+ NEW_LINE + TAB +"Invoice nr. #314"
				+ NEW_LINE + "-----------------------------------------------------" 
				+ NEW_LINE + "Coffee"   + TAB + "2.0x" + TAB + "DKK 4,99"
				+ NEW_LINE + "Sandwich" + TAB + "1.0x" + TAB + "DKK 9,99"
			    + NEW_LINE + "Discount" + TAB + "1.0x" + TAB + "DKK 1,49"
			    + NEW_LINE + "-----------------------------------------------------" 
			    + NEW_LINE + "Total" + TAB + TAB +"DKK 13,49" 
				);
				PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

				aset.add(OrientationRequested.PORTRAIT);
				aset.add(new MediaPrintableArea((float)0.0,(float)0.0,100, 185, MediaPrintableArea.MM));


				PrintService service = PrintServiceLookup.lookupDefaultPrintService();
				
				try {
					textPane.print(new MessageFormat("NOBU STREET FOOD"), new MessageFormat("“Let’s give them all a 12” -Teachers"), false, service, aset, false);
				} catch (PrinterException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		contentPane.add(btnPrintSpasser, BorderLayout.CENTER);
	}

}
