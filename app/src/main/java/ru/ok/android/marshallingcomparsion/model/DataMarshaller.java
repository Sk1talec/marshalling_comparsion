package ru.ok.android.marshallingcomparsion.model;

import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ok.android.marshallingcomparsion.flatbuffer.DataFlat;
import ru.ok.android.marshallingcomparsion.flatbuffer.DataFlats;
import ru.ok.android.marshallingcomparsion.flatbuffer.NodeFlat;

public class DataMarshaller {
    public static ByteBuffer toByteBuffer(List<Data> datas) {
        FlatBufferBuilder builder = new FlatBufferBuilder();

        int[] dataPointers = new int[datas.size()];

        int iData = 0;
        for (Data data : datas) {

            int children = NodeFlat.createChildrenVector(builder, new int[0]);
            int node = NodeFlat.createNodeFlat(builder, data.getNode().getDepth(), children);

            int s1 = builder.createString(data.getStr1());
            int s2 = builder.createString(data.getStr2());
            dataPointers[iData++] = DataFlat.createDataFlat(builder, data.getInt1(), data.getInt2(),
                    s1, s2, data.isBool1(), data.isBool2(), node);
        }
        int vector = DataFlats.createDatasVector(builder, dataPointers);
        builder.finish(DataFlats.createDataFlats(builder, vector));
        return builder.dataBuffer();
    }

    public static List<Data> unMarshal(DataFlats dataFlats) {
        List<Data> out = new ArrayList<>(dataFlats.datasLength());
        for (int i = 0; i < dataFlats.datasLength(); i++) {
            DataFlat data = dataFlats.datas(i);
            NodeFlat node = data.node();
            out.add(new Data(data.i1(), data.i2(), data.b1(), data.b2(), data.s1(), data.s2(),
                    new Node(node.depth(), Collections.<Node>emptyList())));
        }
        return out;
    }
}
