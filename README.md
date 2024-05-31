# UI Lab 1
![](terminal-icon.png)
![](gui-icon.png)

Це одна з робіт, які доповнюють основний цикл лабораторних робіт #1-8 (проект **Banking**, [Netbeans](https://netbeans.org/)) з ООП.  Основна мета цих додаткових вправ - познайомитись з різними видами інтерфейсів користувача та засобами їх створення. Згадувані 'базові' роботи розміщено в [окремому репозиторії](https://github.com/liketaurus/OOP-JAVA) (якщо будете робити завдання на "4" або "5" раджу переглянути [діаграму класів](https://github.com/liketaurus/OOP-JAVA/blob/master/MyBank.png), аби розуміти які методи є у класів).

В ході першої роботи вам пропонується виконати **наступне завдання** - [Робота 1: TUI з Jexer](https://github.com/ppc-ntu-khpi/TUI-Lab1-Starter/blob/master/Lab%201%20-TUI/Lab%201.md)
  
**Додаткове завдання** (для тих хто зробив все і прагне більшого): [дивіться тут](https://github.com/ppc-ntu-khpi/TUI-Lab1-Starter/blob/master/Lab%201%20-TUI/Lab%201%20-%20add.md)

Всі необхідні бібліотеки містяться у теці [jars](https://github.com/ppc-ntu-khpi/TUI-Lab1-Starter/tree/master/jars). В тому числі - всі необхідні відкомпільовані класи з робіт 1-8 - файл [MyBank.jar](https://github.com/ppc-ntu-khpi/TUI-Lab1-Starter/blob/master/jars/MyBank.jar). Файл даних лежить у теці [data](https://github.com/ppc-ntu-khpi/TUI-Lab1-Starter/tree/master/data).

---
**УВАГА! Не забуваємо здавати завдання через Google Classroom та вказувати посилання на створений для вас репозиторій!**

Також пам'ятайте, що ніхто не заважає вам редагувати файл README у вашому репозиторії😉.
А ще - дуже раджу спробувати нову фічу - інтеграцію з IDE REPL.it (хоч з таким завданням вона може й не впоратись, однак, цікаво ж!).

![](https://img.shields.io/badge/Made%20with-JAVA-red.svg)
![](https://img.shields.io/badge/Made%20with-%20Netbeans-brightgreen.svg)
![](https://img.shields.io/badge/Made%20at-PPC%20NTU%20%22KhPI%22-blue.svg) 

## Завдання на 3 бали
Скриншот запущеної програми:

![](screenshots/screen_for_3.png)

## Завдання на 4 бали

Відредагований код, що містить TDUdemo.java:

```java
public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;

    /**
     *
     * Точка входу в програму. Створює екземпляр TUIdemo і запускає його в
     * окремому потоці.
     *
     * @param args аргументи командного рядка
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    /**
     *
     * Конструктор класу TUIdemo. Встановлює тип бекенду для Jexer на SWING.
     * Ініціалізує тестові дані про клієнтів та їхні рахунки.
     *
     * @throws Exception
     */
    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        // Ініціалізація тестових даних клієнтів
        Bank.addCustomer("Rick", "Deckard");
        Bank.addCustomer("Joe", "K");
        Bank.addCustomer("Rachael", "");
        Bank.addCustomer("Joy", "Real");

        // Ініціалізація тестових даних рахунків
        Customer rick = Bank.getCustomer(0);
        rick.addAccount(new CheckingAccount(150.00));

        Customer joe = Bank.getCustomer(1);
        joe.addAccount(new SavingsAccount(2.00, 0.02));

        Customer joy = Bank.getCustomer(3);
        joy.addAccount(new SavingsAccount(2456.00, 0.07));

        Customer rachael = Bank.getCustomer(2);
        rachael.addAccount(new CheckingAccount(42.00, 0.06));

        // Створення стандартних меню
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

        // Відображення вікна з деталями клієнта
        ShowCustomerDetails();
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich").show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            ShowCustomerDetails();
            return true;
        }
        return super.onMenu(menu);
    }

    /**
     *
     * Відображає вікно для введення номера клієнта та показує його детальну
     * інформацію.
     */
    private void ShowCustomerDetails() {
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

                    Customer customer = Bank.getBank().getCustomer(custNum);

                    if (customer != null) {

                        Account account = customer.getAccount(0);

                        String accountType = account instanceof CheckingAccount ? "Checking" : "Savings";

                        details.setText("Owner Name: " + customer.getFirstName() + " " + customer.getLastName() + " (id=" + custNum + ")\n"
                                + "Account Type: " + accountType + "\n"
                                + "Account Balance: $" + account.getBalance());
                    } else {
                        messageBox("Error", "Customer with number " + custNum + " not found!").show();
                    }
                } catch (Exception e) {
                    messageBox("Error", "You must provide a valid customer number!").show();
                }
            }
        });
    }
}
```
![](screenshots/screenshot_for_4.1.png)
![](screenshots/screenshot_for_4.2.png)
![](screenshots/screenshot_for_4.3.png)
![](screenshots/screenshot_for_4.4.png)
