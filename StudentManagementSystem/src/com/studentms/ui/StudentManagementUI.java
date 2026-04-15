package com.studentms.ui;

import com.studentms.dao.StudentDAO;
import com.studentms.models.Student;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main UI class for the Student Management System.
 * Provides a modern Swing GUI with Add, View, and Delete functionality.
 */
public class StudentManagementUI extends JFrame {

    // ── Colour Palette ──────────────────────────────────────────────
    private static final Color PRIMARY      = new Color(45, 80, 150);
    private static final Color PRIMARY_DARK = new Color(30, 60, 120);
    private static final Color ACCENT       = new Color(0, 150, 136);
    private static final Color BG_DARK      = new Color(30, 33, 40);
    private static final Color BG_CARD      = new Color(40, 44, 55);
    private static final Color BG_INPUT     = new Color(50, 55, 68);
    private static final Color TEXT_PRIMARY  = new Color(230, 235, 245);
    private static final Color TEXT_SECONDARY = new Color(160, 170, 185);
    private static final Color DANGER       = new Color(220, 70, 70);
    private static final Color DANGER_HOVER = new Color(200, 50, 50);
    private static final Color SUCCESS      = new Color(46, 160, 100);
    private static final Color BORDER_COLOR = new Color(60, 65, 80);
    private static final Color TABLE_ROW_ALT = new Color(35, 38, 48);
    private static final Color TABLE_HEADER = new Color(50, 55, 70);

    // ── Fonts ───────────────────────────────────────────────────────
    private static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_INPUT    = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_TABLE    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TABLE_H  = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_STAT     = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_STAT_LBL = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Components ──────────────────────────────────────────────────
    private JTextField txtName, txtEmail, txtCourse, txtAge;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalStudents;
    private final StudentDAO studentDAO;

    public StudentManagementUI() {
        studentDAO = new StudentDAO();
        initUI();
        addSampleData();
        refreshTable();
    }

    private void initUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        // ── Header ──────────────────────────────────────────────────
        add(createHeader(), BorderLayout.NORTH);

