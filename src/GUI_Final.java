import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.GradientPaint;
import java.util.Scanner;
import java.awt.event.ActionEvent;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.HashMap;

import java.io.File;
import java.io.FileNotFoundException;

public class GUI_Final{
	final private static String uri = "mongodb+srv://pompompies:QAIrhJsAt7tFRz6I@cis207final.hmn0gcf.mongodb.net/?retryWrites=true&w=majority";
	private static MongoClient mongoClient;
	private static MongoDB thisdbase;  
	private static BankUser thisUser;
	private static boolean canPressBTN = true;
	
	
	//========== PANEL COMPONENTS ==========//
	//========== BASIC SETTINGS
	private final static Color HEADER_COLOR = GUIHelper.getColor("Header");
	private final static Color CONTENT_COLOR = GUIHelper.getColor("Content");
	private final static Color FOOTER_COLOR = GUIHelper.getColor("Footer");
	private final static int FIELD_HEIGHT = GUIHelper.getDefaultFieldHeight();
	
	private final static Dimension fullScreenSize = new Dimension(600,500);
	private final static Dimension fullPanelSize = new Dimension(500,500);
	private final static Dimension contentPanelSize = new Dimension(500,300);
	private final static Dimension footerPanelSize = new Dimension(500,100);
	private final static Dimension typicalBTNSize = new Dimension(100,30);
	private final static Dimension headerPanelSize = new Dimension(500,300);
	
	//========= CONTENT PANELS :: MAIN MENU	
	private static JFrame mainMenuFrame = new JFrame();
	
	//------------ Main Menu Panel
	private static JButton main_loginBTN = new JButton("Login");
	private static JButton main_registerBTN = new JButton("Register");
	private static JButton main_forgotPassBTN = new JButton("Forgot Password?");
	
