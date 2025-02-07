package librarysystem;

import librarysystem.ui.auth.LoginFrame;

import javax.swing.*;

public class LibraryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
