package sample;

import com.jfoenix.controls.JFXTreeView;
import com.sun.org.apache.xpath.internal.NodeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class knowledgeGraphCont implements Initializable {
    //variables
    private Label lblTreeTitle=new Label();
    private Label lblTaskId=new Label();
    private TreeItem<Node>copyItem;

    @FXML private TreeView<Node> treeKnow;
    private TreeItem<Node>root;
    @FXML private TextArea txtAns;
    @FXML private TextArea txtQuest;

    //constructors
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        copyItem=new TreeItem<>(new Node(-1));
        System.out.println("Tree Title: "+lblTreeTitle.getText());

        root=new TreeItem<>(new Node());
        setUpNodes();

        treeKnow.setRoot(root);
        treeKnow.setEditable(true);

        treeKnow.setCellFactory(new Callback<TreeView<Node>, TreeCell<Node>>() {
            @Override
            public TreeCell<Node> call(TreeView<Node> param) {
                return new TextFieldTreeCellImpl(copyItem,lblTaskId.getText(),knowledgeGraphCont.this);
            }
        });
        treeKnow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=null){
                txtQuest.textProperty().unbindBidirectional(oldValue.getValue().questionProperty());
                txtAns.textProperty().unbindBidirectional(oldValue.getValue().questionProperty());
            }
            if(newValue!=null){
                txtQuest.textProperty().bindBidirectional(newValue.getValue().questionProperty());
                txtAns.textProperty().bindBidirectional(newValue.getValue().answerProperty());
            }
        });
        txtAns.setTooltip(new Tooltip("Add or edit the parent node's answer"));
        txtQuest.setTooltip(new Tooltip("Add or edit node question"));
        Tooltip tooltip=new Tooltip("Right-click to add node or edit knowledge graph");
        treeKnow.setTooltip(tooltip);
        treeKnow.getSelectionModel().selectFirst();
    }

    //methods
    public void setUpNodes(){
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder=factory.newDocumentBuilder();
            File fin=new File("tasks.xml");
            Document doc=builder.parse(fin);

            XPathFactory xpFact=XPathFactory.newInstance();
            XPath path=xpFact.newXPath();
            String input=lblTaskId.getText();
            String query="/tasks/task[@id = '"+input+"']/node";
            NodeList nodes= (NodeList) path.evaluate(query,doc,XPathConstants.NODESET);

            for(int i=0;i<nodes.getLength();i++){
                org.w3c.dom.Node curNode=nodes.item(i);
                if(curNode.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE) {
                    Element curElem= (Element) curNode;
                    addItems(curElem,root);
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
        }
    }
    private void addItems(Element curElem, TreeItem<Node>curItem) {
        Node newNode=new Node();
        newNode.setNodeNo(Integer.parseInt(curElem.getAttribute("no")));
        curItem.setValue(newNode);

        System.out.println(newNode);

        NodeList list = curElem.getChildNodes();
        for (int j = 0; j < list.getLength(); j++) {
            org.w3c.dom.Node listNode = list.item(j);
            if (listNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element listElem = (Element) listNode;

                if(listElem.getTagName().matches("question")){
                    newNode.setQuestion(listElem.getTextContent());
                    System.out.println(newNode.questionProperty().get());
                }else if(listElem.getTagName().matches("answer")){
                    newNode.setAnswer(listElem.getTextContent());
                    System.out.println(newNode.answerProperty().get());
                }
                else {
                    for(int i=0;i<listElem.getChildNodes().getLength();i++){
                        org.w3c.dom.Node curNode=listElem.getChildNodes().item(i);
                        if(curNode.getNodeType()== org.w3c.dom.Node.ELEMENT_NODE){
                            Element elem= (Element) curNode;
                            curItem.getChildren().add(new TreeItem<>(new Node()));
                            addItems(elem, curItem.getChildren().get(curItem.getChildren().size()-1));
                        }
                    }
                }


            }
        }
    }
    public void setLblTreeTitle(String title){
        lblTreeTitle.setText(title);
    }
    public void setLblTaskId(String taskId){
        lblTaskId.setText(taskId);
    }


}
