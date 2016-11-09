package ru.ok.android.marshallingcomparsion.flatbuffer;

import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ok.android.marshallingcomparsion.model.Node;

public class NodeMarshaller {

    private final static int[] EMPTY_ARRAY = new int[0];

    public static ByteBuffer toByteBuffer(Node node) {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int root = marshalRec(builder, node);
        builder.finish(root);
        return builder.dataBuffer();
    }

    public static int marshalRec(FlatBufferBuilder builder, Node node) {
        if (node.getChildren().size() == 0) {
            int vec = NodeFlat.createChildrenVector(builder, EMPTY_ARRAY);
            return NodeFlat.createNodeFlat(builder, node.getDepth(), vec);
        } else {
            List<Node> children = node.getChildren();
            int size = children.size();
            int[] childrenOffsets = new int[size];
            int i = 0;
            for (Node child : children) {
                childrenOffsets[i++] = marshalRec(builder, child);
            }
            int vec = NodeFlat.createChildrenVector(builder, childrenOffsets);
            return NodeFlat.createNodeFlat(builder, node.getDepth(), vec);
        }
    }

    public static Node unMarshal(NodeFlat nodeFlat) {
        if (nodeFlat.childrenLength() == 0) {
            return new Node(nodeFlat.depth(), Collections.<Node>emptyList());
        } else {
            List<Node> list = new ArrayList<>();
            for (int i = 0; i < nodeFlat.childrenLength(); i++) {
                list.add(unMarshal(nodeFlat.children(i)));
            }
            return new Node(nodeFlat.depth(), list);
        }
    }
}
