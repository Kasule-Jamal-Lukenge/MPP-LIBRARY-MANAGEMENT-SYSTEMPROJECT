package librarysystem.ui.members;

import business.Address;
import business.LibraryMember;
import business.SystemController;
import librarysystem.util.MemberUtil;

import javax.swing.*;
import java.awt.*;

public class AddMemberForm extends JFrame {
    private Runnable refreshCallback; // Callback to refresh member list
    private GridBagConstraints gbc;

    JLabel firstNameLabel;
    JTextField firstNameField;
    JLabel lastNameLabel;
    JTextField lastNameField;
    JLabel telephoneLabel;
    JTextField telephoneField;
    JLabel addressLabel;
    JLabel streetLabel;
    JTextField streetField;
    JLabel cityLabel;
    JTextField cityField;
    JLabel stateLabel;
    JTextField stateField;
    JLabel zipLabel;
    JTextField zipField;

    public AddMemberForm(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;

        setTitle("Add New Member");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Use GridBagLayout for better control

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Components
        firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(15); // No re-declaration
        lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(15);
        telephoneLabel = new JLabel("Telephone:");
        telephoneField = new JTextField(15);
        addressLabel = new JLabel("Address:");
        streetLabel = new JLabel("Street:");
        streetField = new JTextField(15);
        cityLabel = new JLabel("City:");
        cityField = new JTextField(15);
        stateLabel = new JLabel("State:");
        stateField = new JTextField(15);
        zipLabel = new JLabel("Zip Code:");
        zipField = new JTextField(15);
        JButton saveButton = new JButton("Save");

        // Add components to the form
        setGridConstraints();

        add(saveButton, gbc);

        // Save button action
        saveButton.addActionListener(e -> {
            // Capture data from the form
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String telephone = telephoneField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String zip = zipField.getText();

            // Generate a unique member ID
            String memberId = MemberUtil.generateMemberId();  // Generate random member ID

            // Call the addMember method to save the new member
            (new SystemController()).addMember(memberId,firstName,
                                    lastName, telephone, street,
                                    city, state, zip);

            // Show success message
            JOptionPane.showMessageDialog(this, "Member Added");

            // Refresh member table in MemberPanel
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            // Close the form after saving
            dispose();
        });

        setVisible(true);
    }

    private void setGridConstraints() {
        // Row 1 - First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(firstNameLabel, gbc);

        gbc.gridx = 1;
        add(firstNameField, gbc);

        // Row 2 - Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lastNameLabel, gbc);

        gbc.gridx = 1;
        add(lastNameField, gbc);

        // Row 3 - Telephone
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(telephoneLabel, gbc);

        gbc.gridx = 1;
        add(telephoneField, gbc);

        // Row 4 - Address Label (Spanning Two Columns)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(addressLabel, gbc);
        gbc.gridwidth = 1; // Reset

        // Row 5 - Street
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(streetLabel, gbc);

        gbc.gridx = 1;
        add(streetField, gbc);

        // Row 6 - City
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(cityLabel, gbc);

        gbc.gridx = 1;
        add(cityField, gbc);

        // Row 7 - State
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(stateLabel, gbc);

        gbc.gridx = 1;
        add(stateField, gbc);

        // Row 8 - Zip Code
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(zipLabel, gbc);

        gbc.gridx = 1;
        add(zipField, gbc);

        // Row 9 - Save Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
    }
}
