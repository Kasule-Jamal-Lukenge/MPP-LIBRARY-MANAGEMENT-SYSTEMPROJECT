package org.miu.mppproject.librarysystem.libraryapp.frontend.features;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.miu.mppproject.librarysystem.libraryapp.frontend.model.CheckoutEntry;
import org.miu.mppproject.librarysystem.libraryapp.frontend.model.ContactInfo;
import org.miu.mppproject.librarysystem.libraryapp.frontend.model.Member;
import org.miu.mppproject.librarysystem.libraryapp.frontend.navigation.NavigationController;

public class DashboardScreen extends Screen {
    private final NavigationController navigationController;
    private final String userRole;
    private final String userName;
    // The contentArea StackPane supports overlays (like the dimming effect)
    private final StackPane contentArea = new StackPane();

    public DashboardScreen(NavigationController navigationController, String userRole, String userName) {
        this.navigationController = navigationController;
        this.userRole = userRole;
        this.userName = userName;
        createView();
    }

    private void createView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f4f4f4;");
        mainLayout.setLeft(createSidebar());
        mainLayout.setTop(createTopBar());
        mainLayout.setCenter(contentArea);

        // Default sizes so that the dashboard nearly covers the screen.
        mainLayout.setMinWidth(800);
        mainLayout.setMinHeight(600);
        mainLayout.setPrefWidth(1200);
        mainLayout.setPrefHeight(800);
        BorderPane.setMargin(contentArea, new Insets(10));

