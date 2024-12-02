package org.example.javalecturehomework.controller;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeCloseResponse;
import com.oanda.v20.trade.TradeSpecifier;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.javalecturehomework.Config;
import org.example.javalecturehomework.model.*;
import org.example.javalecturehomework.service.MNBArfolyamServiceSoap;
import org.example.javalecturehomework.service.MNBArfolyamServiceSoapImpl;
import org.example.javalecturehomework.utils.DatabaseConnection;

import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.oanda.v20.pricing.PricingGetResponse;
public class DatabaseMenuController {

//***************************************************************************************************************************************
    @FXML private TextField startDateField, endDateField;
    @FXML private CheckBox eurCheckBox, usdCheckBox;
    @FXML private Pane graphFormPane;
    @FXML private ComboBox<String> graphDataComboBox;
    @FXML private Pane download2FormPane;

    @FXML
    private TableView<MarketPosition> positionTableView;
    @FXML
    private TableColumn<MarketPosition, String> currencyColumn;
    @FXML
    private TableColumn<MarketPosition, Integer> quantityColumn;
    @FXML
    private TableColumn<MarketPosition, String> statusColumn;


    private Context ctx = new Context(Config.URL, Config.TOKEN);
//***************************************************************************************************************************************

    @FXML private Button finalDeleteButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelDeleteButton;


    //    @FXML private ComboBox<String> matchComboBox, spectatorComboBox;
@FXML private Pane changeDataPane; // The pane for the Change Data form
 @FXML private ComboBox<String> recordIdComboBox;
 @FXML private ComboBox<String> DeleterecordIdComboBox;
    @FXML private Label labelId;
    @FXML private Label DeletelabelId;
    @FXML private ComboBox<String> matchIdComboBox;
    @FXML private ComboBox<String> spectatorIdComboBox;
    @FXML private Label formTitle, label1, label2, label3, label4;
    @FXML private TextField field1, field2, field3, field4;
    @FXML private Label formAddTitle, Addlabel1, Addlabel2, Addlabel3, Addlabel4, Addlabel5;
    @FXML private TextField Addfield1, Addfield2, Addfield3, Addfield4, Addfield5;
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

        formAddTitle.setText("Add New Record to " + selectedTable);

        // Set labels based on selected table
        switch (selectedTable) {
            case "matches":
                Addlabel1.setText("ID");
                Addlabel2.setText("Date");
                Addlabel3.setText("Start Time");
                Addlabel4.setText("Ticket Price");
                Addlabel5.setText("Match Type");
                Addlabel1.setVisible(true);
                Addlabel2.setVisible(true);
                Addlabel3.setVisible(true);
                Addlabel4.setVisible(true);
                Addlabel5.setVisible(true);
                Addfield1.setVisible(true);
                Addfield2.setVisible(true);
                Addfield3.setVisible(true);
                Addfield4.setVisible(true);
                Addfield5.setVisible(true);
                matchIdComboBox.setVisible(false);
                spectatorIdComboBox.setVisible(false);
                break;
            case "spectators":
                Addlabel1.setText("ID");
                Addlabel2.setText("Name");
                Addlabel3.setText("Sex (1 -> Male OR 0 -> Female)");
                Addlabel4.setText("has Pass 1 (1 -> YES OR 0 -> NO)");
                Addlabel1.setVisible(true);
                Addlabel2.setVisible(true);
                Addlabel3.setVisible(true);
                Addlabel4.setVisible(true);
                Addlabel5.setVisible(false);
                Addfield1.setVisible(true);
                Addfield2.setVisible(true);
                Addfield3.setVisible(true);
                Addfield4.setVisible(true);
                Addfield5.setVisible(false);
                matchIdComboBox.setVisible(false);
                spectatorIdComboBox.setVisible(false);
                break;
            case "entries":
                Addlabel1.setText("Match ID");
                Addlabel2.setText("Spectator ID");
                Addlabel3.setText("Time Stamp");
                Addlabel1.setVisible(true);
                Addlabel2.setVisible(true);
                Addlabel3.setVisible(true);
                Addlabel4.setVisible(false);
                Addlabel5.setVisible(false);
                Addfield1.setVisible(false);
                Addfield2.setVisible(false);
                Addfield3.setVisible(true);
                Addfield4.setVisible(false);
                Addfield5.setVisible(false);
                matchIdComboBox.setVisible(true);
                spectatorIdComboBox.setVisible(true);
                loadComboBoxData();

                break;
            default:
                System.out.println("Unknown table selected.");
                return;
        }

