package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import main.model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    public LoginModel loginModel = new LoginModel();
    public static String username = "";
    @FXML
    private Label isConnected;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Stage stage;
    @FXML
    private Button loginButton;
    @FXML
    private Label failMessage;

    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (loginModel.isDbConnected())
        {
            isConnected.setText("Connected");
        }
        else
        {
            isConnected.setText("Not Connected");
        }
    }
    /* login Action method
       check if user input is the same as database.
     */
    public void Login(ActionEvent event)
    {
        try
        {
            username = txtUsername.getText();
            if (loginModel.isLogin(username,txtPassword.getText()))
            {
                loginSuccess();
            }
            else
            {
                failMessage.setText("Invalid. Please try again.");
            }
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loginSuccess() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/home.fxml"));
        stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void signUp(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/signup.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void reset(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/confirm_rs_pass.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}