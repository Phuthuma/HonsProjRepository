package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class logInCont implements Initializable {
    //variables
    private ArrayList<String>types;
    private ObservableList<String> obsTypes;

    @FXML private JFXTextField txtUname;
    @FXML private JFXComboBox<String>cmbType;
    @FXML private JFXPasswordField txtPass;
    @FXML private JFXButton btnSignIn;
    @FXML private JFXButton lblForgot;
    @FXML private JFXButton lblSignIn;

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        types=new ArrayList<>();
        obsTypes=FXCollections.observableList(types);
        obsTypes.add("Student");
        obsTypes.add("Lecturer");
        obsTypes.add("System Administrator");
        cmbType.setItems(obsTypes);
        setValidations();
        lblSignIn.setOnAction(event -> {
            Parent addRoot = null;
            try {
                addRoot=FXMLLoader.load(getClass().getResource("signUp.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene newScene=new Scene(addRoot);
            Stage primeStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
            Stage stage=new Stage();
            stage.initOwner(primeStage);
            stage.setScene(newScene);
            stage.setTitle("SingUp");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setWidth(750.0);
            stage.setHeight(750.0);
            stage.showAndWait();
        });
        lblForgot.setOnAction(event -> {
            Parent addRoot = null;
            try {
                addRoot=FXMLLoader.load(getClass().getResource("forgotPass.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene newScene=new Scene(addRoot);
            Stage primeStage= (Stage) ((Node)event.getSource()).getScene().getWindow();
            Stage stage=new Stage();
            stage.initOwner(primeStage);
            stage.setScene(newScene);
            stage.setTitle("Password Recovery");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setWidth(750.0);
            stage.setHeight(750.0);
            stage.showAndWait();
        });
    }
    private void setValidations(){
        RequiredFieldValidator val=new RequiredFieldValidator();
        val.setMessage("Required Field");
        txtUname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue){
                txtUname.getValidators().add(val);
                txtUname.validate();
            }
        });
        txtPass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue){
                txtPass.getValidators().add(val);
                txtPass.validate();
            }
        });
    }
}
