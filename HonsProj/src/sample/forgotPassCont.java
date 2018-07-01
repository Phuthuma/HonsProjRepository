package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgotPassCont implements Initializable {
    //variables
    Connection con=null;

    @FXML private JFXTextField txtInput;
    @FXML private JFXButton btnSend;

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSend.setOnAction(event -> {
            if(validEmail()){


            }else{

            }
        });

    }

    private boolean validEmail(){
        Pattern p=Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m=p.matcher(txtInput.getText());
        if (m.find() && m.group().equals(txtInput.getText()))
            return true;
        return false;
    }

    private void connect(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(true){
            String connectionString="";

            try {
                con=DriverManager.getConnection(connectionString);

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
