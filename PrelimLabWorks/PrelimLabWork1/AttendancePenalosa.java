
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AttendancePenalosa extends JFrame {
    private JTextField attendanceNameField;
    private JTextField courseYearField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private JButton submitButton;
    private JButton clearButton;

    public AttendancePenalosa() {
        // Frame setup
        setTitle("Attendance Tracking System - Penalosa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(600, 400);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 245, 250));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("Attendance Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Attendance Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        attendanceNameField = new JTextField(25);
        attendanceNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        attendanceNameField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        formPanel.add(attendanceNameField, gbc);

        // Course/Year
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        courseYearField = new JTextField(25);
        courseYearField.setFont(new Font("Arial", Font.PLAIN, 12));
        courseYearField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        formPanel.add(courseYearField, gbc);

        // Time In
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(timeInLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        timeInField = new JTextField(25);
        timeInField.setFont(new Font("Arial", Font.PLAIN, 12));
        timeInField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(236, 240, 241));
        populateTimeIn();
        formPanel.add(timeInField, gbc);

        // E-Signature
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel signatureLabel = new JLabel("E-Signature:");
        signatureLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(signatureLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        eSignatureField = new JTextField(25);
        eSignatureField.setFont(new Font("Courier New", Font.PLAIN, 10));
        eSignatureField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        eSignatureField.setEditable(false);
        eSignatureField.setBackground(new Color(236, 240, 241));
        generateSignature();
        formPanel.add(eSignatureField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 245, 250));

        submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(140, 35));
        submitButton.addActionListener(e -> handleSubmit());

        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setBackground(new Color(231, 76, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(100, 35));
        clearButton.addActionListener(e -> handleClear());

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Populates the Time In field with current system date and time
     */
    private void populateTimeIn() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeInField.setText(now.format(formatter));
    }

    /**
     * Generates a unique E-Signature using UUID
     * Can also use timestamp-based or random number approaches
     */
    private void generateSignature() {
        String signature = UUID.randomUUID().toString().substring(0, 16).toUpperCase();
        eSignatureField.setText(signature);
    }

    /**
     * Handles the Submit button action
     */
    private void handleSubmit() {
        String name = attendanceNameField.getText().trim();
        String course = courseYearField.getText().trim();

        if (name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields:\n- Attendance Name\n- Course/Year",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = "Attendance Record Submitted:\n\n" +
                "Name: " + name + "\n" +
                "Course/Year: " + course + "\n" +
                "Time In: " + timeInField.getText() + "\n" +
                "E-Signature: " + eSignatureField.getText();

        JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        handleClear();
    }

    /**
     * Handles the Clear button action
     */
    private void handleClear() {
        attendanceNameField.setText("");
        courseYearField.setText("");
        populateTimeIn();
        generateSignature();
    }

    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AttendancePenalosa frame = new AttendancePenalosa();
            frame.setVisible(true);
        });
    }
}
