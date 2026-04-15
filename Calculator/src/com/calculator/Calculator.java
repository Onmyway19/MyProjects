package com.calculator;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A modern, dark-themed Swing calculator application.
 * Supports: +, -, ×, ÷, %, sign toggle, decimal, and clear.
 * Includes full input validation and error handling.
 */
public class Calculator extends JFrame {

    // ── Colour Palette ──────────────────────────────────────────
    private static final Color BG_FRAME     = new Color(22, 22, 30);
    private static final Color BG_DISPLAY   = new Color(30, 30, 42);
    private static final Color BG_NUM       = new Color(44, 44, 58);
    private static final Color BG_NUM_HOVER = new Color(58, 58, 75);
    private static final Color BG_OP        = new Color(108, 99, 255);
    private static final Color BG_OP_HOVER  = new Color(130, 122, 255);
    private static final Color BG_FUNC      = new Color(55, 55, 72);
    private static final Color BG_FUNC_HOVER= new Color(70, 70, 90);
    private static final Color TEXT_WHITE    = new Color(235, 235, 245);
    private static final Color TEXT_MUTED    = new Color(145, 145, 165);
    private static final Color ACCENT       = new Color(108, 99, 255);
    private static final Color DANGER       = new Color(255, 82, 82);

    // ── Fonts ───────────────────────────────────────────────────
    private static final Font FONT_DISPLAY  = new Font("Segoe UI", Font.PLAIN, 48);
    private static final Font FONT_EXPR     = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_BTN      = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BTN_SM   = new Font("Segoe UI", Font.BOLD, 18);

    // ── State ───────────────────────────────────────────────────
    private double firstOperand = 0;
    private String operator = "";
    private boolean startNewInput = true;
    private boolean hasError = false;

    // ── Components ──────────────────────────────────────────────
    private JLabel displayLabel;
    private JLabel expressionLabel;

    public Calculator() {
        initUI();
    }

