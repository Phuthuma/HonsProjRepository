package sample;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class knowledgeGraphCont implements Initializable {
    //variables
    private Label lblTreeTitle=new Label();
    @FXML private JFXTreeView<String> treeKnow;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Tree Title: "+lblTreeTitle.getText());
        TreeItem<String>root=new TreeItem<>(lblTreeTitle.getText());
        treeKnow.setRoot(root);
        treeKnow.setEditable(true);
        treeKnow.getSelectionModel().selectFirst();
        treeKnow.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new TextFieldTreeCellImpl();
            }
        });
    }

    public void setLblTreeTitle(String title){
        lblTreeTitle.setText(title);
    }


}
