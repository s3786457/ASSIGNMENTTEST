package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.SQLConnection;
import main.model.BookingManagementModel;
import main.model.CheckBookingModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookingManagement implements Initializable
{
    private BookingHistory BookingHistory = new BookingHistory();
    private CheckBookingModel checkBookingModel = new CheckBookingModel();
    @FXML
    private TableView<BookingManagementModel> table;
    @FXML
    private TableColumn<BookingManagementModel, Integer> col_id;
    @FXML
    private TableColumn<BookingManagementModel, String> col_username;
    @FXML
    private TableColumn<BookingManagementModel, String> col_date;
    @FXML
    private TableColumn<BookingManagementModel, String> col_time;
    @FXML
    private TableColumn<BookingManagementModel, String> col_seat;
    @FXML
    private TableColumn<BookingManagementModel, String> col_approved;
    @FXML
    private Label txtID;
    @FXML
    private Label txtUsername;
    @FXML
    private Label txtDate;
    @FXML
    private Label txtTime;
    @FXML
    private Label txtSeat;
    @FXML
    private Label txtApproved;
    @FXML
    private Label failMessage;
    @FXML
    private Label successMessage;

    ObservableList<BookingManagementModel> listM;
    int index = -1;

    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            BookingHistory.autoReject();
            UpdateTable();
            failMessage.setText("Please select an option.");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static ObservableList<BookingManagementModel> getDatabookings() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        ObservableList<BookingManagementModel> list = FXCollections.observableArrayList();
        try
        {
            preparedStatement = connectionDB.prepareStatement("SELECT * FROM Booking");
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                list.add(new BookingManagementModel(Integer.parseInt(rs.getString("id")), rs.getString("username"),
                        rs.getString("date"), rs.getString("time"), rs.getString("seat"),rs.getString("approved")));
            }
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
        }
        return list;
    }

    public void UpdateTable() throws SQLException
    {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_seat.setCellValueFactory(new PropertyValueFactory<>("seat"));
        col_approved.setCellValueFactory(new PropertyValueFactory<>("approved"));

        listM = getDatabookings();
        table.setItems(listM);
    }

    @FXML
    void getSelected(MouseEvent event)
    {
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1)
        {
            return;
        }
        txtID.setText(col_id.getCellData(index).toString());
        txtUsername.setText(col_username.getCellData(index));
        txtDate.setText(col_date.getCellData(index));
        txtTime.setText(col_time.getCellData(index));
        txtSeat.setText(col_seat.getCellData(index));
        txtApproved.setText(col_approved.getCellData(index));
    }

    public void Cancel() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;

        String id = txtID.getText();
        String value = txtApproved.getText();

        if (checkBookingModel.checkDateTime(txtDate.getText(),txtTime.getText()))
        {
            failMessage.setText("This booking has already been completed.");
            successMessage.setText("");
        }
        else
        {
            if (value.equals("no"))
            {
                failMessage.setText("This booking has already been cancelled.");
                successMessage.setText("");
            }
            else
            {
                String sql = "UPDATE Booking SET approved ='" + "no" + "'WHERE id = '" + id + "';";
                try
                {
                    preparedStatement = connectionDB.prepareStatement(sql);
                    preparedStatement.execute();

                    failMessage.setText(null);
                    successMessage.setText("Booking has been cancelled.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    e.getCause();
                }
                finally
                {
                    preparedStatement.close();
                }
                UpdateTable();
            }
        }
    }

    public void Approve() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;

        String id = txtID.getText();
        String value = txtApproved.getText();


        if (checkBookingModel.checkDateTime(txtDate.getText(),txtTime.getText()))
        {
            failMessage.setText("This booking has already been completed.");
            successMessage.setText("");
        }
        else
        {
            if (value.equals("yes"))
            {
                failMessage.setText("This booking has already been approved.");
                successMessage.setText("");
            }
            else
            {
                String sql = "UPDATE Booking SET approved ='" + "yes" + "'WHERE id = '" + id + "';";
                try
                {
                    preparedStatement = connectionDB.prepareStatement(sql);
                    preparedStatement.execute();

                    failMessage.setText(null);
                    successMessage.setText("Booking Approved!");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    e.getCause();
                }
                finally
                {
                    preparedStatement.close();
                }
                UpdateTable();
            }
        }
    }

    public void export() throws SQLException, FileNotFoundException
    {
        PrintWriter pw= new PrintWriter(new File("./export/Booking.csv"));
        StringBuilder sb=new StringBuilder();

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Booking";

        try
        {
            preparedStatement = connectionDB.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                sb.append(rs.getString("id"));
                sb.append(",");
                sb.append(rs.getString("username"));
                sb.append(",");
                sb.append(rs.getString("date"));
                sb.append(",");
                sb.append(rs.getString("time"));
                sb.append(",");
                sb.append(rs.getString("seat"));
                sb.append(",");
                sb.append(rs.getString("approved"));
                sb.append("\r\n");
            }
            pw.write(sb.toString());
            pw.close();
        }
        catch (Exception e)
        {
            e.getCause();
            e.printStackTrace();
        }
        finally
        {
            preparedStatement.close();
            rs.close();
            connectionDB.close();
        }
    }

    public void Back(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Export(ActionEvent event) throws IOException, SQLException
    {
        export();
        Parent anotherRoot = FXMLLoader.load(getClass().getResource("../ui/export.fxml"));
        Stage anotherStage = new Stage();
        Scene anotherScene = new Scene(anotherRoot);
        anotherStage.setScene(anotherScene);
        anotherStage.show();
    }
}