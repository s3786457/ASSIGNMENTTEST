package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import main.SQLConnection;
import main.model.BookingHistoryModel;
import main.model.CheckBookingModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class BookingHistory implements Initializable
{
    int index = -1;
    static String id,date,time;
    ObservableList<BookingHistoryModel> oblist;

    private static Home Home = new Home();
    private CheckBookingModel checkBookingModel = new CheckBookingModel();
    @FXML
    private TableView<BookingHistoryModel> table;
    @FXML
    private TableColumn<BookingHistoryModel, Integer> col_id;
    @FXML
    private TableColumn<BookingHistoryModel, String> col_date;
    @FXML
    private TableColumn<BookingHistoryModel, String> col_time;
    @FXML
    private TableColumn<BookingHistoryModel, String> col_seat;
    @FXML
    private TableColumn<BookingHistoryModel, String> col_approved;
    @FXML
    private Label successMessage;
    @FXML
    private Label failMessage;

    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            autoReject();
            UpdateTable();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ObservableList<BookingHistoryModel> getDatausers() throws SQLException
    {

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;;

        ObservableList<BookingHistoryModel> list = FXCollections.observableArrayList();
        try
        {
            preparedStatement = connectionDB.prepareStatement("SELECT * FROM Booking WHERE username = '" + Home.al_username + "';");
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                list.add(new BookingHistoryModel(rs.getInt("id"), rs.getString("date"),
                        rs.getString("time"), rs.getString("seat"), rs.getString("approved")));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            preparedStatement.close();
            rs.close();
        }
        return list;
    }

    public void UpdateTable() throws SQLException
    {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_seat.setCellValueFactory(new PropertyValueFactory<>("seat"));
        col_approved.setCellValueFactory(new PropertyValueFactory<>("approved"));

        oblist = getDatausers();
        table.setItems(oblist);
    }

    @FXML
    void getSelected (MouseEvent event)
    {
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1)
        {
            return;
        }
        id = col_id.getCellData(index).toString();
        date = col_date.getCellData(index);
        time = col_time.getCellData(index);
    }


    public void deleteBooking() throws SQLException
    {
        boolean check = false;
        if(!checkBookingModel.checkDateTime(date,time))
        {
            SQLConnection sqlConnection = new SQLConnection();
            Connection connectionDB = sqlConnection.connect();
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;

            try
            {
                String sql = "SELECT * FROM Booking WHERE id = ?";
                preparedStatement = connectionDB.prepareStatement(sql);
                preparedStatement.setString(1, id);
                rs = preparedStatement.executeQuery();
                if (rs.next())
                {
                    String r_approved = rs.getString("approved");
                    if (r_approved.equals("no"))
                        check = false;
                    else
                    {
                        check = true;
                        String query = "DELETE FROM Booking WHERE id = ?";
                        preparedStatement = connectionDB.prepareStatement(query);
                        preparedStatement.setString(1, id);
                        preparedStatement.execute();
                    }
                }
                UpdateTable();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                e.getCause();
            }
            finally
            {
                preparedStatement.close();
                rs.close();
                connectionDB.close();
            }
            if (check)
            {
                failMessage.setText("");
                successMessage.setText("Booking cancelled!");
            }
            else
                successMessage.setText("");
            failMessage.setText("Unable to cancel.");
        }
        else
        {
            successMessage.setText("");
            failMessage.setText("Booking Already Completed.");
        }
    }


    public void autoReject() throws SQLException
    {

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;;

        try
        {
            preparedStatement = connectionDB.prepareStatement("SELECT * FROM Booking;");
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                String r_approved = rs.getString("approved").toLowerCase();
                if (r_approved.equals("pending"))
                {
                    int r_id = rs.getInt("id");
                    LocalDate r_date = LocalDate.parse(rs.getString("date"));
                    LocalTime r_time = LocalTime.parse(rs.getString("time"));

                    LocalDate localDate = LocalDate.now();
                    LocalTime localTime = LocalTime.now();

                    int dateDiff = r_date.compareTo(localDate);
                    if (dateDiff == 0)
                    {
                        int hourBeforeCancel = 1;
                        long hoursBetween = ChronoUnit.HOURS.between(localTime, r_time);

                        if (hoursBetween < hourBeforeCancel)
                        {
                            String sql = "UPDATE Booking SET approved = '" + "no(auto cancel)" + "'WHERE id = '" + r_id + "';";
                            preparedStatement = connectionDB.prepareStatement(sql);
                            preparedStatement.execute();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
        finally
        {
            connectionDB.close();
            preparedStatement.close();
            rs.close();
        }
    }

    public void Cancel(ActionEvent event) throws IOException, SQLException
    {
        deleteBooking();
    }
}