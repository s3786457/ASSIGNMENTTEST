package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import main.model.ResetModel;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Reset implements Initializable
{
    public ResetModel resetModel = new ResetModel();
    @FXML
    private Label isConnected;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtSecret;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Label failMessage;
    private Stage stage;
    private Boolean check = false;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (resetModel.isDbConnected())
        {
            isConnected.setText("Connected");
        }
        else
        {
            isConnected.setText("Not Connected");
        }
    }

    public void Confirm() throws SQLException
    {
        try
        {
            if (resetModel.isConfirm(txtUsername.getText(),txtSecret.getText(),txtAnswer.getText()))
            {
                check = true;
            }
            else
            {
                failMessage.setText("Invalid. Please try again.");
                check = false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void loginPage(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

        public void resetPage(ActionEvent event) throws IOException, SQLException {
        Confirm();
        if (check == true)
        {
            Parent root = FXMLLoader.load(getClass().getResource("../ui/resetpass.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}