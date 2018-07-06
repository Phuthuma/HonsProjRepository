package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class newTaskCont implements Initializable {
    private Connection con;
    private Label lblModCode=new Label();

    @FXML private JFXTextField txtCode;
    @FXML private JFXTextField txtTitle;
    @FXML private Spinner<Integer>spnNo;
    @FXML private JFXButton btnSave;
    @FXML private JFXButton btnClose;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(lblModCode.getText());
        setValidations();
        btnSave.setOnAction(event -> {
            connect();
            String sql="select TaskCode from Task where TaskCode = ?";
            try {
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.setString(1, txtCode.getText());
                ResultSet result=stmt.executeQuery();
                if(result.next()){
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(result.getString("TaskCode")+" Already exists, enter a different task code!");
                    alert.showAndWait();
                    disconnect();
                }
                else {
                    connect();
                    String sqlQuery="insert into Task(TaskCode,TaskNo,TaskTitle,ModCode) values (?,?,?,?)";
                    PreparedStatement stmtInsert=con.prepareStatement(sqlQuery);
                    stmtInsert.setString(1,txtCode.getText());
                    stmtInsert.setInt(2,spnNo.getValue());
                    stmtInsert.setString(3,txtTitle.getText());
                    stmtInsert.setString(4,lblModCode.getText());
                    stmtInsert.executeUpdate();


                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Status");
                    alert.setHeaderText(txtCode.getText()+ " has been inserted into database");
                    alert.showAndWait();
                    disconnect();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        btnClose.setOnAction(event -> {
            Stage stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    public void setModCode(String modCode){
        lblModCode.setText(modCode);
    }
    private void setValidations(){
        RequiredFieldValidator val=new RequiredFieldValidator();
        val.setMessage("Required Field");
        txtCode.getValidators().add(val);
        txtCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue){
                txtCode.validate();
            }
        });

        txtTitle.getValidators().add(val);
        txtTitle.getValidators().add(val);
        txtTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue)
                txtTitle.validate();
        });
    }

    private void connect(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(true){
            String connectionString="jdbc:sqlserver://postgrad.nmmu.ac.za;database=SolAssist";

            try {
                con=DriverManager.getConnection(connectionString,"solassistuser","Dfjf8d02fdjjJ");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void disconnect(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
