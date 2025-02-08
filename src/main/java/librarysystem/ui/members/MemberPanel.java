package librarysystem.ui.members;

import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class MemberPanel extends JFrame {
    private SystemController systemController = new SystemController();
    private DefaultTableModel tableModel;
    private JTable memberTable;

    public MemberPanel(String role) {
        setTitle("Manage Members");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Column headers
        String[] columns = {"Member ID", "First Name", "Last Name", "Telephone", "Address"};

        // Initialize table with an empty model
        tableModel = new DefaultTableModel(columns, 0);
        memberTable = new JTable(tableModel);
        loadMemberData(); // Load actual members from system
        // Adjust column widths
        TableColumn addressColumn = memberTable.getColumnModel().getColumn(4); // Address column (index 4)
        addressColumn.setPreferredWidth(250); // Increase width

        // Wrap table in a JPanel with border & padding
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Library Members", TitledBorder.CENTER, TitledBorder.TOP),
                new EmptyBorder(10, 10, 10, 10) // Padding: Top, Left, Bottom, Right
        ));
        tablePanel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add Member");

        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding for the search panel
        topPanel.add(new JLabel("Search by First Name:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(tablePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Role-based access control
        if (role.equals("ADMIN") || role.equals("BOTH")) {
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding for the bottom button
            bottomPanel.add(addButton);
            add(bottomPanel, BorderLayout.SOUTH);
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
