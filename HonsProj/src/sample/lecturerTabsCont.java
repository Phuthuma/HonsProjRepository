package sample;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class lecturerTabsCont implements Initializable {
    //variables
    private Connection con=null;
    private String lectCode="?";

    private ArrayList<Module>mods;
    private ObservableList<Module> obsMods;

    @FXML private JFXListView<Module> lstMods;
    @FXML private Label lblCode;

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mods=new ArrayList<>();
        obsMods=FXCollections.observableList(mods);
        setUpMods();
        lstMods.setItems(obsMods);
        lstMods.getSelectionModel().selectFirst();

    }

    private void setUpMods(){
        connect();
        String sql="select * from Module where LectCode = ?";
        try {
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.setString(1,lblCode.getText());
            ResultSet result=stmt.executeQuery();
            while (result.next()){
               String modCode=result.getString("ModCode");
               String modName=result.getString("ModName");
               Integer modLevel=result.getInt("ModLevel");
               String lectCode=result.getString("LectCode");
               Module newMod=new Module(modCode,modName,modLevel,lectCode);
               obsMods.add(newMod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }
    public void setCode(String code){
        lblCode.setText(code);
    }
    private String getCode(){
        return lblCode.getText();
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
