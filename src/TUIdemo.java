package com.mybank.tui;
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import com.mybank.domain.CheckingAccount;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Alexander 'Taurus' Babich modified by Sofiia Zhabotynska
 */
public class TUIdemo3 extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;

    public static void main(String[] args) throws Exception {
        TUIdemo3 tdemo = new TUIdemo3();
        (new Thread(tdemo)).start();
    }

    public TUIdemo3() throws Exception {
        super(BackendType.SWING);

        addToolMenu();
        
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);
        

        addWindowMenu();

        
        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");
        

        setFocusFollowsMouse(true);
        
        loadCustomerData("data\\test.dat");
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich").show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            loadCustomerData("data\\test.dat");
            return true;
        }
        return super.onMenu(menu);
    }

    private void loadCustomerData(String fileName) {
        
         try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    int numAccounts = Integer.parseInt(parts[2]);
                    Bank.addCustomer(firstName, lastName);
                    for (int i = 0; i < numAccounts; i++) {
                        line = reader.readLine();
                        parts = line.split("\t");
                        if (parts[0].equals("S")) {
                            double balance = Double.parseDouble(parts[1]);
                            double interestRate = parts.length >= 3 ? Double.parseDouble(parts[1]) : 0.0;
                            Bank.getCustomer(Bank.getNumberOfCustomers() - 1).addAccount(new SavingsAccount(balance, interestRate));
                        } else if (parts[0].equals("C")) {
                            double balance = Double.parseDouble(parts[1]);
                            double overdraftLimit = parts.length >= 3 ? Double.parseDouble(parts[2]) : 0.0; 
                            Bank.getCustomer(Bank.getNumberOfCustomers() - 1).addAccount(new CheckingAccount(balance, overdraftLimit));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        TWindow custWin = addWindow("Customer Window", 2, 1, 40, 10, TWindow.NOZOOMBOX);
        custWin.newStatusBar("Enter valid customer number and press Show...");

        custWin.addLabel("Enter customer number: ", 2, 2);
        TField custNo = custWin.addField(24, 2, 3, false);
        TText details = custWin.addText("Owner Name: \nAccount Type: \nAccount Balance: ", 2, 4, 38, 8);
        
        custWin.addButton("&Show", 28, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    int custNum = Integer.parseInt(custNo.getText());
                    Customer customer = Bank.getCustomer(custNum);
                    Account account = customer.getAccount(0);
                    //details about customer with index==custNum
                    String accType = customer.getAccount(0) instanceof CheckingAccount ? "Checking" : "Savings";
                    details.setText("Owner Name: "+customer.getFirstName()+ " " + customer.getLastName() +"(id="+custNum+")\nAccount Type: "+accType+"\nAccount Balance: "+account.getBalance());
                } catch (Exception e) {
                    messageBox("Error", "You must provide a valid customer number!").show();
                }
            }
        });
    }
}