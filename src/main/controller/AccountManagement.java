package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.SQLConnection;
import main.model.AccountManagementModel;
import main.model.SignUpModel;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountManagement implements Initializable {
    private SignUpModel signupModel = new SignUpModel();
    @FXML
    private TableView<AccountManagementModel> table;
    @FXML
    private TableColumn<AccountManagementModel, Integer> col_id;
    @FXML
    private TableColumn<AccountManagementModel, String> col_firstname;
    @FXML
    private TableColumn<AccountManagementModel, String> col_lastname;
    @FXML
    private TableColumn<AccountManagementModel, String> col_role;
    @FXML
    private TableColumn<AccountManagementModel, String> col_username;
    @FXML
    private TableColumn<AccountManagementModel, String> col_password;
    @FXML
    private TableColumn<AccountManagementModel, String> col_question;
    @FXML
    private TableColumn<AccountManagementModel, String> col_answer;
    @FXML
    private Label txtID;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;
    @FXML
    private TextField txtRole;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtQuestion;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Label failMessage;
    @FXML
    private Label successMessage;

    ObservableList<AccountManagementModel> listM;
    int index = -1;

    public void initialize(URL location, ResourceBundle resources)
    {
        try
        {
            UpdateTable();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public static ObservableList<AccountManagementModel> getDatausers() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        ObservableList<AccountManagementModel> list = FXCollections.observableArrayList();
        try
        {
            preparedStatement = connectionDB.prepareStatement("SELECT * FROM Employee");
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                list.add(new AccountManagementModel(Integer.parseInt(rs.getString("id")), rs.getString("name"),
                        rs.getString("surname"), rs.getString("role"), rs.getString("username"),
                        rs.getString("password"), rs.getString("secret_qs"), rs.getString("answer")));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
        finally
        {
            preparedStatement.close();
            rs.close();
        }
        return list;
    }

    public void UpdateTable() throws SQLException
    {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_question.setCellValueFactory(new PropertyValueFactory<>("question"));
        col_answer.setCellValueFactory(new PropertyValueFactory<>("answer"));

        listM = getDatausers();
        table.setItems(listM);
    }

    @FXML
    void getSelected(MouseEvent event)
    {
        index = table.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtID.setText(col_id.getCellData(index).toString());
        txtFirstname.setText(col_firstname.getCellData(index));
        txtLastname.setText(col_lastname.getCellData(index));
        txtRole.setText(col_role.getCellData(index));
        txtUsername.setText(col_username.getCellData(index));
        txtPassword.setText(col_password.getCellData(index));
        txtQuestion.setText(col_question.getCellData(index));
        txtAnswer.setText(col_answer.getCellData(index));
    }

    public void Delete() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        String sql = "DELETE FROM Employee WHERE username = ?";
        try
        {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(sql);
            preparedStatement.setString(1, txtUsername.getText());
            preparedStatement.execute();

            failMessage.setText(null);
            successMessage.setText("User deleted!");

            UpdateTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void Add() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;

        String value1 = txtFirstname.getText();
        String value2 = txtLastname.getText();
        String value3 = txtRole.getText().toLowerCase();
        String value4 = txtUsername.getText();
        String value5 = txtPassword.getText();
        String value6 = txtQuestion.getText();
        String value7 = txtAnswer.getText();

        if (value1.trim().isEmpty() || value2.trim().isEmpty() || value3.trim().isEmpty() || value4.trim().isEmpty() || value5.trim().isEmpty() || value6.trim().isEmpty() || value7.trim().isEmpty())
        {
            successMessage.setText(null);
            failMessage.setText("Provide all info!");
        }
        else
        {
            if (value3.equals("admin") || value3.equals("staff"))
            {
                if (signupModel.accountExist(value4))
                {
                    String sql = "INSERT INTO Employee (name, surname, role, username, password, secret_qs, answer) VALUES (?,?,?,?,?,?,?) ";
                    try
                    {
                        preparedStatement = connectionDB.prepareStatement(sql);
                        preparedStatement.setString(1, value1);
                        preparedStatement.setString(2, value2);
                        preparedStatement.setString(3, value3);
                        preparedStatement.setString(4, value4);
                        preparedStatement.setString(5, value5);
                        preparedStatement.setString(6, value6);
                        preparedStatement.setString(7, value7);
                        preparedStatement.execute();

                        failMessage.setText(null);
                        successMessage.setText("User has been added!");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        e.getCause();
                    }
                    finally
                    {
                        preparedStatement.close();
                    }
                }
                else
                {
                    successMessage.setText(null);
                    failMessage.setText("Account already exists.");
                }
            }
            else
            {
                successMessage.setText(null);
                failMessage.setText("Please choose 'Admin' or 'Staff' for Role.");
            }
        }
        UpdateTable();
    }

    public void Update() throws SQLException
    {
        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();

        try
        {
            String id = txtID.getText();
            String value1 = txtFirstname.getText();
            String value2 = txtLastname.getText();
            String value3 = txtRole.getText().toLowerCase();
            String value4 = txtUsername.getText();
            String value5 = txtPassword.getText();
            String value6 = txtQuestion.getText();
            String value7 = txtAnswer.getText();

            if (value3.equals("Admin") || value3.equals("Staff"))
            {
                {
                    String sql = "UPDATE Employee SET name = '" + value1 + "', surname = '" + value2 + "', role = '" +
                            value3 + "', username = '" + value4 + "', password = '" + value5 + "', secret_qs = '" +
                            value6 + "', answer = '" + value7 + "'WHERE id = '" + id + "';";
                    PreparedStatement preparedStatement = connectionDB.prepareStatement(sql);
                    preparedStatement.execute();

                    UpdateTable();
                    failMessage.setText(null);
                    successMessage.setText("User info has been updated!");
                }
            }
            else
            {
                successMessage.setText(null);
                failMessage.setText("Please choose 'Admin' or 'Staff' for Role.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void export() throws SQLException, FileNotFoundException
    {
        PrintWriter pw= new PrintWriter(new File("./export/Employee.csv"));
        StringBuilder sb=new StringBuilder();

        SQLConnection sqlConnection = new SQLConnection();
        Connection connectionDB = sqlConnection.connect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Employee";

        try
        {
            preparedStatement = connectionDB.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                sb.append(rs.getString("id"));
                sb.append(",");
                sb.append(rs.getString("firstname"));
                sb.append(",");
                sb.append(rs.getString("lastname"));
                sb.append(",");
                sb.append(rs.getString("role"));
                sb.append(",");
                sb.append(rs.getString("username"));
                sb.append(",");
                sb.append(rs.getString("password"));
                sb.append(",");
                sb.append(rs.getString("secret_qs"));
                sb.append(",");
                sb.append(rs.getString("answer"));
                sb.append("\r\n");
            }
            pw.write(sb.toString());
            pw.close();
        }
        catch (Exception e)
        {
            e.getCause();
            e.printStackTrace();
        }
        finally
        {
            preparedStatement.close();
            rs.close();
            connectionDB.close();
        }
    }

    public void Back(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("../ui/home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Export(ActionEvent event) throws IOException, SQLException
    {
        export();
        Parent anotherRoot = FXMLLoader.load(getClass().getResource("../ui/export.fxml"));
        Stage anotherStage = new Stage();
        Scene anotherScene = new Scene(anotherRoot);
        anotherStage.setScene(anotherScene);
        anotherStage.show();
    }
}