package org.example.javalecturehomework.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.example.javalecturehomework.model.Entry;
import org.example.javalecturehomework.model.Match;
import org.example.javalecturehomework.model.Spectator;
import org.example.javalecturehomework.utils.DatabaseConnection;

import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DatabaseMenuController {
//***************************************************************************************************************************************


//    @FXML private ComboBox<String> matchComboBox, spectatorComboBox;
    @FXML private Label formTitle, label1, label2, label3, label4, label5;
    @FXML private TextField field1, field2, field3, field4, field5;
    @FXML private Pane addDataPane;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextField filterTextField;
    @FXML private RadioButton filterRadioButton;
    @FXML private CheckBox filterCheckBox;
    @FXML private ComboBox<String> tableComboBox;
    @FXML private TableView<Object> dataTable;



    //private String selectedTable;
//    @FXML
//    private Button updateButton;
//    @FXML
//    private ComboBox<String> tableComboBox1;
//
//    private Map<String, String> selectedRowData;
//***************************************************************************************************************************************
    private Connection connection;

    @FXML
    public void initialize() {
        // Initialize ComboBox with table names
        tableComboBox.getItems().addAll("matches", "entries", "spectators");
        tableComboBox.setOnAction(event -> fetchData());
        tableComboBox.setOnAction(event -> {
            fetchData();
            setupFilterOptions();
        });
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //***************************************************************************************************************************************
    private void setupFilterOptions() {
        String selectedTable = tableComboBox.getSelectionModel().getSelectedItem();
        if (selectedTable == null) return;

        filterComboBox.getItems().clear();

        if (selectedTable.equals("matches")) {
            filterComboBox.getItems().addAll("ID", "Match Date", "Start Time", "Ticket Price", "Match Type");
        } else if (selectedTable.equals("entries")) {
            filterComboBox.getItems().addAll("Spectator ID", "Match ID", "Timestamp");
        } else if (selectedTable.equals("spectators")) {
            filterComboBox.getItems().addAll("ID", "Name", "Gender", "Has Pass");
        }
    }
//***************************************************************************************************************************************

    private void fetchData() {
        String selectedTable = tableComboBox.getSelectionModel().getSelectedItem();
        if (selectedTable == null) return;

        ObservableList<Object> data = FXCollections.observableArrayList();
        String query = buildQueryForTable(selectedTable);

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                if (selectedTable.equals("matches")) {
                    int id = resultSet.getInt("id");
                    Date mdate = resultSet.getDate("mdate");
                    Time startsAt = resultSet.getTime("startsAt");
                    double ticketPrice = resultSet.getDouble("ticketPrice");
                    String mtype = resultSet.getString("mtype");

                    Match match = new Match(id, mdate, startsAt, ticketPrice, mtype);
                    data.add(match);
                } else if (selectedTable.equals("entries")) {
                    int spectatorId = resultSet.getInt("spectatorid");
                    int matchId = resultSet.getInt("matchid");
                    Time timestamp = resultSet.getTime("timestamp");

                    data.add(new Entry(spectatorId, matchId, timestamp));
                } else if (selectedTable.equals("spectators")) {
                    int id = resultSet.getInt("id");
                    String sname = resultSet.getString("sname");
                    boolean male = resultSet.getBoolean("male");
                    boolean hasPass = resultSet.getBoolean("haspass");

                    data.add(new Spectator(id, sname, male, hasPass));
                }
            }

            updateTableView(selectedTable, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //***************************************************************************************************************************************
    private void updateTableView(String selectedTable, ObservableList<Object> data) {
        // Clear previous columns
        dataTable.getColumns().clear();

        if (selectedTable.equals("matches")) {
            TableColumn<Object, String> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Match) cellData.getValue()).getId())));

            TableColumn<Object, String> dateColumn = new TableColumn<>("Match Date");
            dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getMdate().toString()));

            TableColumn<Object, String> startColumn = new TableColumn<>("Start Time");
            startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getStartsAt().toString()));

            TableColumn<Object, String> priceColumn = new TableColumn<>("Ticket Price");
            priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Match) cellData.getValue()).getTicketPrice())));

            TableColumn<Object, String> typeColumn = new TableColumn<>("Match Type");
            typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getMtype()));

            dataTable.getColumns().addAll(idColumn, dateColumn, startColumn, priceColumn, typeColumn);
        } else if (selectedTable.equals("entries")) {
            TableColumn<Object, String> spectatorColumn = new TableColumn<>("Spectator ID");
            spectatorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Entry) cellData.getValue()).getSpectatorId())));

            TableColumn<Object, String> matchColumn = new TableColumn<>("Match ID");
            matchColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Entry) cellData.getValue()).getMatchId())));

            TableColumn<Object, String> timestampColumn = new TableColumn<>("Timestamp");
            timestampColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Entry) cellData.getValue()).getTimestamp().toString()));

            dataTable.getColumns().addAll(spectatorColumn, matchColumn, timestampColumn);
        } else if (selectedTable.equals("spectators")) {
            TableColumn<Object, String> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Spectator) cellData.getValue()).getId())));

            TableColumn<Object, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Spectator) cellData.getValue()).getSname()));

            TableColumn<Object, String> genderColumn = new TableColumn<>("Gender");
            genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Spectator) cellData.getValue()).isMale() ? "Male" : "Female"));

            TableColumn<Object, String> passColumn = new TableColumn<>("Has Pass");
            passColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((Spectator) cellData.getValue()).hasPass() ? "Yes" : "No"));

            dataTable.getColumns().addAll(idColumn, nameColumn, genderColumn, passColumn);
        }

        // Set the data
        dataTable.setItems(data);
    }
    //***************************************************************************************************************************************
    private String buildFilteredQuery(String tableName, String column, String value, boolean includeSpecialItems, boolean filterOptionEnabled) {
        String query = "SELECT * FROM " + tableName + " WHERE ";

        // Map column names to database fields
        switch (tableName) {
            case "matches":
                if (column.equals("ID")) query += "id = " + value;
                else if (column.equals("Match Date")) query += "mdate = '" + value + "'";
                else if (column.equals("Start Time")) query += "startsAt = '" + value + "'";
                else if (column.equals("Ticket Price")) query += "ticketPrice = " + value;
                else if (column.equals("Match Type")) query += "mtype LIKE '%" + value + "%'";
                break;
            case "entries":
                if (column.equals("Spectator ID")) query += "spectatorid = " + value;
                else if (column.equals("Match ID")) query += "matchid = " + value;
                else if (column.equals("Timestamp")) query += "timestamp = '" + value + "'";
                break;
            case "spectators":
                if (column.equals("ID")) query += "id = " + value;
                else if (column.equals("Name")) query += "sname LIKE '%" + value + "%'";
                else if (column.equals("Gender")) query += "male = " + (value.equalsIgnoreCase("Male") ? 1 : 0);
                else if (column.equals("Has Pass")) query += "haspass = " + (value.equalsIgnoreCase("Yes") ? 1 : 0);
                break;
        }

        if (includeSpecialItems) {
            query += " AND special = 1"; // Example of special condition
        }

        if (filterOptionEnabled) {
            query += " ORDER BY " + column; // Example of filter option logic
        }

        return query;
    }
    //***************************************************************************************************************************************
    private String buildQueryForTable(String tableName) {
        if (tableName.equals("matches")) {
            return "SELECT * FROM matches";
        } else if (tableName.equals("entries")) {
            return "SELECT * FROM entries";
        } else if (tableName.equals("spectators")) {
            return "SELECT * FROM spectators";
        }
        return "";
    }
    @FXML
    private void handleReadAction() {
        System.out.println("Reading data...");
        fetchData();  // Load the data based on the selected table
    }
    //***************************************************************************************************************************************
    @FXML
    private void handleRead2Action() {
        String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
        String filterValue = filterTextField.getText();
        boolean includeSpecialItems = filterCheckBox.isSelected();
        boolean filterOptionEnabled = filterRadioButton.isSelected();

        if (selectedColumn == null || filterValue.isEmpty()) {
            System.out.println("Filter criteria are not set properly.");
            return;
        }

        String selectedTable = tableComboBox.getSelectionModel().getSelectedItem();
        if (selectedTable == null) return;

        ObservableList<Object> data = FXCollections.observableArrayList();

        String query = buildFilteredQuery(selectedTable, selectedColumn, filterValue, includeSpecialItems, filterOptionEnabled);

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                if (selectedTable.equals("matches")) {
                    int id = resultSet.getInt("id");
                    Date mdate = resultSet.getDate("mdate");
                    Time startsAt = resultSet.getTime("startsAt");
                    double ticketPrice = resultSet.getDouble("ticketPrice");
                    String mtype = resultSet.getString("mtype");

                    Match match = new Match(id, mdate, startsAt, ticketPrice, mtype);
                    data.add(match);
                } else if (selectedTable.equals("entries")) {
                    int spectatorId = resultSet.getInt("spectatorid");
                    int matchId = resultSet.getInt("matchid");
                    Time timestamp = resultSet.getTime("timestamp");

                    data.add(new Entry(spectatorId, matchId, timestamp));
                } else if (selectedTable.equals("spectators")) {
                    int id = resultSet.getInt("id");
                    String sname = resultSet.getString("sname");
                    boolean male = resultSet.getBoolean("male");
                    boolean hasPass = resultSet.getBoolean("haspass");

                    data.add(new Spectator(id, sname, male, hasPass));
                }
            }

            updateTableView(selectedTable, data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//***************************************************************************************************************************************


    @FXML
    private void handleWriteAction() {
        String selectedTable = tableComboBox.getValue();

        if (selectedTable == null) {
            System.out.println("Please select a table first.");
            return;
        }

        formTitle.setText("Add New Record to " + selectedTable);

        // Set labels based on selected table
        switch (selectedTable) {
            case "matches":
                label1.setText("ID");
                label2.setText("Date");
                label3.setText("Start Time");
                label4.setText("Ticket Price");
                label5.setText("Match Type");
                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(true);
                label5.setVisible(true);
                field1.setVisible(true);
                field2.setVisible(true);
                field3.setVisible(true);
                field4.setVisible(true);
                field5.setVisible(true);
                break;
            case "spectators":
                label1.setText("ID");
                label2.setText("Name");
                label3.setText("Sex");
                label4.setText("has Pass");
                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(true);
                label5.setVisible(false);
                field1.setVisible(true);
                field2.setVisible(true);
                field3.setVisible(true);
                field4.setVisible(true);
                field5.setVisible(false);
                break;
            case "entries":
                label1.setText("Match ID");
                label2.setText("Spectator ID");
                label3.setText("Time Stamp");
                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(false);
                label5.setVisible(false);
                field1.setVisible(true);
                field2.setVisible(true);
                field3.setVisible(true);
                field4.setVisible(false);
                field5.setVisible(false);
                break;
            default:
                System.out.println("Unknown table selected.");
                return;
        }

        // Clear previous data in fields
        field1.clear();
        field2.clear();
        field3.clear();
        field4.clear();
        field5.clear();

        // Show the form
        addDataPane.setVisible(true);
    }



    @FXML
    private void submitAddData() {
        String selectedTable = tableComboBox.getValue();

        // Validate selection and input
        if (selectedTable == null) {
            System.out.println("No table selected.");
            return;
        }

        // Collect data from form
        String value1 = field1.getText();
        String value2 = field2.getText();
        String value3 = field3.getText();
        String value4 = field4.getText();
        String value5 = field5.getText();

        // Prepare query
        String query = "";
        int parameterCount = 0;
        switch (selectedTable) {
            case "matches":
                query = "INSERT INTO matches (id, mdate, startsat, ticketprice, mtype) VALUES (?, ?, ?, ?, ?)";
                parameterCount = 5;
                break;
            case "spectators":
                query = "INSERT INTO spectators  (id, sname, male, haspass) VALUES (?, ?, ?, ?)";
                parameterCount = 4;
                break;
            case "entries":
                query = "INSERT INTO entries (spectatorid, matchid, timestamp) VALUES (?, ?, ?)";
                parameterCount = 3;
                break;
            default:
                System.out.println("Unsupported table.");
                return;
        }

        // Execute query
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (parameterCount >= 1) stmt.setString(1, value1);
            if (parameterCount >= 2) stmt.setString(2, value2);
            if (parameterCount >= 3) stmt.setString(3, value3);
            if (parameterCount >= 4) stmt.setString(4, value4);
            if (parameterCount >= 5) stmt.setString(5, value5);

            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Record added successfully." : "Failed to add record.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Hide the form
        addDataPane.setVisible(false);
    }


    @FXML
    private void cancelAddData() {
        addDataPane.setVisible(false);
    }


//***************************************************************************************************************************************
//***************************************************************************************************************************************
//***************************************************************************************************************************************
//***************************************************************************************************************************************

    @FXML
    public void handleChangeAction() {
        System.out.println("Changing data...");
    }
    @FXML
    public void handleUpdateRecord(javafx.event.ActionEvent actionEvent) {
        System.out.println("Updating data...");
    }
    @FXML
    public void handleRowSelect(MouseEvent mouseEvent) {
        System.out.println("Row selected...");
    }

    //***************************************************************************************************************************************
    //***************************************************************************************************************************************
    //***************************************************************************************************************************************
    //***************************************************************************************************************************************






    @FXML
    private void handleDeleteAction() {
        System.out.println("Deleting data...");
        // Implement the logic for deleting data from the database
        // You can use a selected row or some other criteria to delete data
    }

}