        // ── Main Content ────────────────────────────────────────────
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createFormPanel(), createTablePanel());
        splitPane.setDividerLocation(360);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        splitPane.setBackground(BG_DARK);
        add(splitPane, BorderLayout.CENTER);

        // ── Footer ──────────────────────────────────────────────────
        add(createFooter(), BorderLayout.SOUTH);
    }

    // ═══════════════════════════════════════════════════════════════
    //  HEADER
    // ═══════════════════════════════════════════════════════════════
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY, getWidth(), 0, PRIMARY_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(12, 25, 12, 25));

        JLabel title = new JLabel("📚  Student Management System");
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        // Stats card in header
        JPanel statsPanel = createStatsCard();
        header.add(statsPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createStatsCard() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        lblTotalStudents = new JLabel("0");
        lblTotalStudents.setFont(FONT_STAT);
        lblTotalStudents.setForeground(Color.WHITE);

        JLabel lbl = new JLabel("Total Students");
        lbl.setFont(FONT_STAT_LBL);
        lbl.setForeground(new Color(200, 210, 230));

        JPanel inner = new JPanel(new GridLayout(2, 1, 0, -4));
        inner.setOpaque(false);
        inner.add(lblTotalStudents);
        inner.add(lbl);
        panel.add(inner);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════════
    //  LEFT — ADD STUDENT FORM
    // ═══════════════════════════════════════════════════════════════
    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 10));

        // Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Section title
        JLabel sectionTitle = new JLabel("Add New Student");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sectionTitle);

        JLabel sectionSub = new JLabel("Fill in the details below to register a student.");
        sectionSub.setFont(FONT_SUBTITLE);
        sectionSub.setForeground(TEXT_SECONDARY);
        sectionSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sectionSub);
        card.add(Box.createVerticalStrut(20));

        // Fields
        txtName = createField(card, "Full Name");
        txtEmail = createField(card, "Email Address");
        txtCourse = createField(card, "Programme / Course");
        txtAge = createField(card, "Age");

        card.add(Box.createVerticalStrut(10));

        // Buttons row
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton btnAdd = createStyledButton("✚  Add Student", SUCCESS, SUCCESS.darker());
        btnAdd.addActionListener(e -> addStudent());
        btnRow.add(btnAdd);

        JButton btnClear = createStyledButton("✖  Clear", BG_INPUT, BORDER_COLOR);
        btnClear.addActionListener(e -> clearForm());
        btnRow.add(btnClear);

        card.add(btnRow);

        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Helper: creates a labelled text-field and adds it to the parent panel.
     */
    private JTextField createField(JPanel parent, String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));

        JTextField field = new JTextField();
        field.setFont(FONT_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_INPUT);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createVerticalStrut(14));

        return field;
    }

    // ═══════════════════════════════════════════════════════════════
    //  RIGHT — TABLE VIEW
    // ═══════════════════════════════════════════════════════════════
    private JPanel createTablePanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_DARK);
        wrapper.setBorder(new EmptyBorder(20, 10, 20, 20));

        // Card
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel tableLbl = new JLabel("Student Records");
        tableLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tableLbl.setForeground(TEXT_PRIMARY);
        topBar.add(tableLbl, BorderLayout.WEST);

        JButton btnDelete = createStyledButton("🗑  Delete Selected", DANGER, DANGER_HOVER);
        btnDelete.setPreferredSize(new Dimension(170, 36));
        btnDelete.addActionListener(e -> deleteStudent());
        topBar.add(btnDelete, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Email", "Course", "Age"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        styleTable(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(BG_DARK);
        card.add(scrollPane, BorderLayout.CENTER);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setForeground(TEXT_PRIMARY);
        table.setBackground(BG_DARK);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(36);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_H);
        header.setBackground(TABLE_HEADER);
        header.setForeground(TEXT_SECONDARY);
        header.setBorder(new LineBorder(BORDER_COLOR));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);  // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(200);  // Email
        table.getColumnModel().getColumn(3).setPreferredWidth(160);  // Course
        table.getColumnModel().getColumn(4).setPreferredWidth(60);   // Age
        table.getColumnModel().getColumn(4).setMaxWidth(70);

        // Alternating row colours
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BG_DARK : TABLE_ROW_ALT);
                    setForeground(TEXT_PRIMARY);
                }
                return this;
            }
        });
    }

    // ═══════════════════════════════════════════════════════════════
    //  FOOTER
    // ═══════════════════════════════════════════════════════════════
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BG_DARK);
        footer.setBorder(new EmptyBorder(6, 0, 10, 0));

        JLabel lbl = new JLabel("Student Management System  •  Java Swing Application");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_SECONDARY);
        footer.add(lbl);

        return footer;
    }

    // ═══════════════════════════════════════════════════════════════
    //  BUTTON FACTORY
    // ═══════════════════════════════════════════════════════════════
    private JButton createStyledButton(String text, Color bg, Color hoverBg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });

        return btn;
    }

    // ═══════════════════════════════════════════════════════════════
    //  ACTIONS
    // ═══════════════════════════════════════════════════════════════

    private void addStudent() {
        String name   = txtName.getText().trim();
        String email  = txtEmail.getText().trim();
        String course = txtCourse.getText().trim();
        String ageStr = txtAge.getText().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || course.isEmpty() || ageStr.isEmpty()) {
            showMessage("Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 1 || age > 120) {
                showMessage("Please enter a valid age (1-120).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            showMessage("Age must be a number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMessage("Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        studentDAO.addStudent(name, email, course, age);
        refreshTable();
        clearForm();
        showMessage("Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student:\n\n" +
                "   ID: " + id + "\n   Name: " + name + "\n",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            studentDAO.deleteStudent(id);
            refreshTable();
            showMessage("Student deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getCourse(), s.getAge()
            });
        }
        lblTotalStudents.setText(String.valueOf(studentDAO.getStudentCount()));
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtCourse.setText("");
        txtAge.setText("");
        txtName.requestFocusInWindow();
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // ═══════════════════════════════════════════════════════════════
    //  SAMPLE DATA
    // ═══════════════════════════════════════════════════════════════
    private void addSampleData() {
        studentDAO.addStudent("James Moyo", "james.moyo@nust.ac.zw", "Computer Science", 21);
        studentDAO.addStudent("Tatenda Chikwanha", "t.chikwanha@nust.ac.zw", "Information Systems", 22);
        studentDAO.addStudent("Rudo Maphosa", "r.maphosa@nust.ac.zw", "Software Engineering", 20);
    }
}
