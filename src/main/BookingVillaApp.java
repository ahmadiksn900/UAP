import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class BookingVillaApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }

    public static class LoginFrame extends JFrame {
        private static JTextField usernameField;
        private static JPasswordField passwordField;
        private JButton loginButton;
        private JLabel messageLabel;
        private static Image backgroundImage;

        public LoginFrame() {
            setTitle("Login");
            setSize(450, 450);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            backgroundImage = new ImageIcon("C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\baground2.jpg").getImage();

            JPanel headerPanel = new JPanel();
            JLabel headerLabel = new JLabel("Booking Villa");
            headerLabel.setFont(new Font("Forte", Font.BOLD, 24));
            headerLabel.setForeground(new Color(118, 60, 175));
            headerPanel.add(headerLabel);
            add(headerPanel, BorderLayout.NORTH);

            JPanel contentPanel = new BackgroundPanel();
            contentPanel.setLayout(new GridBagLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            add(contentPanel, BorderLayout.CENTER);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel usernameLabel = new JLabel("Username:");
            usernameLabel.setFont(new Font("Bodoni MT", Font.BOLD, 18));
            usernameLabel.setForeground(new Color(255, 255, 255));
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(usernameLabel, gbc);

            usernameField = new JTextField(10);
            gbc.gridx = 1;
            contentPanel.add(usernameField, gbc);

            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setFont(new Font("Bodoni MT", Font.BOLD, 18));
            passwordLabel.setForeground(new Color(255, 255, 255));
            gbc.gridx = 0;
            gbc.gridy = 1;
            contentPanel.add(passwordLabel, gbc);

            passwordField = new JPasswordField(20);
            gbc.gridx = 1;
            contentPanel.add(passwordField, gbc);

            messageLabel = new JLabel("");
            messageLabel.setForeground(Color.RED);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            contentPanel.add(messageLabel, gbc);

            loginButton = new JButton("Login");
            styleBut    ton(loginButton);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            contentPanel.add(loginButton, gbc);

            loginButton.addActionListener(new LoginButtonListener());

            setVisible(true);
        }

        private void styleButton(JButton button) {
            button.setBackground(new Color(118, 60, 175));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Bodoni MT", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Change color on mouse hover
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(161, 160, 160));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(118, 60, 175));
                }
            });
        }

        public class LoginButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin")) {
                    dispose();
                    new BookingVillaFrame();
                } else {
                    messageLabel.setText("Username atau password tidak valid");
                }
            }
        }

        public static class BackgroundPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }
    }
}

class BookingVillaFrame extends JFrame {
    private JComboBox<String> villaComboBox;
    private JTextField nameField, checkInField, checkOutField;
    private JLabel totalCostLabel;
    private JButton calculateButton, bookButton, changeStatusButton, deleteBookingButton, viewImagesButton;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    private final String[] villas = {"Pilih Villa", "Villa A", "Villa B", "Villa C", "Villa D", "Villa E"};
    private final int[] prices = {0, 100, 125, 170, 220, 285};
    private final XWPFDocument document = new XWPFDocument();
    private final XWPFDocument checkoutDocument = new XWPFDocument();
    private final String checkintFileName = "Data_Checkin_Villa.docx";
    private final String checkoutFileName = "Data_Checkout_Villa.docx";

