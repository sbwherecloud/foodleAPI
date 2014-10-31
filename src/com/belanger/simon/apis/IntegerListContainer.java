package com.belanger.simon.apis;

import java.util.List;

public class IntegerListContainer {
    public List<Integer> getIntegersList() {
        return integersList;
    }
    public void setIntegersList(List<Integer> objectsList) {
        this.integersList = objectsList;
    }
    private List<Integer> integersList;
}