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

import main.model.SignUpModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUp implements Initializable
{
    public SignUpModel signupModel = new SignUpModel();
    @FXML
    private Label isConnected;
    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtRole;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtSecret;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Label successMessage;
    @FXML
    private Label failMessage;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (signupModel.isDbConnected())
        {
            isConnected.setText("Connected");
        }
        else
        {
            isConnected.setText("Not Connected");
        }
    }

    public void Signup(ActionEvent event) throws SQLException {
        registerUser();
    }

    public void registerUser() throws SQLException
    {
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String role = txtRole.getText().toLowerCase();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String secret = txtSecret.getText();
        String answer = txtAnswer.getText();

        if (firstname.trim().isEmpty() || lastname.trim().isEmpty() || role.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || secret.trim().isEmpty() || answer.trim().isEmpty())
        {
            successMessage.setText(null);
            failMessage.setText("Please provide all information!");
        }

        else
        {
            if (signupModel.accountExist(username))
            {
                if (role.equals("admin") || role.equals("staff"))
                {
                    signupModel.addDatabase(firstname, lastname, role, username, password, secret, answer);

                    failMessage.setText(null);
                    successMessage.setText("User has been registered!");
                }
                else
                {
                    successMessage.setText(null);
                    failMessage.setText("Role can only be 'Admin' or 'Staff'!");
                }
            }
            else
            {
                successMessage.setText(null);
                failMessage.setText("Account Already Exists.");
            }
        }
    }

    public void Login(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}