        // Clear previous data in fields
        Addfield1.clear();
        Addfield2.clear();
        Addfield3.clear();
        Addfield4.clear();
        Addfield5.clear();

        // Show the form
        addDataPane.setVisible(true);
    }


    private void loadComboBoxData() {
        // Clear existing items
        matchIdComboBox.getItems().clear();
        spectatorIdComboBox.getItems().clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load match IDs
            String matchQuery = "SELECT id FROM matches";
            try (PreparedStatement matchStmt = conn.prepareStatement(matchQuery);
                 ResultSet matchRs = matchStmt.executeQuery()) {
                while (matchRs.next()) {
                    matchIdComboBox.getItems().add(matchRs.getString("id"));
                }
            }

            // Load spectator IDs
            String spectatorQuery = "SELECT id FROM spectators";
            try (PreparedStatement spectatorStmt = conn.prepareStatement(spectatorQuery);
                 ResultSet spectatorRs = spectatorStmt.executeQuery()) {
                while (spectatorRs.next()) {
                    spectatorIdComboBox.getItems().add(spectatorRs.getString("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String value1 = Addfield1.getText();
        String value2 = Addfield2.getText();
        String value3 = Addfield3.getText();
        String value4 = Addfield4.getText();
        String value5 = Addfield5.getText();

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
                value1 = matchIdComboBox.getValue(); // Use selected match ID
                value2 = spectatorIdComboBox.getValue(); // Use selected spectator I
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
//*********************************************Soap******************************************************************************************

    private String downloadSoapData(String startDate, String endDate, String selectedCurrency) throws Exception {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        return service.getExchangeRates(startDate, endDate, selectedCurrency);
    }




    private void saveDataToFile(String data) {
        // Get the current project directory
        String projectDirectory = System.getProperty("user.dir");

        // Create the file path inside the project directory
        File file = new File(projectDirectory + File.separator + "bank.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Write the data to the file (overwrites the file if it already exists)
            writer.write(data);
        } catch (IOException e) {
            // Show error if unable to save the file
            showAlert("Error", "Failed to save the file: " + e.getMessage());
        }
    }


    // Method to handle SOAP data download based on filters
    private void downloadFilteredData(String startDate, String endDate, String currency, boolean includeMetaData, String dataOption, ProgressBar progressBar) {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        try {
            // Simulate data download progress
            for (int i = 0; i <= 100; i += 10) {
                final int progress = i;
                Thread.sleep(50); // Simulate processing delay
                progressBar.setProgress(progress / 100.0);
            }

            // Call SOAP service to get filtered data
            String exchangeRates = service.getExchangeRates(startDate, endDate, currency);

            // Add metadata or modify data if required
            if (includeMetaData) {
                exchangeRates += "\nMetadata: Data generated with option " + dataOption;
            }

            // Save data to Bank.txt
            saveDataToFile(exchangeRates);

            showAlert("Download Successful", "Filtered data has been saved to Bank.txt.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred during the download: " + e.getMessage());
        }
    }
    // Method to graph the data

    private LineChart<String, Number> generateGraph(String startDate, String endDate, String currency, boolean includeMetaData) {
        // Simulated data fetching and processing
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            data.add(new XYChart.Data<>("Day " + i, random.nextDouble() * 100)); // Simulated exchange rate
        }

        // Graph series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Exchange Rate for " + currency + " (" + startDate + " to " + endDate + ")");
        series.setData(data);

        // Include metadata, if selected
        if (includeMetaData) {
            series.setName(series.getName() + " - Metadata Included");
        }

        // Line chart setup
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Exchange Rate");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);
        lineChart.setTitle("Currency Exchange Rates");

        return lineChart;
    }
//***************************************************************************************************************************************
//***************************************************************************************************************************************

    @FXML
    public void handleChangeAction() {
        String selectedTable = tableComboBox.getValue();

        if (selectedTable == null) {
            System.out.println("Please select a table first.");
            return;
        }

        formTitle.setText("Change Record in " + selectedTable);

        // Set up form labels based on the selected table
        switch (selectedTable) {
            case "matches":
                labelId.setText("Select Match ID");
                label1.setText("Date");
                label2.setText("Start Time");
                label3.setText("Ticket Price");
                label4.setText("Match Type");
                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(true);
                field1.setVisible(true);
                field2.setVisible(true);
                field3.setVisible(true);
                field4.setVisible(true);
                matchIdComboBox.setVisible(false);
                spectatorIdComboBox.setVisible(false);
                break;
            case "spectators":
                labelId.setText("Select Spectator ID");
                label1.setText("Name");
                label2.setText("Sex (1 -> Male OR 0 -> Female)");
                label3.setText("has Pass 1 (1 -> YES OR 0 -> NO)");
                label1.setVisible(true);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(false);
                field1.setVisible(true);
                field2.setVisible(true);
                field3.setVisible(true);
                field4.setVisible(false);
                matchIdComboBox.setVisible(false);
                spectatorIdComboBox.setVisible(false);
                break;
            case "entries":
                labelId.setText("Select Entry ID");
                label1.setText("Match ID");
                label2.setText("Spectator ID");
                label3.setText("Time Stamp");
                label1.setVisible(true);
                labelId.setVisible(false);
                recordIdComboBox.setVisible(false);
                label2.setVisible(true);
                label3.setVisible(true);
                label4.setVisible(false);
                field1.setVisible(false);  // Hide text field for Match ID
                field2.setVisible(false);  // Hide text field for Spectator ID
                field3.setVisible(true);   // Show text field for Time Stamp
                field4.setVisible(false);
                matchIdComboBox.setVisible(true);
                spectatorIdComboBox.setVisible(true);

                // Populate the ComboBoxes for Match and Spectator IDs
                loadMatchIdComboBox();  // Load Match IDs into ComboBox
                loadSpectatorIdComboBox();  // Load Spectator IDs into ComboBox
                break;
            default:
                System.out.println("Unknown table selected.");
                return;
        }

        // Load record IDs into the ComboBox
        loadRecordIdComboBox(selectedTable);

        // Show the form
        changeDataPane.setVisible(true);
    }
    private void loadMatchIdComboBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id FROM matches")) {

            while (resultSet.next()) {
                matchIdComboBox.getItems().add(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading Match IDs: " + e.getMessage());
        }
    }

    private void loadSpectatorIdComboBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id FROM spectators")) {

            while (resultSet.next()) {
                spectatorIdComboBox.getItems().add(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading Spectator IDs: " + e.getMessage());
        }
    }

    private void loadRecordIdComboBox(String tableName) {
        ObservableList<String> recordIds = FXCollections.observableArrayList();
        String query = "SELECT id FROM " + tableName;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                recordIds.add(resultSet.getString("id"));
            }

            recordIdComboBox.setItems(recordIds);
        } catch (SQLException e) {
            System.out.println("Error loading record IDs: " + e.getMessage());
        }
    }


    @FXML
    private void loadRecordData() {
        String selectedTable = tableComboBox.getValue();
        String selectedId = recordIdComboBox.getValue();

        if (selectedId == null) {
            return;
        }

        String query = "SELECT * FROM " + selectedTable + " WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectedId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    field1.setText(resultSet.getString(2)); // Adjust based on column order
                    field2.setText(resultSet.getString(3));
                    field3.setText(resultSet.getString(4));
                    field4.setText(resultSet.getString(5));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading record data: " + e.getMessage());
        }
    }

    @FXML
    private void submitChangeData() {String selectedTable = tableComboBox.getValue();
        String selectedId = recordIdComboBox.getValue(); // This is no longer used for entries table

        if (selectedTable == null) {
            System.out.println("Please select a table to update.");
            return;
        }

        String query = "";
        switch (selectedTable) {
            case "matches":
                query = "UPDATE matches SET mdate = ?, startsat = ?, ticketprice = ?, mtype = ? WHERE id = ?";
                break;
            case "spectators":
                query = "UPDATE spectators SET sname = ?, male = ?, haspass = ? WHERE id = ?";
                break;
            case "entries":
                // The query for entries table will use both spectatorid and matchid in the WHERE clause
                query = "UPDATE entries SET spectatorid = ?, matchid = ?, timestamp = ? WHERE spectatorid = ? AND matchid = ?";
                break;
            default:
                System.out.println("Unknown table selected.");
                return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameters for the prepared statement
            switch (selectedTable) {
                case "matches":
                    preparedStatement.setString(1, field1.getText());  // mdate
                    preparedStatement.setString(2, field2.getText());  // startsat
                    preparedStatement.setString(3, field3.getText());  // ticketprice
                    preparedStatement.setString(4, field4.getText());  // mtype
                    preparedStatement.setString(5, selectedId);  // match id (only for matches)
                    break;
                case "spectators":
                    preparedStatement.setString(1, field1.getText());  // sname
                    preparedStatement.setString(2, field2.getText());  // male
                    preparedStatement.setString(3, field3.getText());  // haspass
                    preparedStatement.setString(4, selectedId);  // spectator id (only for spectators)
                    break;
                case "entries":
                    preparedStatement.setString(1, spectatorIdComboBox.getValue());  // spectatorid
                    preparedStatement.setString(2, matchIdComboBox.getValue());  // matchid
                    preparedStatement.setString(3, field3.getText());  // timestamp
                    // Use the spectatorid and matchid from the ComboBoxes in the WHERE clause
                    preparedStatement.setString(4, spectatorIdComboBox.getValue());  // spectatorid in WHERE clause
                    preparedStatement.setString(5, matchIdComboBox.getValue());  // matchid in WHERE clause
                    break;
            }

            // Execute the query
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record was updated.");
            }

            // Hide the form
            changeDataPane.setVisible(false);
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    @FXML
    private void cancelChangeData() {
        changeDataPane.setVisible(false);
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
        String selectedTable = tableComboBox.getValue();

        // Check if a table is selected
        if (selectedTable == null) {
            System.out.println("Please select a table to delete from.");
            return;
        }

        // Show the record selection options (ComboBox and final Delete button)
        deleteButton.setVisible(false);  // Hide the "Delete Data" button once clicked
        DeletelabelId.setVisible(true);  // Make the "Select Record ID" label visible
        DeleterecordIdComboBox.setVisible(true);  // Make the record ID ComboBox visible
        finalDeleteButton.setVisible(true);  // Make the final "Delete" button visible
        cancelDeleteButton.setVisible(true);

        // Load the record IDs for the selected table
        loadRecordIds(selectedTable);
    }


    private void loadRecordIds(String selectedTable) {
        DeleterecordIdComboBox.getItems().clear();  // Clear existing items from the ComboBox

        // Define the query to fetch IDs for the selected table
        String query = "SELECT id FROM " + selectedTable;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                DeleterecordIdComboBox.getItems().add(resultSet.getString("id"));
            }

            // If no IDs are loaded, show an alert
            if (DeleterecordIdComboBox.getItems().isEmpty()) {
                showAlert("No Records", "No records found in the selected table.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Error loading record IDs: " + e.getMessage());
        }
    }

    @FXML
    private void submitDeleteData() {
        String selectedTable = tableComboBox.getValue();
        String selectedId = DeleterecordIdComboBox.getValue();

        // Check if both a table and record ID are selected
        if (selectedId == null || selectedTable == null) {
            showAlert("Error", "Please select both a table and record ID to delete.");
            return;
        }

        // Define the DELETE query
        String query = "DELETE FROM " + selectedTable + " WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the ID parameter
            preparedStatement.setString(1, selectedId);

            // Execute the DELETE query
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                showAlert("Success", "Record deleted successfully.");
            } else {
                showAlert("Failure", "No record was deleted. Please check if the ID exists.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Error deleting record: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    private void deleteData() {
        String selectedTable = tableComboBox.getValue();
        String selectedId = DeleterecordIdComboBox.getValue();

        // Check if both a table and record ID are selected
        if (selectedTable == null || selectedId == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Entry Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select both a table and a record ID to delete.");
            alert.showAndWait();
            return;
        }

        // Display a confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Record");
        alert.setContentText("Are you sure you want to delete the record with ID " + selectedId + " from the table " + selectedTable + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteRecord(selectedTable, selectedId);
        }

        // Reload the record IDs after deletion attempt
        loadRecordIds(selectedTable);
    }

    private void deleteRecord(String selectedTable, String selectedId) {
        // Define the DELETE query
        String query = "DELETE FROM " + selectedTable + " WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_homework", "root", "mynewpass");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the ID parameter
            preparedStatement.setString(1, selectedId);

            // Execute the DELETE query
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Record Deleted");
                alert.setContentText("Record with ID " + selectedId + " was successfully deleted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Failed");
                alert.setContentText("No record was deleted. Please check the ID or table.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Deleting Record");
            alert.setContentText("Error deleting record: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void cancelDeleteData() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to cancel the delete operation?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Hide delete-related elements
            DeletelabelId.setVisible(false);
            DeleterecordIdComboBox.setVisible(false);
            finalDeleteButton.setVisible(false);

            // Clear ComboBox selection and items
            DeleterecordIdComboBox.getSelectionModel().clearSelection();
            DeleterecordIdComboBox.getItems().clear();
            deleteButton.setVisible(true);
        }
    }

    //***************************************************************************************************************************************
    //***************************************************************************************************************************************
    public void openDownloadForm(ActionEvent actionEvent) {
        // Create a new window
        Stage downloadStage = new Stage();
        downloadStage.setTitle("Download SOAP Data");


        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        Label currencyLabel = new Label("Currency:");
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "HUF");


        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);


        Button downloadButton = new Button("Download Data");
        downloadButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String selectedCurrency = currencyComboBox.getValue();

            if (startDate == null || endDate == null || selectedCurrency == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }


            if (startDate.isAfter(endDate)) {
                showAlert("Error -_-", "Start date cannot be after the end date.");
                return;
            }


            Thread downloadThread = new Thread(() -> {
                try {
                    // Simulate progress updates
                    for (int i = 0; i <= 100; i++) {
                        double progress = i / 100.0;
                        Thread.sleep(10); // Simulate progress delay
                        final int currentProgress = i;


                        javafx.application.Platform.runLater(() -> progressBar.setProgress(progress));
                    }


                    String exchangeRates = downloadSoapData(startDate.toString(), endDate.toString(), selectedCurrency);


                    saveDataToFile(exchangeRates);


                    javafx.application.Platform.runLater(() ->
                            showAlert("Download Successful", "All data has been downloaded to the file bank.txt.")
                    );

                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() ->
                            showAlert("Error", "An error occurred during the download: " + ex.getMessage())
                    );
                }
            });

            downloadThread.start();
        });


        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                currencyLabel, currencyComboBox,
                progressBar, downloadButton
        );


        Scene scene = new Scene(vbox, 300, 350);
        downloadStage.setScene(scene);
        downloadStage.show();
    }

    public void downloadFilteredSoapData(javafx.event.ActionEvent actionEvent){

        Stage filterStage = new Stage();
        filterStage.setTitle("Download Filtered SOAP Data");

        // Create input components
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        Label currencyLabel = new Label("Currency:");
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "HUF");

        Label optionsLabel = new Label("Additional Options:");
        CheckBox includeMetaDataCheckbox = new CheckBox("Include Metadata");
        RadioButton detailedDataRadio = new RadioButton("Detailed Data");
        RadioButton summaryDataRadio = new RadioButton("Summary Data");


        ToggleGroup dataOptionsGroup = new ToggleGroup();
        detailedDataRadio.setToggleGroup(dataOptionsGroup);
        summaryDataRadio.setToggleGroup(dataOptionsGroup);


        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setVisible(false);


        Button downloadButton = new Button("Download Data");
        downloadButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String selectedCurrency = currencyComboBox.getValue();
            boolean includeMetaData = includeMetaDataCheckbox.isSelected();
            RadioButton selectedDataOption = (RadioButton) dataOptionsGroup.getSelectedToggle();

            if (startDate == null || endDate == null || selectedCurrency == null || selectedDataOption == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            if (((java.time.LocalDate) startDate).isAfter(endDate)) {
                showAlert("Error", "Start date cannot be after end date.");
                return;
            }

            String dataOption = selectedDataOption.getText();


            progressBar.setVisible(true);
            downloadFilteredData(startDate.toString(), endDate.toString(), selectedCurrency, includeMetaData, dataOption, progressBar);
        });

        // Arrange components in a layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                currencyLabel, currencyComboBox,
                optionsLabel, includeMetaDataCheckbox, detailedDataRadio, summaryDataRadio,
                progressBar, downloadButton
        );

        // Set the scene and show the stage
        Scene scene = new Scene(vbox, 350, 400);
        filterStage.setScene(scene);
        filterStage.show();
    }

    public void graphSoapData(ActionEvent actionEvent) {
        {

            Stage graphStage = new Stage();
            graphStage.setTitle("Graph SOAP Data");


            Label startDateLabel = new Label("Start Date:");
            DatePicker startDatePicker = new DatePicker();

            Label endDateLabel = new Label("End Date:");
            DatePicker endDatePicker = new DatePicker();

            Label currencyLabel = new Label("Currency:");
            ComboBox<String> currencyComboBox = new ComboBox<>();
            currencyComboBox.getItems().addAll("USD", "EUR", "HUF");

            CheckBox includeMetaDataCheckBox = new CheckBox("Include Metadata");

            Button generateGraphButton = new Button("Generate Graph");


            VBox graphContainer = new VBox(10);

            generateGraphButton.setOnAction(e -> {
                String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
                String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
                String selectedCurrency = currencyComboBox.getValue();

                if (startDate.isEmpty() || endDate.isEmpty() || selectedCurrency == null) {
                    showAlert("Error -_-", "Please fill in all fields");
                    return;
                }


                graphContainer.getChildren().clear();


                LineChart<String, Number> lineChart = generateGraph(startDate, endDate, selectedCurrency, includeMetaDataCheckBox.isSelected());
                graphContainer.getChildren().add(lineChart);
            });


            VBox vbox = new VBox(10);
            vbox.setPadding(new javafx.geometry.Insets(10));
            vbox.getChildren().addAll(
                    startDateLabel, startDatePicker,
                    endDateLabel, endDatePicker,
                    currencyLabel, currencyComboBox,
                    includeMetaDataCheckBox,
                    generateGraphButton,
                    graphContainer
            );

            Scene scene = new Scene(vbox, 800, 600);
            graphStage.setScene(scene);
            graphStage.show();
        }
    }

    public void parallelAction(ActionEvent actionEvent) {

        Label labelOne = new Label("Label 1 : Waiting... ");
        Label labelTwo = new Label("Label 2 : Waiting... ");
        Button startButton = new Button("Start the parallel tasks");


        startButton.setOnAction(e -> {
            startParallelTask(labelOne, labelTwo);
        });


        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(labelOne, labelTwo, startButton);


        Stage parallelStage = new Stage();
        parallelStage.setTitle("Parallel Programming Demo");
        parallelStage.setScene(new Scene(layout, 300, 200));
        parallelStage.show();
    }

    private void startParallelTask(Label labelOne, Label labelTwo) {

        Task<Void> taskOne = new Task<>() {
            @Override
            protected Void call() {
                int count = 0;
                while (!isCancelled()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    int finalCount = count;
                    Platform.runLater(() -> labelOne.setText("Label 1: Count " + finalCount));
                    count++;
                }
                return null;
            }
        };

        Task<Void> taskTwo = new Task<>() {
            @Override
            protected Void call() {
                int count = 0;
                while (!isCancelled()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    int finalCount = count;
                    Platform.runLater(() -> labelTwo.setText("Label 2: Count " + finalCount));
                    count++;
                }
                return null;
            }
        };


        Thread threadOne = new Thread(taskOne);
        threadOne.setDaemon(true);
        threadOne.start();

        Thread threadTwo = new Thread(taskTwo);
        threadTwo.setDaemon(true);
        threadTwo.start();
    }

    public void accountInformationAction(ActionEvent actionEvent) {
        Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);


        Stage progressStage = new Stage();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label progressLabel = new Label("Loading account information...");
        GridPane progressPane = new GridPane();
        progressPane.setHgap(10);
        progressPane.setVgap(10);
        progressPane.add(progressIndicator, 0, 0);
        progressPane.add(progressLabel, 0, 1);
        Scene progressScene = new Scene(progressPane, 300, 200);
        progressStage.setScene(progressScene);
        progressStage.setTitle("Please Wait");
        progressStage.show();


        Task<AccountSummary> task = new Task<>() {
            @Override
            protected AccountSummary call() throws Exception {

                return ctx.account.summary(new AccountID(Config.ACCOUNTID)).getAccount();
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            progressStage.close();
            AccountSummary summary = task.getValue();


            Stage infoStage = new Stage();
            GridPane infoPane = new GridPane();
            infoPane.setHgap(10);
            infoPane.setVgap(10);

            infoPane.add(new Label("Account ID:"), 0, 0);
            infoPane.add(new Label(summary.getId().toString()), 1, 0);

            infoPane.add(new Label("Alias:"), 0, 1);
            infoPane.add(new Label(summary.getAlias()), 1, 1);

            infoPane.add(new Label("Currency:"), 0, 2);
            infoPane.add(new Label(summary.getCurrency().toString()), 1, 2);

            infoPane.add(new Label("Balance:"), 0, 3);
            infoPane.add(new Label(summary.getBalance().toString()), 1, 3);

            infoPane.add(new Label("NAV:"), 0, 4);
            infoPane.add(new Label(summary.getNAV().toString()), 1, 4);

            infoPane.add(new Label("Unrealized P/L:"), 0, 5);
            infoPane.add(new Label(summary.getUnrealizedPL().toString()), 1, 5);

            infoPane.add(new Label("Margin Rate:"), 0, 6);
            infoPane.add(new Label(summary.getMarginRate().toString()), 1, 6);

            infoPane.add(new Label("Open Trades:"), 0, 7);
            infoPane.add(new Label(String.valueOf(summary.getOpenTradeCount())), 1, 7);


            Scene infoScene = new Scene(infoPane, 400, 300);
            infoStage.setScene(infoScene);
            infoStage.setTitle("Account Information");
            infoStage.show();
        });


        task.setOnFailed(workerStateEvent -> {
            progressStage.close();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load Account Information");
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();
        });


        new Thread(task).start();
    }

    public void CurrentPriceDeci(ActionEvent actionEvent) {
        Stage priceStage = new Stage();
        priceStage.setTitle("Current prices");


        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        Label titleLabel = new Label("Check Current Prices");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Please Select a currency pair.");
        currencyPairDropdown.setPrefWidth(200);


        Button fetchPriceButton = new Button("Get Current Price");
        fetchPriceButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");


        Label priceLabel = new Label();
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");


        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);


        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, fetchPriceButton, loadingIndicator, priceLabel);


        Scene scene = new Scene(mainLayout, 400, 300);
        priceStage.setScene(scene);
        priceStage.show();


        fetchPriceButton.setOnAction(e -> {
            String selectedPair = currencyPairDropdown.getValue();

            if (selectedPair == null) {
                priceLabel.setText("Please select a currency pair.");
                return;
            }


            loadingIndicator.setVisible(true);
            priceLabel.setText("");


            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {

                PricingGetRequest request = new PricingGetRequest(
                        new com.oanda.v20.account.AccountID(Config.ACCOUNTID),
                        List.of(selectedPair.replace("/", "_"))
                );


                PricingGetResponse pricingResponse = ctx.pricing.get(request);


                ClientPrice clientPrice = pricingResponse.getPrices().get(0);
                String bidVal = clientPrice.getBids().get(0).getPrice().toString();
                String askVal = clientPrice.getAsks().get(0).getPrice().toString();

                priceLabel.setText(String.format("Bid: %s | Ask: %s", bidVal, askVal));



                priceLabel.setText(String.format("Bid: %s | Ask: %s", bidVal, askVal));
            } catch (Exception ex) {
                priceLabel.setText("Failed to load the prices -_-.");
                ex.printStackTrace();
            } finally {

                loadingIndicator.setVisible(false);
            }
        });
    }

    public void historicalPricesAction(ActionEvent actionEvent) {
        Stage historicalStage = new Stage();
        historicalStage.setTitle("Historical Prices");



        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));


        Label titleLabel = new Label("Historical Prices");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Select a currency pair");
        currencyPairDropdown.setPrefWidth(200);


        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");


        Button displayPriceButton = new Button("Get Historical Prices");
        displayPriceButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");


        TableView<PriceData> priceTable = new TableView<>();
        TableColumn<PriceData, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        TableColumn<PriceData, String> bidColumn = new TableColumn<>("Bid");
        bidColumn.setCellValueFactory(cellData -> cellData.getValue().bidProperty());
        TableColumn<PriceData, String> askColumn = new TableColumn<>("Ask");
        askColumn.setCellValueFactory(cellData -> cellData.getValue().askProperty());

        priceTable.getColumns().addAll(dateColumn, bidColumn, askColumn);


        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, startDatePicker, endDatePicker, displayPriceButton, priceTable);


        Scene scene = new Scene(mainLayout, 600, 400);
        historicalStage.setScene(scene);
        historicalStage.show();


        displayPriceButton.setOnAction(e -> {
            String selectedPair = currencyPairDropdown.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (selectedPair == null || startDate == null || endDate == null) {

                Error("Please select a currency pair and dates.");
                return;
            }


            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {

                PricingGetRequest request = new PricingGetRequest(
                        new AccountID(Config.ACCOUNTID),
                        List.of(selectedPair.replace("/", "_"))
                );


                PricingGetResponse pricingResponse = ctx.pricing.get(request);


                List<PriceData> historicalPrices = new ArrayList<>();
                for (ClientPrice clientPrice : pricingResponse.getPrices()) {
                    // Assuming the historical data is available in the ClientPrice object
                    String bid = clientPrice.getBids().get(0).getPrice().toString();
                    String ask = clientPrice.getAsks().get(0).getPrice().toString();
                    // Populate the list with the historical data
                    historicalPrices.add(new PriceData(clientPrice.getTime().toString(), bid, ask));
                }


                priceTable.getItems().setAll(historicalPrices);
            } catch (Exception ex) {
                Error("Failed to fetch historical prices.");
                ex.printStackTrace();
            }
        });
    }
    private void Error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }    public void positionOpeningAction(ActionEvent actionEvent) {

        Stage positionStage = new Stage();
        positionStage.setTitle("Open Position");


        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));


        Label titleLabel = new Label("Open a Position");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Please Select a currency pair ");
        currencyPairDropdown.setPrefWidth(200);


        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        quantityField.setPrefWidth(200);


        ComboBox<String> directionDropdown = new ComboBox<>();
        directionDropdown.getItems().addAll(" Buy ", " Sell ");
        directionDropdown.setPromptText("Select direction to do");
        directionDropdown.setPrefWidth(200);


        Button openPositionButton = new Button("Open Position");
        openPositionButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");


        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");


        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, quantityField, directionDropdown, openPositionButton, resultLabel);


        Scene scene = new Scene(mainLayout, 400, 300);
        positionStage.setScene(scene);
        positionStage.show();


        openPositionButton.setOnAction(e -> {
            String selectedCurrPair = currencyPairDropdown.getValue();
            String quantityText = quantityField.getText();
            String direction = directionDropdown.getValue();


            if (selectedCurrPair == null || quantityText.isEmpty() || direction == null) {
                resultLabel.setText("Please fill in all fields.");
                return;
            }


            double quantity;
            try {
                quantity = Double.parseDouble(quantityText);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid quantity.");
                return;
            }

            try {

                String action = (direction.equals("Buy")) ? "Buying" : "Selling";
                resultLabel.setText(String.format("Opening %s position for %s with quantity %.3f", action, selectedCurrPair, quantity));



            } catch (Exception ex) {
                resultLabel.setText("Failed to open position.");
                ex.printStackTrace();
            }
        });
    }



    public void positionCloseAction(ActionEvent actionEvent) {

        Stage closingStage = new Stage();
        closingStage.setTitle("Close Position");


        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        Label titleLabel = new Label("Close Position");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        Label positionIdLabel = new Label("Enter Position ID:");


        TextField positionIdField = new TextField();
        positionIdField.setPromptText("Position ID");


        Button closePositionButton = new Button("Close Position");
        closePositionButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");


        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");


        mainLayout.getChildren().addAll(titleLabel, positionIdLabel, positionIdField, closePositionButton, resultLabel);


        Scene scene = new Scene(mainLayout, 400, 300);
        closingStage.setScene(scene);
        closingStage.show();


        closePositionButton.setOnAction(e -> {
            String positionId = positionIdField.getText().trim();

            if (positionId.isEmpty()) {
                resultLabel.setText("Please enter a position ID.");
                return;
            }




            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {

                TradeSpecifier tradeSpecifier = new TradeSpecifier(positionId);


                TradeCloseRequest closeRequest = new TradeCloseRequest(Config.ACCOUNTID, tradeSpecifier);


                TradeCloseResponse closeResponse = ctx.trade.close(closeRequest);


                if (closeResponse != null) {
                    resultLabel.setText("Position closed successfully!");
                } else {
                    resultLabel.setText("Failed to close position.");
                }
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }


    public void openPositionsAction(ActionEvent actionEvent) {

        Stage openedPositionsStage = new Stage();
        openedPositionsStage.setTitle("Opened Positions");


        VBox mainLayout = new VBox(15);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));


        Label titleLabel = new Label("Opened Positions");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        TableView<org.example.javalecturehomework.model.Position> positionsTable = new TableView<>();
        positionsTable.setPrefWidth(400);


        TableColumn<Position, String> currencyPairColumn = new TableColumn<>("Currency Pair");
        currencyPairColumn.setCellValueFactory(new PropertyValueFactory<>("currencyPair"));

        TableColumn<Position, String> directionColumn = new TableColumn<>("Direction");
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));

        TableColumn<Position, Double> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        positionsTable.getColumns().addAll(currencyPairColumn, directionColumn, quantityColumn);


        ObservableList<Position> openedPositions = FXCollections.observableArrayList(
                new Position("EUR/USD", "Buy", 1000),
                new Position("GBP/USD", "Sell", 1500),
                new Position("USD/JPY", "Buy", 2000)
        );


        positionsTable.setItems(openedPositions);


        mainLayout.getChildren().addAll(titleLabel, positionsTable);


        Scene scene = new Scene(mainLayout, 500, 350);
        openedPositionsStage.setScene(scene);
        openedPositionsStage.show();
    }



    //***************************************************************************************************************************************
    //***************************************************************************************************************************************  //***************************************************************************************************************************************
    //    //***************************************************************************************************************************************
}
