package ru.ok.android.marshallingcomparsion.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NodeFactory {

    private final int maxChildren;
    private final Random rnd = new Random(12346);

    public NodeFactory(int maxChildren) {
        this.maxChildren = maxChildren;
    }

    public Node generateTree(int depth) {
        return new Node(0, generateChildren(1, depth));
    }

    private List<Node> generateChildren(int curDepth, int maxDepth) {
        if (curDepth >= maxDepth) {
            return Collections.emptyList();
        }
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < rnd.nextInt(maxChildren); ++i) {
            children.add(new Node(curDepth, generateChildren(curDepth + 1, maxDepth)));
        }
        return children;
    }
}
