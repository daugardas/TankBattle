package com.tankbattle.server.memento;

import java.util.ArrayList;
import java.util.List;

public class LevelCaretaker {
    public List<LevelMemento> mementoList = new ArrayList<>();
    public void addMemento(LevelMemento memento) {
        mementoList.add(memento);
    }

    public LevelMemento getMemento(int index) {
        if (index < 0 || index >= mementoList.size()) {
            throw new IndexOutOfBoundsException("Invalid memento index.");
        }
        return mementoList.get(index);
    }

    public int getMementoCount() {
        return mementoList.size();
    }
}