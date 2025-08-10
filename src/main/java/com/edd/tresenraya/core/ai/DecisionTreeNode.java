package com.edd.tresenraya.core.ai;

import java.util.*;

public class DecisionTreeNode {
    private GameState state;
    private List<DecisionTreeNode> children;
    int utility; // solo para nodos hoja
    int depth;

    public DecisionTreeNode(GameState State,int difficultyDepth) {
        this.state = State;
        this.children = new ArrayList<>();
        this.depth = difficultyDepth;
    }

    public void addChild(DecisionTreeNode child) {
        children.add(child);
    }

    public GameState getState() {
        return state;
    }

    public List<DecisionTreeNode> getChildren() {
        return children;
    }
}
