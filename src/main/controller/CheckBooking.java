package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.model.CheckBookingModel;

import java.io.IOException;
import java.sql.SQLException;

public class CheckBooking
{
    public static String bd_username, bd_date, bd_time;
    public LoginController LoginController = new LoginController();
    public CheckBookingModel checkBookingModel = new CheckBookingModel();
    @FXML
    private ChoiceBox<String> cbhour;
    @FXML
    private ChoiceBox<String> cbminute;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Label failMessage;
    @FXML
    private Label failMessage2;
    @FXML
    private Hyperlink clickHere;

    public CheckBooking() {
    }

    public void initialize()
    {

        cbhour.getItems().addAll("08", "09", "10", "11", "12", "13", "14", "15", "16", "17");
        cbminute.getItems().addAll("00", "15", "30", "45");
    }


    public boolean getInput() throws SQLException
    {
        try
        {
            String username = LoginController.username;
            String date = dpDate.getValue().toString();
            String time = cbhour.getValue().toString() + ":" + cbminute.getValue().toString();

            if (checkBookingModel.bookingExist(username, date, time))
            {
                return true;
            }
            else
            {
                failMessage.setText("Error,");
                clickHere.setText("Please click here ");
                failMessage2.setText("to find out more.");
                return false;
            }
        }
        catch (Exception e)
        {
            failMessage.setText("Please provide all info!");
            return false;
        }
    }

    public void storeData()
    {
        bd_username = LoginController.username;
        bd_date = dpDate.getValue().toString();
        bd_time = cbhour.getValue().toString() + ":" + cbminute.getValue().toString();
    }

    @FXML
    public void confirmBooking(ActionEvent event) throws IOException, SQLException
    {
        if (getInput()) {
            storeData();
            Parent root = FXMLLoader.load(getClass().getResource("../ui/seatbooking.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            return;
        }
    }

    @FXML
    public void clickHereHyperlink(ActionEvent event) throws IOException
    {
        Parent anotherRoot = FXMLLoader.load(getClass().getResource("../ui/errorbooking.fxml"));
        Stage anotherStage = new Stage();
        Scene anotherScene = new Scene(anotherRoot);
        anotherStage.setScene(anotherScene);
        anotherStage.show();
        anotherStage.setTitle("Learn more");
    }
}