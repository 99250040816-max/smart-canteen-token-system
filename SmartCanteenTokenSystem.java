import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Smart Canteen Token System
 * -------------------------------------------------------------
 * A Java Swing Desktop Application developed for 2nd Year B.Tech
 * CSE / AIML Mini Project.
 *
 * Concepts Demonstrated:
 * 1. Java Swing GUI (JFrame, JPanel, JTable, JComboBox, JSpinner, JScrollPane)
 * 2. Data Structures: Queue Interface using LinkedList (FIFO queue management)
 * 3. Event-Driven Programming with ActionListeners
 * 4. Model-View separation with DefaultTableModel
 * 5. Input Validation and Dialog Management (JOptionPane)
 */
public class SmartCanteenTokenSystem extends JFrame {

    // ==========================================
    // 1. Inner Static Order Class
    // ==========================================
    public static class Order {
        int token;
        String studentName;
        String foodName;
        int quantity;
        int amount;
        String status;

        public Order(int token, String studentName, String foodName, int quantity, int amount, String status) {
            this.token = token;
            this.studentName = studentName;
            this.foodName = foodName;
            this.quantity = quantity;
            this.amount = amount;
            this.status = status;
        }

        public int getToken() {
            return token;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getFoodName() {
            return foodName;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    // ==========================================
    // 2. Data Structures & State Variables
    // ==========================================
    // FIFO Queue for order preparation using LinkedList implementation
    private final Queue<Order> orderQueue = new LinkedList<>();
    
    // List to keep all orders for searching and historical tracking
    private final List<Order> allOrders = new ArrayList<>();
    
    // Auto-incrementing token counter starting from 1
    private int nextTokenNumber = 1;

    // Food Menu items as required
    private final String[] foodMenu = {
        "Idly - Rs.20",
        "Dosa - Rs.40",
        "Meals - Rs.70",
        "Fried Rice - Rs.80",
        "Tea - Rs.15",
        "Juice - Rs.30"
    };

    // ==========================================
    // 3. UI Components
    // ==========================================
    private JTextField txtStudentName;
    private JComboBox<String> cmbFoodMenu;
    private JSpinner spinnerQuantity;
    private JButton btnGenerateToken;

    private JTable tableOrders;
    private DefaultTableModel tableModel;

    private JButton btnCallNext;
    private JButton btnMarkReady;
    private JButton btnMarkServed;

    private JLabel lblQueueStatus;

    /**
     * Constructor for the Main Dashboard Window (900 x 550)
     */
    public SmartCanteenTokenSystem() {
        // Set Frame Properties
        setTitle("Smart Canteen Token System");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen
        setLayout(new BorderLayout(10, 10));

        // Initialize and assemble UI components
        initHeader();
        initContent();
        initFooter();
    }

    // ==========================================
    // 4. UI Layout Initialization
    // ==========================================
    private void initHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(24, 43, 73));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("Smart Canteen Token System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("B.Tech CSE/AIML Mini Project - FIFO Queue Management");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 215, 235));

        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 2, 2));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        lblQueueStatus = new JLabel("Active Queue: 0 orders waiting");
        lblQueueStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblQueueStatus.setForeground(new Color(255, 215, 0));

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(lblQueueStatus, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void initContent() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(12, 12));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Left Panel: Order Entry Form
        JPanel orderEntryPanel = new JPanel();
        orderEntryPanel.setLayout(new BoxLayout(orderEntryPanel, BoxLayout.Y_AXIS));
        orderEntryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 210), 1),
                " New Order Entry ",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(24, 43, 73)
            ),
            BorderFactory.createEmptyBorder(12, 15, 15, 15)
        ));
        orderEntryPanel.setPreferredSize(new Dimension(280, 0));

        // Field 1: Student Name
        JLabel lblStudent = new JLabel("Student Name:");
        lblStudent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStudent.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtStudentName = new JTextField();
        txtStudentName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtStudentName.setPreferredSize(new Dimension(240, 32));
        txtStudentName.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Field 2: Food Menu
        JLabel lblFood = new JLabel("Select Food:");
        lblFood.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFood.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbFoodMenu = new JComboBox<>(foodMenu);
        cmbFoodMenu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        cmbFoodMenu.setPreferredSize(new Dimension(240, 32));
        cmbFoodMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Field 3: Quantity (1 to 20, default 1)
        JLabel lblQuantity = new JLabel("Quantity (1 - 20):");
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQuantity.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinnerQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spinnerQuantity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        spinnerQuantity.setPreferredSize(new Dimension(240, 32));
        spinnerQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Button: Generate Token
        btnGenerateToken = new JButton("Generate Token");
        btnGenerateToken.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerateToken.setBackground(new Color(16, 124, 65));
        btnGenerateToken.setForeground(Color.WHITE);
        btnGenerateToken.setFocusPainted(false);
        btnGenerateToken.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnGenerateToken.setPreferredSize(new Dimension(240, 38));
        btnGenerateToken.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGenerateToken.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGenerateToken();
            }
        });

        // Assemble Order Entry Panel
        orderEntryPanel.add(lblStudent);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        orderEntryPanel.add(txtStudentName);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        orderEntryPanel.add(lblFood);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        orderEntryPanel.add(cmbFoodMenu);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        orderEntryPanel.add(lblQuantity);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        orderEntryPanel.add(spinnerQuantity);
        orderEntryPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        orderEntryPanel.add(btnGenerateToken);
        orderEntryPanel.add(Box.createVerticalGlue());

        // Right Panel: Table and Status Actions
        JPanel tableContainerPanel = new JPanel(new BorderLayout(8, 8));

        // Table Setup
        String[] columnNames = {"Token", "Student", "Food", "Quantity", "Amount", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table cells are read-only
            }
        };

        tableOrders = new JTable(tableModel);
        tableOrders.setRowHeight(28); // Required: row height approximately 28 pixels
        tableOrders.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableOrders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableOrders.getTableHeader().setBackground(new Color(235, 240, 248));
        tableOrders.getTableHeader().setForeground(new Color(24, 43, 73));
        tableOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Center align Token, Quantity, Amount, and Status columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableOrders.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableOrders.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableOrders.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableOrders.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Set column widths
        tableOrders.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableOrders.getColumnModel().getColumn(1).setPreferredWidth(140);
        tableOrders.getColumnModel().getColumn(2).setPreferredWidth(130);
        tableOrders.getColumnModel().getColumn(3).setPreferredWidth(70);
        tableOrders.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableOrders.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tableOrders);
        tableContainerPanel.add(scrollPane, BorderLayout.CENTER);

        // Status Control Action Buttons Bar
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actionButtonPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        btnCallNext = new JButton("Call Next (FIFO)");
        btnCallNext.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCallNext.setBackground(new Color(0, 102, 204));
        btnCallNext.setForeground(Color.WHITE);
        btnCallNext.setFocusPainted(false);
        btnCallNext.setPreferredSize(new Dimension(140, 34));
        btnCallNext.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnMarkReady = new JButton("Mark Ready");
        btnMarkReady.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMarkReady.setBackground(new Color(218, 120, 0));
        btnMarkReady.setForeground(Color.WHITE);
        btnMarkReady.setFocusPainted(false);
        btnMarkReady.setPreferredSize(new Dimension(130, 34));
        btnMarkReady.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnMarkServed = new JButton("Mark Served");
        btnMarkServed.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMarkServed.setBackground(new Color(34, 139, 34));
        btnMarkServed.setForeground(Color.WHITE);
        btnMarkServed.setFocusPainted(false);
        btnMarkServed.setPreferredSize(new Dimension(130, 34));
        btnMarkServed.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Attach Action Listeners
        btnCallNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCallNext();
            }
        });

        btnMarkReady.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMarkReady();
            }
        });

        btnMarkServed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleMarkServed();
            }
        });

        actionButtonPanel.add(btnCallNext);
        actionButtonPanel.add(btnMarkReady);
        actionButtonPanel.add(btnMarkServed);

        tableContainerPanel.add(actionButtonPanel, BorderLayout.SOUTH);

        // Put Left and Right panels together
        mainContentPanel.add(orderEntryPanel, BorderLayout.WEST);
        mainContentPanel.add(tableContainerPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void initFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 8, 15));
        footerPanel.setBackground(new Color(245, 247, 250));

        JLabel infoLabel = new JLabel("Queue Operations: offer() on new order | peek() on Call Next | poll() on Mark Served");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 110, 125));

        JLabel creditLabel = new JLabel("B.Tech Mini Project | Java Swing & Collections Framework");
        creditLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        creditLabel.setForeground(new Color(100, 110, 125));

        footerPanel.add(infoLabel, BorderLayout.WEST);
        footerPanel.add(creditLabel, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // 5. Business Logic & Action Handlers
    // ==========================================

    /**
     * Handles New Order Generation:
     * - Validates Student Name and Quantity
     * - Calculates Total Amount (Food Price * Quantity)
     * - Creates Order instance with status "WAITING"
     * - Enqueues order using orderQueue.offer(order)
     * - Adds row to JTable
     * - Displays success dialog with Token Number
     */
    private void handleGenerateToken() {
        String studentName = txtStudentName.getText().trim();

        // Validation: Student name cannot be empty
        if (studentName.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter the student's name.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            txtStudentName.requestFocus();
            return;
        }

        // Selected food
        String selectedFoodItem = (String) cmbFoodMenu.getSelectedItem();
        if (selectedFoodItem == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a food item.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Extract food name and unit price from string (e.g. "Dosa - Rs.40")
        int unitPrice = extractPrice(selectedFoodItem);
        String foodName = extractFoodName(selectedFoodItem);

        // Quantity validation
        int quantity = (Integer) spinnerQuantity.getValue();
        if (quantity < 1 || quantity > 20) {
            JOptionPane.showMessageDialog(
                this,
                "Quantity must be between 1 and 20.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Amount calculation: Total Amount = Food Price × Quantity
        int totalAmount = unitPrice * quantity;

        // Create Order object with status "WAITING"
        int currentToken = nextTokenNumber;
        Order newOrder = new Order(currentToken, studentName, foodName, quantity, totalAmount, "WAITING");

        // Increment token number automatically for next order
        nextTokenNumber++;

        // Add to FIFO Queue using orderQueue.offer(order)
        orderQueue.offer(newOrder);
        allOrders.add(newOrder);

        // Insert row into JTable
        tableModel.addRow(new Object[]{
            newOrder.getToken(),
            newOrder.getStudentName(),
            newOrder.getFoodName(),
            newOrder.getQuantity(),
            "Rs." + newOrder.getAmount(),
            newOrder.getStatus()
        });

        // Reset input fields
        txtStudentName.setText("");
        spinnerQuantity.setValue(1);
        cmbFoodMenu.setSelectedIndex(0);
        updateQueueLabel();

        // Display success confirmation message
        JOptionPane.showMessageDialog(
            this,
            "Order placed successfully!\nYour Token Number: " + currentToken,
            "Order Confirmation",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Handles "Call Next":
     * - Checks if Queue is empty
     * - Uses orderQueue.peek() to get the head of the FIFO queue
     * - Changes its status to "PREPARING"
     * - Updates the JTable
     * - Displays notification
     */
    private void handleCallNext() {
        // Check whether the queue is empty
        if (orderQueue.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No waiting orders.",
                "Queue Empty",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Retrieve the first order using peek() without removing it yet
        Order currentOrder = orderQueue.peek();

        // Change status to PREPARING
        currentOrder.setStatus("PREPARING");

        // Update the JTable row
        updateTable(currentOrder);
        updateQueueLabel();

        // Display: Now Preparing Token: X
        JOptionPane.showMessageDialog(
            this,
            "Now Preparing Token: " + currentOrder.getToken(),
            "Order in Preparation",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Handles "Mark Ready":
     * - Checks whether an order is selected from the JTable
     * - Finds order via findOrder(token)
     * - Updates status to "READY"
     * - Updates the JTable
     * - Displays notification: Token X is READY!
     */
    private void handleMarkReady() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an order.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int token = (Integer) tableModel.getValueAt(selectedRow, 0);
        Order order = findOrder(token);

        if (order != null) {
            order.setStatus("READY");
            updateTable(order);

            JOptionPane.showMessageDialog(
                this,
                "Token " + order.getToken() + " is READY!",
                "Order Ready",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Handles "Mark Served":
     * - Checks whether an order is selected from the JTable
     * - Finds order via findOrder(token)
     * - Changes status to "SERVED"
     * - Updates the JTable
     * - If that order is at front of queue, removes it using orderQueue.poll()
     * - Displays notification: Token X has been SERVED.
     */
    private void handleMarkServed() {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an order.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int token = (Integer) tableModel.getValueAt(selectedRow, 0);
        Order order = findOrder(token);

        if (order != null) {
            order.setStatus("SERVED");
            updateTable(order);

            // If this order is currently at the front of the queue, poll() it
            if (!orderQueue.isEmpty() && orderQueue.peek().getToken() == order.getToken()) {
                orderQueue.poll();
            } else {
                // If served out of strict front order, remove from queue safely
                orderQueue.remove(order);
            }

            updateQueueLabel();

            JOptionPane.showMessageDialog(
                this,
                "Token " + order.getToken() + " has been SERVED.",
                "Order Completed",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ==========================================
    // 6. Required Helper Methods
    // ==========================================

    /**
     * Searches for an order using its token number.
     *
     * @param token The token number to find
     * @return The Order object if found, otherwise null
     */
    public Order findOrder(int token) {
        for (Order order : allOrders) {
            if (order.getToken() == token) {
                return order;
            }
        }
        return null;
    }

    /**
     * Updates the status of the corresponding row in the JTable.
     *
     * @param order The order with modified status
     */
    public void updateTable(Order order) {
        if (order == null) return;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int rowToken = (Integer) tableModel.getValueAt(i, 0);
            if (rowToken == order.getToken()) {
                tableModel.setValueAt(order.getStatus(), i, 5);
                break;
            }
        }
    }

    /**
     * Updates the top-right queue summary banner.
     */
    private void updateQueueLabel() {
        int waitingCount = 0;
        for (Order o : orderQueue) {
            if ("WAITING".equals(o.getStatus()) || "PREPARING".equals(o.getStatus())) {
                waitingCount++;
            }
        }
        lblQueueStatus.setText("Active Queue: " + waitingCount + " in progress");
    }

    /**
     * Parses the price from the menu string, e.g., "Dosa - Rs.40" -> 40
     */
    private int extractPrice(String menuItem) {
        try {
            int rsIndex = menuItem.indexOf("Rs.");
            if (rsIndex != -1) {
                String priceStr = menuItem.substring(rsIndex + 3).trim();
                return Integer.parseInt(priceStr);
            }
        } catch (NumberFormatException e) {
            // Fallback default if parsing encounters unexpected format
        }
        return 0;
    }

    /**
     * Parses the food item name from the menu string, e.g., "Dosa - Rs.40" -> "Dosa"
     */
    private String extractFoodName(String menuItem) {
        int dashIndex = menuItem.indexOf(" - ");
        if (dashIndex != -1) {
            return menuItem.substring(0, dashIndex).trim();
        }
        return menuItem;
    }

    // ==========================================
    // 7. Login Page Window (JFrame)
    // ==========================================
    /**
     * Displays the standalone Login window before opening the main application.
     */
    public static void showLoginPage() {
        JFrame loginFrame = new JFrame("SMART CANTEEN LOGIN");
        loginFrame.setSize(420, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null); // Center on screen
        loginFrame.setLayout(new BorderLayout());

        // Header Panel
        JPanel loginHeader = new JPanel();
        loginHeader.setBackground(new Color(24, 43, 73));
        loginHeader.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblHeader = new JLabel("SMART CANTEEN LOGIN");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        loginHeader.add(lblHeader);

        // Center Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JPasswordField txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtPassword, gbc);

        // Footer / Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(24, 43, 73));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(130, 36));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Enter key shortcut to login
        loginFrame.getRootPane().setDefaultButton(btnLogin);

        // Login Action Handling
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());

                // Default credentials verification: admin / 1234
                if ("admin".equals(username) && "1234".equals(password)) {
                    // Close the login window
                    loginFrame.dispose();

                    // Open the Smart Canteen Token System dashboard
                    SmartCanteenTokenSystem dashboard = new SmartCanteenTokenSystem();
                    dashboard.setVisible(true);
                } else {
                    // Display: Invalid Username or Password!
                    JOptionPane.showMessageDialog(
                        loginFrame,
                        "Invalid Username or Password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                }
            }
        });

        buttonPanel.add(btnLogin);

        // Assemble Login Window
        loginFrame.add(loginHeader, BorderLayout.NORTH);
        loginFrame.add(formPanel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);

        loginFrame.setVisible(true);
    }

    // ==========================================
    // 8. Main Method Entry Point
    // ==========================================
    public static void main(String[] args) {
        // Set Look and Feel to System Look and Feel if available
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Falls back to default Swing look and feel
        }

        // Launch Application on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showLoginPage();
            }
        });
    }
}
