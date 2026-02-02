import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class StudentRecordSystem extends JFrame {
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JTextField idField, nameField, gradeField;
    private JLabel statusLabel;
    private boolean hasUnsavedChanges = false;
    private final String FILE_NAME = "class_records.csv";

    // Colors
    private final Color PRIMARY_COLOR = new Color(0x3F51B5);
    private final Color ADD_GREEN = new Color(0x4CAF50);
    private final Color DELETE_RED = new Color(0xF44336);
    private final Color SAVE_BLUE = new Color(0x2196F3);

    public StudentRecordSystem
() {
        setupFrame();
        initComponents();
        loadData();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Student Records Manager");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        setLayout(new BorderLayout());
    }

private void initComponents() {
    // --- NORTH: Input Panel (Improved Layout) ---
    // Using a main container with padding
    JPanel topContainer = new JPanel(new BorderLayout());
    topContainer.setBackground(new Color(0xFAFAFA));
    
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
    inputPanel.setOpaque(false); // Let the container color show through

    idField = new JTextField(6);
    nameField = new JTextField(15);
    gradeField = new JTextField(4);

    // Using text instead of emojis to ensure they show up on all systems
    JButton addButton = createStyledButton("ADD +", ADD_GREEN);
    JButton deleteButton = createStyledButton("DELETE -", DELETE_RED);
    JButton saveButton = createStyledButton("SAVE DATA", SAVE_BLUE);

    // Adding components with labels
    inputPanel.add(new JLabel("ID:")); 
    inputPanel.add(idField);
    inputPanel.add(new JLabel("Name:")); 
    inputPanel.add(nameField);
    inputPanel.add(new JLabel("Grade:")); 
    inputPanel.add(gradeField);
    
    // Grouping buttons so they don't wrap individually
    inputPanel.add(addButton);
    inputPanel.add(deleteButton);
    inputPanel.add(saveButton);

    topContainer.add(inputPanel, BorderLayout.CENTER);
    topContainer.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); // Bottom border
    
    add(topContainer, BorderLayout.NORTH);

    // --- CENTER: JTable (Rest of your code remains the same) ---
    String[] columns = {"Student ID", "Name", "Grade"};
    tableModel = new DefaultTableModel(columns, 0);
    studentTable = new JTable(tableModel);
        
        // Styling Table
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentTable.setSelectionBackground(new Color(0xBBDEFB));
        studentTable.setGridColor(new Color(0xE0E0E0));
        studentTable.setAutoCreateRowSorter(true);
        
        // Custom Header
        JTableHeader header = studentTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));

        // Alignment & Alternating Colors
        setupTableRenderers();

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new LineBorder(Color.GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);

        // --- SOUTH: Status Bar ---
        statusLabel = new JLabel("Records loaded: 0 | Last action: None");
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        addButton.addActionListener(e -> addRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        saveButton.addActionListener(e -> saveToFile());
    }

    private void setupTableRenderers() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF5F5F5));
                }
                setHorizontalAlignment(column == 1 ? JLabel.LEFT : JLabel.CENTER);
                return c;
            }
        };
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    // --- CRUD Logic ---
    // Made by Travis Penalosa

    private void addRecord() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String gradeText = gradeField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || gradeText.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            int grade = Integer.parseInt(gradeText);
            if (grade < 0 || grade > 100) throw new NumberFormatException();

            // Check duplicates
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(id)) {
                    JOptionPane.showMessageDialog(this, "ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            tableModel.addRow(new Object[]{id, name, grade});
            updateStatus("Added record: " + name);
            markUnsaved();
            clearInputs();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Grade must be 0-100!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(row);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete " + name + "? This cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            updateStatus("Deleted record: " + name);
            markUnsaved();
        }
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.println(tableModel.getValueAt(i, 0) + "," +
                               tableModel.getValueAt(i, 1) + "," +
                               tableModel.getValueAt(i, 2));
            }
            hasUnsavedChanges = false;
            setTitle("Student Records Manager");
            updateStatus("All changes saved to " + FILE_NAME);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed: " + e.getMessage());
        }
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 3) tableModel.addRow(parts);
            }
            updateStatus("Loaded " + tableModel.getRowCount() + " records");
        } catch (FileNotFoundException e) {
            updateStatus("No existing record file found.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file.");
        }
    }

    private void markUnsaved() {
        hasUnsavedChanges = true;
        setTitle("Student Records Manager (*)");
    }

    private void updateStatus(String msg) {
        statusLabel.setText(String.format("Records loaded: %d | Last action: %s", tableModel.getRowCount(), msg));
    }

    private void clearInputs() {
        idField.setText(""); nameField.setText(""); gradeField.setText("");
    }

    private void confirmExit() {
        if (hasUnsavedChanges) {
            int res = JOptionPane.showConfirmDialog(this, "Unsaved changes exist. Exit anyway?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (res != JOptionPane.YES_OPTION) return;
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRecordSystem
    ::new);
    }
}
// Travis Penalosa