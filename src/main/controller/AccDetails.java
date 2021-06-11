package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.SQLConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccDetails implements Initializable
{
    private static Home Home = new Home();
    @FXML
    private Label txtUsername;
    @FXML
    private Label txtFirstname;
    @FXML
    private Label txtLastname;
    @FXML
    private Label txtRole;
    @FXML
    private Label failMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            getDetail();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void getDetail() throws SQLException
    {

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try
        {
            preparedStatement = connectionDB.prepareStatement("SELECT * FROM Employee WHERE username = '" + Home.al_username + "';");
            rs = preparedStatement.executeQuery();

            if (rs.next())
            {
                txtUsername.setText(rs.getString("username"));
                txtFirstname.setText(rs.getString("name"));
                txtLastname.setText(rs.getString("surname"));
                txtRole.setText(rs.getString("role"));
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
    }

    public boolean isAdmin()
    {
        if ("admin".equals(txtRole.getText())) {
            failMessage.setText("");
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void ManageAccount(ActionEvent event) throws IOException
    {
        if (isAdmin())
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../ui/manageaccount.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        else
        {
            failMessage.setText("Only Admin can access this file");
        }
    }

    @FXML
    public void ManageBooking(ActionEvent event) throws IOException
    {
        if (isAdmin()) {
            Parent root = FXMLLoader.load(getClass().getResource("../ui/managebooking.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            failMessage.setText("Only Admin can access this file");
        }
    }
}