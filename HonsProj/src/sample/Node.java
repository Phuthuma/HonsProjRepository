package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Node {
    //variables
    private IntegerProperty nodeNo;
    private StringProperty question;
    private StringProperty answer;
    private ObservableList<Node>childern;

    //constructors
    public Node(int no){
       this.nodeNo=new SimpleIntegerProperty(no);
       this.question=new SimpleStringProperty();
       this.answer=new SimpleStringProperty();
       this.childern=FXCollections.observableList(new ArrayList<Node>());
    }
    public Node(int no,String question){
        this.nodeNo=new SimpleIntegerProperty(no);
        this.question=new SimpleStringProperty(question);
        this.answer=new SimpleStringProperty();
        this.childern=FXCollections.observableList(new ArrayList<Node>());
    }
    public Node(int no,String question,String answer){
        this.nodeNo=new SimpleIntegerProperty(no);
        this.question=new SimpleStringProperty(question);
        this.answer=new SimpleStringProperty(answer);
        this.childern=FXCollections.observableList(new ArrayList<Node>());
    }

    //methods
    @Override
    public String toString() {
        return String.format("%s",nodeNo.get());
    }

    public IntegerProperty nodeNoProperty() {
        return nodeNo;
    }
    public StringProperty questionProperty() {
        return question;
    }
    public StringProperty answerProperty() {
        return answer;
    }

    public void addNode(Node Node){

    }
}
