package librarysystem.ui;

import librarysystem.ui.books.BookPanel;
import librarysystem.ui.members.MemberPanel;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard(String role) {
        setTitle("Library Dashboard - " + role);
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton membersButton = new JButton("Manage Members");
        JButton booksButton = new JButton("Manage Books");
        JButton exitButton = new JButton("Exit");

        membersButton.addActionListener(e -> new MemberPanel(role));
        booksButton.addActionListener(e -> new BookPanel(role));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(membersButton);
        panel.add(booksButton);
        panel.add(exitButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}

