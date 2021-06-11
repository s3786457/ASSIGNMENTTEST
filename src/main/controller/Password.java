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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import main.model.PasswordModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Password implements Initializable
{
    public PasswordModel passwordModel = new PasswordModel();
    @FXML
    private Label isConnected;
    @FXML
    private Label txtNewPass;
    @FXML
    private Button copyButton;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (passwordModel.isDbConnected())
        {
            isConnected.setText("Connected");
        }
        else
        {
            isConnected.setText("Not Connected");
        }
        setPassword();
    }

    public void setPassword()
    {
        String newPass = passwordModel.passwordGenerate(16, 4);
        txtNewPass.setText(newPass);
        passwordModel.addPassword(newPass);
    }

    public void loginPage(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Copy(ActionEvent event) throws IOException
    {
        copyButton.setText("Copied");
        String copyText = txtNewPass.getText();

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(copyText);
        clipboard.setContent(content);
    }
}