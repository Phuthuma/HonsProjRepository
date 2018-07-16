package sample;

import com.sun.org.apache.xpath.internal.NodeSet;
import javafx.geometry.Insets;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class TextFieldTreeCellImpl extends TreeCell<Node> {
    //Variables
    private TreeItem<Node>copyItem;
    private TextField txtField;
    private ContextMenu menu = new ContextMenu();
    private knowledgeGraphCont cont;

    //Constructors
    public TextFieldTreeCellImpl(TreeItem<Node>copyItem, String taskId,knowledgeGraphCont cont){
        this.copyItem=copyItem;
        this.cont=cont;

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
                //add to xml file
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder=factory.newDocumentBuilder();
                    Document doc=builder.parse(new File("tasks.xml"));

                    XPathFactory xpFact=XPathFactory.newInstance();
                    XPath path=xpFact.newXPath();

                    String query="//task[@id='"+taskId+"']//node[@no = '"+getTreeItem().getValue().nodeNoProperty().getValue()+"' ]";
                    NodeList nodes= (NodeList) path.evaluate(query,doc,XPathConstants.NODESET);

                    System.out.println("No of Nodes: "+nodes.getLength());
                    for(int i=0;i<nodes.getLength();i++){
                        org.w3c.dom.Node curNode=nodes.item(i);
                        if(curNode.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE){
                            Element curElem= (Element) curNode;
                            NodeList children=curElem.getChildNodes();

                            //we must add to child elem
                            Element childElem= (Element) children.item(5);

                            //create new node element
                            Element newElem=doc.createElement("node");
                            newElem.setAttribute("no",txtNo.getText());

                            Element quesElem=doc.createElement("question");
                            Text quesText=doc.createTextNode(txtQues.getText());
                            quesElem.appendChild(quesText);
                            newElem.appendChild(quesElem);

                            Element ansElem=doc.createElement("answer");
                            Text ansText=doc.createTextNode(txtAns.getText());
                            ansElem.appendChild(ansText);
                            newElem.appendChild(ansElem);

                            Element childrenElem=doc.createElement("children");
                            newElem.appendChild(childrenElem);

                            childElem.appendChild(newElem);
                            saveDoc(doc,"tasks.xml");
                            getTreeView().getRoot().getChildren().clear();
                            cont.setUpNodes();
                        }
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                /*getTreeView().getSelectionModel().getSelectedItem().getChildren().clear();
                getTreeView().getSelectionModel().getSelectedItem().setValue(copyItem.getValue());
                getTreeView().getSelectionModel().getSelectedItem().getChildren().addAll(copyItem.getChildren());*/

                //add to xml file
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder=factory.newDocumentBuilder();
                    Document doc=builder.parse("tasks.xml");

                    XPathFactory xPathFac=XPathFactory.newInstance();
                    XPath path=xPathFac.newXPath();

                    String query="//task[@id='"+taskId+"']//node[@no = '"+getTreeItem().getValue().nodeNoProperty().getValue()+"' ]";

                    NodeList nodes= (NodeList) path.evaluate(query,doc,XPathConstants.NODESET);
                    for(int i=0;i<nodes.getLength();i++){
                        org.w3c.dom.Node curNode=nodes.item(i);
                        if(curNode.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE){
                            Element curElem= (Element) curNode;
                            curElem.setAttribute("no",copyItem.getValue().nodeNoProperty().getValue().toString());

                            Element quesElem= (Element) curElem.getChildNodes().item(1);
                            quesElem.setTextContent(copyItem.getValue().questionProperty().getValue());

                            Element ansElem= (Element) curElem.getChildNodes().item(3);
                            ansElem.setTextContent(copyItem.getValue().answerProperty().getValue());

                            Element childrenElem= (Element) curElem.getChildNodes().item(5);
                            //traverse copy item children and add nodes

                            System.out.println("Node Tag: "+curElem.getTagName()+" Node no: "+curElem.getAttribute("no"));
                        }
                    }

                    saveDoc(doc,"tasks.xml");
                    getTreeView().getRoot().getChildren().clear();
                    cont.setUpNodes();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
    private static void saveDoc(Document doc, String filename) throws Exception {
        // obtain serializer
        DOMImplementation impl = doc.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
        LSSerializer ser = implLS.createLSSerializer();
        ser.getDomConfig().setParameter("format-pretty-print", true);

        // create file to save too
        FileOutputStream fout = new FileOutputStream(filename);

        // set encoding options
        LSOutput lsOutput = implLS.createLSOutput();
        lsOutput.setEncoding("UTF-8");

        // tell to save xml output to file
        lsOutput.setByteStream(fout);

        // FINALLY write output
        ser.write(doc, lsOutput);

        // close file
        fout.close();
    }

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
