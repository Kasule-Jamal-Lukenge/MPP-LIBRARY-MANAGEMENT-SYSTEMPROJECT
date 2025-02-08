package librarysystem.ui.books;

import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Book Details Popup
class BookDetailsFrame extends JFrame {
    private SystemController systemController = new SystemController();
    private Runnable refreshBookTable;
    private JTable bookTable;
    private DefaultTableModel model;
    private String role;

    public BookDetailsFrame(Runnable refreshBookTableMethod, JTable bookTable, DefaultTableModel model, String isbn, String title, String authors, String maxCheckout, int numCopies, int availableCopies, String role) {
        setTitle("Book Details - " + title);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));
        this.refreshBookTable = refreshBookTableMethod;
        this.bookTable = bookTable; // Assign passed bookTable
        this.model = model;
        this.role = role;
        systemController = new SystemController();

        add(new JLabel("ISBN:"));
        add(new JLabel(isbn));
        add(new JLabel("Title:"));
        add(new JLabel(title));
        add(new JLabel("Authors:"));
        add(new JLabel(authors));
        add(new JLabel("Max Checkout:"));
        add(new JLabel(maxCheckout));

        // Role-based Buttons
        if (role.equals("ADMIN") || role.equals("BOTH")) {
            JButton addCopyButton = new JButton("Add Copy");
            addCopyButton.addActionListener(e -> {
                // Add a copy to the system
                systemController.addBookCopy(isbn, numCopies); // Assuming addBookCopy is defined to add a copy

                // Refresh the book table
                this.refreshBookTable.run();
                JOptionPane.showMessageDialog(this, "Copy Added");
            });
            add(addCopyButton);
        }

        if (role.equals("LIBRARIAN") || role.equals("BOTH")) {
            JButton checkoutButton = new JButton("Checkout A Copy");
            checkoutButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Book Checked Out"));
            add(checkoutButton);
        }

        setVisible(true);
    }
    // Other methods...

}


