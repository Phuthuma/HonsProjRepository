package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lecturer {
    //variables
    private SimpleStringProperty lectCode;
    private SimpleStringProperty lectName;
    private SimpleStringProperty lectEmail;

    //constructors
    public Lecturer(String code,String name,String email){
        lectCode=new SimpleStringProperty(code);
        lectName=new SimpleStringProperty(name);
        lectEmail=new SimpleStringProperty(email);
    }

    //methods
    public SimpleStringProperty lectCodeProperty() {
        return lectCode;
    }
    public SimpleStringProperty lectNameProperty() {
        return lectName;
    }
    public SimpleStringProperty lectEmailProperty() {
        return lectEmail;
    }
}