        this.setView(mainLayout);
        loadDefaultContent();
    }

    private void loadDefaultContent() {
        Label welcomeLabel = new Label("Welcome to the MIU Library System");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
        applyContentTransition();

        contentArea.getChildren().clear();
        contentArea.getChildren().add(welcomeLabel);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(250);
        sidebar.setAlignment(Pos.TOP_LEFT);

        addRoleBasedButtons(sidebar);

        // Place logout button at the bottom.
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);
        Button logoutButton = createSidebarButton("Logout", "logout_icon.png", this::handleLogout);
        sidebar.getChildren().add(logoutButton);

        return sidebar;
    }

    /**
     * Adds sidebar buttons based on the logged-in role.
     */
    private void addRoleBasedButtons(VBox sidebar) {
        sidebar.getChildren().add(createSidebarButton("Members", "members_icon.png", this::showMembersList));
        sidebar.getChildren().add(createSidebarButton("Books", "books_icon.png", this::showBooksList));
        sidebar.getChildren().add(createSidebarButton("Checkout Book", "checkout_icon.png", this::showCheckoutScreen));
        sidebar.getChildren().add(createSidebarButton("Return Book", "return_icon.png", this::showReturnScreen));
    }

    /**
     * Displays the Members screen.
     * The table includes columns: Member ID, Full Name, Phone, and Address.
     */
    private void showMembersList() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.TOP_CENTER);

        Label header = new Label("Library Members");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Search container
        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by member id, name, or phone...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(300);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button addMemberButton = null;
        if ("ADMIN".equals(userRole) || "BOTH".equals(userRole)) {
            addMemberButton = new Button("➕ Add Member");
            addMemberButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-height: 40px;");
            searchContainer.getChildren().addAll(searchField, addMemberButton);
        } else {
            searchContainer.getChildren().add(searchField);
        }

        // Create TableView with correct type
        TableView<Member> table = new TableView<>();
        TableColumn<Member, String> idCol = new TableColumn<>("Member ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMemberId()));
        idCol.setMinWidth(120);

        TableColumn<Member, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactInfo().getFullname()));
        nameCol.setMinWidth(200);

        TableColumn<Member, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactInfo().getPhonenumber()));
        phoneCol.setMinWidth(150);

        TableColumn<Member, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactInfo().getAddress()));
        addressCol.setMinWidth(250);

        table.getColumns().addAll(idCol, nameCol, phoneCol, addressCol);

        // Dummy Member Data
        ObservableList<Member> dummyData = FXCollections.observableArrayList(
                new Member("M001", new ContactInfo("Alice Johnson", "123-456-7890", "123 Main St, Anytown, ST, 12345")),
                new Member("M002", new ContactInfo("Bob Smith", "234-567-8901", "456 Oak Ave, Othertown, ST, 23456")),
                new Member("M003", new ContactInfo("Charlie Davis", "345-678-9012", "789 Pine Rd, Sometown, ST, 34567"))
        );

        FilteredList<Member> filteredData = new FilteredList<>(dummyData, b -> true);

        // Create dim overlay for search delay effect
        StackPane tableStack = new StackPane(table);
        Node dimOverlay = createDimOverlay();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!tableStack.getChildren().contains(dimOverlay)) {
                tableStack.getChildren().add(dimOverlay);
            }
            pause.stop();
            pause.setOnFinished(event -> {
                filteredData.setPredicate(member -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    return member.getMemberId().toLowerCase().contains(lowerCaseFilter) ||
                            member.getContactInfo().getFullname().toLowerCase().contains(lowerCaseFilter) ||
                            member.getContactInfo().getPhonenumber().toLowerCase().contains(lowerCaseFilter);
                });
                tableStack.getChildren().remove(dimOverlay);
            });
            pause.playFromStart();
        });

        SortedList<Member> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // Row click to show details
        table.setRowFactory(tv -> {
            TableRow<Member> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Member memberData = row.getItem();
                    if ("ADMIN".equals(userRole) || "BOTH".equals(userRole)) {
                        showMemberDetailsEditable(memberData);
                    } else {
                        showMemberDetailsViewOnly(memberData);
                    }
                }
            });
            return row;
        });

        container.getChildren().addAll(header, searchContainer, tableStack);
        if (addMemberButton != null) {
            addMemberButton.setOnAction(e -> showAddMemberForm(container, table));
        }
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
        applyContentTransition();
    }


    /**
     * Displays detailed member information (Editable version for ADMIN/BOTH).
     * Divides the screen into Contact Information and Checkout Entries sections.
     */
    private void showMemberDetailsEditable(Member memberData) {
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Member Details (Editable)");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Contact Information section.
        Label contactHeader = new Label("Contact Information");
        contactHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label idLabel = new Label("Member ID: " + memberData.getMemberId());
        Label nameLabel = new Label("Full Name: " + memberData.getContactInfo().getFullname());
        Label phoneLabel = new Label("Phone: " + memberData.getContactInfo().getPhonenumber());
        Label addressLabel = new Label("Address: " + memberData.getContactInfo().getAddress());
        VBox contactBox = new VBox(5, contactHeader, idLabel, nameLabel, phoneLabel, addressLabel);

        // Checkout Entries section.
        Label checkoutHeader = new Label("Checkout Entries");
        checkoutHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ObservableList<CheckoutEntry> checkoutEntries = FXCollections.observableArrayList(memberData.getCheckoutEntries());
        Node checkoutSection;

        if (checkoutEntries.isEmpty()) {
            checkoutSection = new Label("Checkout entry empty");
        } else {
            TableView<CheckoutEntry> checkoutTable = new TableView<>();

            TableColumn<CheckoutEntry, String> bookCol = new TableColumn<>("Book Title");
            bookCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
            bookCol.setMinWidth(200);

            TableColumn<CheckoutEntry, String> borrowedCol = new TableColumn<>("Date Borrowed");
            borrowedCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateBorrowed()));
            borrowedCol.setMinWidth(120);

            TableColumn<CheckoutEntry, String> dueCol = new TableColumn<>("Due Date");
            dueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate()));
            dueCol.setMinWidth(120);

            TableColumn<CheckoutEntry, String> overdueCol = new TableColumn<>("Overdue");
            overdueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isOverDue() ? "Yes" : "No"));
            overdueCol.setMinWidth(80);

            TableColumn<CheckoutEntry, String> fineCol = new TableColumn<>("Fine Amount");
            fineCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getFineAmount())));
            fineCol.setMinWidth(100);

            checkoutTable.getColumns().addAll(bookCol, borrowedCol, dueCol, overdueCol, fineCol);

            SortedList<CheckoutEntry> sortedCheckoutData = new SortedList<>(checkoutEntries);
            sortedCheckoutData.comparatorProperty().bind(checkoutTable.comparatorProperty());
            checkoutTable.setItems(sortedCheckoutData);

            checkoutTable.setRowFactory(tv -> {
                TableRow<CheckoutEntry> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty()) {
                        CheckoutEntry entry = row.getItem();
                        showCheckoutEntryDetails(entry, memberData, true);
                    }
                });
                return row;
            });

            checkoutSection = checkoutTable;
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        backButton.setOnAction(e -> showMembersList());

        detailsContainer.getChildren().addAll(header, contactBox, checkoutHeader, checkoutSection, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }

    /**
     * Displays detailed member information (View–only version for LIBRARIAN).
     */
    private void showMemberDetailsViewOnly(Member memberData) {
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Member Details (View Only)");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label contactHeader = new Label("Contact Information");
        contactHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label idLabel = new Label("Member ID: " + memberData.getMemberId());
        Label nameLabel = new Label("Full Name: " + memberData.getContactInfo().getFullname());
        Label phoneLabel = new Label("Phone: " + memberData.getContactInfo().getPhonenumber());
        Label addressLabel = new Label("Address: " + memberData.getContactInfo().getAddress());
        VBox contactBox = new VBox(5, contactHeader, idLabel, nameLabel, phoneLabel, addressLabel);

        Label checkoutHeader = new Label("Checkout Entries");
        checkoutHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ObservableList<CheckoutEntry> checkoutEntries = FXCollections.observableArrayList(memberData.getCheckoutEntries());
        Node checkoutSection;

        if (checkoutEntries.isEmpty()) {
            checkoutSection = new Label("Checkout entry empty");
        } else {
            TableView<CheckoutEntry> checkoutTable = new TableView<>();

            TableColumn<CheckoutEntry, String> bookCol = new TableColumn<>("Book Title");
            bookCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookTitle()));
            bookCol.setMinWidth(200);

            TableColumn<CheckoutEntry, String> borrowedCol = new TableColumn<>("Date Borrowed");
            borrowedCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateBorrowed()));
            borrowedCol.setMinWidth(120);

            TableColumn<CheckoutEntry, String> dueCol = new TableColumn<>("Due Date");
            dueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate()));
            dueCol.setMinWidth(120);

            TableColumn<CheckoutEntry, String> overdueCol = new TableColumn<>("Overdue");
            overdueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isOverDue() ? "Yes" : "No"));
            overdueCol.setMinWidth(80);

            TableColumn<CheckoutEntry, String> fineCol = new TableColumn<>("Fine Amount");
            fineCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getFineAmount())));
            fineCol.setMinWidth(100);

            checkoutTable.getColumns().addAll(bookCol, borrowedCol, dueCol, overdueCol, fineCol);

            SortedList<CheckoutEntry> sortedCheckoutData = new SortedList<>(checkoutEntries);
            sortedCheckoutData.comparatorProperty().bind(checkoutTable.comparatorProperty());
            checkoutTable.setItems(sortedCheckoutData);

            checkoutTable.setRowFactory(tv -> {
                TableRow<CheckoutEntry> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty()) {
                        CheckoutEntry entry = row.getItem();
                        showCheckoutEntryDetails(entry, memberData, false);
                    }
                });
                return row;
            });

            checkoutSection = checkoutTable;
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        backButton.setOnAction(e -> showMembersList());

        detailsContainer.getChildren().addAll(header, contactBox, checkoutHeader, checkoutSection, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }


    /**
     * Displays detailed checkout entry information.
     *
     * @param checkoutEntry an array: [0]=Book Title, [1]=Date Borrowed, [2]=Due Date, [3]=Overdue, [4]=Fine Amount.
     * @param memberData    the member’s data (for back navigation).
     * @param isEditable    flag indicating whether to return to the editable member details.
     */
    private void showCheckoutEntryDetails(CheckoutEntry checkoutEntry, Member memberData, boolean isEditable) {
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Checkout Entry Details");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label bookTitle = new Label("Book Title: " + checkoutEntry.getBookTitle());
        Label dateBorrowed = new Label("Date Borrowed: " + checkoutEntry.getDateBorrowed());
        Label dueDate = new Label("Due Date: " + checkoutEntry.getDueDate());
        Label overdue = new Label("Overdue: " + checkoutEntry.isOverDue());
        Label fine = new Label("Fine Amount($): " + checkoutEntry.getFineAmount());

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        backButton.setOnAction(e -> {
            if (isEditable) {
                showMemberDetailsEditable(memberData);
            } else {
                showMemberDetailsViewOnly(memberData);
            }
        });

        detailsContainer.getChildren().addAll(header, bookTitle, dateBorrowed, dueDate, overdue, fine, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }


    /**
     * Displays the Books screen.
     * The table includes columns: ISBN, Available Copies, Unavailable Copies, and Max Checkout Length.
     */
    public void showBooksList() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.TOP_CENTER);

        Label header = new Label("Library Books");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchContainer = new HBox(10);
        searchContainer.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by ISBN...");
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(300);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button addBookButton = null;
        if ("ADMIN".equals(userRole) || "BOTH".equals(userRole)) {
            addBookButton = new Button("➕ Add Book");
            addBookButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-height: 40px;");
            searchContainer.getChildren().addAll(searchField, addBookButton);
        } else {
            searchContainer.getChildren().add(searchField);
        }

        TableView<String[]> table = new TableView<>();

        TableColumn<String[], String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        isbnCol.setMinWidth(120);

        TableColumn<String[], String> availableCol = new TableColumn<>("Available Copies");
        availableCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        availableCol.setMinWidth(150);

        TableColumn<String[], String> unavailableCol = new TableColumn<>("Unavailable Copies");
        unavailableCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        unavailableCol.setMinWidth(150);

        TableColumn<String[], String> checkoutLengthCol = new TableColumn<>("Max Checkout Length (days)");
        checkoutLengthCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        checkoutLengthCol.setMinWidth(180);

        table.getColumns().addAll(isbnCol, availableCol, unavailableCol, checkoutLengthCol);

        // Dummy book data: [ISBN, available copies, unavailable copies, checkout length]
        ObservableList<String[]> dummyData = FXCollections.observableArrayList(
                new String[]{"1234567890", "3", "2", "14"},
                new String[]{"0987654321", "5", "0", "7"},
                new String[]{"5678901234", "2", "1", "21"}
        );
        FilteredList<String[]> filteredData = new FilteredList<>(dummyData, b -> true);

        StackPane tableStack = new StackPane(table);
        Node dimOverlay = createDimOverlay();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!tableStack.getChildren().contains(dimOverlay)) {
                tableStack.getChildren().add(dimOverlay);
            }
            pause.stop();
            pause.setOnFinished(event -> {
                filteredData.setPredicate(data -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    return data[0].toLowerCase().contains(newValue.toLowerCase());
                });
                tableStack.getChildren().remove(dimOverlay);
            });
            pause.playFromStart();
        });
        SortedList<String[]> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        // Row click: show detailed Book information.
        table.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    String[] rowData = row.getItem();
                    showBookDetails(rowData);
                }
            });
            return row;
        });

        container.getChildren().addAll(header, searchContainer, tableStack);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
        applyContentTransition();

        if (addBookButton != null) {
            addBookButton.setOnAction(e -> showAddBookForm(container, table));
        }
    }

    /**
     * Displays detailed Book information.
     * Divides the screen into Basic Book Information, Authors section, and Book Copies section.
     */
    private void showBookDetails(String[] bookData) {
        // bookData: [0]=ISBN, [1]=available copies, [2]=unavailable copies, [3]=max checkout length.
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Book Details");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Basic Book Information section.
        VBox basicInfoBox = new VBox(5);
        Label isbnLabel = new Label("ISBN: " + bookData[0]);
        Label checkoutLabel = new Label("Max Checkout Length: " + bookData[3] + " days");
        basicInfoBox.getChildren().addAll(isbnLabel, checkoutLabel);
        basicInfoBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        // Authors section.
        VBox authorsBox = new VBox(5);
        Label authorsHeader = new Label("Authors");
        authorsHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ObservableList<String[]> authorsList = getDummyAuthors(bookData[0]);
        FlowPane authorsFlow = new FlowPane(10, 10);
        for (String[] authorData : authorsList) {
            Label authorLabel = new Label(authorData[0]);
            authorLabel.setStyle("-fx-text-fill: #2980b9; -fx-underline: true; -fx-cursor: hand;");
            authorLabel.setOnMouseClicked(e -> showAuthorDetails(authorData));
            authorsFlow.getChildren().add(authorLabel);
        }
        authorsBox.getChildren().addAll(authorsHeader, authorsFlow);
        authorsBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        // Book Copies section.
        VBox copiesBox = new VBox(5);
        Label copiesHeader = new Label("Book Copies");
        copiesHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TableView<String[]> copiesTable = new TableView<>();
        TableColumn<String[], String> copyNumCol = new TableColumn<>("Copy Number");
        copyNumCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        copyNumCol.setMinWidth(150);
        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        statusCol.setMinWidth(250);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if ("true".equals(status)) {
                        setGraphic(new Label("✅ Available"));
                    } else {
                        // data[2] holds borrower contact info.
                        setText("Borrowed: " + getTableView().getItems().get(getIndex())[2]);
                    }
                }
            }
        });
        copiesTable.getColumns().addAll(copyNumCol, statusCol);
        copiesTable.setItems(getDummyBookCopies(bookData[0]));
        copiesBox.getChildren().addAll(copiesHeader, copiesTable);
        copiesBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        backButton.setOnAction(e -> showBooksList());

        detailsContainer.getChildren().addAll(header, basicInfoBox, authorsBox, copiesBox, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }

    /**
     * Returns dummy authors for a given book ISBN.
     * Each author: [0]=Name, [1]=Short Bio, [2]=Contact Info, [3]=Credentials (semicolon separated)
     */
    private ObservableList<String[]> getDummyAuthors(String isbn) {
        ObservableList<String[]> authors = FXCollections.observableArrayList();
        if ("1234567890".equals(isbn)) {
            authors.add(new String[]{"Author A", "Short bio for Author A", "authorA@example.com, 111-222-3333", "Credential1;Credential2"});
            authors.add(new String[]{"Author B", "Short bio for Author B", "authorB@example.com, 444-555-6666", "Credential3"});
        } else if ("0987654321".equals(isbn)) {
            authors.add(new String[]{"Author C", "Short bio for Author C", "authorC@example.com, 777-888-9999", "Credential4"});
        } else if ("5678901234".equals(isbn)) {
            authors.add(new String[]{"Author D", "Short bio for Author D", "authorD@example.com, 000-111-2222", "Credential5;Credential6"});
        }
        return authors;
    }

    /**
     * Returns dummy book copies for a given book ISBN.
     * Each copy: [0]=Copy Number, [1]="true" if available, "false" if not, [2]=Borrower contact info (if not available)
     */
    private ObservableList<String[]> getDummyBookCopies(String isbn) {
        ObservableList<String[]> copies = FXCollections.observableArrayList();
        if ("1234567890".equals(isbn)) {
            copies.add(new String[]{"Copy 1", "true", ""});
            copies.add(new String[]{"Copy 2", "false", "Member M001, 123-456-7890"});
        } else if ("0987654321".equals(isbn)) {
            copies.add(new String[]{"Copy 1", "true", ""});
        } else if ("5678901234".equals(isbn)) {
            copies.add(new String[]{"Copy 1", "false", "Member M002, 234-567-8901"});
            copies.add(new String[]{"Copy 2", "true", ""});
        }
        return copies;
    }

    /**
     * Displays detailed Author information.
     * Shows short bio, contact info, and credentials. Each credential is clickable.
     */
    private void showAuthorDetails(String[] authorData) {
        // authorData: [0]=Name, [1]=Short Bio, [2]=Contact, [3]=Credentials (semicolon separated)
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Author Details");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label nameLabel = new Label("Name: " + authorData[0]);
        Label bioLabel = new Label("Short Bio: " + authorData[1]);
        Label contactLabel = new Label("Contact: " + authorData[2]);

        // Credentials section.
        VBox credBox = new VBox(5);
        Label credHeader = new Label("Credentials:");
        credHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        String[] credentials = authorData[3].split(";");
        FlowPane credFlow = new FlowPane(10, 10);
        for (String cred : credentials) {
            Label credLabel = new Label(cred.trim());
            credLabel.setStyle("-fx-text-fill: #2980b9; -fx-underline: true; -fx-cursor: hand;");
            credLabel.setOnMouseClicked(e -> showCredentialDetails(cred.trim()));
            credFlow.getChildren().add(credLabel);
        }
        credBox.getChildren().addAll(credHeader, credFlow);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        // For simplicity, return to Book Details using dummy data.
        backButton.setOnAction(e -> showBookDetails(getDummyBookDataForCurrentAuthor()));

        detailsContainer.getChildren().addAll(header, nameLabel, bioLabel, contactLabel, credBox, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }

    /**
     * Displays detailed Credential information.
     * Shows a description and associated degree.
     */
    private void showCredentialDetails(String credential) {
        VBox detailsContainer = new VBox(20);
        detailsContainer.setPadding(new Insets(15));
        detailsContainer.setAlignment(Pos.TOP_LEFT);

        Label header = new Label("Credential Details");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label credLabel = new Label("Credential: " + credential);
        Label descriptionLabel = new Label("Description: Detailed description for " + credential);
        Label degreeLabel = new Label("Degree: " + (credential.endsWith("1") || credential.endsWith("5") ? "Bachelor's" : "Master's"));
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        // Return to Author Details via dummy author data.
        backButton.setOnAction(e -> showAuthorDetails(getDummyAuthorForCurrentCredential()));

        detailsContainer.getChildren().addAll(header, credLabel, descriptionLabel, degreeLabel, backButton);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(detailsContainer);
        applyContentTransition();
    }

    // Dummy helper methods for "Back" actions.
    private String[] getDummyAuthorForCurrentCredential() {
        return new String[]{"Author A", "Short bio for Author A", "authorA@example.com, 111-222-3333", "Credential1;Credential2"};
    }

    private String[] getDummyBookDataForCurrentAuthor() {
        return new String[]{"1234567890", "3", "2", "14"};
    }

    /**
     * Displays a form to add a new book (for ADMIN/BOTH).
     */
    private void showAddBookForm(VBox container, TableView<String[]> table) {
        table.setVisible(false);
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(10));

        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN");
        TextField availableField = new TextField();
        availableField.setPromptText("Enter Available Copies");
        TextField unavailableField = new TextField();
        unavailableField.setPromptText("Enter Unavailable Copies");
        TextField checkoutField = new TextField();
        checkoutField.setPromptText("Max Checkout Length (days)");

        Button submitButton = new Button("✔ Submit");
        submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px;");
        submitButton.setOnAction(e -> {
            String[] newBook = {
                    isbnField.getText(),
                    availableField.getText(),
                    unavailableField.getText(),
                    checkoutField.getText()
            };
            table.getItems().add(newBook);
            container.getChildren().remove(formContainer);
            table.setVisible(true);
        });

        Button cancelButton = new Button("❌ Cancel");
        cancelButton.setOnAction(e -> {
            container.getChildren().remove(formContainer);
            table.setVisible(true);
        });

        formContainer.getChildren().addAll(isbnField, availableField, unavailableField, checkoutField, submitButton, cancelButton);
        container.getChildren().add(formContainer);
    }

    /**
     * Displays a form to add a new member (for ADMIN/BOTH).
     */
    private void showAddMemberForm(VBox container, TableView<String[]> table) {
        table.setVisible(false);
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Enter Member ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter Full Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter Phone");
        TextField addressField = new TextField();
        addressField.setPromptText("Enter Address (street, city, state, zip)");

        Button submitButton = new Button("✔ Submit");
        submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px;");
        submitButton.setOnAction(e -> {
            String[] newMember = {
                    idField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            };
            table.getItems().add(newMember);
            container.getChildren().remove(formContainer);
            table.setVisible(true);
        });

        Button cancelButton = new Button("❌ Cancel");
        cancelButton.setOnAction(e -> {
            container.getChildren().remove(formContainer);
            table.setVisible(true);
        });

        formContainer.getChildren().addAll(idField, nameField, phoneField, addressField, submitButton, cancelButton);
        container.getChildren().add(formContainer);
    }


    /**
     * Displays a simple return form.
     */
    private void showReturnScreen() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);

        Label header = new Label("Return Book");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField memberIdField = new TextField();
        memberIdField.setPromptText("Enter Member ID");
        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter Book ISBN");
        TextField copyNumberField = new TextField();
        copyNumberField.setPromptText("Enter Copy Number");

        Button returnButton = new Button("Return");
        returnButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10px;");
        cancelButton.setOnAction(e -> showDefaultScreen());

        HBox buttonContainer = new HBox(10, returnButton, cancelButton);
        buttonContainer.setAlignment(Pos.CENTER);

        returnButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book returned successfully!");
            alert.showAndWait();
            showDefaultScreen();
        });

        container.getChildren().addAll(header, memberIdField, isbnField, copyNumberField, buttonContainer);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(container);
        applyContentTransition();
    }

    // Returns to the default welcome screen.
    private void showDefaultScreen() {
        loadDefaultContent();
    }

    private void applyContentTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), contentArea);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private Button createSidebarButton(String text, String iconPath, Runnable action) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 200px; -fx-border-radius: 5px;");
        button.setPadding(new Insets(10));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setGraphic(createIcon(iconPath));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        if (action != null) {
            button.setOnAction(e -> action.run());
        }
        return button;
    }

    private ImageView createIcon(String iconPath) {
        Image image = new Image("file:" + iconPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #34495e;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label("📚");
        iconLabel.setStyle("-fx-font-size: 40px;");
        Label titleLabel = new Label("MIU Library");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        topBar.getChildren().addAll(iconLabel, titleLabel);
        return topBar;
    }


    private void showCheckoutScreen() {
        // Create a form with fields: Book ISBN and Copy Number.
        VBox container = new VBox(15);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);

        Label header = new Label("Checkout Book");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField isbnField = new TextField();
        isbnField.setPromptText("Enter Book ISBN");

        TextField copyNumberField = new TextField();
        copyNumberField.setPromptText("Enter Copy Number (e.g., Copy 1)");

        Button fetchButton = new Button("Fetch Book Info");
        fetchButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");

        fetchButton.setOnAction(e -> {
            String isbn = isbnField.getText().trim();
            String copyNumber = copyNumberField.getText().trim();
            if (isbn.isEmpty() || copyNumber.isEmpty()) {
                showCustomAlert("Please fill in all fields", false, () -> showCheckoutScreen());
            } else {
                // Simulate fetching book info.
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    if (isValidBook(isbn, copyNumber)) {
                        showAssociateMemberScreen(isbn, copyNumber);
                    } else {
                        showCustomAlert("Book not found or invalid copy number", false, () -> showCheckoutScreen());
                    }
                });
                pause.play();
            }
        });

        VBox form = new VBox(10, header, isbnField, copyNumberField, fetchButton);
        form.setAlignment(Pos.CENTER);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(form);
        applyContentTransition();
    }


    /**
     * Displays a custom alert dialog as an overlay.
     * The dialog appears without window decorations and fades away after 2 seconds.
     *
     * @param message  The message to show.
     * @param success  true for success (green), false for error (red).
     * @param onFinish Runnable to execute after the alert fades away.
     */
    private void showCustomAlert(String message, boolean success, Runnable onFinish) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setPrefSize(contentArea.getWidth(), contentArea.getHeight());

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 20px; " +
                (success ? "-fx-background-color: green;" : "-fx-background-color: red;"));
        overlay.getChildren().add(messageLabel);
        contentArea.getChildren().add(overlay);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), overlay);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(ev -> {
                contentArea.getChildren().remove(overlay);
                onFinish.run();
            });
            fade.play();
        });
        pause.play();
    }

    private boolean isValidBook(String isbn, String copyNumber) {
        // Dummy validation: valid ISBNs are "1234567890", "0987654321", "5678901234"
        if (isbn.equals("1234567890")) {
            return copyNumber.equalsIgnoreCase("Copy 1") || copyNumber.equalsIgnoreCase("Copy 2");
        } else if (isbn.equals("0987654321")) {
            return copyNumber.equalsIgnoreCase("Copy 1");
        } else if (isbn.equals("5678901234")) {
            return copyNumber.equalsIgnoreCase("Copy 1") || copyNumber.equalsIgnoreCase("Copy 2");
        }
        return false;
    }


    private boolean isValidMember(String memberId) {
        // Dummy validation: valid if memberId starts with "M" and length is 4.
        return memberId.startsWith("M") && memberId.length() == 4;
    }

    private void showAssociateMemberScreen(String isbn, String copyNumber) {
        // New screen: allow user to input Member ID for checkout confirmation.
        VBox container = new VBox(15);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);

        Label header = new Label("Associate Member for Checkout");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label infoLabel = new Label("Book ISBN: " + isbn + " | Copy: " + copyNumber);
        TextField memberIdField = new TextField();
        memberIdField.setPromptText("Enter Member ID");

        Button confirmButton = new Button("Confirm Checkout");
        confirmButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");

        confirmButton.setOnAction(e -> {
            String memberId = memberIdField.getText().trim();
            if (memberId.isEmpty()) {
                showCustomAlert("Please enter a Member ID", false, () -> showAssociateMemberScreen(isbn, copyNumber));
            } else {
                // Simulate member validation.
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    if (isValidMember(memberId)) {
                        // Simulate marking the book copy as unavailable.
                        showCustomAlert("Checkout successful!", true, () -> loadDefaultContent());
                    } else {
                        showCustomAlert("Invalid Member ID", false, () -> showAssociateMemberScreen(isbn, copyNumber));
                    }
                });
                pause.play();
            }
        });

        VBox form = new VBox(10, header, infoLabel, memberIdField, confirmButton);
        form.setAlignment(Pos.CENTER);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(form);
        applyContentTransition();
    }

    private void handleLogout() {
        navigationController.publishEvent(new ShowScreenEvent(new LoginScreen(navigationController)));
    }

    private Node createDimOverlay() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        overlay.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        ProgressIndicator pi = new ProgressIndicator();
        overlay.getChildren().add(pi);
        return overlay;
    }

}
