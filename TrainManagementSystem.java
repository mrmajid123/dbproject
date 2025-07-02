package com.orangeline;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TrainManagementSystem extends JFrame {
    private JTextField txtUsername, txtTrainNo, txtFrom, txtTo, txtTime, txtStationId, txtStationName, txtLocation,
            txtPassengerId, txtPassengerName, txtTicketId, txtTicketTrainNo, txtPassengerNameTicket, txtDate;
    private JPasswordField txtPassword;
    private JTable table, stationTable, bookingTable, passengerTable;
    private DefaultTableModel tableModel, stationTableModel, bookingTableModel, passengerTableModel;
    private int selectedRow = -1, selectedStationRow = -1, selectedBookingRow = -1, selectedPassengerRow = -1;
    private JButton btnLogin, btnLogout, btnBack;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public TrainManagementSystem() {
        setTitle("Orange Train Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 500));

        cardPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) cardPanel.getLayout();

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(new JLabel("Orange Train Management System", JLabel.CENTER), gbc);
        gbc.gridwidth = 1; gbc.gridy = 1;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(txtUsername = new JTextField(15), gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(txtPassword = new JPasswordField(15), gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(btnLogin = new JButton("Login"), gbc);
        gbc.gridx = 1;
        loginPanel.add(new JButton("Exit") {{
            addActionListener(e -> System.exit(0));
        }}, gbc);
        cardPanel.add(loginPanel, "Login");

        // Menu Dashboard Panel
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        menuPanel.add(createButton("Train Schedule", e -> cardLayout.show(cardPanel, "TrainSchedule")), gbc);
        gbc.gridy = 1;
        menuPanel.add(createButton("Station Info", e -> cardLayout.show(cardPanel, "StationInfo")), gbc);
        gbc.gridy = 2;
        menuPanel.add(createButton("Ticket Booking", e -> cardLayout.show(cardPanel, "TicketBooking")), gbc);
        gbc.gridy = 3;
        menuPanel.add(createButton("Passenger", e -> cardLayout.show(cardPanel, "Passenger")), gbc);
        cardPanel.add(menuPanel, "Menu");

        // Train Schedule Panel
        JPanel trainSchedulePanel = new JPanel(new BorderLayout(10, 10));
        trainSchedulePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tableModel = new DefaultTableModel(new String[]{"Train No", "From", "To", "Time"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                txtTrainNo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtFrom.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtTo.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtTime.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });
        trainSchedulePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Train Details"));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Train No:"), gbc); gbc.gridx = 1; inputPanel.add(txtTrainNo = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("From:"), gbc); gbc.gridx = 1; inputPanel.add(txtFrom = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("To:"), gbc); gbc.gridx = 1; inputPanel.add(txtTo = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Time:"), gbc); gbc.gridx = 1; inputPanel.add(txtTime = new JTextField(10), gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(createButton("Add", e -> addTrain()));
        buttonPanel.add(createButton("View", e -> loadTrainData()));
        buttonPanel.add(createButton("Edit", e -> editTrain()));
        buttonPanel.add(createButton("Delete", e -> deleteTrain()));
        buttonPanel.add(btnBack = createButton("Back", e -> cardLayout.show(cardPanel, "Menu")));
        trainSchedulePanel.add(inputPanel, BorderLayout.NORTH);
        trainSchedulePanel.add(buttonPanel, BorderLayout.SOUTH);
        cardPanel.add(trainSchedulePanel, "TrainSchedule");

        // Station Info Panel
        JPanel stationInfoPanel = new JPanel(new BorderLayout(10, 10));
        stationInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        stationTableModel = new DefaultTableModel(new String[]{"Station ID", "Name", "Location"}, 0);
        stationTable = new JTable(stationTableModel);
        stationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stationTable.getSelectionModel().addListSelectionListener(e -> {
            selectedStationRow = stationTable.getSelectedRow();
            if (selectedStationRow != -1) {
                txtStationId.setText(stationTableModel.getValueAt(selectedStationRow, 0).toString());
                txtStationName.setText(stationTableModel.getValueAt(selectedStationRow, 1).toString());
                txtLocation.setText(stationTableModel.getValueAt(selectedStationRow, 2).toString());
            }
        });
        stationInfoPanel.add(new JScrollPane(stationTable), BorderLayout.CENTER);
        JPanel stationInputPanel = new JPanel(new GridBagLayout());
        stationInputPanel.setBorder(BorderFactory.createTitledBorder("Station Details"));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        stationInputPanel.add(new JLabel("Station ID:"), gbc); gbc.gridx = 1; stationInputPanel.add(txtStationId = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        stationInputPanel.add(new JLabel("Name:"), gbc); gbc.gridx = 1; stationInputPanel.add(txtStationName = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        stationInputPanel.add(new JLabel("Location:"), gbc); gbc.gridx = 1; stationInputPanel.add(txtLocation = new JTextField(10), gbc);
        JPanel stationButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        stationButtonPanel.add(createButton("Add", e -> addStation()));
        stationButtonPanel.add(createButton("View", e -> viewStations()));
        stationButtonPanel.add(createButton("Edit", e -> editStation()));
        stationButtonPanel.add(createButton("Delete", e -> deleteStation()));
        stationButtonPanel.add(btnBack = createButton("Back", e -> cardLayout.show(cardPanel, "Menu")));
        stationInfoPanel.add(stationInputPanel, BorderLayout.NORTH);
        stationInfoPanel.add(stationButtonPanel, BorderLayout.SOUTH);
        cardPanel.add(stationInfoPanel, "StationInfo");

        // Ticket Booking Panel
        JPanel ticketBookingPanel = new JPanel(new BorderLayout(10, 10));
        ticketBookingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bookingTableModel = new DefaultTableModel(new String[]{"Ticket ID", "Train No", "Passenger Name", "Date"}, 0);
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.getSelectionModel().addListSelectionListener(e -> {
            selectedBookingRow = bookingTable.getSelectedRow();
            if (selectedBookingRow != -1) {
                txtTicketId.setText(bookingTableModel.getValueAt(selectedBookingRow, 0).toString());
                txtTicketTrainNo.setText(bookingTableModel.getValueAt(selectedBookingRow, 1).toString());
                txtPassengerNameTicket.setText(bookingTableModel.getValueAt(selectedBookingRow, 2).toString());
                txtDate.setText(bookingTableModel.getValueAt(selectedBookingRow, 3).toString());
            }
        });
        ticketBookingPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        JPanel ticketInputPanel = new JPanel(new GridBagLayout());
        ticketInputPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        ticketInputPanel.add(new JLabel("Ticket ID:"), gbc); gbc.gridx = 1; ticketInputPanel.add(txtTicketId = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        ticketInputPanel.add(new JLabel("Train No:"), gbc); gbc.gridx = 1; ticketInputPanel.add(txtTicketTrainNo = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        ticketInputPanel.add(new JLabel("Passenger Name:"), gbc); gbc.gridx = 1; ticketInputPanel.add(txtPassengerNameTicket = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        ticketInputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc); gbc.gridx = 1; ticketInputPanel.add(txtDate = new JTextField(10), gbc);
        JPanel ticketButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        ticketButtonPanel.add(createButton("Book Ticket", e -> bookTicket()));
        ticketButtonPanel.add(createButton("View Bookings", e -> viewBookings()));
        ticketButtonPanel.add(createButton("Delete Booking", e -> deleteBooking()));
        ticketButtonPanel.add(btnBack = createButton("Back", e -> cardLayout.show(cardPanel, "Menu")));
        ticketBookingPanel.add(ticketInputPanel, BorderLayout.NORTH);
        ticketBookingPanel.add(ticketButtonPanel, BorderLayout.SOUTH);
        cardPanel.add(ticketBookingPanel, "TicketBooking");

        // Passenger Panel
        JPanel passengerPanel = new JPanel(new BorderLayout(10, 10));
        passengerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        passengerTableModel = new DefaultTableModel(new String[]{"Passenger ID", "Name"}, 0);
        passengerTable = new JTable(passengerTableModel);
        passengerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passengerTable.getSelectionModel().addListSelectionListener(e -> {
            selectedPassengerRow = passengerTable.getSelectedRow();
            if (selectedPassengerRow != -1) {
                txtPassengerId.setText(passengerTableModel.getValueAt(selectedPassengerRow, 0).toString());
                txtPassengerName.setText(passengerTableModel.getValueAt(selectedPassengerRow, 1).toString());
            }
        });
        passengerPanel.add(new JScrollPane(passengerTable), BorderLayout.CENTER);
        JPanel passengerInputPanel = new JPanel(new GridBagLayout());
        passengerInputPanel.setBorder(BorderFactory.createTitledBorder("Passenger Details"));
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        passengerInputPanel.add(new JLabel("Passenger ID:"), gbc); gbc.gridx = 1; passengerInputPanel.add(txtPassengerId = new JTextField(10), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        passengerInputPanel.add(new JLabel("Name:"), gbc); gbc.gridx = 1; passengerInputPanel.add(txtPassengerName = new JTextField(10), gbc);
        JPanel passengerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        passengerButtonPanel.add(createButton("Add Passenger", e -> addPassenger()));
        passengerButtonPanel.add(createButton("View Passengers", e -> viewPassengers()));
        passengerButtonPanel.add(createButton("Delete Passenger", e -> deletePassenger()));
        passengerButtonPanel.add(btnBack = createButton("Back", e -> cardLayout.show(cardPanel, "Menu")));
        passengerPanel.add(passengerInputPanel, BorderLayout.NORTH);
        passengerPanel.add(passengerButtonPanel, BorderLayout.SOUTH);
        cardPanel.add(passengerPanel, "Passenger");

        btnLogin.addActionListener(e -> {
            if (txtUsername.getText().equals("admin") && new String(txtPassword.getPassword()).equals("admin123")) {
                JOptionPane.showMessageDialog(null, "Logged in!");
                cardLayout.show(cardPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials!");
            }
        });

        add(cardPanel);
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        button.addActionListener(action);
        return button;
    }

    private void loadTrainData() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM train_schedule")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("train_no"), rs.getString("from_station"),
                        rs.getString("to_station"), rs.getString("time")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private void addTrain() {
        if (txtTrainNo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Train No cannot be empty!");
            return;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM train_schedule WHERE train_no = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO train_schedule (train_no, from_station, to_station, time) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, Integer.parseInt(txtTrainNo.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Train No already exists!");
                return;
            }
            insertStmt.setInt(1, Integer.parseInt(txtTrainNo.getText()));
            insertStmt.setString(2, txtFrom.getText());
            insertStmt.setString(3, txtTo.getText());
            insertStmt.setString(4, txtTime.getText());
            insertStmt.executeUpdate();
            loadTrainData();
            JOptionPane.showMessageDialog(null, "Train added!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void editTrain() {
        if (selectedRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE train_schedule SET from_station = ?, to_station = ?, time = ? WHERE train_no = ?")) {
                pstmt.setString(1, txtFrom.getText());
                pstmt.setString(2, txtTo.getText());
                pstmt.setString(3, txtTime.getText());
                pstmt.setInt(4, Integer.parseInt(txtTrainNo.getText()));
                pstmt.executeUpdate();
                loadTrainData();
                JOptionPane.showMessageDialog(null, "Train updated!");
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteTrain() {
        if (selectedRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM train_schedule WHERE train_no = ?")) {
                int trainNo = (int) tableModel.getValueAt(selectedRow, 0);
                pstmt.setInt(1, trainNo);
                pstmt.executeUpdate();
                loadTrainData();
                JOptionPane.showMessageDialog(null, "Train deleted!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a train to delete!");
        }
    }

    private void addStation() {
        if (txtStationId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Station ID cannot be empty!");
            return;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM station_info WHERE station_id = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO station_info (station_id, station_name, location) VALUES (?, ?, ?)")) {
            int stationId = Integer.parseInt(txtStationId.getText());
            pstmt.setInt(1, stationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Station ID already exists!");
                return;
            }
            insertStmt.setInt(1, stationId);
            insertStmt.setString(2, txtStationName.getText());
            insertStmt.setString(3, txtLocation.getText());
            insertStmt.executeUpdate();
            viewStations();
            JOptionPane.showMessageDialog(null, "Station added!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void viewStations() {
        stationTableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM station_info")) {
            while (rs.next()) {
                stationTableModel.addRow(new Object[]{rs.getInt("station_id"), rs.getString("station_name"),
                        rs.getString("location")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private void editStation() {
        if (selectedStationRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE station_info SET station_name = ?, location = ? WHERE station_id = ?")) {
                int stationId = (int) stationTableModel.getValueAt(selectedStationRow, 0);
                pstmt.setString(1, txtStationName.getText());
                pstmt.setString(2, txtLocation.getText());
                pstmt.setInt(3, stationId);
                pstmt.executeUpdate();
                viewStations();
                JOptionPane.showMessageDialog(null, "Station updated!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteStation() {
        if (selectedStationRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM station_info WHERE station_id = ?")) {
                int stationId = (int) stationTableModel.getValueAt(selectedStationRow, 0);
                pstmt.setInt(1, stationId);
                pstmt.executeUpdate();
                viewStations();
                JOptionPane.showMessageDialog(null, "Station deleted!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a station to delete!");
        }
    }

    private void bookTicket() {
        if (txtTicketId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ticket ID cannot be empty!");
            return;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM ticket_booking WHERE ticket_id = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO ticket_booking (ticket_id, train_no, passenger_name, booking_date) VALUES (?, ?, ?, ?)")) {
            int ticketId = Integer.parseInt(txtTicketId.getText());
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Ticket ID already exists!");
                return;
            }
            insertStmt.setInt(1, ticketId);
            insertStmt.setInt(2, Integer.parseInt(txtTicketTrainNo.getText()));
            insertStmt.setString(3, txtPassengerNameTicket.getText());
            insertStmt.setDate(4, java.sql.Date.valueOf(txtDate.getText()));
            insertStmt.executeUpdate();
            viewBookings();
            JOptionPane.showMessageDialog(null, "Ticket booked!");
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void viewBookings() {
        bookingTableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ticket_booking")) {
            while (rs.next()) {
                bookingTableModel.addRow(new Object[]{rs.getInt("ticket_id"), rs.getInt("train_no"),
                        rs.getString("passenger_name"), rs.getDate("booking_date")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private void deleteBooking() {
        if (selectedBookingRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ticket_booking WHERE ticket_id = ?")) {
                int ticketId = (int) bookingTableModel.getValueAt(selectedBookingRow, 0);
                pstmt.setInt(1, ticketId);
                pstmt.executeUpdate();
                viewBookings();
                JOptionPane.showMessageDialog(null, "Booking deleted!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a booking to delete!");
        }
    }

    private void addPassenger() {
        if (txtPassengerId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Passenger ID cannot be empty!");
            return;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM passenger WHERE passenger_id = ?");
             PreparedStatement insertStmt = conn.prepareStatement(
                     "INSERT INTO passenger (passenger_id, name) VALUES (?, ?)")) {
            int passengerId = Integer.parseInt(txtPassengerId.getText());
            pstmt.setInt(1, passengerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Passenger ID already exists!");
                return;
            }
            insertStmt.setInt(1, passengerId);
            insertStmt.setString(2, txtPassengerName.getText().isEmpty() ? "" : txtPassengerName.getText());
            insertStmt.executeUpdate();
            viewPassengers();
            JOptionPane.showMessageDialog(null, "Passenger added!");
            txtPassengerId.setText("");
            txtPassengerName.setText("");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void viewPassengers() {
        passengerTableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM passenger")) {
            while (rs.next()) {
                passengerTableModel.addRow(new Object[]{rs.getInt("passenger_id"), rs.getString("name")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private void deletePassenger() {
        if (selectedPassengerRow != -1) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/orange_line_db", "root", "khan1223");
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM passenger WHERE passenger_id = ?")) {
                int passengerId = (int) passengerTableModel.getValueAt(selectedPassengerRow, 0);
                pstmt.setInt(1, passengerId);
                pstmt.executeUpdate();
                viewPassengers();
                JOptionPane.showMessageDialog(null, "Passenger deleted!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a passenger to delete!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TrainManagementSystem::new);
    }
}