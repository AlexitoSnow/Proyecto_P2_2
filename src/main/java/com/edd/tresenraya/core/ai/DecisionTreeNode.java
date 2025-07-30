package com.edd.tresenraya.core.ai;

import java.util.*;

public class DecisionTreeNode<T> {
    private T data;
    private List<DecisionTreeNode<T>> children;

    public DecisionTreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public void addChild(DecisionTreeNode<T> child) {
        children.add(child);
    }

    public T getData() {
        return data;
    }

    public List<DecisionTreeNode<T>> getChildren() {
        return children;
    }
}
