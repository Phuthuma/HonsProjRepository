package sample;

import javafx.beans.property.*;

public class Module {
    //variables
    private StringProperty modCode;
    private StringProperty modName;
    private IntegerProperty modLevel;
    private StringProperty lectCode;

    //constructors
    public Module(String code, String name,Integer level, String lectCode){
        this.modCode=new SimpleStringProperty(code);
        this.modName=new SimpleStringProperty(name);
        this.modLevel=new SimpleIntegerProperty(level);
        this.lectCode=new SimpleStringProperty(lectCode);
    }

    //methods
    @Override
    public String toString() {
        return String.format("%s: %s",modCode.get(),modName.get());
    }

    public StringProperty modCodeProperty() {
        return modCode;
    }
    public StringProperty modNameProperty() {
        return modName;
    }
    public String getModName() {
        return modName.get();
    }

    public IntegerProperty modLevelProperty() {
        return modLevel;
    }
    public StringProperty lectCodeProperty() {
        return lectCode;
    }
}
