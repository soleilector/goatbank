import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Panel {
	final static Dimension typicalBTNSize = new Dimension(100,30);
	final static String[] panelNames = {"InternetConnection"};
	
	static HashMap<String, JPanel> panels = new HashMap<>() {{
		put("InternetConnection",new JPanel() {{
			setPreferredSize(new Dimension(500,500));
			setBackground(Color.gray);
		}});
	}};
	
	static HashMap<String,JButton> internetPanel_BTNs = new HashMap<>() {{
		put("Connect",Panel.createBTN("Connect",typicalBTNSize));
	}};
	
	static HashMap<String,JLabel> internetPanel_Labels = new HashMap<>() {{
		put("msgLabel",new JLabel("<html>"
				+ "<body style='word-wrap:normal;'>"
					+ "This application requires internet connectivity. Please connect to the Internet, then try again."
				+ "</body>"
				+ "</html>") {{
					setPreferredSize(new Dimension(400,50));
				}});
	}};
	
	static HashMap<String, HashMap> btns = new HashMap<>() {{
		put("InternetConnection",internetPanel_BTNs);
	}};
	
	static HashMap<String, HashMap> labels = new HashMap<>() {{
		put("InternetConnection",internetPanel_Labels);
	}};
	
	static HashMap<String,JButton> thisPanelBTNs = new HashMap<>();
	
	public static JPanel createPanel(String panelName) { // return hashmap of components
		HashMap<String, JButton> panelBTNs = btns.get(panelName);
		HashMap<String, JLabel> panelLabels = labels.get(panelName);
		
		for (String btnName: panelBTNs.keySet()) {
			JButton thisBTN = panelBTNs.get(btnName);
			panels.get(panelName).add(thisBTN);
		}
		
		for (String labelName : panelLabels.keySet()) {
			panels.get(panelName).add(panelLabels.get(labelName));
		}
		
		return panels.get(panelName);
	}
	
	public static HashMap<String, JButton> getBTNs(String panelName, String btnName) {
		HashMap<String, JButton> panelBTNs = btns.get(panelName);
		
		return panelBTNs;
	}
	
	public static HashMap<String, JButton> getLabels(String panelName, String btnName) {
		HashMap<String, JButton> panelLabels = labels.get(panelName);
		
		return panelLabels;
	}
	
	public static JButton createBTN(String text) {
		JButton newBTN = new JButton(text);
		newBTN.setForeground(Color.BLACK);
		newBTN.setBackground(Color.WHITE);
		newBTN.setFocusPainted(false);
		
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		newBTN.setBorder(margin);
		
		return newBTN;
	}
	
	public static JButton createBTN(String text,Dimension dim) {
		JButton newBTN = createBTN(text);
		newBTN.setPreferredSize(dim);
		
		return newBTN;
	}
}
