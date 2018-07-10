package sample;

import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class TextFieldTreeCellImpl extends TreeCell<Node> {
    //Variables
    private TreeItem<Node>copyItem;
    private TextField txtField;
    private ContextMenu menu = new ContextMenu();

    //Constructors
    public TextFieldTreeCellImpl(TreeItem<Node>copyItem){
        this.copyItem=copyItem;

        MenuItem addStud=new MenuItem("Add");
        addStud.setOnAction(event -> {
            Dialog dialog=new Dialog();
            dialog.setTitle("New Node");

            GridPane grid=new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20,150,10,10));

            TextField txtNo=new TextField();
            txtNo.setPromptText("Enter Node No.");
            TextArea txtQues=new TextArea("");
            txtQues.setPromptText("Enter Question");
            TextArea txtAns=new TextArea("");
            txtAns.setPromptText("Enter Parent Answer");

            grid.add(new Label("Node No:"),0,0);
            grid.add(txtNo,1,0);
            grid.add(new Label("Question:"), 0, 1);
            grid.add(txtQues, 1, 1);
            grid.add(new Label("Parent Answer:"), 0, 2);
            grid.add(txtAns, 1, 2);


            dialog.getDialogPane().setContent(grid);

            ButtonType saveButtonType=new ButtonType("Save",ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType,ButtonType.CANCEL);

            Optional<ButtonType>result=dialog.showAndWait();
            if (result.get()==saveButtonType){

                getTreeItem().getChildren().add(new TreeItem<>(new Node(Integer.parseInt(txtNo.getText()),
                        txtQues.getText(),txtAns.getText())));
            }

        });
        menu.getItems().add(addStud);


        MenuItem copyMenuItem=new MenuItem("Copy");
        copyMenuItem.setOnAction(event -> {
            copyItem.getChildren().clear();
            copyItem.setValue(getTreeView().getSelectionModel().getSelectedItem().getValue());
            copyItem.getChildren().addAll(getTreeView().getSelectionModel().getSelectedItem().getChildren());
        });
        menu.getItems().addAll(copyMenuItem);

        MenuItem pastItem=new MenuItem("Paste");
        pastItem.setOnAction(event -> {
            if ((copyItem != null)&&(!copyItem.getChildren().contains(getTreeItem()))) {
                getTreeView().getSelectionModel().getSelectedItem().getChildren().clear();
                getTreeView().getSelectionModel().getSelectedItem().setValue(copyItem.getValue());
                getTreeView().getSelectionModel().getSelectedItem().getChildren().addAll(copyItem.getChildren());
            }else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Can't copy parent to child!");
                alert.showAndWait();
            }
        });
        menu.getItems().add(pastItem);
    }

    //methods
    private void addChildren(TreeItem<Node>copyItem, TreeItem<Node>pasteItem){
        for(int i=0;i<copyItem.getChildren().size();i++){
            TreeItem<Node>curItem=copyItem.getChildren().get(i);
            TreeItem<Node>newItem=new TreeItem<>(new Node(curItem.getValue().nodeNoProperty().get(),
                    curItem.getValue().questionProperty().get(),
                    curItem.getValue().answerProperty().get()));
            pasteItem.getChildren().add(newItem);
            if(curItem.getChildren().size()>0){
                addChildren(curItem,newItem);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if(txtField==null)
            createTextField();
        setText(null);
        setGraphic(txtField);
        txtField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem().toString());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    protected void updateItem(Node item, boolean empty) {
        super.updateItem(item, empty);

        if(empty){
            setText(null);
            setGraphic(null);
        }else {
            if(isEditing()){
                if(txtField!=null){
                    txtField.setText(getNode().toString());
                }
                setText(null);
                setGraphic(txtField);
            }else {
                setText(getNode().toString());
                setGraphic(getTreeItem().getGraphic());
                if(getParent()!=null)
                    setContextMenu(menu);
            }
        }
    }

    private void createTextField(){
        txtField=new TextField(getNode().toString());
        txtField.setOnKeyReleased(event -> {
            if(event.getCode()== KeyCode.ENTER) {
                commitEdit(new Node(Integer.parseInt(txtField.getText()),
                        getItem().questionProperty().get(),getItem().answerProperty().get()));
            }
            else if(event.getCode()==KeyCode.ESCAPE)
                cancelEdit();
        });
    }

    private Node getNode(){
        return getItem() == null? new Node(0):getItem();
    }
}
