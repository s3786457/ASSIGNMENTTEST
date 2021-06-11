package main.model;

import main.SQLConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class CheckBookingModel
{
    public boolean bookingExist(String user, String date, String time) throws SQLException
    {

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select * from booking where username = ?";
        try
        {
            preparedStatement = connectionDB.prepareStatement(query);
            preparedStatement.setString(1, user);

            resultSet = preparedStatement.executeQuery();

            boolean checking = false;
            boolean hasUsed = false;

            while (resultSet.next())
            {
                hasUsed = true;
                String data_date = resultSet.getString("date");
                String data_time = resultSet.getString("time");

                if (!checkDateTime(data_date, data_time))
                {
                    String checkApproved = resultSet.getString("approved");
                    if (checkApproved.equals("pending") || checkApproved.equals("yes"))
                    {
                        checking = true;
                        break;
                    }
                }
            }

            if (!hasUsed)
            {
                if(checkDateTime(date,time))
                    return false;
                else
                    return true;
            }
            else
            {
                if (checking == true)
                {
                    return false;
                }
                else
                {
                    if (checkDateTime(date, time))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
            }
        }

        catch (Exception e)
        {
            return false;
        }
        finally
        {
            preparedStatement.close();
            resultSet.close();
        }
    }

    public boolean checkDateTime(String date, String time)
    {
        LocalDate date_temp = LocalDate.parse(date);
        LocalTime time_temp = LocalTime.parse(time);
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        int timeDiff = time_temp.compareTo(localTime);
        int dateDiff = date_temp.compareTo(localDate);

        if(dateDiff < 0)
            return true;
        else if ((dateDiff == 0) && (timeDiff <= 0))
            return true;
        else
            return false;
    }
}