package org.example.javalecturehomework.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.example.javalecturehomework.model.Entry;
import org.example.javalecturehomework.model.Match;
import org.example.javalecturehomework.model.Spectator;

import java.sql.*;

public class DatabaseMenuController {

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

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
        } catch (SQLException e) {
            e.printStackTrace();
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
        dataTable.setItems(data);

        if (selectedTable.equals("matches")) {
            column1.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Match) cellData.getValue()).getId())));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getMdate().toString()));
            column3.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getStartsAt().toString()));
            column4.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Match) cellData.getValue()).getTicketPrice())));
            column5.setCellValueFactory(cellData -> new SimpleStringProperty(((Match) cellData.getValue()).getMtype()));
        } else if (selectedTable.equals("entries")) {
            column1.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Entry) cellData.getValue()).getSpectatorId())));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Entry) cellData.getValue()).getMatchId())));
            column3.setCellValueFactory(cellData -> new SimpleStringProperty(((Entry) cellData.getValue()).getTimestamp().toString()));
        } else if (selectedTable.equals("spectators")) {
            column1.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Spectator) cellData.getValue()).getId())));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(((Spectator) cellData.getValue()).getSname()));
            column3.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Spectator) cellData.getValue()).isMale())));
            column4.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(((Spectator) cellData.getValue()).hasPass())));
        }
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
        System.out.println("Reading filtered data...");
        // Implement the logic for reading filtered data
        // You can add additional filter criteria before calling fetchData()
        fetchData(); // Modify this to fetch filtered data as needed
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
