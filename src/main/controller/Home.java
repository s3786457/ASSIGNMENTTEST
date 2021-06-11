package main.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import main.FxmlLoader;
import main.model.HomeModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable
{
    static String al_username;
    public LoginController loginController = new LoginController();
    public HomeModel homeModel = new HomeModel();
    @javafx.fxml.FXML
    private Label isConnected;
    @javafx.fxml.FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if (homeModel.isDbConnected())
        {
            al_username = loginController.username;
            isConnected.setText("Welcome, " + al_username);
        }

        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("homescreen");
        mainPane.setCenter(view);
    }

    @javafx.fxml.FXML
    private void loadBooking(ActionEvent event) throws IOException
    {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("bookingscreen");
        mainPane.setCenter(view);
    }

    @javafx.fxml.FXML
    private void loadHome(ActionEvent event) throws IOException
    {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("homescreen");
        mainPane.setCenter(view);
    }

    @javafx.fxml.FXML
    private void loadHistory(ActionEvent event) throws IOException
    {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("historyscreen");
        mainPane.setCenter(view);
    }

    @javafx.fxml.FXML
    private void loadAccountDetail(ActionEvent event) throws IOException
    {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("accountdetail");
        mainPane.setCenter(view);
    }


    @javafx.fxml.FXML
    public void Logout(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}