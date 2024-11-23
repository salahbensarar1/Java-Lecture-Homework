package org.example.javalecturehomework.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.example.javalecturehomework.model.Entry;
import org.example.javalecturehomework.model.Match;
import org.example.javalecturehomework.model.Spectator;

import java.sql.*;

public class DatabaseMenuController {

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TextField filterTextField;

    @FXML
    private RadioButton filterRadioButton;

    @FXML
    private CheckBox filterCheckBox;
    @FXML
    private ComboBox<String> tableComboBox;
    @FXML
    private TableView<Object> dataTable;
    @FXML
    private TableColumn<Object, String> column1;
    @FXML
    private TableColumn<Object, String> column2;
    @FXML
    private TableColumn<Object, String> column3;
    @FXML
    private TableColumn<Object, String> column4;
    @FXML
    private TableColumn<Object, String> column5;

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

    private String buildQueryForTable(String tableName) {
        if (tableName.equals("matches")) {
            return "SELECT * FROM matches LIMIT 10";
        } else if (tableName.equals("entries")) {
            return "SELECT * FROM entries LIMIT 10";
        } else if (tableName.equals("spectators")) {
            return "SELECT * FROM spectators LIMIT 10";
        }
        return "";
    }
    @FXML
    private void handleReadAction() {
        System.out.println("Reading data...");
        fetchData();  // Load the data based on the selected table
    }

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


    @FXML
    private void handleWriteAction() {
        System.out.println("Adding data...");
        // Implement the logic for adding data to the database
        // You can collect input from TextFields or other UI components here
    }

    @FXML
    private void handleChangeAction() {
        System.out.println("Updating data...");
        // Implement the logic for updating data in the database
        // You can collect input from TextFields or other UI components
    }

    @FXML
    private void handleDeleteAction() {
        System.out.println("Deleting data...");
        // Implement the logic for deleting data from the database
        // You can use a selected row or some other criteria to delete data
    }
}
