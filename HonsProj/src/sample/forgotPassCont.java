package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class forgotPassCont implements Initializable {
    //variables
    Connection con=null;

    @FXML private JFXTextField txtInput;
    @FXML private JFXButton btnSend;

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSend.setOnAction(event -> {

           connect();
           String sql="select StudMail, Password " +
                      "from Student " +
                      "where StudNo = ? " +
                      "or StudMail = ?";
            try {
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.setString(1,txtInput.getText());
                stmt.setString(2,txtInput.getText());
                ResultSet result=stmt.executeQuery();

                if(result.next()){
                    String StudMail=result.getString("StudMail");
                    String pass=result.getString("Password");

                    HtmlEmail email=new HtmlEmail();

                    email.setHostName("smtp.gmail.com");
                    email.setSmtpPort(587);
                    email.setStartTLSEnabled(true);
                    email.setAuthentication("phuthumaloyisopetse@gmail.com","sweleba88");

                    try {
                        email.setFrom("phuthumaloyisopetse@gmail.com");
                        email.addTo(StudMail);
                        email.setSubject("SolAssist: Password Recovery");
                        email.setHtmlMsg("Hi user your password is: "+pass);
                        email.send();
                    } catch (EmailException e) {
                        e.printStackTrace();
                    }
                }else {
                    sql="select Email, Password " +
                            "from Lecturer " +
                            "where LectCode = ? " +
                            "or Email = ?";
                    stmt=con.prepareStatement(sql);
                    stmt.setString(1,txtInput.getText());
                    stmt.setString(2,txtInput.getText());
                    result=stmt.executeQuery();

                    if(result.next()){
                        String LectMail=result.getString("Email");
                        String pass=result.getString("Password");

                        HtmlEmail email=new HtmlEmail();

                        email.setHostName("smtp.gmail.com");
                        email.setSmtpPort(587);
                        email.setStartTLSEnabled(true);
                        email.setAuthentication("phuthumaloyisopetse@gmail.com","sweleba88");

                        try {
                            email.setFrom("phuthumaloyisopetse@gmail.com");
                            email.addTo(LectMail);
                            email.setSubject("SolAssist: Password Recovery");
                            email.setHtmlMsg("Hi user your password is: "+pass);
                            email.send();
                        } catch (EmailException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //send error message
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid username or Email!");
                        alert.showAndWait();
                    }
                }

                Stage stage= (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            disconnect();
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
                con=DriverManager.getConnection(connectionString, "solassistuser","Dfjf8d02fdjjJ");

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
