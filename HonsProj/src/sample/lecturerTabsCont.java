package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class lecturerTabsCont implements Initializable {
    //variables
    private Connection con=null;
    private String lectCode="?";

    private ArrayList<Module>mods;
    private ObservableList<Module> obsMods;

    @FXML private JFXListView<Module> lstMods;
    @FXML private JFXListView<Task> lstTasks;
    @FXML private Label lblCode;
    @FXML private JFXButton btnNewTask;
    @FXML private JFXButton btnDelTask;

    //methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mods=new ArrayList<>();
        obsMods=FXCollections.observableList(mods);
        setUpMods();
        setUpTasks();
        lstMods.setItems(obsMods);
        lstMods.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                lstTasks.setItems(newValue.getObsTasks());
                lstTasks.getSelectionModel().selectFirst();
            }
        });
        lstMods.getSelectionModel().selectFirst();

        lstTasks.setOnMouseClicked(event -> {
            if((event.getButton()==MouseButton.PRIMARY)&&(event.getClickCount()==2)){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("knowledgeGraph.fxml"));
                try {
                    Parent parent = loader.load();
                    knowledgeGraphCont cont = loader.<knowledgeGraphCont>getController();
                    cont.setLblTreeTitle(lstTasks.getSelectionModel().getSelectedItem().titleProperty().get());
                    cont.setLblTaskId(lstTasks.getSelectionModel().getSelectedItem().taskIdProperty().get());
                    cont.initialize(loader.getLocation(), loader.getResources());
                    Stage newStage = new Stage();
                    newStage.setTitle("Knowledge Graph");
                    newStage.setScene(new Scene(parent));
                    newStage.setHeight(800.0);
                    newStage.setWidth(800.0);
                    newStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnNewTask.setOnAction(event -> {
            if(lstMods.getSelectionModel().getSelectedItem()!=null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("newTask.fxml"));
                try {
                    Parent parent = loader.load();
                    newTaskCont cont = loader.<newTaskCont>getController();
                    cont.setModCode(lstMods.getSelectionModel().getSelectedItem().modCodeProperty().get());
                    cont.initialize(loader.getLocation(), loader.getResources());
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(parent));
                    newStage.setHeight(400.0);
                    newStage.setWidth(400.0);
                    newStage.showAndWait();

                    //updating tasks after insertion
                    lstTasks.getItems().clear();
                    obsMods.clear();
                    setUpMods();
                    setUpTasks();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Select module in which you want to insert new task");
                alert.showAndWait();
            }
        });
        btnDelTask.setOnAction(event -> {
            Alert delAlert=new Alert(Alert.AlertType.CONFIRMATION);
            delAlert.setTitle("Task Delete");
            delAlert.setContentText("Are you sure you want to delete "+lstTasks.getSelectionModel().getSelectedItem().titleProperty().get()+" ?");
            Optional<ButtonType> result=delAlert.showAndWait();
            if(result.get()==ButtonType.OK){
                connect();
                String sql="Delete from Task where TaskCode = ?";
                try {
                    PreparedStatement stmt=con.prepareStatement(sql);
                    stmt.setString(1,lstTasks.getSelectionModel().getSelectedItem().taskIdProperty().get());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                disconnect();
                lstTasks.getItems().remove(lstTasks.getSelectionModel().getSelectedItem());
            }else{
                delAlert.close();
            }
        });
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
    private void setUpTasks(){
        for (Module module:obsMods) {
            connect();
            String sql="select * from Task where ModCode = ?";
            try {
                PreparedStatement stmt=con.prepareStatement(sql);
                stmt.setString(1,module.modCodeProperty().get());
                ResultSet result=stmt.executeQuery();
                while (result.next()){
                    String taskCode=result.getString("TaskCode");
                    Integer taskNo=result.getInt("TaskNo");
                    String title=result.getString("TaskTitle");
                    String modCode=result.getString("ModCode");
                    module.getObsTasks().add(new Task(taskCode,taskNo,title,modCode));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            disconnect();
        }
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
