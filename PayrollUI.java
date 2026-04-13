import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PayrollUI extends JFrame {

    private JTextField nameField, idField, salaryField, bonusField, hoursField, rateField;
    private JComboBox<String> typeBox;
    private JTextArea outputArea;

    private JPanel bonusPanel, partTimePanel;

    Color bg = new Color(18, 18, 18);
    Color sidebar = new Color(28, 28, 30);
    Color card = new Color(36, 36, 38);
    Color accent = new Color(10, 132, 255);
    Color text = new Color(245, 245, 247);

    public PayrollUI() {
        setTitle("Payroll Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Sidebar =====
        JPanel side = new JPanel();
        side.setBackground(sidebar);
        side.setPreferredSize(new Dimension(180, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Payroll");
        logo.setForeground(text);
        logo.setFont(new Font("San Francisco", Font.BOLD, 18));
        logo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton dashBtn = createSideButton("Dashboard");
        JButton addBtn = createSideButton("Add Employee");

        side.add(logo);
        side.add(dashBtn);
        side.add(addBtn);

        // ===== Main Panel =====
        JPanel main = new JPanel();
        main.setBackground(bg);
        main.setLayout(new BorderLayout());

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(card);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Employee Payroll");
        title.setForeground(text);
        title.setFont(new Font("San Francisco", Font.BOLD, 20));

        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        nameField = createField("Name", cardPanel);
        idField = createField("ID", cardPanel);

        typeBox = new JComboBox<>(new String[]{"Full Time", "Part Time"});
        styleCombo(typeBox);
        cardPanel.add(typeBox);

        salaryField = createField("Base Salary", cardPanel);

        // Bonus
        bonusPanel = new JPanel(new BorderLayout());
        bonusPanel.setBackground(card);
        bonusField = new JTextField();
        styleField(bonusField);
        bonusPanel.add(label("Bonus"), BorderLayout.NORTH);
        bonusPanel.add(bonusField);
        cardPanel.add(bonusPanel);

        // Part Time
        partTimePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        partTimePanel.setBackground(card);

        hoursField = new JTextField();
        rateField = new JTextField();

        styleField(hoursField);
        styleField(rateField);

        partTimePanel.add(labeled("Hours", hoursField));
        partTimePanel.add(labeled("Rate", rateField));

        cardPanel.add(partTimePanel);

        JButton calcBtn = new JButton("Calculate");
        styleButton(calcBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(calcBtn);

        // Output Panel
        outputArea = new JTextArea();
        outputArea.setBackground(new Color(44, 44, 46));
        outputArea.setForeground(text);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(outputArea);

        main.add(cardPanel, BorderLayout.WEST);
        main.add(scroll, BorderLayout.CENTER);

        add(side, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);

        toggleFields();

        typeBox.addActionListener(e -> toggleFields());
        calcBtn.addActionListener(e -> calculate());

        setVisible(true);
    }

    // ===== UI Methods =====

    private JButton createSideButton(String textBtn) {
        JButton btn = new JButton(textBtn);
        btn.setFocusPainted(false);
        btn.setBackground(sidebar);
        btn.setForeground(text);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(58, 58, 60));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(sidebar);
            }
        });

        return btn;
    }

    private JTextField createField(String label, JPanel parent) {
        JTextField field = new JTextField();
        styleField(field);

        parent.add(labeled(label, field));
        parent.add(Box.createRigidArea(new Dimension(0, 10)));

        return field;
    }

    private JPanel labeled(String textLbl, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(card);

        panel.add(label(textLbl), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JLabel label(String txt) {
        JLabel lbl = new JLabel(txt);
        lbl.setForeground(text);
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(58, 58, 60));
        field.setForeground(text);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private void styleCombo(JComboBox<String> box) {
        box.setBackground(new Color(58, 58, 60));
        box.setForeground(text);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 210, 255));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(accent);
            }
        });
    }

    private void toggleFields() {
        boolean isFull = typeBox.getSelectedItem().equals("Full Time");
        bonusPanel.setVisible(isFull);
        partTimePanel.setVisible(!isFull);
    }

    private void calculate() {
        try {
            String name = nameField.getText();
            int id = Integer.parseInt(idField.getText());

            Employee emp;

            if (typeBox.getSelectedItem().equals("Full Time")) {
                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                emp = new FullTimeEmployee(name, id, salary, bonus);
            } else {
                int hours = Integer.parseInt(hoursField.getText());
                double rate = Double.parseDouble(rateField.getText());
                emp = new PartTimeEmployee(name, id, hours, rate);
            }

            double finalSalary = emp.calculateSalary();

            outputArea.append("✔ " + name + " | ₹" + finalSalary + "\n");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }
}