package main.model;

import main.SQLConnection;

import java.sql.*;
import java.util.Random;

public class PasswordModel {
    Connection connection;
    public ResetModel resetModel = new ResetModel();

    public PasswordModel() {
        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);
    }

    public Boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public String passwordGenerate(int max, int min)
    {
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        int passLength = (int) ((Math.random() * (max - min)) + min);

        for(int i = 0; i < passLength; i++)
        {
            int index = random.nextInt(alphaNumeric.length());
            char randomChar = alphaNumeric.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public void addPassword(String newPass)
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        String insertFields = "UPDATE Employee SET password = '" ;
        String insertValues =  newPass + "' WHERE username = '" + resetModel.getAccount() + "'";
        String query = insertFields + insertValues;

        try
        {
            Statement statement = connectionDB.createStatement();
            statement.executeUpdate(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}