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
    private TreeItem<Node>copyItem;
    @FXML private TreeView<Node> treeKnow;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        copyItem=new TreeItem<>(new Node(-1));
        System.out.println("Tree Title: "+lblTreeTitle.getText());
        TreeItem<Node>root=new TreeItem<>(new Node(0));

        treeKnow.setRoot(root);
        treeKnow.setEditable(true);
        treeKnow.setCellFactory(new Callback<TreeView<Node>, TreeCell<Node>>() {
            @Override
            public TreeCell<Node> call(TreeView<Node> param) {
                return new TextFieldTreeCellImpl(copyItem);
            }
        });
        treeKnow.getSelectionModel().selectFirst();
    }

    public void setLblTreeTitle(String title){
        lblTreeTitle.setText(title);
    }


}
