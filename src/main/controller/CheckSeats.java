package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.SQLConnection;
import main.model.CheckSeatsModel;

public class CheckSeats implements Initializable
{
    private CheckBooking bcc = new CheckBooking();
    private CheckSeatsModel checkSeatsModel = new CheckSeatsModel();

    private int inc = 0;
    private String seat = "";
    private String lastClickedIndex = "";
    @FXML
    private Label txtSeat;
    @FXML
    private Label failMessage;
    @FXML
    private Button A1, A2, A3, A4, B1, B2, B3, B4, C1, C2,C3, C4, D1, D2, D3, D4;

    private String [] seatNameArr = new String[16];
    private Boolean[] bookedArr = new Boolean[16];
    private Boolean[] lockedArr = new Boolean[16];
    ArrayList<Button> buttons = new ArrayList<Button>();

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.populateJButtonList();
        buttonArray();
        try
        {
            seatCalling();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void buttonArray()
    {
        Button[] button = new Button[100];

        for (int i = 0; i < 100; i++)
        {
            for (Button btn : buttons)
            {
                button[i] = btn;
            }
        }
    }

    public void populateJButtonList()
    {
        Field[] fields = CheckSeats.class.getDeclaredFields();

        for (Field field : fields)
        {
            if (field.getType().equals(Button.class))
            {
                try
                {
                    buttons.add((Button) field.get(this));
                }
                catch (IllegalArgumentException | IllegalAccessException | SecurityException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void seatCalling() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        PreparedStatement preparedStatement = connectionDB.prepareStatement("select * from seat");
        ResultSet rs = preparedStatement.executeQuery();

        try
        {
            while (rs.next())
            {
                seatNameArr[inc] = rs.getString("seatname");
                bookedArr[inc] = rs.getBoolean("booked");
                lockedArr[inc] = rs.getBoolean("locked");
                if (lockedArr[inc])
                    buttons.get(inc).setStyle("-fx-background-color: #ef8d22");
                else if (bookedArr[inc])
                    buttons.get(inc).setStyle("-fx-background-color: #c92d39");
                inc++;
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
            rs.close();
        }
    }

    public void handleButtonAction(ActionEvent e)
    {
        if (e.getSource() == A1)
        {
            lastClickedIndex = "A1";
        }
        else if (e.getSource() == A2)
        {
            lastClickedIndex = "A2";
        }
        else if (e.getSource() == A3)
        {
            lastClickedIndex = "A3";
        }
        else if (e.getSource() == A4)
        {
            lastClickedIndex = "A4";
        }
        else if (e.getSource() == B1)
        {
            lastClickedIndex = "B1";
        }
        else if (e.getSource() == B2)
        {
            lastClickedIndex = "B2";
        }
        else if (e.getSource() == B3)
        {
            lastClickedIndex = "B3";
        }
        else if (e.getSource() == B4)
        {
            lastClickedIndex = "B4";
        }
        else if (e.getSource() == C1)
        {
            lastClickedIndex = "C1";
        }
        else if (e.getSource() == C2)
        {
            lastClickedIndex = "C2";
        }
        else if (e.getSource() == C3)
        {
            lastClickedIndex = "C3";
        }
        else if (e.getSource() == C4)
        {
            lastClickedIndex = "C4";
        }
        else if (e.getSource() == D1)
        {
            lastClickedIndex = "D1";
        }
        else if (e.getSource() == D2)
        {
            lastClickedIndex = "D2";
        }
        else if (e.getSource() == D3)
        {
            lastClickedIndex = "D3";
        }
        else if (e.getSource() == D4)
        {
            lastClickedIndex = "D4";
        }

        txtSeat.setText(lastClickedIndex);
        seat = txtSeat.getText();
    }

    public boolean checkAvailability(String seat)
    {
        boolean avai = true;
        for (int i = 0; i < seatNameArr.length; i++)
            if (Objects.equals(seatNameArr[i], seat)) {
                if (lockedArr[i]) {
                    avai = false;
                    break;
                } else if (bookedArr[i]) {
                    avai = false;
                    break;
                }
            }
        if (avai) return true;
        else return false;
    }

    @FXML
    public void Confirm(ActionEvent event) throws IOException, SQLException
    {
        if (seat.equals(""))
        {
            failMessage.setText("Please choose a seat.");
        }
        else if (!checkAvailability(seat)) {
            failMessage.setText("This seat is not available. Please choose another seat.");
        } else {
            checkSeatsModel.addBooking(bcc.bd_username, bcc.bd_date, bcc.bd_time, seat);
            checkSeatsModel.updateSeat();
            Parent root = FXMLLoader.load(getClass().getResource("../ui/home.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}