    private void initUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG_FRAME);
        setLayout(new BorderLayout(0, 0));

        // ── Display Panel ───────────────────────────────────────
        add(createDisplay(), BorderLayout.NORTH);

        // ── Button Grid ─────────────────────────────────────────
        add(createButtonPanel(), BorderLayout.CENTER);

        pack();
        setSize(380, 600);
        setLocationRelativeTo(null);

        // Keyboard support
        addKeyboardBindings();
    }

    // ═══════════════════════════════════════════════════════════
    //  DISPLAY
    // ═══════════════════════════════════════════════════════════
    private JPanel createDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_DISPLAY);
        panel.setBorder(new EmptyBorder(30, 24, 20, 24));
        panel.setPreferredSize(new Dimension(380, 140));

        // Expression line (e.g. "25 + 10")
        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(FONT_EXPR);
        expressionLabel.setForeground(TEXT_MUTED);
        expressionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        panel.add(expressionLabel);
        panel.add(Box.createVerticalStrut(8));

        // Main display
        displayLabel = new JLabel("0");
        displayLabel.setFont(FONT_DISPLAY);
        displayLabel.setForeground(TEXT_WHITE);
        displayLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.add(displayLabel);

        // Subtle bottom border
        JPanel border = new JPanel();
        border.setPreferredSize(new Dimension(0, 1));
        border.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        border.setBackground(new Color(60, 60, 80));
        panel.add(Box.createVerticalStrut(12));
        panel.add(border);

        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    //  BUTTON GRID
    // ═══════════════════════════════════════════════════════════
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 8, 8));
        panel.setBackground(BG_FRAME);
        panel.setBorder(new EmptyBorder(12, 16, 20, 16));

        // Row 1: AC  ±  %  ÷
        panel.add(makeButton("AC",  BG_FUNC, BG_FUNC_HOVER, DANGER,      FONT_BTN_SM, this::onFunction));
        panel.add(makeButton("±",   BG_FUNC, BG_FUNC_HOVER, TEXT_WHITE,   FONT_BTN_SM, this::onFunction));
        panel.add(makeButton("%",   BG_FUNC, BG_FUNC_HOVER, TEXT_WHITE,   FONT_BTN_SM, this::onFunction));
        panel.add(makeButton("÷",   BG_OP,   BG_OP_HOVER,   TEXT_WHITE,   FONT_BTN,    this::onOperator));

        // Row 2: 7  8  9  ×
        panel.add(makeButton("7", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("8", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("9", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("×", BG_OP,  BG_OP_HOVER,  TEXT_WHITE, FONT_BTN, this::onOperator));

        // Row 3: 4  5  6  −
        panel.add(makeButton("4", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("5", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("6", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("−", BG_OP,  BG_OP_HOVER,  TEXT_WHITE, FONT_BTN, this::onOperator));

        // Row 4: 1  2  3  +
        panel.add(makeButton("1", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("2", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("3", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("+", BG_OP,  BG_OP_HOVER,  TEXT_WHITE, FONT_BTN, this::onOperator));

        // Row 5: 0 (wide)  .  =
        panel.add(makeWideButton("0", BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton(".",  BG_NUM, BG_NUM_HOVER, TEXT_WHITE, FONT_BTN, this::onDigit));
        panel.add(makeButton("=",  BG_OP,  BG_OP_HOVER,  TEXT_WHITE, FONT_BTN, this::onEquals));

        return panel;
    }

    // ═══════════════════════════════════════════════════════════
    //  BUTTON FACTORY
    // ═══════════════════════════════════════════════════════════
    private JButton makeButton(String text, Color bg, Color hoverBg, Color fg, Font font,
                               java.util.function.Consumer<String> action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(btn, bg, hoverBg, fg, font);
        btn.addActionListener(e -> action.accept(text));
        return btn;
    }

    /**
     * Creates the wide "0" button — spans 2 columns via a wrapper panel.
     */
    private JPanel makeWideButton(String text, Color bg, Color hoverBg, Color fg, Font font,
                                  java.util.function.Consumer<String> action) {
        // We can't span columns in GridLayout, so we use a panel trick
        // Instead, the "0" just occupies one cell. For a true wide button
        // you'd need GridBagLayout — but this keeps the code simpler.
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(btn, bg, hoverBg, fg, font);
        btn.addActionListener(e -> action.accept(text));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);
        return wrapper;
    }

    private void styleButton(JButton btn, Color bg, Color hoverBg, Color fg, Font font) {
        btn.setFont(font);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 70));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
            @Override public void mousePressed(MouseEvent e)  { btn.setBackground(hoverBg.darker()); }
            @Override public void mouseReleased(MouseEvent e) { btn.setBackground(hoverBg); }
        });
    }

    // ═══════════════════════════════════════════════════════════
    //  ACTIONS
    // ═══════════════════════════════════════════════════════════

    private void onDigit(String digit) {
        if (hasError) clearAll();

        String current = displayLabel.getText();

        if (digit.equals(".")) {
            // Input validation: prevent multiple decimals
            if (current.contains(".") && !startNewInput) return;
            if (startNewInput) {
                displayLabel.setText("0.");
                startNewInput = false;
                return;
            }
        }

        if (startNewInput) {
            displayLabel.setText(digit.equals(".") ? "0." : digit);
            startNewInput = false;
        } else {
            // Input validation: cap display length
            if (current.length() >= 15) return;
            displayLabel.setText(current + digit);
        }

        adjustFontSize();
    }

    private void onOperator(String op) {
        if (hasError) return;

        if (!operator.isEmpty() && !startNewInput) {
            calculate();
        }

        firstOperand = parseDisplay();
        operator = op;
        startNewInput = true;
        updateExpression();
    }

    private void onEquals(String ignored) {
        if (hasError || operator.isEmpty()) return;
        calculate();
        operator = "";
        expressionLabel.setText(" ");
    }

    private void onFunction(String func) {
        switch (func) {
            case "AC":
                clearAll();
                break;
            case "±":
                if (hasError) return;
                double val = parseDisplay();
                if (val != 0) {
                    setDisplay(val * -1);
                }
                break;
            case "%":
                if (hasError) return;
                setDisplay(parseDisplay() / 100.0);
                break;
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  CALCULATION ENGINE
    // ═══════════════════════════════════════════════════════════
    private void calculate() {
        double secondOperand = parseDisplay();
        double result;

        switch (operator) {
            case "+":  result = firstOperand + secondOperand; break;
            case "−":  result = firstOperand - secondOperand; break;
            case "×":  result = firstOperand * secondOperand; break;
            case "÷":
                // Input validation: division by zero
                if (secondOperand == 0) {
                    showError("Cannot divide by zero");
                    return;
                }
                result = firstOperand / secondOperand;
                break;
            default:
                return;
        }

        // Overflow / NaN validation
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            showError("Math Error");
            return;
        }

        setDisplay(result);
        firstOperand = result;
        startNewInput = true;
    }

    // ═══════════════════════════════════════════════════════════
    //  DISPLAY HELPERS
    // ═══════════════════════════════════════════════════════════
    private void setDisplay(double value) {
        if (value == (long) value && !Double.isInfinite(value)) {
            displayLabel.setText(String.valueOf((long) value));
        } else {
            // Limit to 10 decimal places and strip trailing zeros
            String formatted = String.format("%.10f", value)
                    .replaceAll("0+$", "")
                    .replaceAll("\\.$", "");
            displayLabel.setText(formatted);
        }
        adjustFontSize();
    }

    private double parseDisplay() {
        try {
            return Double.parseDouble(displayLabel.getText().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateExpression() {
        String numStr;
        if (firstOperand == (long) firstOperand) {
            numStr = String.valueOf((long) firstOperand);
        } else {
            numStr = String.valueOf(firstOperand);
        }
        expressionLabel.setText(numStr + " " + operator);
    }

    private void showError(String message) {
        displayLabel.setText(message);
        displayLabel.setForeground(DANGER);
        hasError = true;
        startNewInput = true;
    }

    private void clearAll() {
        displayLabel.setText("0");
        displayLabel.setFont(FONT_DISPLAY);
        displayLabel.setForeground(TEXT_WHITE);
        expressionLabel.setText(" ");
        firstOperand = 0;
        operator = "";
        startNewInput = true;
        hasError = false;
    }

    /**
     * Shrinks font size when display text gets long.
     */
    private void adjustFontSize() {
        int len = displayLabel.getText().length();
        if (len > 12)      displayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        else if (len > 9)  displayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        else               displayLabel.setFont(FONT_DISPLAY);
    }

    // ═══════════════════════════════════════════════════════════
    //  KEYBOARD SUPPORT
    // ═══════════════════════════════════════════════════════════
    private void addKeyboardBindings() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        // Digits 0-9
        for (int i = 0; i <= 9; i++) {
            String d = String.valueOf(i);
            im.put(KeyStroke.getKeyStroke(d.charAt(0)), "digit_" + d);
            im.put(KeyStroke.getKeyStroke("NUMPAD" + d), "digit_" + d);
            am.put("digit_" + d, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) { onDigit(d); }
            });
        }

        // Decimal
        im.put(KeyStroke.getKeyStroke('.'), "dot");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0), "dot");
        am.put("dot", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { onDigit("."); }
        });

        // Operators
        bindKey(im, am, KeyStroke.getKeyStroke('+'),                     "add",  () -> onOperator("+"));
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0),      "add2", () -> onOperator("+"));
        bindKey(im, am, KeyStroke.getKeyStroke('-'),                      "sub",  () -> onOperator("−"));
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "sub2", () -> onOperator("−"));
        bindKey(im, am, KeyStroke.getKeyStroke('*'),                      "mul",  () -> onOperator("×"));
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, 0), "mul2", () -> onOperator("×"));
        bindKey(im, am, KeyStroke.getKeyStroke('/'),                      "div",  () -> onOperator("÷"));
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, 0),   "div2", () -> onOperator("÷"));

        // Equals
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),  "eq",  () -> onEquals("="));
        bindKey(im, am, KeyStroke.getKeyStroke('='),                    "eq2", () -> onEquals("="));

        // Clear
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear", () -> onFunction("AC"));
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "clear2", () -> onFunction("AC"));

        // Backspace
        bindKey(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "back", this::onBackspace);
    }

    private void bindKey(InputMap im, ActionMap am, KeyStroke ks, String name, Runnable action) {
        im.put(ks, name);
        am.put(name, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }

    private void onBackspace() {
        if (hasError) { clearAll(); return; }
        String current = displayLabel.getText();
        if (current.length() > 1) {
            displayLabel.setText(current.substring(0, current.length() - 1));
        } else {
            displayLabel.setText("0");
            startNewInput = true;
        }
        adjustFontSize();
    }

    // ═══════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ═══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
