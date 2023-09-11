import javax.swing.JLabel;
import java.awt.Color;
import java.util.HashMap;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class GUIHelper extends UI_Base {
	private static final HashMap<String, Color> colors = new HashMap<>() {{
		put("White",Color.white);
		put("Black",Color.black);
		put("Cyan",Color.cyan);
		put("Blue",Color.blue);
		put("Dark Gray",Color.darkGray);
		put("Green",Color.green);
		put("Light Gray",Color.lightGray);
		put("Magenta",Color.magenta);
		put("Orange",Color.orange);
		put("Yellow",Color.yellow);
		put("Pink",Color.pink);
		put("Red",Color.red);
		put("White",Color.white);
		
		put("Footer",Color.black);
		put("Header",Color.black);
		put("Content",new Color(0x9FFFEF));
		put("BTN_BG",Color.black);
		put("BTN_FG",Color.white);
		put("BTN_Select",Color.darkGray);
		put("Field",Color.white);
		put("Main",Color.yellow);
		put("FieldPanel",Color.white);
		put("UserMenuField",new Color(0xF2F2F2));
		put("FieldMain",new Color(0x9FFFEF));
		put("Nav",Color.cyan);
		put("UserContentContainer",Color.magenta);
		put("UserContent",Color.white);
		put("UserMenu",Color.yellow);
		put("Splash",Color.white);
	}};
	
	private static final HashMap<String,Integer> fieldSizes = new HashMap<String,Integer>(){{
		put("Def_X",25);
	}};
	
	public static int getDefaultFieldHeight() {
		return fieldSizes.get("Def_X");
	}
	
	public static void printMsg(String msgName) {
		print(getMsg(msgName));
	}
	
	public static void setMsg(JLabel thisLabel, String msgName) {
		thisLabel.setText(getMsg(msgName));
	}
	
	public static Color getColor(String colorName) {
		return colors.get(colorName);
	}
	
	public static void skin(JButton thisBTN, String bgColorName, String fgColorName) {
		Color thisColorBG = getColor(bgColorName);
		if (thisColorBG!=null) {
			thisBTN.setBackground(thisColorBG);
		}
		
		Color thisColorFG = getColor(fgColorName);
		if (thisColorFG!=null) {
			thisBTN.setForeground(thisColorFG);
		}
		
		thisBTN.setFocusPainted(false);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		thisBTN.setBorder(margin);
	}
	
	public static void skin(JTextField thisField, String bgColorName, String fgColorName) {
		Color thisColorBG = getColor(bgColorName);
		if (thisColorBG!=null) {
			thisField.setBackground(thisColorBG);
		}
		
		Color thisColorFG = getColor(fgColorName);
		if (thisColorFG!=null) {
			thisField.setForeground(thisColorFG);
		}
		
		//thisField.setFocusPainted(false);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		thisField.setBorder(margin);
	}
}
