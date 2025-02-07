package librarysystem.ui.members;

import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class MemberPanel extends JFrame {
    private SystemController systemController = new SystemController();
    private DefaultTableModel tableModel;
    private JTable memberTable;

    public MemberPanel(String role) {
        setTitle("Manage Members");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Column headers
        String[] columns = {"Member ID", "First Name", "Last Name", "Telephone", "Address"};

        // Initialize table with an empty model
        tableModel = new DefaultTableModel(columns, 0);
        memberTable = new JTable(tableModel);
        loadMemberData(); // Load actual members from system

        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add Member");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search by First Name:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(new JScrollPane(memberTable), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Role-based access control
        if (role.equals("ADMIN") || role.equals("BOTH")) {
            add(addButton, BorderLayout.SOUTH);
            addButton.addActionListener(e -> new AddMemberForm(this::loadMemberData)); // Pass refresh callback
        }

        setVisible(true);
    }

    // Method to fetch and reload table data
    public void loadMemberData() {
        tableModel.setRowCount(0); // Clear table
        Map<String, LibraryMember> members = systemController.allMembers();

        for (LibraryMember member : members.values()) {
            tableModel.addRow(new Object[]{
                    member.getMemberId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getTelephone(),
                    member.getAddress().toString()
            });
        }
    }
}
