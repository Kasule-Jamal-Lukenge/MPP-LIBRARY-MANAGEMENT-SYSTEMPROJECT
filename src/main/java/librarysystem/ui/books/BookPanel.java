package librarysystem.ui.books;

import business.Book;
import business.BookCopy;
import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class BookPanel extends JFrame {

    private SystemController systemController = new SystemController();
    // maybe recheck above
    private JTable bookTable;
    private DefaultTableModel model;
    private String role;

    public BookPanel(String role) {
        this.role = role;
        setTitle("Manage Books");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fetch all books from the systemController
        Map<String, Book> books = systemController.allBooks();
        System.out.println(books);

        // Prepare the columns for the table
        String[] columns = {"ISBN", "Title", "Authors", "Max Checkout", "numOfCopies", "Available Copies"};

        // Initialize the DefaultTableModel with empty data
        model = new DefaultTableModel(columns, 0);
        // Loop through the books and add them to the table model
        for (Book book : books.values()) {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            String authors = String.join(", ", book.getAuthors().stream()
                    .map(author -> author.getFirstName() +" "+ author.getLastName()) // Assuming `Author` has a `getName()` method
                    .toArray(String[]::new));
            int maxCheckoutLength = book.getMaxCheckoutLength();
            int copies = book.getNumCopies(); // Get the number of copies
            int availableCopies = (int) Arrays.stream(book.getCopies())  // Stream the array of BookCopy objects
                    .filter((BookCopy b) -> b.isAvailable())  // Filter the copies where isAvailable is true
                    .count();  // Count the number of available copies

            // Add the book data to the model
            model.addRow(new Object[]{isbn, title, authors, maxCheckoutLength, copies, availableCopies});
        }

        bookTable = new JTable(model);

        // Add Buttons Based on Role
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        if (!role.equals("LIBRARIAN")) {
            addButton.addActionListener(e -> new AddBookForm());
            buttonPanel.add(addButton);
        }

        add(new JScrollPane(bookTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Handle Row Click (View Book Details)
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = bookTable.getSelectedRow();
                if (row >= 0) {
                    new BookDetailsFrame(
                            bookTable, // Pass the JTable here
                            model, // Pass the model here
                            model.getValueAt(row, 0).toString(),
                            model.getValueAt(row, 1).toString(),
                            model.getValueAt(row, 2).toString(),
                            model.getValueAt(row, 3).toString(),
                            Integer.parseInt(model.getValueAt(row, 4).toString()),
                            Integer.parseInt(model.getValueAt(row, 5).toString()),
                            role
                    );
                }
            }
        });

        setVisible(true);
    }
}

// Book Details Popup
class BookDetailsFrame extends JFrame {
    private SystemController systemController = new SystemController();
    private JTable bookTable;
    private DefaultTableModel model;
    private String role;

    public BookDetailsFrame(JTable bookTable, DefaultTableModel model, String isbn, String title, String authors, String maxCheckout, int numCopies, int availableCopies, String role) {
        setTitle("Book Details - " + title);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));
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
                refreshBookTable();
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

    private void refreshBookTable() {
// Fetch all books from the systemController again
        Map<String, Book> books = systemController.allBooks();

        // Prepare the columns for the table
        String[] columns = {"ISBN", "Title", "Authors", "Max Checkout", "numCopies", "Available Copies"};

        // Clear existing rows
        model.setRowCount(0);

        // Loop through the books and add them to the table model
        for (Book book : books.values()) {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            String authors = String.join(", ", book.getAuthors().stream()
                    .map(author -> author.getFirstName() + " " + author.getLastName())
                    .toArray(String[]::new));
            int maxCheckoutLength = book.getMaxCheckoutLength();
            int copies = book.getNumCopies(); // Get the number of copies
            int availableCopies = (int) Arrays.stream(book.getCopies())  // Stream the array of BookCopy objects
                    .filter((BookCopy b) -> b.isAvailable())  // Filter the copies where isAvailable is true
                    .count();
            // Add the updated book data to the model
            model.addRow(new Object[]{isbn, title, authors, maxCheckoutLength, copies, availableCopies});
        }

        // Notify the JTable to refresh the data
        bookTable.revalidate();
        bookTable.repaint();
    }
}


class AddBookForm extends JFrame {
    public AddBookForm() {
        setTitle("Add New Book");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField authorsField = new JTextField();
        JTextField checkoutLengthField = new JTextField();
        JButton saveButton = new JButton("Save");

        add(new JLabel("Title:"));
        add(titleField);
        add(new JLabel("ISBN:"));
        add(isbnField);
        add(new JLabel("Authors (comma-separated):"));
        add(authorsField);
        add(new JLabel("Max Checkout Length:"));
        add(checkoutLengthField);
        add(saveButton);

        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Book Added"));

        setVisible(true);
    }
}

