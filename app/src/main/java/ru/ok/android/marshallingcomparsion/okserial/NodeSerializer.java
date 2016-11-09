package ru.ok.android.marshallingcomparsion.okserial;

import java.io.IOException;
import java.util.List;

import ru.ok.android.marshallingcomparsion.model.Node;

public class NodeSerializer implements Serializer<Node> {
    @Override
    public void writeObject(SimpleSerialOutputStream out, Node o) throws IOException {
        out.writeInt(o.getDepth());
        out.writeCollection(o.getChildren());
    }

    @Override
    public Node readObject(SimpleSerialInputStream in) throws IOException {
        int depth = in.readInt();
        List<Node> children = in.readArrayList();
        return new Node(depth, children);
    }

    @Override
    public int getId() {
        return 0;
    }
}
