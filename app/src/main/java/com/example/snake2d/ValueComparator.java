package com.example.snake2d;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator {

    Map _base;
    public ValueComparator(Map base) {
        _base = base;
    }

    public int compare(Object a, Object b) {
        int value = ((Integer)_base.get(b)).compareTo((Integer)_base.get(a));
        return value;
    }
}