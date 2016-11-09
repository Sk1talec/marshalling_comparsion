package ru.ok.android.marshallingcomparsion.model;

import android.support.annotation.NonNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.ok.android.marshallingcomparsion.okserial.Marshaller;
import ru.ok.android.marshallingcomparsion.okserial.NodeSerializer;

@Marshaller(NodeSerializer.class)
public class Node implements Externalizable {
    private int depth;
    private List<Node> children;

    public Node() { }

    public Node(int depth, @NonNull List<Node> children) {
        this.depth = depth;
        this.children = children;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        addTabs(sb);
        sb.append(depth);
        sb.append(": {\n");
        int order = 0;
        for (Node child : children) {
            sb.append(child);
            if (++order < children.size()) {
                sb.append(",");
            }
            sb.append("\n");
        }
        addTabs(sb).append("}");
        return sb.toString();
    }

    private StringBuilder addTabs(StringBuilder sb) {
        for (int i = 0; i < depth; ++i) {
            sb.append("\t");
        }
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (depth != node.depth) return false;
        return children != null ? children.equals(node.children) : node.children == null;

    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    public int size() {
        int size = 0;
        for (Node child : children) {
            size += child.size();
        }
        return 1 + size;
    }

    @Override
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        depth = input.readInt();
        int len = input.readInt();
        children = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            children.add(i, (Node) input.readObject());
        }
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(depth);
        output.writeInt(children.size());
        for (Node child : children) {
            output.writeObject(child);
        }
    }
}
