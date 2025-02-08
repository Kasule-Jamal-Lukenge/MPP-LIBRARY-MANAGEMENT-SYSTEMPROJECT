package librarysystem.ui.books;

import business.Book;
import business.BookCopy;
import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class BookPanel extends JFrame {

    private SystemController systemController = new SystemController();
    private JTable bookTable;
    private DefaultTableModel model;
    private String role;

    public BookPanel(String role) {
        this.role = role;
        setTitle("Manage Books");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fetch all books from the systemController
        Map<String, Book> books = systemController.allBooks();
        System.out.println(books);

        // Prepare the columns for the table
        String[] columns = {"ISBN", "Title", "Authors", "Max Checkout", "num of Copies", "Available Copies"};

        // Initialize the DefaultTableModel with empty data
        model = new DefaultTableModel(columns, 0);
        // Loop through the books and add them to the table model
        for (Book book : books.values()) {
            String isbn = book.getIsbn();
            String title = book.getTitle();
            String authors = String.join(", ", book.getAuthors().stream()
                    .map(author -> author.getFirstName() + " " + author.getLastName()) // Assuming `Author` has a `getName()` method
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
        bookTable.setRowHeight(25); // Increase row height to 25 pixels
        // Set the border around the table
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5); // 1px black border
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(border); // Set the border for the JScrollPane

        // Add Buttons Based on Role
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        if (!role.equals("LIBRARIAN")) {
            addButton.addActionListener(e -> new AddBookForm(this::refreshBookTable));
            buttonPanel.add(addButton);
        }

        add(scrollPane, BorderLayout.CENTER); // Add the JScrollPane with the border
        add(buttonPanel, BorderLayout.SOUTH);

        // Handle Row Click (View Book Details)
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = bookTable.getSelectedRow();
                if (row >= 0) {
                    new BookDetailsFrame(
                            this::refreshBookTable,
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
    public void refreshBookTable() {
// Fetch all books from the systemController again
        Map<String, Book> books = systemController.allBooks();

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