    public BookingVillaFrame() {
        setTitle("App Boking Villa");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        createHeaderPanel();
        createMainContentPanel();
        addManagementTitle();

        styleButtons();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveDocument();
            }
        });

        setVisible(true);
    }

    private void styleButton(JButton button, Color backgroundColor, Color hoverColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Bodoni MT", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    private void styleButtons() {
        Color primaryColor = new Color(118, 60, 175);
        Color hoverColor = new Color(161, 160, 160);
        Color textColor = Color.WHITE;

        styleButton(calculateButton, primaryColor, hoverColor, textColor);
        styleButton(bookButton, primaryColor, hoverColor, textColor);
        styleButton(changeStatusButton, primaryColor, hoverColor, textColor);
        styleButton(deleteBookingButton, primaryColor, hoverColor, textColor);
        styleButton(viewImagesButton, primaryColor, hoverColor, textColor);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(118, 60, 175));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("App Management Booking Villa", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Forte", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);

        headerPanel.add(headerLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainContentPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 16));

        JPanel bookingPanel = createBookingPanel();
        JScrollPane scrollPane = new JScrollPane(bookingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tabbedPane.addTab("Pemesanan", scrollPane);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createBookingPanel() {
        JPanel bookingPanel = new JPanel(new GridBagLayout());
        bookingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        bookingPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Berlin Sans FB Demi", Font.PLAIN, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel villaLabel = new JLabel("Pilih Villa:");
        villaLabel.setFont(labelFont);
        bookingPanel.add(villaLabel, gbc);

        gbc.gridx = 1;
        villaComboBox = new JComboBox<>(villas);
        villaComboBox.setFont(new Font("Clarendon BT", Font.PLAIN, 14));
        bookingPanel.add(villaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Nama Pemesan:");
        nameLabel.setFont(labelFont);
        bookingPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setFont(new Font("Clarendon BT", Font.PLAIN, 14));
        bookingPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel checkInLabel = new JLabel("Tanggal Check-in (YYYY-MM-DD):");
        checkInLabel.setFont(labelFont);
        bookingPanel.add(checkInLabel, gbc);

        gbc.gridx = 1;
        checkInField = new JTextField();
        checkInField.setFont(new Font("Clarendon BT", Font.PLAIN, 14));
        bookingPanel.add(checkInField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel checkOutLabel = new JLabel("Tanggal Check-out (YYYY-MM-DD):");
        checkOutLabel.setFont(labelFont);
        bookingPanel.add(checkOutLabel, gbc);

        gbc.gridx = 1;
        checkOutField = new JTextField();
        checkOutField.setFont(new Font("Clarendon BT", Font.PLAIN, 14));
        bookingPanel.add(checkOutField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel totalCostLabelLabel = new JLabel("Total Biaya:");
        totalCostLabelLabel.setFont(labelFont);
        bookingPanel.add(totalCostLabelLabel, gbc);

        gbc.gridx = 1;
        totalCostLabel = new JLabel(formatCurrency(0));
        totalCostLabel.setFont(new Font("Clarendon Blk BT", Font.ITALIC, 14));
        totalCostLabel.setForeground(new Color(24, 151, 24));
        bookingPanel.add(totalCostLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        calculateButton = new JButton("Hitung Biaya");
        bookingPanel.add(calculateButton, gbc);

        gbc.gridx = 1;
        bookButton = new JButton("Pesan Sekarang");
        bookingPanel.add(bookButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        changeStatusButton = new JButton("Ubah Status");
        bookingPanel.add(changeStatusButton, gbc);

        gbc.gridx = 1;
        deleteBookingButton = new JButton("Hapus Pemesanan");
        bookingPanel.add(deleteBookingButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        viewImagesButton = new JButton("Lihat Gambar Villa");
        bookingPanel.add(viewImagesButton, gbc);

        String[] columnNames = {"ID", "Nama Pemesan", "Villa", "Tanggal Check-in", "Tanggal Check-out", "Total Biaya", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookingTable = new JTable(tableModel);
        bookingTable.setFont(new Font("Clarendon Lt BT", Font.PLAIN, 14)); // Set font untuk JTable
        bookingTable.getTableHeader().setFont(new Font("Clarendon Lt BT", Font.BOLD, 13));
        JScrollPane tableScrollPane = new JScrollPane(bookingTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200));
        gbc.gridy = 8;
        bookingPanel.add(tableScrollPane, gbc);

        calculateButton.addActionListener(new CalculateCostListener());
        bookButton.addActionListener(new BookVillaListener());
        changeStatusButton.addActionListener(e -> changeBookingStatus());
        deleteBookingButton.addActionListener(e -> deleteBooking());
        viewImagesButton.addActionListener(e -> viewVillaImages());

        return bookingPanel;
    }

    private void viewVillaImages() {
        int selectedVillaIndex = villaComboBox.getSelectedIndex();
        if (selectedVillaIndex == 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih villa", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String villaName = villas[selectedVillaIndex];
        String[] selectedImages = getVillaImages(villaName);

        if (selectedImages != null && selectedImages.length > 0) {
            JFrame imageFrame = new JFrame("Gambar Villa: " + villaName);
            imageFrame.setLayout(new FlowLayout());
            imageFrame.setSize(360, 360);

            JPanel imagePanel = new JPanel();
            for (String imagePath : selectedImages) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage();
                    Image scaledImg = img.getScaledInstance(320, 300, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImg);
                    JLabel imageLabel = new JLabel(icon);
                    imagePanel.add(imageLabel);
                }
            }

            JScrollPane scrollPane = new JScrollPane(imagePanel);
            imageFrame.add(scrollPane);
            imageFrame.setLocationRelativeTo(this);
            imageFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Villa ini tidak memiliki gambar.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String[] getVillaImages(String villaName) {
        String[] images = null;

        switch (villaName) {
            case "Villa A":
                images = new String[]{"C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\Villa1.jpg"};
                break;
            case "Villa B":
                images = new String[]{"C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\Villa2.jpg"};
                break;
            case "Villa C":
                images = new String[]{"C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\Villa3.jpg"};
                break;
            case "Villa D":
                images = new String[]{"C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\Villa4.jpg"};
                break;
            case "Villa E":
                images = new String[]{"C:\\Users\\Nabilla Ayu\\OneDrive\\Pictures\\gambar\\Villa5.jpg"};
                break;
            default:
                break;
        }
        return images;
    }

    private class BookVillaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int bookingId = tableModel.getRowCount() + 1;

                String name = nameField.getText().trim();
                if (name.isEmpty()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
                int selectedVillaIndex = villaComboBox.getSelectedIndex();
                if (selectedVillaIndex == 0) throw new IllegalArgumentException("Silakan pilih villa.");
                String checkInStr = checkInField.getText().trim();
                String checkOutStr = checkOutField.getText().trim();
                if (checkInStr.isEmpty() || checkOutStr.isEmpty()) throw new IllegalArgumentException("Tanggal check-in dan check-out harus diisi.");

                if (totalCostLabel.getText().equals(formatCurrency(0))) {
                    throw new IllegalArgumentException("Silakan hitung biaya terlebih dahulu.");
                }

                String villaName = villas[selectedVillaIndex];
                String[] selectedImages = getVillaImages(villaName);
                if (selectedImages == null || selectedImages.length == 0) {
                    throw new IllegalArgumentException("Gambar untuk villa ini tidak tersedia.");
                }

                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setFontFamily("Times New Roman");
                run.setFontSize(12);
                run.setBold(true);

                run.setText("ID Pemesanan: " + bookingId);
                run.addBreak();
                run.addBreak();

                File imageFile = new File(selectedImages[0]);
                if (imageFile.exists()) {
                    try (FileInputStream imageStream = new FileInputStream(imageFile)) {
                        run.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, imageFile.getName(), Units.toEMU(200), Units.toEMU(150));
                        run.addBreak();
                    }
                }

                run.setText("Nama Pemesan: " + name);
                run.addBreak();
                run.setText("Villa: " + villaName);
                run.addBreak();
                run.setText("Tanggal Check-in: " + checkInStr);
                run.addBreak();
                run.setText("Tanggal Check-out: " + checkOutStr);
                run.addBreak();
                run.setText("Total Biaya: " + totalCostLabel.getText());
                run.addBreak();
                run.setText("Status: Check-in");
                run.addBreak();
                run.setText("-------------------------------------------------");

                saveDocument();

                String[] rowData = {
                        String.valueOf(bookingId),
                        name,
                        villaName,
                        checkInStr,
                        checkOutStr,
                        totalCostLabel.getText(),
                        "Check-in"
                };
                tableModel.addRow(rowData);
                JOptionPane.showMessageDialog(BookingVillaFrame.this, "Pemesanan berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                villaComboBox.setSelectedIndex(0);
                nameField.setText("");
                checkInField.setText("");
                checkOutField.setText("");
                totalCostLabel.setText(formatCurrency(0));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(BookingVillaFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDocumentStatus(int bookingId, String newStatus) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            if (text.contains("ID Pemesanan: " + bookingId)) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String runText = run.getText(0);
                    if (runText != null && runText.contains("Status: ")) {
                        runText = runText.replaceFirst("Status: .*", "Status: " + newStatus);
                        run.setText(runText, 0);
                    }
                }
            }
        }
        saveDocument();
    }

    private void changeBookingStatus() {
        String inputId = JOptionPane.showInputDialog(this, "Masukkan ID Pemesanan:");
        if (inputId == null || inputId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Pemesanan tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int bookingId = Integer.parseInt(inputId.trim());
            boolean found = false;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int id = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                if (id == bookingId) {
                    String currentStatus = (String) tableModel.getValueAt(i, 6);
                    if ("Check-in".equals(currentStatus)) {
                        tableModel.setValueAt("Check-out", i, 6);

                        updateDocumentStatus(bookingId, "Check-out"); // Update status in docx

                        JOptionPane.showMessageDialog(this, "Status diubah menjadi Check-out.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Pemesanan sudah Check-out.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "ID Pemesanan tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Pemesanan harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBooking() {
        String inputId = JOptionPane.showInputDialog(this, "Masukkan ID Pemesanan yang sudah Check-out:");
        if (inputId == null || inputId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Pemesanan tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int bookingId = Integer.parseInt(inputId.trim());
            boolean found = false;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int id = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                if (id == bookingId) {
                    String currentStatus = (String) tableModel.getValueAt(i, 6);
                    if ("Check-out".equals(currentStatus)) {
                        String name = (String) tableModel.getValueAt(i, 1);
                        String villaName = (String) tableModel.getValueAt(i, 2);
                        String checkInStr = (String) tableModel.getValueAt(i, 3);
                        String checkOutStr = (String) tableModel.getValueAt(i, 4);
                        String totalCost = (String) tableModel.getValueAt(i, 5);

                        saveToCheckoutDocument(bookingId, name, villaName, checkInStr, checkOutStr, totalCost);

                        tableModel.removeRow(i);

                        removeBookingFromDocument(bookingId);
                        saveDocument();

                        JOptionPane.showMessageDialog(this, "Pemesanan berhasil dihapus dan disalin ke dokumen Check-out.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Pesanan belum Check-out, tidak bisa dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "ID Pemesanan tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Pemesanan harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeBookingFromDocument(int bookingId) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            if (text.contains("ID Pemesanan: " + bookingId)) {
                document.removeBodyElement(document.getPosOfParagraph(paragraph));
                break;
            }
        }
        saveDocument();
    }

    private void saveToCheckoutDocument(int bookingId, String name, String villaName, String checkInStr, String checkOutStr, String totalCost) {
        try {
            XWPFParagraph paragraph = checkoutDocument.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setBold(true);

            run.setText("ID Pemesanan: " + bookingId);
            run.addBreak();
            run.addBreak();

            String[] selectedImages = getVillaImages(villaName);
            if (selectedImages != null && selectedImages.length > 0) {
                File imageFile = new File(selectedImages[0]);
                if (imageFile.exists()) {
                    try (FileInputStream imageStream = new FileInputStream(imageFile)) {
                        run.addPicture(imageStream, XWPFDocument.PICTURE_TYPE_JPEG, imageFile.getName(), Units.toEMU(200), Units.toEMU(150));
                        run.addBreak();
                    }
                }
            }

            run.setText("Nama Pemesanan: " + name);
            run.addBreak();
            run.setText("Villa: " + villaName);
            run.addBreak();
            run.setText("Tanggal Check-in: " + checkInStr);
            run.addBreak();
            run.setText("Tanggal Check-out: " + checkOutStr);
            run.addBreak();
            run.setText("Total Biaya: " + totalCost);
            run.addBreak();
            run.setText("Status: Check-out");
            run.addBreak();
            run.setText("-------------------------------------------------");

            saveCheckoutDocument();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke dokumen Check-out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addManagementTitle() {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = titleParagraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(20);
        run.setBold(true);
        run.setText("Manajemen Booking");
        run.addBreak();
        saveDocument();
    }

    private void saveDocument() {
        try (FileOutputStream out = new FileOutputStream(checkintFileName)) {
            document.write(out);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan dokumen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCheckoutDocument() {
        try (FileOutputStream out = new FileOutputStream(checkoutFileName)) {
            checkoutDocument.write(out);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan dokumen Check-out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormat.format(amount);
    }

    private class CalculateCostListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String checkInDateStr = checkInField.getText().trim();
                String checkOutDateStr = checkOutField.getText().trim();

                if (checkInDateStr.isEmpty() || checkOutDateStr.isEmpty()) {
                    throw new IllegalArgumentException("Tanggal check-in dan check-out harus diisi.");
                }

                LocalDate checkInDate = LocalDate.parse(checkInDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate checkOutDate = LocalDate.parse(checkOutDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                if (daysBetween < 1) {
                    throw new IllegalArgumentException("Minimal nginap satu malam");
                }

                int selectedVillaIndex = villaComboBox.getSelectedIndex();
                if (selectedVillaIndex == 0) throw new IllegalArgumentException("Silakan pilih villa.");

                int pricePerNight = prices[selectedVillaIndex];
                double totalCost = pricePerNight * daysBetween;
                totalCostLabel.setText(formatCurrency(totalCost));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(BookingVillaFrame.this, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}