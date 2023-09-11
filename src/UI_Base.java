import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class UI_Base {
	private static Scanner input = new Scanner(System.in);
	private static boolean isDebugging = false;
	private static NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	//======== MENU OPTIONS ============//
	enum EntryReqError { ENTRY_TOO_SHORT, ENTRY_TOO_LONG, ENTRY_JUST_RIGHT, NO_ENTRY, ENTRY_INVALID_EMAIL,
		ENTRY_NO_BOTH_NAMES, ENTRY_INVALCURR, ENTRY_INVALIDNAME, ENTRY_NEGSTARTBAL, ENTRY_BELOWZERO}
	
	private static HashMap<String, String> messageLibrary = new HashMap<>() {{
		put("Welcome","Welcome to "+AppSettings.getAppName()+"!");
		put("ENTRY_INVALID_EMAIL","This is an invalid email.");
		put("ENTRY_TOO_SHORT","This input is too short.");
		put("ENTRY_TOO_LONG","This input is too long.");
		put("ENTRY_INVALCURR","We're sorry! "+AppSettings.getAppName()+" does not yet support deposits for this currency at this point in time.");
		put("ENTRY_NEGSTARTBAL","You cannot open a bank account with a negative balance.");
		put("ENTRY_BELOWZERO","Please enter a value above 0.");
		put("NO_ENTRY","Invalid input.");
		
		put("DEF_f","Enter an amount...");
		put("DEF_w","Enter an amount...");
		put("DEF_t","Enter an amount...");
		put("DEF_v","Enter current password to confirm changes.");
		put("DEF_x","<html><div width=250 style='overflow-wrap: break-word;hyphens: manual;'>Enter current password to confirm <span style='color:red;'>account deletion</span>:</div></html>");
		put("DEF_sspr","Enter your username to recieve your new password...");
		
		put("LoadingUI","Loading interface... please wait a moment...");
		put("LoadingAccCreate","Creating your account... please wait a moment...");
		put("Loading","Loading...");
		put("SavingWait","Saving... please wait a moment...");
		put("Goodbye","Sorry to see you go!");
		put("NeedsInternet","This program requires internet connectivity. \n<!>Please connect to the internet, then enter return...\n\tor <quit> to terminate application.");
		
		put("LoginSuccess", "Login was successful!");
		put("LogoutSuccess","Sorry to see you go!");
		put("LoginTaken","This login is not available!");
		put("LoginAvailable","Checking login availability... please wait a moment");
		
		put("RegisterSuccess","Registration was a success! Please log in!");
		put("RegisterFail","Registration failed, please try again...");
		put("AccDetailRgstr","We just need a few more details to set up your account."
				+ "\n\tEnter \"quit\" at any time to end the registration process.");
		
		put("NoUserFound","No user found.");
		put("NoUserEmail","Please set an email for this user to request a new password...");
		put("WrongPass","Invalid password.");
		
		put("Enter_pass","Enter your password:");
		put("Enter_fName","Enter your first name:");
		put("Enter_lName","Enter your last name:");
		put("Enter_startBal","Enter the balance you will start out with:");
		put("Enter_email","Enter a valid email address \n\t\t\tor leave blank to set later (not recommended):");
		
		put("NewPassReqSuccess","Password was successfully reset. Please check your email...");
		
		put("EnterUser","Enter your username:");
		put("EnterPass","Enter your password:");
		put("EnterNewPass","Enter your new password:");
		put("EnterDeposit","Enter deposit amount:");
		put("EnterWithdraw","Enter withdrawl amount:");
		put("EnterEmail","Enter your email:");
		
		put("NegStartBal","You cannot open a bank account with a negative balance.");
		
		put("InvalidAction","That action was invalid!");
		put("MainMenuPrompt","Action: ");
		put("SessionCreateFail","Failed to open a session... please try again...");
		
		put("DepositSuccess","Deposit was successful!");
		put("SaveSuccess","Data saved sucessfully");
		put("RegisterWait","Registering...");
		put("WithdrawSuccess","Withdrawal was a success!");
		put("TransferSuccess","Withdrawal was a success!");
		
		put("DebtWithdraw","You cannot withdraw any money. You are in debt.");
		put("InvalidEntry","This input is invalid.");
		put("TryAgain","Try again or enter <quit> to quit.");
		put("NewPassReqFailed","Attempt to reset password failed.");
		
		put("","");
		put("EnterNewName","Enter the current name for this account holder:");
		put("ENTRY_NO_BOTH_NAMES","You must enter both your first name and your last name.");
		put("FailSave","Error: system failed to save user data.");
		put("UserNoEmail","This user has no email. Please set one to access self-service password reset.");
		
		put("ConfirmDelAcc","Enter your password again to confirm the closure of your account\n\t(or enter <quit> to go back).");
	}};
	
	private static HashMap<String,String> mainMenuOptions = new HashMap<>() {{
		put("c","login");
		put("n","register");
		put("l","forgot your password?");
	}};
	
	private static HashMap<String, String> userMainMenuOptions = new HashMap<>() {{
		put("c","view account information");
		put("v","account settings");
		put("k","logout");
		put("f","deposit");
		put("w","withdraw");
		put("t","transfer");
	}};
	
	private static HashMap<String, String> accSettingMenuOptions = new HashMap<>() {{
		put("n","update name");
		put("k","update email");
		put("x","close account");
		put("e","exit menu");
		put("p","change pass");
	}};
	
	private static HashMap<String, String> logoutFailMenuOptions = new HashMap<>() {{
		put("t","try again");
		put("v","log out anyway");
		put("r","back to user menu");
	}};
	
	private static HashMap<String, HashMap<String,String>> menuActions = new HashMap<>() {{
		put("Main", mainMenuOptions);
		put("UserMainMenu",userMainMenuOptions);
		put("AccSettings",accSettingMenuOptions);
		put("LogoutFailed",logoutFailMenuOptions);
	}};
	private static HashMap<String,String> menuPrompts = new HashMap<>() {{
		put("Main","Welcome to "+AppSettings.getAppName()+"!");
		put("UserMainMenu","Account Actions");
		put("AccSettings","Account Configuration:");
		put("LogoutFailed","ERROR: System failed to save your account information.");
	}};
	
	//=================		MENUS 	====================//

	public static String getMenuActionKey(String menuAction) {
		HashMap<String, String> thisMenuActions = getMenuActions("UserMainMenu");
		for (String key : thisMenuActions.keySet()) {
			if (thisMenuActions.get(key).equals(menuAction)) {
				return key;
			}
		}
		return null;
	}
	
	// performMenuAction :: overloaded
	public static void performMenuAction(String menuAction, MongoDB thisdbase) { // for main menu actions
		final String quitKey = AppSettings.getQuitKey();
		
		switch(menuAction) {
			case "c":
				String user;
				String pass;
				
				UI_Base.print("----- LOGIN BEGIN -----");
				
				UI_Base.printMsg("EnterUser");
    			user = input.nextLine();
    			
    			user = UI_Base.untilValidInput("user", user);
    			if (user.equals(quitKey)) { UI_Base.print("----- LOGIN END -----",0,2); break; };
    			
    			UI_Base.printMsg("EnterPass");
    			pass = input.nextLine();
    			
    			pass = UI_Base.untilValidInput("pass", pass);
    			if (pass.equals(quitKey)) { UI_Base.print("----- LOGIN END -----",0,2); break; };
    			
    			UI_Base.printMsg("LoadingUI",2,0); // let the user know we are loading their details
    			
    			if (MongoDB.userExists(user, "db_users") == true) {
    				MongoDoc doc = thisdbase.getDoc("user", user, "db_users");
            		
            		if (doc != null) { // user data found
            			if (pass.equals(doc.getProp("pass"))) { //password for account matches
            				UI_Base.printMsg("LoginSuccess",1,1); // output to user that the login was success
            				UI_Base.print("----- LOGIN END -----",0,2);
            				
            				BankUser thisUser = new BankUser(doc); // create a new bank user object
                			System.out.printf("====== %s =====%nWelcome %s %s to %s!%n"
                					+ "Balance: $ %,3.2f%n%n",
                					UI_Base.getAppName(),
                					thisUser.getFirstName(),
                					thisUser.getLastName(),
                					UI_Base.getAppName(),
                					thisUser.getBalance());
                			
                			while (thisUser.isLoggedIn()) { // while this user is logged in
                				UI_Base.printMenu(thisUser.onMenu()); // print the available options based on the menu the user is currently on
                				
                				System.out.printf("Action:"); // prompt user for a key that corresponds to an action
                				String thisAction = input.nextLine(); // get action we want from user
                				UI_Base.performMenuAction(thisAction, thisUser);
                			}
            			} else {
            				UI_Base.printMsg("WrongPass"); // password for this account is not right
            				UI_Base.print("----- LOGIN END -----",0,2);
            			}
                    } else {
                    	UI_Base.printMsg("SessionCreateFail"); // user account not found
                    	UI_Base.print("----- LOGIN END -----",0,2);
                        //mongoClient.close();
                    }
    			} else {
    				UI_Base.printMsg("NoUserFound",2,2);
    				UI_Base.print("----- LOGIN END -----",0,2);
    			};
    			
    			break;
			case "n":
				
				UI_Base.print("----- REGISTRATION BEGIN -----");
				boolean continueRegistration = true;
				
				while (continueRegistration) { // continue trying as long as the user wants to
					//Validate usernname input
					UI_Base.printMsg("EnterUser");
        			user = input.nextLine();
        			
        			user = UI_Base.untilValidInput("user", user);
        			if (user.equals(quitKey)) { break; };
        			
        			UI_Base.printMsg("LoginAvailable",1,1); // let the user know we are loading their details

        			if (MongoDB.userExists(user, "db_users") != true) { //user does not exist
        				// Register user
        				UI_Base.printMsg("AccDetailRgstr");
        				
        				String accDetailFieldsOrder[] = BankUser.getRegisterAccDetailOrder();
            			HashMap<String,String> accDetailFields = BankUser.getAccDetailFields();
            			
        				for (int fieldOrder=0; fieldOrder<accDetailFieldsOrder.length; ++fieldOrder) {
        					String fieldName = accDetailFieldsOrder[fieldOrder];
        					UI_Base.printDebug("getting details for field "+fieldName+"\n");
        					UI_Base.printMsg("Enter_"+fieldName); // prompt user for input
        					
        					if (fieldName.equals("startBal")) { // requires validation
        						String startingBalance;
                				startingBalance = input.nextLine();
                				
                				while(!startingBalance.equals(quitKey)) {
                					startingBalance = UI_Base.stripInputUntilNum(startingBalance);
                					
                					if (!UI_Base.canStringToNum(startingBalance)){
                						UI_Base.printMsg("InvalidEntry");
                						UI_Base.printMsg("TryAgain");
                						startingBalance = input.nextLine();
                						continue;
                					}
                					
                					while(Double.parseDouble(startingBalance) < 0) { // user trying to open account start with negative balance
                    					UI_Base.printMsg("NegStartBal"); // tell user they cannot start with a negative balance
                    					UI_Base.printMsg("TryAgain");
                    					
                    					UI_Base.printMsg("Enter_"+fieldName);
                        				startingBalance = input.nextLine();
                    				}
                					if (Double.parseDouble(startingBalance) >= 0) { break; }
                				}
                				
                				if (startingBalance.equals(quitKey)) {
        							continueRegistration = false;
        							break;
        						} else {
        							accDetailFields.put(fieldName, startingBalance);
        						}
        					} else {
        						String entry = input.nextLine();
        						
        						entry = UI_Base.untilValidInput(fieldName, entry);
        						
        						if (entry.equals(quitKey)) {
        							continueRegistration = false;
        							break;
        						} else {
        							accDetailFields.put(fieldName, entry);
        						}
        					}
        				}
            			
        				if (continueRegistration) {
        					UI_Base.printMsg("LoadingAccCreate",2,0);
        					
                			boolean registerSuccess = MongoDB.userRegister(user,accDetailFields.get("pass"), accDetailFields.get("fName"), accDetailFields.get("lName"), Double.parseDouble(accDetailFields.get("startBal")));
                			if (registerSuccess) {
                				UI_Base.printMsg("RegisterSuccess");
                				break;
                			} else {
                				UI_Base.printMsg("RegisterFail");
                				
                				UI_Base.printMsg("ContinueRegster");
                    			String continueRegister = input.nextLine();
                    			continueRegistration = (continueRegister==quitKey) ? false : true;
                			}
                			
        				}
            			
        			} else {
        				UI_Base.printMsg("LoginTaken");
        			}
				}
				
				
				UI_Base.print("----- REGISTRATION END -----\n\n");
				
				break;
			case "l":
				
				// Validate UserName
				UI_Base.printMsg("EnterUser");
				String userName = input.nextLine();
				
				userName = UI_Base.untilValidInput("user",userName);
				if (userName.equals(quitKey)) { UI_Base.printMsg("",1,1); break; }
				
				UI_Base.printMsg("Loading",1,1);
				
				if (MongoDB.userExists(userName,"db_users")) {
					MongoDoc doc = thisdbase.getDoc("user", userName, "db_users");
					BankUser thisUser = new BankUser(doc);
					String thisEmail = thisUser.getEmail();
					
					if (thisEmail.equals("")) { // this email has no ""
						UI_Base.printMsg("UserNoEmail",0,2);
					} else {
						String newPass = MongoDB.genNewPass(userName);
						if (newPass!=null) {
							//UI_Base.printMsg("Loading",0,2);
	    					SendEmail.sendNewPassReq(thisEmail, newPass, userName);
	    					UI_Base.printMsg("NewPassReqSuccess",0,2);
						} else {
							UI_Base.printMsg("NewPassReqFailed",2,2);
						}
					}
				} else {
					UI_Base.printMsg("NoUserFound");
				}
				
				break;
    		default:
    			UI_Base.printMsg("InvalidAction");
		}
	}
	
	public static void performMenuAction(String actionCommand, BankUser thisUser) { // for submenu actions
		String menuName = thisUser.onMenu();
		HashMap<String, String> thisMenuActions = getMenuActions(menuName);
		if (thisMenuActions != null) {
			UI_Base.printDebug("Menu exists");
			
			boolean validMenuAction = isValidMenuAction(menuName,actionCommand);
			if (validMenuAction) {
				final String quitKey = AppSettings.getQuitKey();
				
				UI_Base.printDebug("Menu action exists");
				
				switch(menuName) {
					case "UserMainMenu":
						
						switch(actionCommand) {
							case "c":
								System.out.printf("%n%n----- Account Details BEGIN-----%n%n"
										+ "%s %s%n\tBalance: $ %,5.2f%n"
										+ "\tEmail: %s%n"
										+ "%n%n----- Account Details END -----%n%n",
										thisUser.getFirstName(),
										thisUser.getLastName(),
										thisUser.getBalance(),
										(thisUser.getEmail().equals("")) ? "no email set" : thisUser.getEmail() );
								break;
							case "v":
								thisUser.setMenu("AccSettings");
								break;
							case "k":
								boolean saveSuccess = MongoDB.userSave(thisUser);
								if (saveSuccess) {
									thisUser.logOut();
									UI_Base.printMsg("LogoutSuccess",2,2);
								} else {
									thisUser.setMenu("LogoutFailed");
								}
								break;
							case "f":
								// add deposit field requirement
								// add new print messages
								UI_Base.print("---------- DEPOSIT BEGIN --------",1,1);
								UI_Base.printMsg("EnterDeposit");
								String depositAmount = input.nextLine();
								
								depositAmount = UI_Base.untilValidInput("deposit",depositAmount);
								if (depositAmount.equals(quitKey)) { UI_Base.print("---------- DEPOSIT END --------",1,1); UI_Base.printMsg("",1,1); break; }
								
								String[] args = depositAmount.split(" ");
								if (depositAmount.contains(" ") && args.length > 1) { // they have specified a currency
									//String[] args = depositAmount.split(" ");
									String convertFromCurrency = args[1];
									depositAmount = UI_Base.stripInputUntilNum(args[0]);
									
									if (CurrencyHandler.canConvertCurrency(convertFromCurrency)) {
										double conversionRate_inverse = CurrencyHandler.convertCurrency(Double.parseDouble(depositAmount), convertFromCurrency,"USD");
										double conversionRate = 1/conversionRate_inverse;
										double convertedAmount = conversionRate * Double.parseDouble(depositAmount);
										
										String formattedRate = String.format("%.2f",convertedAmount);
										double roundedRate = Double.parseDouble(formattedRate);
										
										//System.out.println(conversionRate+"\n"+convertedAmount+"\n"+formattedRate+"\n"+roundedRate);
										
										String thisMsg = String.format("Are you sure you want to deposit %s %s ( $%s dollar(s) )? (yes/no)",
												//CurrencyHandler.getCurrencySymbol(convertFromCurrency),
												depositAmount,
												CurrencyHandler.getCurrencyName(convertFromCurrency),
												roundedRate);
										
										UI_Base.print(thisMsg);
										if (input.nextLine().equals("yes")) {
											thisUser.deposit(roundedRate);
											MongoDB.userSave(thisUser);
										} else { UI_Base.lineBreak(2); }
									} else {
										UI_Base.printMsg("ENTRY_INVALCURR",1,1);
									};
								} else {
									depositAmount = UI_Base.stripInputUntilNum(depositAmount);
									thisUser.deposit(depositAmount);
									MongoDB.userSave(thisUser);
								}
								UI_Base.print("---------- DEPOSIT END --------",1,1);
								
								break;
							case "w":
								if (thisUser.canWithdraw()) { // can the user withdraw money?
									UI_Base.print("---------- WITHDRAW BEGIN --------",1,1);
									UI_Base.printMsg("EnterWithdraw");
									String withdrawAmount = input.nextLine();
									
									withdrawAmount = UI_Base.untilValidInput("withdraw",withdrawAmount);
									if (withdrawAmount.equals(quitKey)) { UI_Base.printMsg("",1,1); break; }
									
									String[] w_args = withdrawAmount.split(" ");
									if (withdrawAmount.contains(" ") && w_args.length > 1) { // they have specified a currency
										
										String convertFromCurrency = w_args[1];
										withdrawAmount = UI_Base.stripInputUntilNum(w_args[0]);
										
										if (CurrencyHandler.canConvertCurrency(convertFromCurrency)) {
											double conversionRate_inverse = CurrencyHandler.convertCurrency(Double.parseDouble(withdrawAmount), convertFromCurrency, "USD");
											double conversionRate = 1/conversionRate_inverse;
											double convertedAmount = conversionRate * Double.parseDouble(withdrawAmount);
											
											String formattedRate = String.format("%.2f",convertedAmount);
											double roundedRate = Double.parseDouble(formattedRate);
											
											//System.out.println(conversionRate+"\n"+convertedAmount+"\n"+formattedRate+"\n"+roundedRate);
											
											String thisMsg = String.format("Are you sure you want to withdraw %s %s ( $%s dollar(s) )? (yes/no)",
													//CurrencyHandler.getCurrencySymbol(convertFromCurrency),
													withdrawAmount,
													CurrencyHandler.getCurrencyName(convertFromCurrency),
													roundedRate);
											
											UI_Base.print(thisMsg);
											if (input.nextLine().equals("yes")) {
												thisUser.withdraw(roundedRate);
												MongoDB.userSave(thisUser);
											} else { UI_Base.lineBreak(2); }
										} else {
											UI_Base.printMsg("ENTRY_INVALCURR",1,1);
										};
									} else {
										withdrawAmount = UI_Base.stripInputUntilNum(withdrawAmount);
										thisUser.withdraw(withdrawAmount);
										MongoDB.userSave(thisUser);
									}
									
									/*
									withdrawAmount = UI_Base.stripInputUntilNum(withdrawAmount);
									thisUser.withdraw(withdrawAmount);
									MongoDB.userSave(thisUser);
									*/
									UI_Base.print("---------- WITHDRAW END --------",1,1);
								} else {
									UI_Base.printMsg("DebtWithdraw",2,2);
								}
								break;
							case "t":
								if (thisUser.canWithdraw()) { // can the user withdraw money
									System.out.println("\n----- MONEY TRANSFER BEGIN -----\n"
											+ "What is the username of the person you want to transfer money to:");
									String transferee;
									do {
										transferee = input.nextLine();
										System.out.print(!MongoDB.userExists(transferee, "db_users") ? "This user was not found." : "" );
									} while (!MongoDB.userExists(transferee, "db_users") && !transferee.equals(quitKey));
									
									if (MongoDB.userExists(transferee, "db_users") && !transferee.equals(quitKey)) { // user exists
										String amount;
										
										// error is thrown when user enters unaccounted for text that attempt to convert to double
										// solution: enclose all double conversions from user input in try/catch
										
										do {
											System.out.printf("Enter amount to transfer to user %s or <quit> to end transaction.\n",transferee);
											amount = input.nextLine();
											
											GUIHelper.EntryReqError entryMetReq = GUIHelper.fieldMeetsRequirements("transfer", amount);
											boolean isEntryErr = !entryMetReq.equals(GUIHelper.EntryReqError.ENTRY_JUST_RIGHT);
											
											amount = UI_Base.untilValidInput("transfer",amount);
											if (amount.equals(quitKey)) { UI_Base.printMsg("",1,1); break; }
											
											amount = UI_Base.stripInputUntilNum(amount);
											if (amount.equals(quitKey)) {
												break;
											} else {
												double realAmount = Double.parseDouble(amount);
												String msg;
												if (realAmount < 0) {
													msg = "Please enter an amount above 0.";
												} else if (thisUser.getBalance() - realAmount < 0) {
													msg = "You cannot transfer more than what you have.";
												} else {
													msg = "\nInitializing...";
												}
												System.out.println(msg);
											}
										} while (Double.parseDouble(amount) < 0 || (thisUser.getBalance()-Double.parseDouble(amount)) < 0);
										
										if (!amount.equals(quitKey)) {
											thisUser.transfer(Double.parseDouble(amount), transferee);
											MongoDB.userSave(thisUser);
										}
										
									} else if (!transferee.equals(quitKey)) {
										// should be a dead path
										System.out.println("User was not found. Try again or enter <quit> to end transaction.");
									}
									System.out.println("\n----- MONEY TRANSFER END -----");
								} else {
									UI_Base.printMsg("DebtWithdraw",2,2);
								}
								break;
							default:
								//
						}
						
						break;
					case "AccSettings":
						
						switch(actionCommand) {
							case "n": // go back to user main menu
								UI_Base.printMsg("EnterNewName");
								String newName = input.nextLine();
								lineBreak();
								
								newName = untilValidInput("newName", newName);
								
								if (newName.equals(quitKey)) { UI_Base.lineBreak(2); break; }
								
								String[] newNames = newName.split(" ");
								
								thisUser.setProperty("fName",newNames[0]);
								thisUser.setProperty("lName", newNames[1]);
								MongoDB.userSave(thisUser);
								
								lineBreak(2);
								
								break;
							case "e":
								thisUser.setMenu("UserMainMenu");
								UI_Base.lineBreak();
								break;
							case "k":
								UI_Base.printMsg("EnterEmail");
								String email = input.nextLine();
								//lineBreak();
								
								email = untilValidInput("email", email);
								
								if (email.equals(quitKey)) { UI_Base.lineBreak(2); break; }
								
								thisUser.setProperty("email", email);
								
								MongoDB.userSave(thisUser);
								
								break;
							case "p":
								// enter current pass
								UI_Base.printMsg("EnterNewPass",2,1);
								String newPass = input.nextLine();
								
								String thisMsg = String.format("Confirm new password [%s]? (yes/no)", newPass);
								UI_Base.print(thisMsg);
								if (input.nextLine().equals("yes")) {
									newPass = untilValidInput("pass", newPass);
									if (newPass.equals(quitKey)) { UI_Base.lineBreak(2); break; }
									
									thisUser.setProperty("pass", newPass);
									
									MongoDB.userSave(thisUser);
								} else { UI_Base.lineBreak(); }
								
								break;
							case "x":
								UI_Base.print("------- Account Deletion BEGIN -------");
								UI_Base.printMsg("ConfirmDelAcc",1,1);
								String thisPass = input.nextLine();
								
								if (!thisPass.equals(AppSettings.getQuitKey())) {
									if (thisPass.equals(thisUser.getPass())) {
										MongoDB.closeAccount(thisUser);
										UI_Base.printMsg("Goodbye");
										thisUser.logOut();
										UI_Base.lineBreak(3);
									}
									
								}
								UI_Base.print("------- Account Deletion END -------");
								break;
							default:
								//
						}
						
						break;
					case "LogoutFailed":
						
						switch(actionCommand) {
							case "t": // try to save their data anyways
								boolean saveSuccess = MongoDB.userSave(thisUser);
								if (saveSuccess) {
									thisUser.logOut();
									UI_Base.printMsg("LogoutSuccess",2,2);
								}
								break;
							case "v": // log out without saving
								thisUser.logOut();
								UI_Base.printMsg("LogoutSuccess",2,2);
								break;
							case "r": // back to user menu
								thisUser.setMenu("UserMainMenu");
								break;
							default:
								//
						}
						
						break;
					default: // no actions for this menu available
						//
				}
			}
		}
		
	}
	
	public static HashMap<String, String> getMenuActions(String menuName) { // get a menu's actions
		HashMap<String, String> thisMenuActions = menuActions.get(menuName);
		if (thisMenuActions != null) {
			return thisMenuActions;
		}
		return null;
	}
	
	//============ DATA VALIDATION ============//
	public static boolean isValidMenuAction(String menuName, String actionCommand) { // checks if this is a valid menu action
		HashMap<String, String> thisMenuActions = getMenuActions(menuName);
		if (thisMenuActions != null) { // menu exists
			
			for (String key : thisMenuActions.keySet()) { // iterate through key values in menu actions
				if (key.equals(actionCommand)) { // if this key is equal to action command
					return true; // yes, this command is valid
				}
			}
		}
		return false; // no, this command is not valid
	}
	
	public static EntryReqError fieldMeetsRequirements(String fieldName, String entry) {
		EntryReqError justRight = EntryReqError.ENTRY_JUST_RIGHT;
		EntryReqError invalidEmail = EntryReqError.ENTRY_INVALID_EMAIL;
		
		printDebug("Min entry chars: "+AppSettings.getMinEntryChar(fieldName));
		if (entry.equals("") && !AppSettings.fieldCanBeBlank(fieldName)) { return EntryReqError.NO_ENTRY; }
		else if (entry.equals(AppSettings.getQuitKey())) { return justRight; }
		else if (entry.length() < AppSettings.getMinEntryChar(fieldName) && !AppSettings.fieldCanBeBlank(fieldName)) { return EntryReqError.ENTRY_TOO_SHORT; }
		else if (entry.length() > AppSettings.getMaxEntryChar(fieldName)) { return EntryReqError.ENTRY_TOO_LONG; }
		else {
			switch(fieldName) {
				case "email":
					printDebug("Entry empty?"+entry.equals(""));
					printDebug("Field can be blank:"+String.valueOf(AppSettings.fieldCanBeBlank(fieldName)));
					if (entry.equals("")) {
						if (!AppSettings.fieldCanBeBlank(fieldName)) {
							return EntryReqError.ENTRY_INVALID_EMAIL;
						}
					} else {
						if (!entry.contains("@") || !entry.contains(".")) {
							return EntryReqError.ENTRY_INVALID_EMAIL;
						}
					}
					break;
				case "newName":
					if (entry.contains(" ")) { // they have specified a currency
						String[] args = entry.split(" ");
						if (!(args.length == 2)) {
							return EntryReqError.ENTRY_NO_BOTH_NAMES;
						}
					} else {
						return EntryReqError.ENTRY_NO_BOTH_NAMES;
					}
					break;
					//
				case "currency":
					if (!CurrencyHandler.canConvertCurrency(entry)) {
						return EntryReqError.ENTRY_INVALCURR;
					}
					break;
				case "deposit":
					if (entry.contains(" ")) {
						//String[] split = entry.split(" ");
						if (entry.split(" ").length > 1) {
							return justRight;
						} else {
							//return EntryReqError.NO_ENTRY;
						}
					} else {
						try {
							double thisdbl = Double.parseDouble(stripInputUntilNum(entry));
							//UI_Base.print(String.valueOf(thisdbl));
							if (thisdbl < 0) {
								return EntryReqError.ENTRY_BELOWZERO;
							}
						} catch(Exception e) {
							return EntryReqError.NO_ENTRY;
						}
					}
					break;
				case "startBal":
					if (entry.contains(" ")) {
						return EntryReqError.NO_ENTRY;
					} else {
						try {
							double thisdbl = Double.parseDouble(stripInputUntilNum(entry));
							//UI_Base.print(String.valueOf(thisdbl));
							if (thisdbl < 0) {
								return EntryReqError.ENTRY_NEGSTARTBAL;
							}
						} catch(Exception e) {
							return EntryReqError.NO_ENTRY;
						}
					}
					break;
				case "withdraw":
					if (entry.contains(" ")) {
						if (entry.split(" ").length > 1) {
							return justRight;
						} else {
							return EntryReqError.NO_ENTRY;
						}
					} else {
						try {
							double thisdbl = Double.parseDouble(stripInputUntilNum(entry));
							//UI_Base.print(String.valueOf(thisdbl));
							if (thisdbl < 0) {
								return EntryReqError.ENTRY_BELOWZERO;
							}
						} catch(Exception e) {
							return EntryReqError.NO_ENTRY;
						}
					}
					break;
				case "transfer":
					if (entry.contains(" ")) {
						return EntryReqError.NO_ENTRY;
					} else {
						try {
							double thisdbl = Double.parseDouble(stripInputUntilNum(entry));
							//UI_Base.print(String.valueOf(thisdbl));
							if (thisdbl < 0) {
								return EntryReqError.ENTRY_BELOWZERO;
							}
						} catch(Exception e) {
							return EntryReqError.NO_ENTRY;
						}
					}
					break;
				case "pass":
					if (entry.equals(AppSettings.getQuitKey())) { return EntryReqError.NO_ENTRY; }
				default:
					return justRight;
					//
			}
		}
		
		return justRight;
	}
	
	public static String untilValidInput(String fieldName, String value) {
		UI_Base.EntryReqError entryMetReq = UI_Base.fieldMeetsRequirements(fieldName, value);
		boolean isEntryErr = !entryMetReq.equals(UI_Base.EntryReqError.ENTRY_JUST_RIGHT);
		
		while (isEntryErr) {
			UI_Base.printMsg(entryMetReq.toString());
			UI_Base.printMsg("TryAgain");
			
			value = input.nextLine();
			entryMetReq = UI_Base.fieldMeetsRequirements(fieldName, value);
			isEntryErr = !UI_Base.fieldMeetsRequirements(fieldName, value).equals(UI_Base.EntryReqError.ENTRY_JUST_RIGHT);
		}
		
		return value;
	}
	
	public static boolean canStringToNum(String str) {
		if(str.equals("")) {
			return false;
		}
		
		String newStr = UI_Base.stripInputUntilNum(str);
		UI_Base.printDebug("Str: "+newStr);
		try {
			Double.parseDouble(newStr);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	//================ OUTPUT =================//
	public static void printMenu(String menuName) {
		HashMap<String, String> thisMenuActions = getMenuActions(menuName);
		if (!menuName.equals("Main")) {
			System.out.printf("%s%n", menuPrompts.get(menuName)); // printPrompt
		} else {
			System.out.printf("Welcome to %s!%n",getAppName());
		}
		
		for (String key : thisMenuActions.keySet()) { // iterate through key values in menu actions
			System.out.printf("\t%s : %s%n", key,thisMenuActions.get(key));
		}
	}
	
	// printMsg :: overloaded
	public static String getMsg(String msgName) {
		return messageLibrary.get(msgName);
	}
	
	public static void printMsg(String msgName, int newLinesBefore, int newLinesAfter) {
		String thisMsg = messageLibrary.get(msgName);
		if (thisMsg!=null) { // this message exists
			lineBreak(newLinesBefore);
			System.out.println(thisMsg);
			lineBreak(newLinesAfter);
		} else {
			System.out.printf("Message [%s] not found.%n",thisMsg);
		}
	}
	
	public static void printMsg(String msgName) {
		String thisMsg = messageLibrary.get(msgName);
		if (thisMsg!=null) { // this message exists
			System.out.println(thisMsg);
		} else {
			System.out.printf("Message [%s] not found.",thisMsg);
		}
	}
	
	public static void printDebug(String msg) {
		if (isDebugging) {
			System.out.printf(msg);
		}
	}
	
	public static void print(String toPrint) {
		System.out.println(toPrint);
	}
	
	public static void print(String toPrint,int linesBefore,int linesAfter) {
		lineBreak(linesBefore);
		System.out.println(toPrint);
		lineBreak(linesAfter);
	}
	
	// linebreak :: overloaded
	public static void lineBreak(int linesToBreak) {
		for (int i = 0; i < linesToBreak; i++) { System.out.println(); }
	}
	
	public static void lineBreak() {
		System.out.println();
	}
	
	//============== DEBUGGING ===============//
	
	public static void setDebugging(boolean isDebug) {
		isDebugging = isDebug;
	}
	
	public static String getAppName() {
		return AppSettings.getAppName();
	}
	
	//============ DATA FORMATTING ==============//d
	
	public static String stripInputUntilNum(String str) {
		//return str.replaceAll("[,/$^a-zA-Z_-]","");
		String newStr = stripSpaces(str);
		return newStr.replaceAll("[,/$^a-zA-Z_]","");
	}
	
	public static String stripSpaces(String str) {
		return str.replaceAll(" ","");
	}
	
	public static String formatNum(String money) {
		String moneyString = formatter.format(Double.parseDouble(money));
		return money;
	}
	
	public static String formatNum(double money) {
		String moneyString = formatter.format(money);
		return moneyString;
	}
	
	public static String constructPass(char[] passChars) {
		String pass = "";
		for (char thisChar : passChars) {
			pass += thisChar;
		}
		
		return pass;
	}
	
	public static HashMap<String,String> splitFullName(String fullName) {
		String[] newNames = fullName.split(" ");
		
		HashMap<String, String> newNamesMap = new HashMap<>(){{
			put("fName",newNames[0]);
			put("lName",newNames[1]);
		}};
		return newNamesMap;
	}
	
	public static String getFileText(File thisFile) {
		Scanner thisFileScanner;
		try {
			thisFileScanner = new Scanner(thisFile);
			String s = "";
			
			 while (thisFileScanner.hasNextLine()) {
				 s += thisFileScanner.nextLine();
			 }
			// construct string
		           
			 return s;
		} catch (Exception e) {
			UI_Base.print("could not load css file");
		} // scanner class
		return null;
	}
}
