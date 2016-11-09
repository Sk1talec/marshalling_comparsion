package ru.ok.android.marshallingcomparsion.okserial;

import java.io.IOException;

import ru.ok.android.marshallingcomparsion.model.Data;
import ru.ok.android.marshallingcomparsion.model.Node;

public class DataSerializer implements Serializer<Data> {
    @Override
    public void writeObject(SimpleSerialOutputStream out, Data o) throws IOException {
        out.writeInt(o.getInt1());
        out.writeInt(o.getInt2());
        out.writeString(o.getStr1());
        out.writeString(o.getStr2());
        out.writeBoolean(o.isBool1());
        out.writeBoolean(o.isBool2());
        out.writeObject(o.getNode());
    }

    @Override
    public Data readObject(SimpleSerialInputStream in) throws IOException {
        int i1 = in.readInt();
        int i2 = in.readInt();
        String s1 = in.readString();
        String s2 = in.readString();
        boolean b1 = in.readBoolean();
        boolean b2 = in.readBoolean();
        Node node = in.readObject();
        return new Data(i1, i2, b1, b2, s1, s2, node);
    }

    @Override
    public int getId() {
        return 1;
    }
}
