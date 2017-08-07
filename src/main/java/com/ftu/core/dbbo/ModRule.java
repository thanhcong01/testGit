package com.ftu.core.dbbo;

public class ModRule {

    private int value;
    private int modValue;
    private String sessionName;

    public String getSessionName() {
        return this.sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getModValue() {
        return this.modValue;
    }

    public void setModValue(int modValue) {
        this.modValue = modValue;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
