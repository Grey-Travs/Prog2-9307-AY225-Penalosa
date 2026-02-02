import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PrelimCalculatorGUI extends JFrame {
    
    // Constants
    private static final double ATTENDANCE_WEIGHT = 0.40;
    private static final double LAB_WORK_WEIGHT = 0.60;
    private static final double CLASS_STANDING_WEIGHT = 0.30;
    private static final double PRELIM_EXAM_WEIGHT = 0.70;
    private static final double PASSING_GRADE = 75.0;
    private static final double EXCELLENT_GRADE = 100.0;
    
    // GUI Components
    private JTextField attendanceField;
    private JTextField lab1Field;
    private JTextField lab2Field;
    private JTextField lab3Field;
    private JTextArea resultsArea;
    private JButton calculateButton;
    private JButton resetButton;
    private DecimalFormat df = new DecimalFormat("#.##");
    
    public PrelimCalculatorGUI() {
        // Frame setup
        setTitle("Prelim Grade Calculator - Lab Work 3");
        setSize(700, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Input panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }
    
    /**
     * Creates the header panel with title and instructions
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 240));
        
        // Title
        JLabel titleLabel = new JLabel("📚 Prelim Grade Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Lab Work 3 - Window-Based Application");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Instructions
        JLabel instructionsLabel = new JLabel("Enter your grades to calculate required Prelim Exam score");
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        instructionsLabel.setForeground(new Color(120, 120, 120));
        instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(instructionsLabel);
        
        return panel;
    }
    
    /**
     * Creates the input fields and results panel
     */
    private JPanel createInputPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2, 15, 0));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Left side - Input fields
        JPanel inputFieldsPanel = new JPanel();
        inputFieldsPanel.setLayout(new BoxLayout(inputFieldsPanel, BoxLayout.Y_AXIS));
        inputFieldsPanel.setBackground(Color.WHITE);
        inputFieldsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            "📋 Student Grades (0-100)",
            0, 0,
            new Font("Arial", Font.BOLD, 12),
            new Color(51, 51, 51)
        ));
        inputFieldsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "📋 Student Grades (0-100)",
                0, 0,
                new Font("Arial", Font.BOLD, 12),
                new Color(51, 51, 51)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Attendance field
        inputFieldsPanel.add(createLabeledInputField("Attendance Score:", attendanceField = new JTextField()));
        inputFieldsPanel.add(Box.createVerticalStrut(10));
        
        // Lab 1 field
        inputFieldsPanel.add(createLabeledInputField("Lab Work 1:", lab1Field = new JTextField()));
        inputFieldsPanel.add(Box.createVerticalStrut(10));
        
        // Lab 2 field
        inputFieldsPanel.add(createLabeledInputField("Lab Work 2:", lab2Field = new JTextField()));
        inputFieldsPanel.add(Box.createVerticalStrut(10));
        
        // Lab 3 field
        inputFieldsPanel.add(createLabeledInputField("Lab Work 3:", lab3Field = new JTextField()));
        inputFieldsPanel.add(Box.createVerticalGlue());
        
        // Right side - Results area
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBackground(new Color(240, 240, 240));
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "📊 Computation Results",
                0, 0,
                new Font("Arial", Font.BOLD, 12),
                new Color(51, 51, 51)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        resultsArea.setBackground(Color.WHITE);
        resultsArea.setText("Results will appear here...\n\n"
            + "Enter your grades and click 'Calculate'\n"
            + "to see:\n"
            + "  • Lab Work Average\n"
            + "  • Class Standing\n"
            + "  • Required Prelim Exam Scores\n"
            + "  • Academic Standing Remarks");
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(inputFieldsPanel);
        mainPanel.add(resultsPanel);
        
        return mainPanel;
    }
    
    /**
     * Creates a labeled input field
     */
    private JPanel createLabeledInputField(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(8, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(51, 51, 51));
        label.setPreferredSize(new Dimension(120, 25));
        
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the button panel with Calculate and Reset buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(new Color(240, 240, 240));
        
        calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 13));
        calculateButton.setPreferredSize(new Dimension(120, 40));
        calculateButton.setBackground(new Color(102, 102, 204));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setBorder(BorderFactory.createRaisedBevelBorder());
        calculateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calculateButton.addActionListener(new CalculateButtonListener());
        
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 13));
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.setBackground(new Color(220, 220, 220));
        resetButton.setForeground(new Color(51, 51, 51));
        resetButton.setBorder(BorderFactory.createRaisedBevelBorder());
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(new ResetButtonListener());
        
        panel.add(calculateButton);
        panel.add(resetButton);
        
        return panel;
    }
    
    /**
     * Action listener for Calculate button
     */
    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get input values
                double attendance = parseInput(attendanceField.getText(), "Attendance");
                double lab1 = parseInput(lab1Field.getText(), "Lab Work 1");
                double lab2 = parseInput(lab2Field.getText(), "Lab Work 2");
                double lab3 = parseInput(lab3Field.getText(), "Lab Work 3");
                
                // Computations
                double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                double classStanding = (attendance * ATTENDANCE_WEIGHT) + (labWorkAverage * LAB_WORK_WEIGHT);
                
                // Required Prelim Exam scores
                double requiredExamPass = calculateRequiredExam(PASSING_GRADE, classStanding);
                double requiredExamExcellent = calculateRequiredExam(EXCELLENT_GRADE, classStanding);
                
                // Clamp values to valid range (0-100)
                requiredExamPass = Math.max(0, Math.min(100, requiredExamPass));
                requiredExamExcellent = Math.max(0, Math.min(100, requiredExamExcellent));
                
                // Display results
                displayResults(attendance, lab1, lab2, lab3, labWorkAverage, classStanding, requiredExamPass, requiredExamExcellent);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    PrelimCalculatorGUI.this,
                    "⚠️ Invalid input! Please enter numeric values only (0-100).",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    PrelimCalculatorGUI.this,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Action listener for Reset button
     */
    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            attendanceField.setText("");
            lab1Field.setText("");
            lab2Field.setText("");
            lab3Field.setText("");
            resultsArea.setText("Results will appear here...\n\n"
                + "Enter your grades and click 'Calculate'\n"
                + "to see:\n"
                + "  • Lab Work Average\n"
                + "  • Class Standing\n"
                + "  • Required Prelim Exam Scores\n"
                + "  • Academic Standing Remarks");
            attendanceField.requestFocus();
        }
    }
    
    /**
     * Parses and validates input
     */
    private double parseInput(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("⚠️ " + fieldName + " cannot be empty!");
        }
        
        double value = Double.parseDouble(text.trim());
        
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("⚠️ " + fieldName + " must be between 0 and 100!");
        }
        
        return value;
    }
    
    /**
     * Calculates the required Prelim Exam score
     */
    private double calculateRequiredExam(double targetGrade, double classStanding) {
        return (targetGrade - (classStanding * CLASS_STANDING_WEIGHT)) / PRELIM_EXAM_WEIGHT;
    }
    
    /**
     * Displays the results in the results area
     */
    private void displayResults(double attendance, double lab1, double lab2, double lab3,
                                double labWorkAverage, double classStanding,
                                double requiredExamPass, double requiredExamExcellent) {
        StringBuilder results = new StringBuilder();
        
        results.append("╔════════════════════════════════════════╗\n");
        results.append("║      COMPUTATION RESULTS               ║\n");
        results.append("╚════════════════════════════════════════╝\n\n");
        
        results.append("📊 INPUT SUMMARY:\n");
        results.append("  Attendance:    ").append(df.format(attendance)).append("\n");
        results.append("  Lab Work 1:    ").append(df.format(lab1)).append("\n");
        results.append("  Lab Work 2:    ").append(df.format(lab2)).append("\n");
        results.append("  Lab Work 3:    ").append(df.format(lab3)).append("\n\n");
        
        results.append("📈 COMPUTED VALUES:\n");
        results.append("  Lab Average:   ").append(df.format(labWorkAverage)).append("\n");
        results.append("  Class Standing:").append(df.format(classStanding)).append("%\n\n");
        
        results.append("📋 REQUIRED PRELIM EXAM SCORES:\n");
        results.append("  For PASSING (75):   ").append(df.format(requiredExamPass)).append("%\n");
        results.append("  For EXCELLENT (100):").append(df.format(requiredExamExcellent)).append("%\n\n");
        
        results.append("╔════════════════════════════════════════╗\n");
        results.append("║       ACADEMIC STANDING REMARKS        ║\n");
        results.append("╚════════════════════════════════════════╝\n\n");
        
        results.append(generateRemarks(requiredExamPass, requiredExamExcellent, classStanding));
        
        resultsArea.setText(results.toString());
    }
    
    /**
     * Generates remarks based on required exam scores
     */
    private String generateRemarks(double requiredPass, double requiredExcellent, double classStanding) {
        if (requiredPass <= 0) {
            return "✅ EXCELLENT: Your current Class Standing\n" +
                   "(" + df.format(classStanding) + "%) is already sufficient\n" +
                   "for a Passing grade!\n\n" +
                   "🎯 Even a 0% on the Prelim Exam will\n" +
                   "result in a passing grade.\n\n" +
                   "RECOMMENDATION: Focus on maintaining\n" +
                   "your current performance.";
        } else if (requiredExcellent <= 0) {
            return "✅ VERY GOOD: Your current Class Standing\n" +
                   "(" + df.format(classStanding) + "%) is very strong!\n\n" +
                   "You need at least " + df.format(requiredPass) + "%\n" +
                   "on the Prelim Exam to PASS.\n\n" +
                   "NOTE: You will automatically achieve\n" +
                   "an Excellent grade regardless of\n" +
                   "Prelim Exam score.";
        } else if (requiredPass <= 50) {
            return "✅ GOOD: Your current Class Standing\n" +
                   "(" + df.format(classStanding) + "%) is solid.\n\n" +
                   "Required for PASSING (75): " + df.format(requiredPass) + "%\n" +
                   "Required for EXCELLENT (100): " + df.format(requiredExcellent) + "%\n\n" +
                   "RECOMMENDATION: Maintain your current\n" +
                   "performance and aim for a moderate\n" +
                   "Prelim score.";
        } else if (requiredPass <= 75) {
            return "⚠️  MODERATE: Your current Class Standing\n" +
                   "(" + df.format(classStanding) + "%) needs improvement.\n\n" +
                   "Required for PASSING (75): " + df.format(requiredPass) + "%\n" +
                   "Required for EXCELLENT (100): " + df.format(requiredExcellent) + "%\n\n" +
                   "RECOMMENDATION: Focus on achieving a\n" +
                   "solid Prelim Exam score to ensure\n" +
                   "passing.";
        } else {
            return "❌ CRITICAL: Your current Class Standing\n" +
                   "(" + df.format(classStanding) + "%) is very low.\n\n" +
                   "Required for PASSING (75): " + df.format(requiredPass) + "%\n" +
                   "Required for EXCELLENT (100): " + df.format(requiredExcellent) + "%\n\n" +
                   "RECOMMENDATION: Prioritize your Prelim\n" +
                   "Exam preparation immediately!";
        }
    }
    
    /**
     * Main method to launch the GUI application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrelimCalculatorGUI();
            }
        });
    }
}