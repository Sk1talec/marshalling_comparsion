package ru.ok.android.marshallingcomparsion.protobuff;

import java.util.List;

import ru.ok.android.marshallingcomparsion.model.Data;
import ru.ok.android.marshallingcomparsion.model.Node;

public class ProtobuffMarshaller {
    public static NodeProtobuffProtos.NodeProtobuff toNodeProtobuff(Node node) {
        NodeProtobuffProtos.NodeProtobuff.Builder builder = NodeProtobuffProtos.NodeProtobuff.newBuilder()
                .setDepth(node.getDepth());
        for (Node child : node.getChildren()) {
            builder.addChildren(toNodeProtobuff(child));
        }
        return builder.build();
    }

    public static DataProtobuffProtos.DataBundle toDataProtobuff(List<Data> datas) {
        DataProtobuffProtos.DataBundle.Builder datasBuilder = DataProtobuffProtos.DataBundle.newBuilder();
        for (Data data : datas) {
            datasBuilder.addData(
                    DataProtobuffProtos.DataProtobuff.newBuilder()
                            .setI1(data.getInt1())
                            .setI2(data.getInt2())
                            .setS1(data.getStr1())
                            .setS2(data.getStr2())
                            .setB1(data.isBool1())
                            .setB2(data.isBool2())
                            .setNode(NodeProtobuffProtos.NodeProtobuff.newBuilder().setDepth(data.getNode().getDepth()))
            );
        }
        return datasBuilder.build();
    }
}
