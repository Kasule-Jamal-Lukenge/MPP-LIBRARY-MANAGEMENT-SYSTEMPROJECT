package librarysystem.ui.books;

import business.Author;
import business.Book;
import business.SystemController;
import business.Address;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddBookForm extends JFrame {
    private JTextField titleField;
    private JTextField isbnField;
    private JComboBox<Integer> checkoutLengthDropdown;
    private JPanel authorsPanel;
    private List<AuthorFields> authorFieldsList;
    private SystemController systemController;

    public AddBookForm() {
        systemController = new SystemController();
        authorFieldsList = new ArrayList<>();

        setTitle("Add New Book");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adds padding

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        titleField = new JTextField();
        isbnField = new JTextField();

        Integer[] checkoutOptions = {7, 21};
        checkoutLengthDropdown = new JComboBox<>(checkoutOptions);

        authorsPanel = new JPanel();
        authorsPanel.setLayout(new BoxLayout(authorsPanel, BoxLayout.Y_AXIS));

        JButton addAuthorButton = new JButton("Add Author");
        addAuthorButton.addActionListener(e -> addAuthorFields(true));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveBook());

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Max Checkout Length:(In days)"));
        formPanel.add(checkoutLengthDropdown);

        JPanel authorsContainer = new JPanel(new BorderLayout());
        authorsContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Adds padding around authors
        authorsContainer.add(new JLabel("Authors:"), BorderLayout.NORTH);
        authorsContainer.add(authorsPanel, BorderLayout.CENTER);
        authorsContainer.add(addAuthorButton, BorderLayout.SOUTH);

        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(authorsContainer, BorderLayout.CENTER);
        contentPanel.add(saveButton, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER); // Adds everything inside a padded panel

        addAuthorFields(false); // Add default author fields (cannot be removed)

        setVisible(true);
    }


    private void addAuthorFields(boolean removable) {
        AuthorFields authorFields = new AuthorFields(removable);
        authorFieldsList.add(authorFields);
        authorsPanel.add(authorFields);
        authorsPanel.revalidate();
        authorsPanel.repaint();
    }

    private void removeAuthorFields(AuthorFields authorFields) {
        authorFieldsList.remove(authorFields);
        authorsPanel.remove(authorFields);
        authorsPanel.revalidate();
        authorsPanel.repaint();
    }

    private void saveBook() {
        String title = titleField.getText().trim();
        String isbn = isbnField.getText().trim();
        int maxCheckoutLength = (int) checkoutLengthDropdown.getSelectedItem();

        if (title.isEmpty() || isbn.isEmpty() || authorFieldsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and add at least one author.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Author> authors = new ArrayList<>();
        for (AuthorFields fields : authorFieldsList) {
            String firstName = fields.firstNameField.getText().trim();
            String lastName = fields.lastNameField.getText().trim();
            String telephone = fields.telephoneField.getText().trim();
            String addressStr = fields.addressField.getText().trim();
            String bio = fields.bioField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || addressStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All author fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Address address = parseAddress(addressStr);
            if (address == null) {
                JOptionPane.showMessageDialog(this, "Invalid address format. Please use 'Street, City, State, Zip' format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            authors.add(new Author(firstName, lastName, telephone, address, bio));
        }

        if (authors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one valid author.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book newBook = new Book(isbn, title, maxCheckoutLength, authors);
        systemController.saveBook(newBook);
        JOptionPane.showMessageDialog(this, "Book Added Successfully!");
        dispose();
    }

    private Address parseAddress(String addressStr) {
        String[] parts = addressStr.split(",");
        if (parts.length == 4) {
            return new Address(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        }
        return null;
    }

    private class AuthorFields extends JPanel {
        private JTextField firstNameField;
        private JTextField lastNameField;
        private JTextField telephoneField;
        private JTextField addressField;
        private JTextField bioField;
        private JButton removeButton;

        public AuthorFields(boolean removable) {
            setLayout(new GridLayout(6, 2, 5, 5));

            firstNameField = new JTextField();
            lastNameField = new JTextField();
            telephoneField = new JTextField();
            addressField = new JTextField();
            bioField = new JTextField();

            add(new JLabel("First Name:"));
            add(firstNameField);
            add(new JLabel("Last Name:"));
            add(lastNameField);
            add(new JLabel("Telephone:"));
            add(telephoneField);
            add(new JLabel("Address (Street, City, State, Zip):"));
            add(addressField);
            add(new JLabel("Bio:"));
            add(bioField);

            if (removable) {
                removeButton = new JButton("Remove");
                removeButton.addActionListener(e -> removeAuthorFields(this));
                add(new JLabel()); // Placeholder for alignment
                add(removeButton);
            }
        }
    }
}
