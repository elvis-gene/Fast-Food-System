/*
    Elvis Presley Gene (217304338)
    Nico Fortuin (216237912)
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import com.sun.speech.freetts.*;

public class FastFoodGui extends JFrame{

    private ArrayList<FoodMenuItem> totalSalesList = new ArrayList<>();

    private static final String VOICENAME = "kevin16";
    private String salesReport;
    private String costumerReport;

    private JComboBox jComboBoxStarter;
    private JComboBox jComboBoxMain;
    private JComboBox jComboBoxDrinks;

    private Costumer costumer = new Costumer();
    private ArrayList<FoodMenuItem> costumerOrderList = new ArrayList<>();

    private ArrayList<FoodMenuItem> menuItems = new ArrayList<>(Arrays.asList(
            new FoodMenuItem("Cheese burger", "main meal",11.50, null),
            new FoodMenuItem("Gatsby", "main meal", 8.50,null),
            new FoodMenuItem("Cappuccino", "drinks", 10.25,null),
            new FoodMenuItem("Hot wings", "starter", 6.00,null),
            new FoodMenuItem("Fish and chips" , "main meal", 9.50,null),
            new FoodMenuItem("Coffee", "drinks",5.00,null),
            new FoodMenuItem("Calamari", "starter", 5.50,null)));

    private ArrayList<FoodMenuItem> startersList = new ArrayList<>();
    private ArrayList<FoodMenuItem> drinksList = new ArrayList<>();
    private ArrayList<FoodMenuItem> mainList = new ArrayList<>();

    JPanel tabsPanel = new JPanel();
    JTabbedPane tabs = new JTabbedPane();
    private JTable drinksListTable = new JTable();
    private JTable startersListTable = new JTable();
    private JTable mainListTable = new JTable();
    private DefaultTableModel modelDrinksTable;
    private DefaultTableModel modelStartersTable;
    private DefaultTableModel modelMainTable;

    private JPanel bottomPanel = new JPanel();

    private JPanel topPanel = new JPanel();

    //Menu
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Options");
    private JMenuItem mAddFood = new JMenuItem("Add Food Item");
    private JMenuItem mEditItem = new JMenuItem("Edit Food Item");
    private JMenuItem mDeleteFood = new JMenuItem("Delete Food Item");
    private JMenuItem mSortFood = new JMenuItem("Sort");
    private JMenuItem mSaleTrans = new JMenuItem("Sale Transaction");
    private JMenuItem mSaleReport = new JMenuItem("Sale Report");
    private JMenuItem mExit = new JMenuItem("Exit");

    //Buttons
    private JButton checkout = new JButton("CHECKOUT");
    private JButton addToOrder = new JButton("ADD TO ORDER");

    //Time
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now;

    public FastFoodGui()
    {
        super("Joe's Joint");

        addTopPanel();
        createTables();
        createAndAddTabs();
        createMenu();
        createBottomButtons();

        addFoodItemsToTabs();
        runMenuOptions();
        sortFoodItems();

        implementBottomButtons();

        for (int i = 0; i < modelStartersTable.getRowCount(); i++) {
            modelStartersTable.setValueAt(false,i,0);
        }
        for (int i = 0; i < modelDrinksTable.getRowCount(); i++) {
            modelDrinksTable.setValueAt(false,i,0);
        }
        for (int i = 0; i < modelMainTable.getRowCount(); i++) {
            modelMainTable.setValueAt(false,i,0);
        }

        checkout.setEnabled(false);
        mSaleReport.setEnabled(false);
    }


        public void addTopPanel(){
        ImageIcon logo  = new ImageIcon(getClass().getResource("images\\logo.png"));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setSize(100,100);

        topPanel.add(imageLabel);
        topPanel.setLayout(new FlowLayout());
        add(topPanel, BorderLayout.NORTH);
        }

    public void createAndAddTabs()
         {
             tabsPanel.setLayout(new BorderLayout());
             //Tab 1
             JScrollPane drinksTab = new JScrollPane(drinksListTable);
             //Tab 2
             JScrollPane startersTab = new JScrollPane(startersListTable);
             //Tab 3
             JScrollPane mainTab = new JScrollPane(mainListTable);

             tabs.addTab("Drinks", drinksTab);
             tabs.addTab("Starters", startersTab);
             tabs.add("Main", mainTab);
             tabs.setTabPlacement(SwingConstants.RIGHT);

             tabsPanel.add(tabs);

             add(tabsPanel, BorderLayout.CENTER);
         }

         public void createTables()
         {
             //Table Model definition for the drinks table
             modelDrinksTable = new DefaultTableModel(){
                 @Override
                 public Class<?> getColumnClass(int columnIndex) {
                     //return super.getColumnClass(columnIndex);
                     if (columnIndex == 0)
                         return Boolean.class;
                     else if (columnIndex == 1)
                         return String.class;
                     else if (columnIndex == 2)
                         return String.class;
                     else // replaced by a Combo Box later
                         return String.class;
                 }

                 // Make the price and food name columns uneditable
                 @Override
                 public boolean isCellEditable(int row, int column){
                     return column == 0 || column == 3;
                 }
             };

             //Table Model definition for the starters table
             modelStartersTable = new DefaultTableModel(){
                 @Override
                 public Class<?> getColumnClass(int columnIndex) {
                     //return super.getColumnClass(columnIndex);
                     if (columnIndex == 0)
                         return Boolean.class;
                     else if (columnIndex == 1)
                         return String.class;
                     else if (columnIndex == 2)
                         return String.class;
                     else // replaced by a Combo Box later
                         return String.class;
                 }

                 @Override
                 public boolean isCellEditable(int row, int column){
                     return column == 0 || column == 3;
                 }
             };

             //Table Model definition for the main meal table
             modelMainTable = new DefaultTableModel(){
                 @Override
                 public Class<?> getColumnClass(int columnIndex) {
                     //return super.getColumnClass(columnIndex);
                     if (columnIndex == 0)
                         return Boolean.class;
                     else if (columnIndex == 1)
                         return String.class;
                     else if (columnIndex == 2)
                         return String.class;
                     else // replaced by a Combo Box later
                         return String.class;
                 }

                 @Override
                 public boolean isCellEditable(int row, int column){
                     // Run through list for the number of items to know the number of row and just use two columns 1 and 2
                     return column == 0 || column == 3;
                 }
             };

             // Adding columns titles to each table
             modelDrinksTable.addColumn("Select");
             modelDrinksTable.addColumn("Item");
             modelDrinksTable.addColumn("Price in R");
             modelDrinksTable.addColumn("Quantity (click & chose)");

             modelStartersTable.addColumn("Select");
             modelStartersTable.addColumn("Item");
             modelStartersTable.addColumn("Price in R");
             modelStartersTable.addColumn("Quantity (click & chose)");

             modelMainTable.addColumn("Select");
             modelMainTable.addColumn("Item");
             modelMainTable.addColumn("Price in R");
             modelMainTable.addColumn("Quantity (click & chose)");

             drinksListTable.setModel(modelDrinksTable);
             startersListTable.setModel(modelStartersTable);
             mainListTable.setModel(modelMainTable);


             // Creating the combo box
             String [] quantityNumbers = {"1","2","3","4","5","6","7","8","9","Enter number"};
             jComboBoxStarter = new JComboBox<>(quantityNumbers);
             jComboBoxDrinks = new JComboBox<>(quantityNumbers);
             jComboBoxMain = new JComboBox<>(quantityNumbers);

             // Editing the last column from the table model and replacing it with a this combo box
             TableColumn columnDrinksTable = drinksListTable.getColumnModel().getColumn(3);
             columnDrinksTable.setCellEditor(new DefaultCellEditor(jComboBoxDrinks));

             TableColumn columnStartersTable = startersListTable.getColumnModel().getColumn(3);
             columnStartersTable.setCellEditor(new DefaultCellEditor(jComboBoxStarter));

             TableColumn columnMainTable = mainListTable.getColumnModel().getColumn(3);
             columnMainTable.setCellEditor(new DefaultCellEditor(jComboBoxMain));

            //Hide table grids
            drinksListTable.setShowGrid(false);
            startersListTable.setShowGrid(false);
            mainListTable.setShowGrid(false);

             // Center strings in the cells
             DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
             centerRenderer.setHorizontalAlignment( JLabel.CENTER );
             drinksListTable.setDefaultRenderer(String.class, centerRenderer);
             startersListTable.setDefaultRenderer(String.class, centerRenderer);
             mainListTable.setDefaultRenderer(String.class, centerRenderer);
         }


         public void createMenu(){
             //Adding MenuBar to frame
             setJMenuBar(menuBar);

             //Add to menu
             menu.add(mAddFood);
             menu.add(mDeleteFood);
             menu.add(mSortFood);
             menu.add(mSaleTrans);
             menu.add(mSaleReport);
             menu.add(mExit);

             //Add menu to menu bar
             menuBar.add(menu);
         }

         public void createBottomButtons(){
             //Add bottom panel
             bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
             bottomPanel.add(addToOrder);
             bottomPanel.add(checkout);
             add(bottomPanel, BorderLayout.SOUTH);
         }


         // Add food items to the tabs
         public void addFoodItemsToTabs() {
             int itemListSize = menuItems.size();

             // Separate the main array list to three array lists per category
             for (int i = 0; i < itemListSize; i++) {
                 if (menuItems.get(i).getCategory().equalsIgnoreCase("drinks")){
                     drinksList.add(menuItems.get(i));
                 }
                 else if (menuItems.get(i).getCategory().equalsIgnoreCase("main meal")) {
                     mainList.add(menuItems.get(i));
                 }
                 else
                     startersList.add(menuItems.get(i));
             }

             // add each items of a specific category to its table
             for (int i = 0; i < drinksList.size(); i++){
                 modelDrinksTable.addRow(new Object[i]);

                 modelDrinksTable.setValueAt(drinksList.get(i).getFoodItem(), i, 1);
                 modelDrinksTable.setValueAt(drinksList.get(i).getPrice(), i, 2);
                 modelDrinksTable.setValueAt(jComboBoxDrinks.getItemAt(0), i, 3);
             }

             for (int i = 0; i < startersList.size(); i++){
                 modelStartersTable.addRow(new Object[i]);

                 modelStartersTable.setValueAt(startersList.get(i).getFoodItem(), i, 1);
                 modelStartersTable.setValueAt(startersList.get(i).getPrice(), i, 2);
                 modelStartersTable.setValueAt(jComboBoxStarter.getItemAt(0), i, 3);
             }

             for (int i = 0; i < mainList.size(); i++) {
                 modelMainTable.addRow(new Object[i]);

                 modelMainTable.setValueAt(mainList.get(i).getFoodItem(), i, 1);
                 modelMainTable.setValueAt(mainList.get(i).getPrice(), i, 2);
                 modelMainTable.setValueAt(jComboBoxMain.getItemAt(0), i, 3);
             }

         }



         public void runMenuOptions(){

            mAddFood.addActionListener((ActionEvent e)-> {

                // Creating a new frame for category selection
                JFrame addFoodItemWindow = new JFrame("Select category");
                addFoodItemWindow.setSize(300,80);
                JPanel choiceComboBoxPanel = new JPanel(new BorderLayout());

                JComboBox categories = new JComboBox<>(new Object[]{"Choose Category","Starter","Main Meal","Drink"});
                choiceComboBoxPanel.add(categories, BorderLayout.CENTER);
                addFoodItemWindow.add(choiceComboBoxPanel);

                addFoodItemWindow.setVisible(true);
                // Placing the new Frame in the middle of the screen, making it be in the middle of the bigger frame
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                addFoodItemWindow.setLocation(dim.width/2-addFoodItemWindow.getSize().width/2, dim.height/2-addFoodItemWindow.getSize().height/2);

                // Allowing the user to cancel/ close the category choice
               addFoodItemWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                   @Override
                   public void windowClosing(WindowEvent e) {
                        addFoodItemWindow.dispose();
                   }

               });

               categories.addItemListener((ItemEvent i)-> {
                   if (i.getStateChange() == ItemEvent.SELECTED) {

                       if (categories.getSelectedIndex() == 1) { // Index 1 is for Starters
                           // closing category choice window before inputting the new item's details
                           addFoodItemWindow.dispose();

                           try {
                               String itemName = JOptionPane.showInputDialog(null, "Enter name of the " + (String) categories.getSelectedItem());

                               int ind = -1;
                               for (int k = 0; k < modelStartersTable.getRowCount(); k++){
                                   if (itemName.equalsIgnoreCase((modelStartersTable.getValueAt(k,1)).toString()))
                                   ind = k;
                               }

                               if (ind != -1){
                                   JOptionPane.showMessageDialog(null, "Item "+ itemName +" already exists");
                               }

                               else {
                                   boolean badInput = true;
                                   do {
                                       try {
                                           double priceOfItem = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter price of " + itemName));

                                           //Adding new Item into the array list.
                                           menuItems.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));
                                           startersList.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));
                                           //Adding the new food item to its category table
                                           modelStartersTable.addRow(new Object[startersList.size() - 1]);
                                           modelStartersTable.setValueAt(false, startersList.size() - 1, 0);
                                           modelStartersTable.setValueAt(itemName, startersList.size() - 1, 1);
                                           modelStartersTable.setValueAt(priceOfItem, startersList.size() - 1, 2);
                                           modelStartersTable.setValueAt(jComboBoxStarter.getItemAt(0), startersList.size() - 1, 3);
                                           badInput = false;
                                       }catch (NumberFormatException n){
                                           JOptionPane.showMessageDialog(null, "Item price should be a number");
                                       }
                                   }while (badInput);
                               }
                           }catch (NullPointerException n){
                               JOptionPane.showMessageDialog(null, "Item details must be entered");
                           }


                       } else if (categories.getSelectedIndex() == 2) { // Index 2 is for Main meals
                           // closing category choice window before inputting the new item's details
                           addFoodItemWindow.dispose();

                           try {
                               String itemName = JOptionPane.showInputDialog(null, "Enter name of the " + (String) categories.getSelectedItem());

                               int ind = -1;
                               for (int k = 0; k < modelMainTable.getRowCount(); k++) {
                                   if (itemName.equalsIgnoreCase((modelMainTable.getValueAt(k, 1)).toString()))
                                   ind = k;
                               }

                               if (ind != -1) {
                                   JOptionPane.showMessageDialog(null, "Item " + itemName + " already exists");
                               } else {
                                   boolean badInput = true;
                                   do {
                                       try {
                                           double priceOfItem = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter price of " + itemName));

                                           //Adding new Item into the array list.
                                           menuItems.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));
                                           mainList.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));

                                           //Adding the new food item to its category table
                                           modelMainTable.addRow(new Object[mainList.size() - 1]);
                                           modelMainTable.setValueAt(false, mainList.size() - 1, 0);
                                           modelMainTable.setValueAt(itemName, mainList.size() - 1, 1);
                                           modelMainTable.setValueAt(priceOfItem, mainList.size() - 1, 2);
                                           modelMainTable.setValueAt(jComboBoxMain.getItemAt(0), mainList.size() - 1, 3);
                                           badInput = false;
                                       }catch (NumberFormatException n){
                                           JOptionPane.showMessageDialog(null, "Item price should be a number");
                                       }
                                   }while (badInput);
                               }
                           }catch (NullPointerException n){
                               JOptionPane.showMessageDialog(null, "Item details must be entered");
                           }
                       }

                       else {    // Index 3 is for Drinks
                           // closing category choice window before inputting the new item's details
                           addFoodItemWindow.dispose();

                           try {
                               String itemName = JOptionPane.showInputDialog(null, "Enter name of the " + (String) categories.getSelectedItem());

                               int ind = -1;
                               for (int k = 0; k < modelDrinksTable.getRowCount(); k++) {
                                   if (itemName.equalsIgnoreCase((modelDrinksTable.getValueAt(k, 1)).toString()))
                                       ind = k;
                               }

                               if (ind != -1) {
                                   JOptionPane.showMessageDialog(null, "Item " + itemName + " already exists");
                               } else {
                                   boolean badInput = true;
                                   do {
                                       try {
                                           double priceOfItem = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter price of " + itemName));

                                           //Adding new Item into the array list.
                                           menuItems.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));
                                           drinksList.add(new FoodMenuItem(itemName, (String) categories.getSelectedItem(), priceOfItem, null));

                                           //Adding the new food item to its category table
                                           modelDrinksTable.addRow(new Object[drinksList.size() - 1]);
                                           modelDrinksTable.setValueAt(false, drinksList.size() - 1, 0);
                                           modelDrinksTable.setValueAt(itemName, drinksList.size() - 1, 1);
                                           modelDrinksTable.setValueAt(priceOfItem, drinksList.size() - 1, 2);
                                           modelDrinksTable.setValueAt(jComboBoxDrinks.getItemAt(0), drinksList.size() - 1, 3);
                                           badInput = false;
                                       }catch (NumberFormatException n){
                                           JOptionPane.showMessageDialog(null, "Item price should be a number");
                                       }
                                   }while (badInput);
                               }
                           } catch (NullPointerException n) {
                               JOptionPane.showMessageDialog(null, "Item details should be entered");
                           }
                       }
                   }
               });

            });

            mEditItem.addActionListener((ActionEvent e)->{

            });


            // Delete option from menu selected
            mDeleteFood.addActionListener((ActionEvent e)-> {
                // New frame
                    JFrame deleteFoodItemWindow = new JFrame("Select category");
                    deleteFoodItemWindow.setSize(300,80);
                    JPanel choiceComboBoxPanel = new JPanel(new BorderLayout());

                    JComboBox categories = new JComboBox<>(new Object[]{"Choose Category","Starters","Main Meals","Drinks"});
                    choiceComboBoxPanel.add(categories, BorderLayout.CENTER);
                    deleteFoodItemWindow.add(choiceComboBoxPanel);

                    deleteFoodItemWindow.setVisible(true);
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    deleteFoodItemWindow.setLocation(dim.width/2-deleteFoodItemWindow.getSize().width/2, dim.height/2-deleteFoodItemWindow.getSize().height/2);

                    deleteFoodItemWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            deleteFoodItemWindow.dispose();
                        }

                    });

                    categories.addItemListener((ItemEvent i)-> {
                        if (i.getStateChange() == ItemEvent.SELECTED) {
                            if (categories.getSelectedIndex() == 1) {
                                // closing category choice window before inputting the new item's details
                                deleteFoodItemWindow.dispose();

                                JFrame deleteSpecificFoodItemWindow = new JFrame("Select food item");
                                deleteFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                deleteSpecificFoodItemWindow.setLocation(dim.width/2-deleteFoodItemWindow.getSize().width/2, dim.height/2-deleteFoodItemWindow.getSize().height/2);

                                JPanel deleteSpecificItemComboBoxPanel = new JPanel(new BorderLayout());
                                JComboBox <String> starterItems = new JComboBox<>();

                                // Adding all food items into combo box (Starter category)
                                starterItems.addItem("Choice Item");
                                for (int j = 0; j < startersList.size(); j++) {
                                    starterItems.addItem(startersList.get(j).getFoodItem());
                                }

                                // Adding combo box to new the new frame
                                deleteSpecificFoodItemWindow.add(starterItems, BorderLayout.CENTER);
                                deleteFoodItemWindow.add(deleteSpecificItemComboBoxPanel);

                                deleteSpecificFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
                                deleteFoodItemWindow.setLocation(dimen.width/2-deleteFoodItemWindow.getSize().width/2, dimen.height/2-deleteFoodItemWindow.getSize().height/2);

                                deleteFoodItemWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        deleteSpecificFoodItemWindow.dispose();
                                    }

                                });

                                // Deleting the selected food item from the array lists and from the Table
                               starterItems.addItemListener((ItemEvent ie)-> {
                                   if (ie.getStateChange() == ItemEvent.SELECTED) {
                                       int index  = 0;

                                       for (int a = 0; a < startersList.size(); a++) {
                                           if (starterItems.getSelectedItem() == startersList.get(a).getFoodItem()){
                                               index = a;
                                           }
                                       }
                                       if (startersList.size() != 0)
                                       startersList.remove(index);

                                       int indexB = 0;
                                       for (int b = 0; b < menuItems.size(); b++){
                                           if (starterItems.getSelectedItem() == menuItems.get(b).getFoodItem()){
                                               indexB = b;
                                           }
                                       }
                                       if (menuItems.size() != 0)
                                       menuItems.remove(indexB);

                                       // Deleting deleted item from the table
                                       int c = 1;//columns
                                       int indexR = -1;
                                       for (int r = 0; r < modelStartersTable.getRowCount(); r++) {//rows
                                          if ((String)(modelStartersTable.getValueAt(r,c)) == starterItems.getSelectedItem()){
                                              indexR = r;
                                          }
                                       }
                                       if (indexR != -1)
                                       modelStartersTable.removeRow(indexR);
                                   }
                               });
                            }// End of the Starter category for deletion

                            else if (categories.getSelectedIndex() == 2) {  //Deletion of an item from the Main Meal list
                                deleteFoodItemWindow.dispose();

                                JFrame deleteSpecificFoodItemWindow = new JFrame("Select food item");
                                deleteFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                deleteSpecificFoodItemWindow.setLocation(dim.width/2-deleteFoodItemWindow.getSize().width/2, dim.height/2-deleteFoodItemWindow.getSize().height/2);

                                JPanel deleteSpecificItemComboBoxPanel = new JPanel(new BorderLayout());
                                JComboBox <String> mainItems = new JComboBox<>();

                                mainItems.addItem("Choice Item");
                                for (int j = 0; j < mainList.size(); j++) {
                                    mainItems.addItem(mainList.get(j).getFoodItem());
                                }

                                deleteSpecificFoodItemWindow.add(mainItems, BorderLayout.CENTER);
                                deleteFoodItemWindow.add(deleteSpecificItemComboBoxPanel);

                                deleteSpecificFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
                                deleteFoodItemWindow.setLocation(dimen.width/2-deleteFoodItemWindow.getSize().width/2, dimen.height/2-deleteFoodItemWindow.getSize().height/2);

                                deleteFoodItemWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        deleteSpecificFoodItemWindow.dispose();
                                    }

                                });

                                // Combo box list for main meals
                                mainItems.addItemListener((ItemEvent ie)-> {
                                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                                        int index  = 0;

                                        for (int a = 0; a < mainList.size(); a++) {
                                            if (mainItems.getSelectedItem() == mainList.get(a).getFoodItem()){
                                                index = a;
                                            }
                                        }
                                        if (mainList.size() != 0)
                                            mainList.remove(index);

                                        int indexB = 0;
                                        for (int b = 0; b < menuItems.size(); b++){
                                            if (mainItems.getSelectedItem() == menuItems.get(b).getFoodItem()){
                                                indexB = b;
                                            }
                                        }
                                        if (menuItems.size() != 0)
                                            menuItems.remove(indexB);

                                        // Deleting deleted item from the table
                                        int c = 1;//columns
                                        int indexR = -1;
                                        for (int r = 0; r < modelMainTable.getRowCount(); r++) {//rows
                                            if ((String)(modelMainTable.getValueAt(r,c)) == mainItems.getSelectedItem()){
                                                indexR = r;
                                            }
                                        }
                                        if (indexR != -1)
                                        modelMainTable.removeRow(indexR);
                                    }
                                });
                            }

                            else{ // Deletion of drinks
                                deleteFoodItemWindow.dispose();

                                JFrame deleteSpecificFoodItemWindow = new JFrame("Select drink");
                                deleteFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                deleteSpecificFoodItemWindow.setLocation(dim.width/2-deleteFoodItemWindow.getSize().width/2, dim.height/2-deleteFoodItemWindow.getSize().height/2);

                                JPanel deleteSpecificItemComboBoxPanel = new JPanel(new BorderLayout());
                                JComboBox <String> drinkItems = new JComboBox<>();

                                drinkItems.addItem("Choice Item");
                                for (int j = 0; j < drinksList.size(); j++) {
                                    drinkItems.addItem(drinksList.get(j).getFoodItem());
                                }

                                deleteSpecificFoodItemWindow.add(drinkItems, BorderLayout.CENTER);
                                deleteFoodItemWindow.add(deleteSpecificItemComboBoxPanel);

                                deleteSpecificFoodItemWindow.setSize(300,80);
                                deleteSpecificFoodItemWindow.setVisible(true);
                                Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
                                deleteFoodItemWindow.setLocation(dimen.width/2-deleteFoodItemWindow.getSize().width/2, dimen.height/2-deleteFoodItemWindow.getSize().height/2);

                                deleteFoodItemWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        // closing second window to add item
                                        deleteSpecificFoodItemWindow.dispose();
                                    }

                                });

                                // Combo box list for drinks
                                drinkItems.addItemListener((ItemEvent ie)-> {
                                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                                        int index  = 0;

                                        for (int a = 0; a < startersList.size(); a++) {
                                            if (drinkItems.getSelectedItem() == startersList.get(a).getFoodItem()){
                                                index = a;
                                            }
                                        }
                                        if (drinksList.size() != 0)
                                            drinksList.remove(index);

                                        int indexB = 0;
                                        for (int b = 0; b < menuItems.size(); b++){
                                            if (drinkItems.getSelectedItem() == menuItems.get(b).getFoodItem()){
                                                indexB = b;
                                            }
                                        }
                                        if (menuItems.size() != 0)
                                            menuItems.remove(indexB);

                                        // Deleting deleted item from the table
                                        int c = 1;//columns
                                        int indexR = -1;
                                        for (int r = 0; r < modelDrinksTable.getRowCount(); r++) {//rows
                                            if ((String)(modelDrinksTable.getValueAt(r,c)) == drinkItems.getSelectedItem()){
                                                indexR = r;
                                            }
                                        }
                                        if (indexR != -1)
                                        modelDrinksTable.removeRow(indexR);
                                    }
                                });

                            }
                        }
                    });

                });




            mSaleTrans.addActionListener((ActionEvent e)->{
                //New frame
                JFrame selectSaleCategory = new JFrame("Select category");
                selectSaleCategory.setSize(300,80);
                JPanel choiceComboBoxPanel = new JPanel(new BorderLayout());

                JComboBox categories = new JComboBox<>(new Object[]{"Choose Category","Starters","Main Meals","Drinks"});
                choiceComboBoxPanel.add(categories, BorderLayout.CENTER);
                selectSaleCategory.add(choiceComboBoxPanel);

                selectSaleCategory.setVisible(true);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                selectSaleCategory.setLocation(dim.width/2-selectSaleCategory.getSize().width/2, dim.height/2-selectSaleCategory.getSize().height/2);

                sortFoodItems();

                categories.addItemListener((ItemEvent i)-> {
                    if (i.getStateChange() == ItemEvent.SELECTED) {

                        if (categories.getSelectedIndex() == 1) { // Index 1 is for Starters
                            // closing category choice window before inputting the new item's details
                            selectSaleCategory.dispose();

                            JFrame salesList = new JFrame("Select starter");
                            salesList.setSize(200,300);
                            salesList.setLocation(dim.width/2-salesList.getSize().width/2, dim.height/2-salesList.getSize().height/2);


                            // JList diplaying the list of sorted items
                            Object [] startersArray = startersList.toArray();
                            JList<FoodMenuItem> jList = new JList(startersArray);
                            salesList.getContentPane().add(new JScrollPane(jList));
                            salesList.setVisible(true);

                            // Implementing the clicking on an item
                            jList.addListSelectionListener((ListSelectionEvent l)-> {
                                        if (!l.getValueIsAdjusting()) {
                                            int index = jList.getSelectedIndex();

                                            try {
                                                int quantity = 0;
                                                quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));
                                                Sale sale = new Sale();

                                                // Increment quantity
                                                if (quantity != 0)
                                                    sale.incrementItemSold(quantity);

                                                for (int b = 0; b < quantity; b++)
                                                    sale.incrementTotalSales(startersList.get(index).getPrice());

                                                totalSalesList.add(new FoodMenuItem(startersList.get(index).getFoodItem(), startersList.get(index).getCategory(),
                                                        startersList.get(index).getPrice(), sale));

                                                mSaleReport.setEnabled(true);
                                            }catch (NullPointerException n) {
                                                JOptionPane.showMessageDialog(null, "Item quantity should be entered");
                                            } catch (NumberFormatException n) {
                                                JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                            }
                                        }

                            });
                        }


                        else if (categories.getSelectedIndex() == 2) { // Index 2 is for Main meals
                            // closing category choice window before inputting the new item's details
                            selectSaleCategory.dispose();

                            JFrame salesList = new JFrame("Select main meal");
                            salesList.setSize(200,300);
                            salesList.setLocation(dim.width/2-salesList.getSize().width/2, dim.height/2-salesList.getSize().height/2);


                            // JList diplaying the list of sorted items
                            Object [] mainListArray = mainList.toArray();
                            JList<FoodMenuItem> jList = new JList(mainListArray);
                            salesList.getContentPane().add(new JScrollPane(jList));
                            salesList.setVisible(true);

                            // Implementing the clicking on an item
                            jList.addListSelectionListener((ListSelectionEvent l)-> {
                                if (!l.getValueIsAdjusting()) {
                                    int index = jList.getSelectedIndex();

                                    try {
                                        int quantity = 0;
                                        quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));

                                        Sale sale = new Sale();

                                        if (quantity != 0) {
                                            sale.incrementItemSold(quantity);
                                        }
                                        for (int s = 0; s < quantity; s++) {
                                            sale.incrementTotalSales(mainList.get(index).getPrice());
                                        }

                                        totalSalesList.add(new FoodMenuItem(mainList.get(index).getFoodItem(), mainList.get(index).getCategory(),
                                                mainList.get(index).getPrice(), sale));

                                        mSaleReport.setEnabled(true);
                                    }catch (NullPointerException n) {
                                        JOptionPane.showMessageDialog(null, "Item quantity should be entered");
                                    } catch (NumberFormatException n) {
                                        JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                    }
                                }

                            });}

                            else{
                                selectSaleCategory.dispose();

                                JFrame salesList = new JFrame("Select starter");
                                salesList.setSize(200,300);
                                salesList.setLocation(dim.width/2-salesList.getSize().width/2, dim.height/2-salesList.getSize().height/2);


                                // JList diplaying the list of sorted items
                                Object [] drinksArray = drinksList.toArray();
                                JList<FoodMenuItem> jList = new JList(drinksArray);
                                salesList.getContentPane().add(new JScrollPane(jList));
                                salesList.setVisible(true);

                                // Implementing the clicking on an item
                                jList.addListSelectionListener((ListSelectionEvent l)-> {
                                    if (!l.getValueIsAdjusting()) {
                                        int index = jList.getSelectedIndex();

                                        try {
                                            int quantity = 0;
                                            quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));

                                            Sale sale = new Sale();

                                            // Increment quantity
                                            if (quantity != 0)
                                                sale.incrementItemSold(quantity);
                                            for (int s = 0; s < quantity; s++) {
                                                sale.incrementTotalSales(drinksList.get(index).getPrice());
                                            }

                                            totalSalesList.add(new FoodMenuItem(drinksList.get(index).getFoodItem(), drinksList.get(index).getCategory(),
                                                    drinksList.get(index).getPrice(), sale));
//                                        int rem = menuItems.size();
//                                        menuItems.remove(rem-1);
                                            mSaleReport.setEnabled(true);
                                        }catch (NullPointerException n) {
                                            JOptionPane.showMessageDialog(null, "Item quantity should be entered");
                                        } catch (NumberFormatException n) {
                                            JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                        }
                                    }

                                });
                        }
                    }
                });

            });

            mSaleReport.addActionListener((ActionEvent e)->{
                //New frame
                JFrame selectSaleCategory = new JFrame("Select category");
                selectSaleCategory.setSize(300,80);
                JPanel choiceComboBoxPanel = new JPanel(new BorderLayout());

                JComboBox categories = new JComboBox<>(new Object[]{"Choose Category","Sorted by name","Sorted by price"});
                choiceComboBoxPanel.add(categories, BorderLayout.CENTER);
                selectSaleCategory.add(choiceComboBoxPanel);

                selectSaleCategory.setVisible(true);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                selectSaleCategory.setLocation(dim.width/2-selectSaleCategory.getSize().width/2, dim.height/2-selectSaleCategory.getSize().height/2);

                categories.addItemListener((ItemEvent i)-> {
                    if (i.getStateChange() == ItemEvent.SELECTED) {
                        if (categories.getSelectedIndex() == 1) { // Sorted by name

                            Collections.sort(totalSalesList, new Comparator<FoodMenuItem>() {
                                @Override
                                public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                                    return o1.getFoodItem().compareTo(o2.getFoodItem());
                                }
                            });

                            JTextArea area = new JTextArea();
                            salesReport = "    \tJoe’s Fast-Food Joint\n\n" +
                                    "Item \t    Sales Count \t   Total\n\n";

                            area.append(salesReport);
                            area.setFont(new Font("monospaced",Font.PLAIN,12));

                            for (int k = 0; k < totalSalesList.size(); k++) {
                                area.append(String.format("%1$-20s %2$-15d %3$8.2f\n",totalSalesList.get(k).getFoodItem(), totalSalesList.get(k).getSaleInfo().getItemSold(),
                                        totalSalesList.get(k).getSaleInfo().getTotalSalesValue()));
                            }

                            double totalCost = 0;
                            for (int j = 0; j < totalSalesList.size(); j++) {
                                totalCost += totalSalesList.get(j).getSaleInfo().getTotalSalesValue();
                            }

                            area.append("\n\t    Total prize: R" + totalCost +"\n\tTime:\t" +dtf.format(now));
                            JOptionPane.showMessageDialog(null, area);
                        }

                        if (categories.getSelectedIndex() == 2){ // Sorted by price

                            Collections.sort(totalSalesList, new Comparator<FoodMenuItem>() {
                                @Override
                                public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                                    return Double.compare(o1.getSaleInfo().getTotalSalesValue(), o2.getSaleInfo().getTotalSalesValue());
                                }
                            });

                            JTextArea area = new JTextArea();

                            salesReport = "    \tJoe’s Fast-Food Joint\n\n" +
                                    "Item \t    Sales Count \t   Total\n\n";

                            area.append(salesReport);

                            area.setFont(new Font("monospaced",Font.PLAIN,12));

                            for (int k = 0; k < totalSalesList.size(); k++) {
                                area.append(String.format("%1$-20s %2$-15d %3$8.2f\n",totalSalesList.get(k).getFoodItem(), totalSalesList.get(k).getSaleInfo().getItemSold(),
                                        totalSalesList.get(k).getSaleInfo().getTotalSalesValue()));
                            }

                            double totalCost = 0;
                            for (int j = 0; j < totalSalesList.size(); j++) {
                                totalCost += totalSalesList.get(j).getSaleInfo().getTotalSalesValue();
                            }

                            area.append("\n\t    Total prize: R" + totalCost +"\n\tTime:\t" +dtf.format(now));
                            JOptionPane.showMessageDialog(null, area);
                    }
                    }
            });

    });
             mExit.addActionListener((ActionEvent a)->{
                 System.exit(0);
             });
    }

            public void sortFoodItems(){  // Out of the runMenuOptions() method just to not repeat the code for sorting during sales transaction.
                mSortFood.addActionListener((ActionEvent ae)->{

                    //TODO: Might not need this
                    Collections.sort(menuItems, new Comparator<FoodMenuItem>() {
                        @Override
                        public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                            return o1.getFoodItem().compareTo(o2.getFoodItem());
                        }
                    });

                    Collections.sort(startersList, new Comparator<FoodMenuItem>() {
                        @Override
                        public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                            return o1.getFoodItem().compareTo(o2.getFoodItem());
                        }
                    });

                    Collections.sort(mainList, new Comparator<FoodMenuItem>() {
                        @Override
                        public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                            return o1.getFoodItem().compareTo(o2.getFoodItem());
                        }
                    });

                    Collections.sort(drinksList, new Comparator<FoodMenuItem>() {
                        @Override
                        public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                            return o1.getFoodItem().compareTo(o2.getFoodItem());
                        }
                    });
                });


                // To be called inside sales transaction because a sorted list must be displayed to the user
                Collections.sort(menuItems, new Comparator<FoodMenuItem>() {
                    @Override
                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                        return o1.getFoodItem().compareTo(o2.getFoodItem());
                    }
                });

                Collections.sort(startersList, new Comparator<FoodMenuItem>() {
                    @Override
                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                        return o1.getFoodItem().compareTo(o2.getFoodItem());
                    }
                });

                Collections.sort(mainList, new Comparator<FoodMenuItem>() {
                    @Override
                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                        return o1.getFoodItem().compareTo(o2.getFoodItem());
                    }
                });

                Collections.sort(drinksList, new Comparator<FoodMenuItem>() {
                    @Override
                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                        return o1.getFoodItem().compareTo(o2.getFoodItem());
                    }
                });
            }

            public void implementBottomButtons() {
                addToOrder.addActionListener((ActionEvent a) -> {

                    // Check if any item from all table is selected before enabling the checkout button
                    Boolean enableAddToOrder = false;
                    for (int i = 0; i < modelStartersTable.getRowCount(); i++) {
                        if ((Boolean) modelStartersTable.getValueAt(i, 0))
                            enableAddToOrder = true;
                    }
                    for (int i = 0; i < modelMainTable.getRowCount(); i++) {
                        if ((Boolean) modelMainTable.getValueAt(i, 0))
                            enableAddToOrder = true;
                    }
                    for (int i = 0; i < modelDrinksTable.getRowCount(); i++) {
                        if ((Boolean) modelDrinksTable.getValueAt(i, 0))
                            enableAddToOrder = true;
                    }

                    if (enableAddToOrder) {
                        checkout.setEnabled(true);

                        for (int i = 0; i < modelStartersTable.getRowCount(); i++) {
                            if ((Boolean) modelStartersTable.getValueAt(i, 0)) { // If any food item is selected from table starters.
                                Sale sale = new Sale();
                                int quantity = 0;

                                try {
                                    if (jComboBoxStarter.getSelectedIndex() != 9)
                                        quantity = Integer.parseInt(modelStartersTable.getValueAt(i, 3).toString());

                                    else if (jComboBoxStarter.getSelectedItem() == "Enter number") {
                                        quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));
                                    }

                                    sale.incrementItemSold(quantity);
                                    sale.incrementTotalSales(quantity * (Double) modelStartersTable.getValueAt(i, 2));

                                    int ind = -1;
                                    for (int p = 0; p < totalSalesList.size(); p++) {
                                        if (startersList.get(i).getFoodItem().equals(totalSalesList.get(p).getFoodItem())) {
                                            ind = p;
                                        }
                                    }

                                    if (ind != -1) {
                                        totalSalesList.get(ind).getSaleInfo().incrementItemSold(sale.getItemSold());
                                        totalSalesList.get(ind).getSaleInfo().incrementTotalSales(sale.getTotalSalesValue());
                                    } else {
                                        totalSalesList.add(new FoodMenuItem(startersList.get(i).getFoodItem(), startersList.get(i).getCategory(), startersList.get(i).getPrice(), sale));
                                    }
                                    costumerOrderList.add(new FoodMenuItem(startersList.get(i).getFoodItem(), startersList.get(i).getCategory(), startersList.get(i).getPrice(), sale));

                                }catch (NullPointerException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity must be entered");
                                } catch (NumberFormatException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                }
                            }

                        }


                        for (int i = 0; i < modelMainTable.getRowCount(); i++) {
                            if ((Boolean) modelMainTable.getValueAt(i, 0)) {
                                Sale sale = new Sale();
                                int quantity = 0;


                                try {
                                    if (jComboBoxMain.getSelectedIndex() != 9)
                                        quantity = Integer.parseInt(modelMainTable.getValueAt(i, 3).toString());
                                    else
                                        quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));


                                    if (quantity != 0)
                                        sale.incrementItemSold(quantity);

                                    sale.incrementTotalSales(quantity * (Double) modelMainTable.getValueAt(i, 2));

                                    int ind = -1;
                                    for (int p = 0; p < totalSalesList.size(); p++) {
                                        if (mainList.get(i).getFoodItem().equals(totalSalesList.get(p).getFoodItem())) {
                                            ind = p;
                                        }
                                    }

                                    if (ind != -1) {
                                        totalSalesList.get(ind).getSaleInfo().incrementItemSold(sale.getItemSold());
                                        totalSalesList.get(ind).getSaleInfo().incrementTotalSales(sale.getTotalSalesValue());
                                    } else {
                                        totalSalesList.add(new FoodMenuItem(mainList.get(i).getFoodItem(), mainList.get(i).getCategory(), mainList.get(i).getPrice(), sale));
                                    }
                                    costumerOrderList.add(new FoodMenuItem(mainList.get(i).getFoodItem(), mainList.get(i).getCategory(), mainList.get(i).getPrice(), sale));

                                }catch (NullPointerException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity must be entered");
                                } catch (NumberFormatException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                }
                            }
                        }

                        for (int i = 0; i < modelDrinksTable.getRowCount(); i++) {
                            if ((Boolean) modelDrinksTable.getValueAt(i, 0)) { // If any food item is selected from table starters.
                                Sale sale = new Sale();
                                int quantity = 0;

                                try {
                                    if (jComboBoxDrinks.getSelectedIndex() != 9)
                                        quantity = Integer.parseInt(modelDrinksTable.getValueAt(i, 3).toString());
                                    else
                                        quantity = Integer.parseInt(JOptionPane.showInputDialog(null, "How many?"));

                                    if (quantity != 0)
                                        sale.incrementItemSold(quantity);


                                    sale.incrementTotalSales(quantity * (Double) modelDrinksTable.getValueAt(i, 2));

                                    int ind = -1;
                                    for (int p = 0; p < totalSalesList.size(); p++) {
                                        if (drinksList.get(i).getFoodItem().equals(totalSalesList.get(p).getFoodItem())) {
                                            ind = p;
                                        }
                                    }

                                    if (ind != -1) {
                                        totalSalesList.get(ind).getSaleInfo().incrementItemSold(sale.getItemSold());
                                        totalSalesList.get(ind).getSaleInfo().incrementTotalSales(sale.getTotalSalesValue());
                                    } else {
                                        totalSalesList.add(new FoodMenuItem(drinksList.get(i).getFoodItem(), drinksList.get(i).getCategory(), drinksList.get(i).getPrice(), sale));
                                    }
                                    costumerOrderList.add(new FoodMenuItem(drinksList.get(i).getFoodItem(), drinksList.get(i).getCategory(), drinksList.get(i).getPrice(), sale));

                                }catch (NullPointerException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity must be entered");
                                } catch (NumberFormatException n) {
                                    JOptionPane.showMessageDialog(null, "Item quantity should be a number");
                                }
                            }
                        }

                        //Uncheck them after adding to the Order
                        for (int i = 0; i < modelStartersTable.getRowCount(); i++) {
                            modelStartersTable.setValueAt(false, i, 0);
                        }

                        //Uncheck them after adding to the Order
                        for (int i = 0; i < modelMainTable.getRowCount(); i++) {
                            modelMainTable.setValueAt(false, i, 0);
                        }

                        for (int i = 0; i < modelDrinksTable.getRowCount(); i++) {
                            modelDrinksTable.setValueAt(false, i, 0);
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "You have to select an item first");
                });


                checkout.addActionListener((ActionEvent e) -> {
                    costumer.incrementCostumerNum();
                    now = LocalDateTime.now();

                    //New frame
                    JFrame selectSaleCategory = new JFrame("Select category");
                    selectSaleCategory.setSize(300, 80);
                    JPanel choiceComboBoxPanel = new JPanel(new BorderLayout());

                    JComboBox categories = new JComboBox<>(new Object[]{"Choose Category", "Sorted by name", "Sorted by price"});
                    choiceComboBoxPanel.add(categories, BorderLayout.CENTER);
                    selectSaleCategory.add(choiceComboBoxPanel);

                    selectSaleCategory.setVisible(true);
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    selectSaleCategory.setLocation(dim.width / 2 - selectSaleCategory.getSize().width / 2, dim.height / 2 - selectSaleCategory.getSize().height / 2);

                    categories.addItemListener((ItemEvent i) -> {
                        if (i.getStateChange() == ItemEvent.SELECTED) {
                            if (categories.getSelectedIndex() == 1) { // Sorted by name

                                Collections.sort(costumerOrderList, new Comparator<FoodMenuItem>() {
                                    @Override
                                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                                        return o1.getFoodItem().compareTo(o2.getFoodItem());
                                    }
                                });

                                JTextArea area = new JTextArea();

                                costumerReport = "    \tJoe’s Fast-Food Joint\n\n" +
                                        "         \tCostumer #" + costumer.getCostumerNum() +"\n\n"+
                                        "Item \t    Sales Count \t   Total\n\n";

                                area.append(costumerReport);

                                area.setFont(new Font("monospaced",Font.PLAIN,12));

                                for (int k = 0; k < costumerOrderList.size(); k++) {
                                    area.append(String.format("%1$-20s %2$-15d %3$8.2f\n",costumerOrderList.get(k).getFoodItem(), costumerOrderList.get(k).getSaleInfo().getItemSold(),
                                                    costumerOrderList.get(k).getSaleInfo().getTotalSalesValue()));
                                }

                                double totalCost = 0;
                                for (int j = 0; j < costumerOrderList.size(); j++) {
                                    totalCost += costumerOrderList.get(j).getSaleInfo().getTotalSalesValue();
                                }

                                area.append("\n\t    Total prize: R" + totalCost +"\n\tTime:\t" +dtf.format(now));

                                JOptionPane.showMessageDialog(null, area);
                                costumerReport = "";
                                costumerOrderList.clear();

                                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                                Voice voice;
                                VoiceManager vm = VoiceManager.getInstance();
                                voice = vm.getVoice(VOICENAME);

                                voice.allocate();
                                voice.setRate(190);
                                voice.setPitch(150);
                                voice.setVolume(3);

                                String text = "Thank you for your support.";

                                try{
                                    voice.speak(text);
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                            }

                            if (categories.getSelectedIndex() == 2) { // Sorted by price

                                Collections.sort(costumerOrderList, new Comparator<FoodMenuItem>() {
                                    @Override
                                    public int compare(FoodMenuItem o1, FoodMenuItem o2) {
                                        return Double.compare(o1.getSaleInfo().getTotalSalesValue(), o2.getSaleInfo().getTotalSalesValue());
                                    }
                                });

                                JTextArea area = new JTextArea();

                                costumerReport = "    \tJoe’s Fast-Food Joint\n\n" +
                                        "         \tCostumer #" + costumer.getCostumerNum() +"\n\n"+
                                        "Item \t    Sales Count \t   Total\n\n";

                                area.append(costumerReport);

                                area.setFont(new Font("monospaced",Font.PLAIN,12));

                                for (int k = 0; k < costumerOrderList.size(); k++) {
                                    area.append(String.format("%1$-20s %2$-15d %3$8.2f\n",costumerOrderList.get(k).getFoodItem(), costumerOrderList.get(k).getSaleInfo().getItemSold(),
                                            costumerOrderList.get(k).getSaleInfo().getTotalSalesValue()));
                                }

                                double totalCost = 0;
                                for (int j = 0; j < costumerOrderList.size(); j++) {
                                    totalCost += costumerOrderList.get(j).getSaleInfo().getTotalSalesValue();
                                }

                                area.append("\n\t    Total prize: R" + totalCost +"\n\tTime:\t" +dtf.format(now));

                                JOptionPane.showMessageDialog(null, area);

                                costumerReport = "";
                                costumerOrderList.clear();

                                System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
                                Voice voice;
                                VoiceManager vm = VoiceManager.getInstance();
                                voice = vm.getVoice(VOICENAME);

                                voice.allocate();
                                voice.setRate(190);
                                voice.setPitch(150);
                                voice.setVolume(3);

                                String text = "Thank you for your support.";

                                try{
                                    voice.speak(text);
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                            }
                        }
                    });

                    // After the first client, we will be able to see the report
                    mSaleReport.setEnabled(true);

                    // But after the each sale, the checkout button becomes unavailable until a new item is added to the order
                    checkout.setEnabled(false);


                });
            }

}

// TODO: Fix empty strings being added as food items (check if sting is empty before adding it to the table)
