// Student Record System
// Programmer: Travis Penalosa
// Student ID: 13-0629-509

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentRecordSystem extends JFrame {
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JTextField idField, nameField, gradeField;
    private JLabel statusLabel;
    private boolean hasUnsavedChanges = false;
    private final String FILE_NAME = "class_records.csv";

    // Standard Colors for UI Polish
    private final Color PRIMARY_COLOR = new Color(0x3F51B5); // Indigo
    private final Color ADD_GREEN = new Color(0x4CAF50);    // Green
    private final Color DELETE_RED = new Color(0xF44336);   // Red
    private final Color SAVE_BLUE = new Color(0x2196F3);    // Blue
    private final Color BG_LIGHT = new Color(0xFAFAFA);     // Light Gray

    public StudentRecordSystem() {
        // Requirement: Programmer Identifier in Title
        setTitle("Records - Travis Penalosa [13-0629-509]");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Confirm before exit
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        setLayout(new BorderLayout());
        initComponents();
        loadData();
        setVisible(true);
    }

    private void initComponents() {
        // --- NORTH: Input Panel ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(BG_LIGHT);
        
        // Use FlowLayout with smaller text field sizes to prevent button wrapping
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        inputPanel.setOpaque(false);

        idField = new JTextField(6);
        nameField = new JTextField(15);
        gradeField = new JTextField(4);

        JButton addButton = createStyledButton("ADD +", ADD_GREEN);
        JButton deleteButton = createStyledButton("DELETE -", DELETE_RED);
        JButton saveButton = createStyledButton("SAVE DATA", SAVE_BLUE);

        inputPanel.add(new JLabel("ID:")); 
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:")); 
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Grade:")); 
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(saveButton);

        topContainer.add(inputPanel, BorderLayout.CENTER);
        topContainer.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        add(topContainer, BorderLayout.NORTH);

        // --- CENTER: JTable ---
        String[] columns = {"Student ID", "Name", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        
        // Table Styling
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setSelectionBackground(new Color(0xBBDEFB));
        studentTable.setGridColor(new Color(0xE0E0E0));
        studentTable.setAutoCreateRowSorter(true);
        
        // Header Styling
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));

        setupTableRenderers();

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // --- SOUTH: Status Bar ---
        statusLabel = new JLabel("Records loaded: 0 | Last action: None");
        statusLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        add(statusLabel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        saveButton.addActionListener(e -> saveToFile());
    }

    private void setupTableRenderers() {
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Alternating row colors
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF5F5F5));
                }
                
                // ID and Grade centered, Name left-aligned
                setHorizontalAlignment(column == 1 ? JLabel.LEFT : JLabel.CENTER);
                return c;
            }
        };
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }

    private void addRecord() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String gradeStr = gradeField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || gradeStr.isEmpty()) {
                throw new Exception("Please fill all fields.");
            }

            int grade = Integer.parseInt(gradeStr);
            if (grade < 0 || grade > 100) throw new NumberFormatException();

            // Duplicate ID check
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    JOptionPane.showMessageDialog(this, "ID " + id + " already exists!");
                    return;
                }
            }

            tableModel.addRow(new Object[]{id, name, grade});
            updateStatus("Added: " + name);
            markUnsaved();
            idField.setText(""); nameField.setText(""); gradeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Grade must be a number 0-100.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteRecord() {
        int viewRow = studentTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(viewRow);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            updateStatus("Deleted: " + name);
            markUnsaved();
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pw.println(tableModel.getValueAt(i, 0) + "," +
                           tableModel.getValueAt(i, 1) + "," +
                           tableModel.getValueAt(i, 2));
            }
            hasUnsavedChanges = false;
            updateStatus("Data saved to " + FILE_NAME);
            resetTitle();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) tableModel.addRow(data);
            }
            updateStatus("Loaded " + tableModel.getRowCount() + " records.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading records.");
        }
    }

    private void markUnsaved() {
        hasUnsavedChanges = true;
        setTitle("Records - [Your Full Name] [Your Student ID] (*)");
    }

    private void resetTitle() {
        setTitle("Records - [Your Full Name] [Your Student ID]");
    }

    private void updateStatus(String msg) {
        statusLabel.setText("Records: " + tableModel.getRowCount() + " | Action: " + msg);
    }

    private void confirmExit() {
        if (hasUnsavedChanges) {
            int res = JOptionPane.showConfirmDialog(this, "Exit without saving?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
            if (res != JOptionPane.YES_OPTION) return;
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRecordSystem::new);
    }
}