	private static JPanel main_BTNPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,50));
		setBackground(GUIHelper.getColor("Content"));
		add(main_loginBTN);
		add(main_registerBTN);
		add(main_forgotPassBTN);
	}};
	
	//private static JLabel main_userIdLabel = new JLabel("UserId:");
	//private static JLabel main_userPasswordLabel = new JLabel("Password:");
	
	private static JTextField main_userIDField = new JTextField("username",JTextField.CENTER) {{
		setPreferredSize(new Dimension(300,FIELD_HEIGHT));
		setHorizontalAlignment(JTextField.CENTER);
	}};
	
	private static JPanel main_userNamePanel = new JPanel() {{
		setPreferredSize(new Dimension(500,50));
		setBackground(GUIHelper.getColor("Content"));
		//DON'T DELETE; RESTORES ORIGINAL LAYOUT // add(main_userIdLabel,BorderLayout.LINE_START);
		add(main_userIDField,BorderLayout.LINE_END);
	}};
	
	private static JLabel main_msgLabel = new JLabel(GUIHelper.getMsg("Welcome"),JLabel.CENTER) {{
		setPreferredSize(new Dimension(500,50));
		setBackground(GUIHelper.getColor("Content"));
	}};
	private static JPasswordField main_userPasswordField = new JPasswordField("password") {{
		setPreferredSize(new Dimension(300,FIELD_HEIGHT));
		setHorizontalAlignment(JPasswordField.CENTER);
	}};
	
	private static JPanel passPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,50));
		setBackground(GUIHelper.getColor("Content"));
		//DON'T DELETE; RESTORES ORIGINAL LAYOUT //add(main_userPasswordLabel,BorderLayout.LINE_START);
		add(main_userPasswordField,BorderLayout.LINE_END);
	}};
	
	private static JPanel mainMenuPanel = new JPanel() {{
		setPreferredSize(contentPanelSize);
		setBackground(GUIHelper.getColor("Content"));
		add(main_userNamePanel);
		add(passPanel);
		add(main_msgLabel);
		add(main_BTNPanel);
		setVisible(true);
	}};
	
	//------------ Register Panel
		private static JLabel register_userNameLabel = new JLabel("Account Holder's Full Name: (*):");
		private static JLabel register_userEmailLabel = new JLabel("Email:");
		private static JLabel register_userIntialDepositLabel = new JLabel("Intial Deposit:");
		
		private static JTextField register_userNameField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		private static JTextField register_userEmailField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		private static JTextField register_userIntialDepositField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
			setText("$0");
		}};
		
		private static JLabel register_msgLabel = new JLabel("We just need a few more details!",JLabel.CENTER) {{
			setPreferredSize(new Dimension(500,50));
			setBackground(GUIHelper.getColor("Content"));
		}};
		
		private static JPanel register_userNamePanel = new JPanel() {{
			setPreferredSize(new Dimension(500,50));
			setBackground(GUIHelper.getColor("FieldMain"));
			add(register_userNameLabel,BorderLayout.LINE_START);
			add(register_userNameField,BorderLayout.LINE_END);
		}};
		
		private static JPanel register_userEmailPanel = new JPanel() {{
			setPreferredSize(new Dimension(500,50));
			setBackground(GUIHelper.getColor("FieldMain"));
			add(register_userEmailLabel,BorderLayout.LINE_START);
			add(register_userEmailField,BorderLayout.LINE_END);
		}};
		
		private static JPanel register_userInitialDepositPanel = new JPanel() {{
			setPreferredSize(new Dimension(500,50));
			setBackground(GUIHelper.getColor("FieldMain"));
			add(register_userIntialDepositLabel,BorderLayout.LINE_START);
			add(register_userIntialDepositField,BorderLayout.LINE_END);
		}};
		
		private static JButton register_finishBTN = new JButton("Finish Registration");
		private static JButton register_backToMainBTN = new JButton("Back to Main Menu");
		
		private static JPanel register_BTNPanel = new JPanel() {{
			setPreferredSize(new Dimension(500,50));
			setBackground(GUIHelper.getColor("Content"));
			add(register_finishBTN);
			add(register_backToMainBTN);
		}};
		
		private static JPanel registerPanel = new JPanel() {{
			setPreferredSize(contentPanelSize);
			setBackground(GUIHelper.getColor("Content"));
			add(register_userNamePanel);
			add(register_userEmailPanel);
			add(register_userInitialDepositPanel);
			add(register_msgLabel);
			add(register_BTNPanel);
			setVisible(false);
		}};
	
	//----------- Forgot Pass Panel
	private static JLabel forgotPassPanel_msgLabel = new JLabel("Message for the user!",JLabel.CENTER) {{
		setPreferredSize(new Dimension (500,50));
		setBackground(GUIHelper.getColor("Content"));
	}};
	
	private static JLabel forgotPassPanel_userIdLabel = new JLabel("UserId:");
	private static JTextField forgotPassPanel_userIDField = new JTextField() {{
		setPreferredSize(new Dimension(300,FIELD_HEIGHT));
		setBackground(GUIHelper.getColor("Field"));
	}};
	
	private static JButton forgotPassPanel_sendPassReqBTN = new JButton("Get New Password");
	private static JButton forgotPassPanel_backToMainBTN = new JButton("Back To Main Menu");
	
	private static JPanel forgotPassPanel_BTNPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,50));
		add(forgotPassPanel_sendPassReqBTN);
		add(forgotPassPanel_backToMainBTN);
		setBackground(GUIHelper.getColor("Content"));
	}};
	
	private static JPanel forgotPassPanel_userNamePanel = new JPanel() {{
		setPreferredSize(new Dimension(500,50));
		setBackground(GUIHelper.getColor("FieldMain"));
		add(forgotPassPanel_userIdLabel,BorderLayout.LINE_START);
		add(forgotPassPanel_userIDField,BorderLayout.LINE_END);
		add(forgotPassPanel_userIDField,BorderLayout.LINE_END);
	}};
	
	private static JPanel forgotPassPanel = new JPanel() {{
		setPreferredSize(contentPanelSize);
		setBackground(GUIHelper.getColor("Content"));
		add(forgotPassPanel_userNamePanel);
		add(forgotPassPanel_msgLabel);
		add(forgotPassPanel_BTNPanel);
		setVisible(false);
	}};
	
	//---------- User Menu Panel
	
	private static JPanel userMenuNavPanel = new JPanel() {{
		setPreferredSize(new Dimension(180,contentPanelSize.height));
		setBackground(GUIHelper.getColor("Nav"));
	}};
	
	private static ImageIcon userMenuWelcomeIcon = new ImageIcon("src/icon_userwelcome.png");
	private static JLabel  userMenuIconLabel = new JLabel("",userMenuWelcomeIcon,JLabel.CENTER) {{
		setOpaque(false);
	}};
	private static JLabel userMenuWelcomeLabel = new JLabel() {{
		setOpaque(false);
	}}; 
	
	private static JPanel userMenuPanel_MainPanel = new JPanel(){{
		setPreferredSize(new Dimension(300,contentPanelSize.height));
		add(userMenuIconLabel);
		add(userMenuWelcomeLabel);
		setBackground(GUIHelper.getColor("UserContent"));
	}};
	
	private static JPanel userMenuContentPanel = new JPanel(){{
		setPreferredSize(new Dimension(300, contentPanelSize.height));
		setBackground(GUIHelper.getColor("UserContentContainer"));
		add(userMenuPanel_MainPanel);
	}};
	
	private static JLabel userMenuPanel_msgLabel = new JLabel("Message for the user!", JLabel.CENTER);
	
	private static JPanel userMenuPanel_OptionsPanel = new JPanel(); // options panel
	
	private static JPanel userMenuPanel = new JPanel() {{
		setPreferredSize(contentPanelSize);
		setBackground(GUIHelper.getColor("UserMenu"));
		add(userMenuNavPanel);
		add(userMenuContentPanel);
		setVisible(false);
	}};
	
	//====== FATAL ERROR PANELS
	
	//------internet conenction
	static JLabel internetConnectionMsg = new JLabel("<html>"
			+ "<body style='word-wrap:normal;'>"
				+ "This application requires internet connectivity. Please connect to the Internet, then try again."
			+ "</body>"
			+ "</html>") {{
				setPreferredSize(new Dimension(400,50));
			}};
	
	static JButton internetConnectBTN = createBTN("Connect");
	
	static JPanel internetConnectionPanel = new JPanel() {{
		setPreferredSize(fullPanelSize);
		setBackground(Color.gray);
		add(internetConnectionMsg);
		add(internetConnectBTN);
	}};
	
	static JPanel fatalMsgPanel = new JPanel() {{
		setPreferredSize(fullPanelSize);
		setBackground(Color.cyan);
		add(internetConnectionPanel);
		setVisible(false);
	}};
	
	//-- splash panel
	static JLabel splashMsgLabel = new JLabel("",JLabel.CENTER) {{
				setPreferredSize(new Dimension(450,450));
				setBackground(Color.white);
			}};
			 
	private static ImageIcon loading = new ImageIcon("src/loader2_res.gif");
	private static ImageIcon icon = new ImageIcon("src/icon.png");
	private static ImageIcon progImg = new ImageIcon("src/progIcon.png");
	
	private static JLabel splashIconLabel = new JLabel("",icon,JLabel.CENTER);
	private static JLabel splashLoadingLabel = new JLabel("", loading, JLabel.CENTER);
	
	private static JPanel splashLoaderPanel = new JPanel() {{
		setPreferredSize(new Dimension(490,250));
		//add(splashLoadingLabel);
		setBackground(GUIHelper.getColor("Splash"));
	}};
	
	private static JPanel splashIconPanel = new JPanel() {{
		setPreferredSize(new Dimension(490,200));
		//add(splashIconLabel);
		setBackground(GUIHelper.getColor("Splash"));
	}};
	
	static JPanel splashPanel = new JPanel() {{
		setPreferredSize(new Dimension(490,490));
		setBackground(Color.white);
		//add(splashIconPanel);
		//add(splashLoaderPanel);
		add(splashIconLabel);
		add(splashLoadingLabel);
		setVisible(false);
	}};
	
	
	//======== CONTENT PANELS :: LOGGED IN
	//-- PANELS
	//<!> sets up the panel's settings & ONLY the panel's settings 
	private static HashMap<String, JPanel> userMenuPanels = new HashMap<String, JPanel>(){{
		put("c",new JPanel() {{ // account information
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
		put("f",new JPanel() {{ // deposit
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
		put("w",new JPanel() {{ // withdraw
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
		put("t",new JPanel() {{ // transfer
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
		put(GUIHelper.getMenuActionKey("account settings"),new JPanel() {{ // transfer
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
		put("x",new JPanel() {{ // transfer
			setPreferredSize(new Dimension(300, contentPanelSize.height));
			setBackground(Color.white);
			setVisible(false);
		}});
	}};
	
	//-- PANEL COMPONENTS
		//-- account info :: c
		private static JLabel accInfoMsg = new JLabel("==== Account information ===") {{
			setPreferredSize(new Dimension(200,30));
		}};
		
		private static JLabel accInfo_NameLabel = new JLabel("Account Holder: ") {{
			setPreferredSize(new Dimension(250,15));
		}};
		private static JLabel accInfo_BalanceLabel = new JLabel("Balance: ") {{
			setPreferredSize(new Dimension(250,15));
		}};
		
		//-- withdraw money :: w
		private static JLabel withdrawLabel = new JLabel("Withdraw Amount:") {{
			setPreferredSize(new Dimension(125,FIELD_HEIGHT));
		}};
		
		private static JTextField withdrawAmountField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JPanel withdrawInputPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,50));
			add(withdrawLabel,BorderLayout.LINE_START);
			add(withdrawAmountField,BorderLayout.LINE_END);
		}};
		
		private static JLabel withdrawMsg = new JLabel("This is a message for the user.",JLabel.CENTER) {{
			setPreferredSize(new Dimension(300,FIELD_HEIGHT));
			setBackground(Color.gray);
		}};
		
		private static JButton withdrawBTN = new JButton("Withdraw") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		//-- transfer money :: t
		private static JLabel transferAmountLabel = new JLabel("Transfer Amount:") {{
			setPreferredSize(new Dimension(125,FIELD_HEIGHT));
		}};
		
		private static JTextField transferAmountField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JPanel transferAmountInputPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,50));
			add(transferAmountLabel,BorderLayout.LINE_START);
			add(transferAmountField,BorderLayout.LINE_END);
		}};
		
		private static JLabel transfereeNameLabel = new JLabel("Transfer to (username):") {{
			setPreferredSize(new Dimension(150,FIELD_HEIGHT));
		}};
		
		private static JTextField transfereeField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JPanel transfereePanel = new JPanel() {{
			setPreferredSize(new Dimension(300,50));
			add(transfereeNameLabel,BorderLayout.LINE_START);
			add(transfereeField,BorderLayout.LINE_END);
		}};
		
		private static JLabel transferMsg = new JLabel("This is a message for the user.",JLabel.CENTER) {{
			setPreferredSize(new Dimension(300,FIELD_HEIGHT));
			setBackground(Color.gray);
		}};
		
		private static JButton transferBTN = new JButton("Transfer") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		//-- deposit
		private static JLabel depositLabel = new JLabel("Deposit Amount:") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JTextField depositAmountField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JPanel depositInputPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,50));
			add(depositLabel,BorderLayout.LINE_START);
			add(depositAmountField,BorderLayout.LINE_END);
			//setBackground(GUIHelper.getColor("FieldPanel"));
		}};
		
		private static JLabel depositMsg = new JLabel("This is a message for the user.",JLabel.CENTER) {{
			setPreferredSize(new Dimension(300,FIELD_HEIGHT));
			setBackground(Color.gray);
		}};
		
		private static JButton depositBTN = new JButton("Deposit") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		//-- acc settings :: v
		private static JLabel accSettingsNewNameLabel = new JLabel("Account Holder:") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JTextField accSettingsNewNameField = new JTextField() {{
			setPreferredSize(new Dimension(175,FIELD_HEIGHT));
		}};
		
		private static JPanel accSettingsNewNamePanel = new JPanel() {{
			setPreferredSize(new Dimension(300,40));
			add(accSettingsNewNameLabel,BorderLayout.LINE_START);
			add(accSettingsNewNameField,BorderLayout.LINE_END);
		}};
		
		private static JLabel accSettingsNewPassLabel = new JLabel("New Password:") {{
			setPreferredSize(new Dimension(110,FIELD_HEIGHT));
		}};
		
		private static JTextField accSettingsNewPassField = new JTextField() {{
			setPreferredSize(new Dimension(150,FIELD_HEIGHT));
		}};
		
		private static JPanel accSettingsNewPassPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,40));
			add(accSettingsNewPassLabel,BorderLayout.LINE_START);
			add(accSettingsNewPassField,BorderLayout.LINE_END);
		}};
		
		
		private static JLabel accSettingsOldPassLabel = new JLabel("Current Password:") {{
			setPreferredSize(new Dimension(110,FIELD_HEIGHT));
		}};
		
		private static JPasswordField accSettingsOldPassField = new JPasswordField() {{
			setPreferredSize(new Dimension(150,FIELD_HEIGHT));
		}};
		
		private static JPanel accSettingsOldPassPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,40));
			add(accSettingsOldPassLabel,BorderLayout.LINE_START);
			add(accSettingsOldPassField,BorderLayout.LINE_END);
		}};
		
		private static JLabel accSettingsNewEmailLabel = new JLabel("Email:") {{
			setPreferredSize(new Dimension(80,FIELD_HEIGHT));
		}};
		
		private static JTextField accSettingsNewEmailField = new JTextField() {{
			setPreferredSize(new Dimension(180,FIELD_HEIGHT));
		}};
		
		private static JPanel accSettingsNewEmailPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,40));
			add(accSettingsNewEmailLabel,BorderLayout.LINE_START);
			add(accSettingsNewEmailField,BorderLayout.LINE_END);
		}};
		
		private static JLabel accSettingsMsg = new JLabel("This is a message for the user.",JLabel.CENTER) {{
			setPreferredSize(new Dimension(300,FIELD_HEIGHT));
			setBackground(Color.gray);
		}};
		
		
		private static JButton accSettingsSaveBTN = new JButton("Save Settings") {{
			setPreferredSize(new Dimension(150,FIELD_HEIGHT));
		}};
	
		//-- account deletion :: x
		private static JLabel accDelMsg = new JLabel() {{
			setPreferredSize(new Dimension(290,45));
		}};
		
		private static JTextField accDelConfirmField = new JTextField() {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
		private static JPanel accDelConfirmPanel = new JPanel() {{
			setPreferredSize(new Dimension(300,50));
			add(accDelConfirmField);
		}};
		
		private static JButton accDelConfirmBTN = new JButton("DELETE") {{
			setPreferredSize(new Dimension(100,FIELD_HEIGHT));
		}};
		
	//===========	 METHODS   ==============//
	public static void enableComponentsWithin(JPanel thisPanel, boolean enable) {
		Component[] components = thisPanel.getComponents();
		
		for (Component thisComponent : components) {
			thisComponent.setEnabled(enable);
		}
	}
	
	public static void hideComponentsWithin(JPanel thisPanel, boolean hide) {
		Component[] components = thisPanel.getComponents();
		
		for (Component thisComponent : components) {
			thisComponent.setVisible(!hide);
		}
	}
	
	private static JButton[] btns = {main_loginBTN,main_registerBTN,main_forgotPassBTN,
			forgotPassPanel_sendPassReqBTN,forgotPassPanel_backToMainBTN,
			register_finishBTN, register_backToMainBTN,
			withdrawBTN,
			transferBTN,
			depositBTN,
			accSettingsSaveBTN,
			accDelConfirmBTN};
	private static JTextField[] fields = {main_userIDField, main_userPasswordField,
			register_userNameField, register_userEmailField, register_userIntialDepositField,
			forgotPassPanel_userIDField};
	private static JTextField[] userMenuFields = {withdrawAmountField,
			transferAmountField, transfereeField,
			depositAmountField,
			accSettingsNewNameField, accSettingsNewPassField, accSettingsNewEmailField, accSettingsOldPassField,
			accDelConfirmField};
	private static JPanel[] userMenuInputPanels = {
			depositInputPanel,
			transferAmountInputPanel,
			transfereePanel,
			withdrawInputPanel,
			accSettingsNewNamePanel,
			accSettingsNewPassPanel,
			accSettingsOldPassPanel,
			accSettingsNewEmailPanel,
			accDelConfirmPanel
	};
	/*
	private static JPasswordField[] passFields = {
			};
		*/
	public static void disableBTNs(boolean disable) {
		for ( JButton btn : btns) {
			btn.setEnabled(!disable);
		}
	}
	
	//=========== MAIN PANELS
	//-- three start panels
	private static ImageIcon headerImg = new ImageIcon("./src/header2.png");
	
	private static JLabel bannerLabel = new JLabel("", headerImg, JLabel.CENTER) {{
		setPreferredSize(new Dimension(500,100));
		setOpaque(true);
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	}};
	
	private static JPanel headerPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,100));
		setBackground(GUIHelper.getColor("Header"));
		GUIHelper.getColor("Header");
		add(bannerLabel);
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	}};
	
	private static JPanel contentPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,300));
		setBackground(GUIHelper.getColor("Content"));
		add(mainMenuPanel);
		add(registerPanel);
		add(forgotPassPanel);
		add(userMenuPanel);
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	}};
		
	private static JLabel footerLabel = new JLabel("©️ By JSGoatTeam",JLabel.CENTER) {{
		setPreferredSize(new Dimension(500,FIELD_HEIGHT));
		setOpaque(true);
		setBackground(GUIHelper.getColor("Footer"));
		setForeground(GUIHelper.getColor("White"));
	}};
	
	private static JLabel footerSpacerLabel = new JLabel("") {{
		setPreferredSize(new Dimension(500,75));
	}};
	
	private static JPanel footerPanel = new JPanel() {{
		setPreferredSize(new Dimension(500,100));
		setBackground(GUIHelper.getColor("Footer"));
		add(footerLabel,BorderLayout.PAGE_START);
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	}};
	
	private static JPanel startMenuPanel = new JPanel() {{
		setPreferredSize(fullPanelSize);
		setBackground(GUIHelper.getColor("Main"));
		add(headerPanel,BorderLayout.PAGE_START);
		add(contentPanel,BorderLayout.CENTER);
		add(footerPanel, BorderLayout.PAGE_END);
		setVisible(false);
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	}};
	
	//======== PRESS BUTTON LISTENER =================//
	static ActionListener pressBTN = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!canPressBTN) {return;}
			canPressBTN = false;
			
			Object source = e.getSource();
			
			if (source==main_loginBTN) {
				
				String userName = main_userIDField.getText();
				
				// concatenate password
				String pass = GUIHelper.constructPass(main_userPasswordField.getPassword());
    			
    			boolean thisUserNameValid = (GUIHelper.EntryReqError.ENTRY_JUST_RIGHT == GUIHelper.fieldMeetsRequirements("user", userName));
    			boolean thisPassValid = (GUIHelper.EntryReqError.ENTRY_JUST_RIGHT == GUIHelper.fieldMeetsRequirements("pass", pass));
    			
    			if (thisUserNameValid && thisPassValid) {
    				if (MongoDB.userExists(userName, "db_users") == true) {
        				MongoDoc doc = thisdbase.getDoc("user", userName, "db_users");
                		
                		if (doc != null && pass.equals(doc.getProp("pass"))) { // user data found
                			thisUser = new BankUser(doc);
                			
                			userMenuWelcomeLabel.setText(String.format("<html><div width=200 style='overflow-wrap: break-word;hyphens: manual;'>Welcome %s %s to %s!</div></html>",
                					thisUser.getFirstName(),
                					thisUser.getLastName(),
                					AppSettings.getAppName()));
                			
                			main_msgLabel.setText("User found. Logging in...");
                			mainMenuPanel.setVisible(false);
                			userMenuPanel.setVisible(true);
                		} else {
                			main_msgLabel.setText("Credentials were incorrect. Please try again...");
                		}
        			} else {
        				GUIHelper.setMsg(main_msgLabel, "NoUserFound");
        			}
    			} else {
    				main_msgLabel.setText("Invalid username or password.");
    			}
			} else if (source==main_registerBTN) {
				// Check that they have entered details for the username first
				String userName = main_userIDField.getText();
				
				// concatenate password
    			String pass = "";
    			for (char thisChar : main_userPasswordField.getPassword()) {
    				pass += thisChar;
    			}
    			
    			// Data validation
    			boolean thisUserNameValid = (GUIHelper.EntryReqError.ENTRY_JUST_RIGHT == GUIHelper.fieldMeetsRequirements("user", userName));
    			boolean thisPassValid = (GUIHelper.EntryReqError.ENTRY_JUST_RIGHT == GUIHelper.fieldMeetsRequirements("pass", pass));
    			if (thisUserNameValid && thisPassValid) {
    				if (!MongoDB.userExists(userName, "db_users")) { // user does not exist
    					GUIHelper.setMsg(main_msgLabel, "Loading");
        				mainMenuPanel.setVisible(false);
        				registerPanel.setVisible(true);
    				} else {
    					main_msgLabel.setText("Account with that username already exists.");
    				}
    			} else {
    				main_msgLabel.setText("Invalid username or password.");
    			}
			} else if (source==register_finishBTN) {
				// register account!
				String user = main_userIDField.getText();
				String pass = GUIHelper.constructPass(main_userPasswordField.getPassword());
				String startBal = GUIHelper.stripInputUntilNum(register_userIntialDepositField.getText());
				
				HashMap<String, String> fieldsToAutoCheck = new HashMap<>() {{
					put("newName",register_userNameField.getText());
					put("email",register_userEmailField.getText());
					put("startBal",startBal);
				}};
				
				GUIHelper.printDebug(String.valueOf(register_userEmailField.getText()==null));
				GUIHelper.printDebug(String.valueOf(register_userEmailField.getText().equals("")));
				GUIHelper.EntryReqError entryMetReq;
				boolean isEntryErr;
				
				for (String fieldName : fieldsToAutoCheck.keySet()) {
					String fieldValue = fieldsToAutoCheck.get(fieldName);
					
					entryMetReq = GUIHelper.fieldMeetsRequirements(fieldName, fieldValue);
					isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
					
					if (isEntryErr) {
						GUIHelper.setMsg(register_msgLabel, entryMetReq.toString());
						canPressBTN = true;
						return;
					}
				}
				
				GUIHelper.setMsg(register_msgLabel, "RegisterWait");
				
				HashMap<String, String> namesMap = GUIHelper.splitFullName(fieldsToAutoCheck.get("newName"));
				boolean registerSuccess = MongoDB.userRegister(user,pass, namesMap.get("fName"), namesMap.get("lName"), Double.parseDouble(startBal), fieldsToAutoCheck.get("email"));
    			if (registerSuccess) {
    				register_msgLabel.setText(GUIHelper.getMsg("RegisterSuccess"));
    			} else {
    				register_msgLabel.setText(GUIHelper.getMsg("RegisterFail"));
    			}
			} else if (source==register_backToMainBTN) {
				GUIHelper.setMsg(main_msgLabel,"Welcome");
				mainMenuPanel.setVisible(true);
				registerPanel.setVisible(false);
			}else if (source==main_forgotPassBTN) {
				forgotPassPanel_msgLabel.setText(GUIHelper.getMsg("DEF_sspr"));
				mainMenuPanel.setVisible(false);
				forgotPassPanel.setVisible(true);
			} else if (source==forgotPassPanel_sendPassReqBTN) {
				String userName = forgotPassPanel_userIDField.getText();
				
				if (MongoDB.userExists(userName,"db_users")) {
					MongoDoc doc = thisdbase.getDoc("user", userName, "db_users");
					BankUser thisUser = new BankUser(doc);
					String thisEmail = thisUser.getEmail();
					
					if (thisEmail.equals("")) {
						GUIHelper.setMsg(forgotPassPanel_msgLabel, "NoUserEmail");
					} else {
						UI_Base.print("f");
						String newPass = MongoDB.genNewPass(userName);
						if (newPass!=null) {
							UI_Base.print("l");
							SendEmail.sendNewPassReq(thisEmail, newPass, userName);
	    					GUIHelper.setMsg(forgotPassPanel_msgLabel,"NewPassReqSuccess");
						} else {
							GUIHelper.setMsg(forgotPassPanel_msgLabel,"NewPassReqFailed");
						}
					}
				} else {
					GUIHelper.setMsg(forgotPassPanel_msgLabel, "NoUserFound");
				}
			} else if (source==forgotPassPanel_backToMainBTN) {
				GUIHelper.setMsg(main_msgLabel,"Welcome");
				mainMenuPanel.setVisible(true);
				forgotPassPanel.setVisible(false);
			} else if (source==internetConnectBTN) {
				mongoClient = MongoDB.attemptConnection(uri);
				if (mongoClient!=null) {
					//GUIHelper.print("Clicked");
					initialize();
					fatalMsgPanel.setVisible(false);
					startMenuPanel.setVisible(true);
				}
			} else if (source==depositBTN) {
				String depValue = depositAmountField.getText();
				GUIHelper.EntryReqError entryMetReq = GUIHelper.fieldMeetsRequirements("deposit", depValue);
				boolean isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
				
				if (isEntryErr) {
					GUIHelper.setMsg(depositMsg, entryMetReq.toString());
				} else {
					String split[] = depValue.split(" ");
					if (depValue.contains(" ") && split.length > 1) {
						//String split[] = depValue.split(depValue);
						depValue = UI_Base.stripInputUntilNum(split[0]);
						String convertFromCurrency = split[1];
						//depValue = UI_Base.stripInputUntilNum(split[0]);
						
						if (CurrencyHandler.canConvertCurrency(convertFromCurrency)) {
							double conversionRate_inverse = CurrencyHandler.convertCurrency(Double.parseDouble(depValue), convertFromCurrency, "USD");
							double conversionRate = 1/conversionRate_inverse;
							double convertedAmount = conversionRate * Double.parseDouble(depValue);
							
							String formattedRate = String.format("%.2f",convertedAmount);
							double roundedRate = Double.parseDouble(formattedRate);
							
							GUIHelper.setMsg(depositMsg, "Loading");
							
							thisUser.deposit(roundedRate);
							MongoDB.userSave(thisUser);
							
							GUIHelper.setMsg(depositMsg, "DepositSuccess");
						} else {GUIHelper.setMsg(depositMsg, "ENTRY_INVALCURR");}
					} else {
						// remove space
						GUIHelper.setMsg(depositMsg, "Loading");
						
						depValue = UI_Base.stripInputUntilNum(depValue);
						thisUser.deposit(depValue);
						MongoDB.userSave(thisUser);
						
						GUIHelper.setMsg(depositMsg, "DepositSuccess");
					}
					
					/*
					GUIHelper.setMsg(depositMsg, "Loading");
					
					depValue = GUIHelper.stripInputUntilNum(depValue);
					thisUser.deposit(depValue);
					MongoDB.userSave(thisUser);
					
					GUIHelper.setMsg(depositMsg, "DepositSuccess");
					*/
				}
			} else if (source==withdrawBTN) {
				if (thisUser.canWithdraw()) {
					String withdrawValue = withdrawAmountField.getText();
					GUIHelper.EntryReqError entryMetReq = GUIHelper.fieldMeetsRequirements("withdraw", withdrawValue);
					boolean isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
					
					if (isEntryErr) {
						GUIHelper.setMsg(withdrawMsg, entryMetReq.toString());
						//withdrawMsg.setText(GUIHelper.getMsg(entryMetReq.toString()));
					} else {
						GUIHelper.setMsg(withdrawMsg, "Loading");
						//withdrawMsg.setText("Loading...");
						
						withdrawValue = GUIHelper.stripInputUntilNum(withdrawValue);
						thisUser.withdraw(withdrawValue);
						MongoDB.userSave(thisUser);
						
						GUIHelper.setMsg(withdrawMsg, "WithdrawSuccess");
					}
				} else {
					GUIHelper.setMsg(withdrawMsg, "DebtWithdraw");
				}
			} else if (source==transferBTN) {
				if (thisUser.canWithdraw()) {
					transferMsg.setText(GUIHelper.getMsg("Loading"));
					
					String transferValue = transferAmountField.getText();
					String transfereeValue = transfereeField.getText();
					
					GUIHelper.EntryReqError entryMetReq = GUIHelper.fieldMeetsRequirements("transfer", transferValue);
					boolean isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
					
					if (isEntryErr) {
						transferMsg.setText(GUIHelper.getMsg(entryMetReq.toString()));
						canPressBTN = true;
						return;
					} else if (!MongoDB.userExists(transfereeValue, "db_users")) {
						GUIHelper.setMsg(transferMsg, "NoUserFound");
						canPressBTN = true;
						return;
					}
					
					transferMsg.setText("Loading...");
					
					transferValue = GUIHelper.stripInputUntilNum(transferValue);
					transferMsg.setText(transferValue);
					thisUser.transfer(Double.parseDouble(transferValue), transfereeValue);
					MongoDB.userSave(thisUser);
					
					GUIHelper.setMsg(transferMsg,"TransferSuccess");
				} else {
					GUIHelper.setMsg(transferMsg, "DebtWithdraw");
				}
			} else if (source==accSettingsSaveBTN) {
				boolean canSave = true;
				
				HashMap<String, String> fieldsToAutoCheck = new HashMap<>() {{
					put("newName",accSettingsNewNameField.getText());
					put("email",accSettingsNewEmailField.getText());
					put("pass",accSettingsNewPassField.getText());
				}};
				
				GUIHelper.EntryReqError entryMetReq;
				boolean isEntryErr;
				
				for (String fieldName : fieldsToAutoCheck.keySet()) {
					String fieldValue = fieldsToAutoCheck.get(fieldName);
					
					if (fieldName.equals("pass") && fieldValue.equals("")) { // user does not want to change their password
						//
					} else {
						entryMetReq = GUIHelper.fieldMeetsRequirements(fieldName, fieldValue);
						isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
						
						if (isEntryErr) {
							GUIHelper.setMsg(accSettingsMsg, entryMetReq.toString());
							//accSettingsMsg.setText(GUIHelper.getMsg(entryMetReq.toString()));
							canSave = false;
							canPressBTN = true;
							return;
						}
					}
				}
				
				String thisOldPass = GUIHelper.constructPass(accSettingsOldPassField.getPassword());
				canSave = (thisOldPass.equals(thisUser.getPass())) ? true : false;
				
				if (canSave) {
					GUIHelper.setMsg(accSettingsMsg, "SavingWait");
					
					String newPass = accSettingsNewPassField.getText();
					HashMap<String, String> namesMap = GUIHelper.splitFullName(accSettingsNewNameField.getText());
					
					thisUser.setProperty("pass",(newPass.equals("") ? thisUser.getPass() : newPass));
					thisUser.setProperty("email",accSettingsNewEmailField.getText());
					thisUser.setProperty("fName", namesMap.get("fName"));
					thisUser.setProperty("lName", namesMap.get("lName"));
					
					MongoDB.userSave(thisUser);
					GUIHelper.setMsg(accSettingsMsg, "SaveSuccess");
				} else {
					accSettingsMsg.setText("Password is incorrect.");
				}
			} else if (source==accDelConfirmBTN) {
				if (accDelConfirmField.getText().equals(thisUser.getPass())) {
					MongoDB.closeAccount(thisUser);
					logout();
				}
			}
			
			canPressBTN = true;
		}
		
	};
	
	
	public static void main(String[] args) {		
		//============== MONGODB ==================//
        Scanner input = new Scanner(System.in);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
	        
		//=============== GUI =================//
		mainMenuFrame.setSize(fullPanelSize);
		mainMenuFrame.setTitle("Main Menu | JSGoat Banking");
		mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenuFrame.setResizable(false);
		
		//========== PANEL COMPONENTS
		// MAIN MENU PANEL COMPONENTS
		
		//-- btns
		main_loginBTN.addActionListener(pressBTN);
		main_registerBTN.addActionListener(pressBTN);
		main_forgotPassBTN.addActionListener(pressBTN);
		
		//-- btns
		forgotPassPanel_sendPassReqBTN.addActionListener(pressBTN);
		forgotPassPanel_backToMainBTN.addActionListener(pressBTN);
		
		//--
		register_backToMainBTN.addActionListener(pressBTN);
		register_finishBTN.addActionListener(pressBTN);
		
		depositBTN.addActionListener(pressBTN);
		withdrawBTN.addActionListener(pressBTN);
		transferBTN.addActionListener(pressBTN);
		
		accSettingsSaveBTN.addActionListener(pressBTN);
		accDelConfirmBTN.addActionListener(pressBTN);
		//== coloring
		UIManager.put("Button.select", GUIHelper.getColor("BTN_Select"));
		
		// this is only for creating the content displays for each user menu action!
		// should only be run once! do NOT move!
		for (String actionKey : userMenuPanels.keySet()) { // for each user menu content action panel
			JPanel thisPanel = userMenuPanels.get(actionKey); // get user menu content action panel associated with this key
			
			switch(actionKey) { // content action panel setup; setting up the elements within the content action panel
				case "c": // view account info
					thisPanel.add(accInfoMsg,BorderLayout.PAGE_START);
					thisPanel.add(accInfo_NameLabel,BorderLayout.CENTER);
					thisPanel.add(accInfo_BalanceLabel,BorderLayout.CENTER);
					break;
				case "f": // deposit					
					thisPanel.add(depositInputPanel);
					thisPanel.add(depositMsg);
					thisPanel.add(depositBTN);
					break;
				case "w": // withdraw
					thisPanel.add(withdrawInputPanel);
					thisPanel.add(withdrawMsg);
					thisPanel.add(withdrawBTN);
					break;
				case "t": // transfer					
					thisPanel.add(transferAmountInputPanel);
					thisPanel.add(transfereePanel);
					thisPanel.add(transferMsg);
					thisPanel.add(transferBTN);
					break;
				case "v":					
					thisPanel.add(accSettingsNewNamePanel);
					thisPanel.add(accSettingsNewEmailPanel);
					thisPanel.add(accSettingsNewPassPanel);
					thisPanel.add(accSettingsOldPassPanel);
					thisPanel.add(accSettingsMsg);
					thisPanel.add(accSettingsSaveBTN);
					break;
				case "x":
					thisPanel.add(accDelMsg);
					thisPanel.add(accDelConfirmPanel);
					thisPanel.add(accDelConfirmBTN);
					break;
				default://
			}
			
			userMenuContentPanel.add(thisPanel); // add it to the user menu content panel
			thisPanel.setVisible(false);
		}
		
		HashMap<String,JLabel> userMenuLabels = new HashMap<>() {{
			put("t",transferMsg);
			put("v",accSettingsMsg);
			put("f",depositMsg);
			put("w",withdrawMsg);
			put("x",accDelMsg);
		}};
		
		HashMap<String, String> thisMenuActions = GUIHelper.getMenuActions("UserMainMenu");
		thisMenuActions.put("x", "close account");
		for (String key : thisMenuActions.keySet()) { // for each action button for this menu
			String actionName = thisMenuActions.get(key);
			
			JButton thisNavBTN = new JButton(actionName) {{
				setPreferredSize(new Dimension(180,25));
				
			}}; // create a new button
			
			thisNavBTN.addActionListener(new ActionListener() { // add a function when pressed
				public void actionPerformed(ActionEvent e) {
					if (!canPressBTN) {return;}
					canPressBTN = false;
					hideComponentsWithin(userMenuContentPanel,true); // hide all panels within
						
					// get corresponding panel
					JPanel thisBTN_Panel = userMenuPanels.get(key); // get corresponding panel
					if (thisBTN_Panel!=null) { // panel exists
						if (key == "c") {
							accInfo_NameLabel.setText(String.format("Account Holder: %s %s",
									thisUser.getFirstName(),
									thisUser.getLastName())
							);
							accInfo_BalanceLabel.setText("Balance: "+GUIHelper.formatNum(thisUser.getBalance()));
						} else if (key == "v") {
							// make the fields autofill
							accSettingsNewNameField.setText(String.format("%s %s", thisUser.getFirstName(), thisUser.getLastName()));
							accSettingsNewEmailField.setText(thisUser.getEmail());
						}
						
						JLabel thisMsgLabel = userMenuLabels.get(key);
						if (thisMsgLabel!=null) { thisMsgLabel.setText(GUIHelper.getMsg("DEF_"+key)); }
						
						thisBTN_Panel.setVisible(true); // make corresponding panel visible
					} else if(actionName=="logout") {
						logout();
					}
					
					canPressBTN = true;
				}
			});
			
			userMenuNavPanel.add(thisNavBTN);
			GUIHelper.skin(thisNavBTN, "BTN_BG", "BTN_FG");
		}
		
		
		//============= FATAL ERROR PANELS
		internetConnectBTN.setPreferredSize(new Dimension(100,50));
		internetConnectionMsg.setPreferredSize(new Dimension(400,50));
		
		//======= FINALIZATION
		for (JButton thisBTN : btns) {
			if (thisBTN==accDelConfirmBTN) {
				GUIHelper.skin(thisBTN, "Red", "BTN_FG");
			} else {
				GUIHelper.skin(thisBTN,"BTN_BG","BTN_FG");
			}
		}
		
		for (JTextField thisField : fields) {
			GUIHelper.skin(thisField,"Field","Black");
		}
		for (JTextField thisField : userMenuFields) {
			GUIHelper.skin(thisField,"UserMenuField","Black");
		}
		for (JPanel thisPanel : userMenuInputPanels) {
			thisPanel.setBackground(GUIHelper.getColor("FieldPanel"));
		}
		
		JPanel displayGUIPanel = new JPanel() {{
			setPreferredSize(fullPanelSize);
			setBackground(GUIHelper.getColor("Main"));
			add(fatalMsgPanel);
			add(startMenuPanel);
			add(splashPanel);
			setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		}};
		
		mainMenuFrame.setIconImage(progImg.getImage());
		
		splashPanel.setVisible(true);
		
		mainMenuFrame.add(displayGUIPanel);
		mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenuFrame.setVisible(true);
		
		mongoClient = MongoDB.attemptConnection(uri);
		if (mongoClient==null) {
			startMenuPanel.setVisible(false);
			fatalMsgPanel.setVisible(true);
		} else {
			initialize();
			fatalMsgPanel.setVisible(false);
			startMenuPanel.setVisible(true);
		}
		
		splashPanel.setVisible(false);
	}
	
	public static void initialize() {
		thisdbase = new MongoDB("db_users", mongoClient);
		thisdbase.addCollection("db_users"); // prepare collection to be searched
	}
	
	public static void logout() {
		hideComponentsWithin(userMenuContentPanel,true);
		userMenuPanel_MainPanel.setVisible(true);
		GUIHelper.setMsg(main_msgLabel, "Welcome");
		mainMenuPanel.setVisible(true);
		userMenuPanel.setVisible(false);
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
