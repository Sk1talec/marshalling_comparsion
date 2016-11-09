package ru.ok.android.marshallingcomparsion.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Random;

import ru.ok.android.marshallingcomparsion.okserial.DataSerializer;
import ru.ok.android.marshallingcomparsion.okserial.Marshaller;

@Marshaller(DataSerializer.class)
public class Data implements Externalizable {
    private int int1;
    private int int2;
    private boolean bool1;
    private boolean bool2;
    private String str1;
    private String str2;
    private Node node;

    public Data() {

    }

    public Data(int int1, int int2, boolean bool1, boolean bool2, String str1, String str2, Node node) {
        this.int1 = int1;
        this.int2 = int2;
        this.bool1 = bool1;
        this.bool2 = bool2;
        this.str1 = str1;
        this.str2 = str2;
        this.node = node;
    }

    private static byte[] sharedBuff = new byte[120];
    private static int counter = 0;

    public static Data gen() {
        Random rnd = new Random(counter++);
        byte[] buff = sharedBuff;
        rnd.nextBytes(buff);
        String str1 = randomString(rnd);
        String str2 = randomString(rnd);
        return new Data(rnd.nextInt(), rnd.nextInt(), rnd.nextBoolean(), rnd.nextBoolean(),
                str1, str2, new Node(rnd.nextInt(10), Collections.<Node>emptyList()));
    }

    private static String randomString(Random rnd) {
        for (int i = 0; i < sharedBuff.length; i++) {
            sharedBuff[i] = (byte) ('a' + rnd.nextInt(20));
        }
        return new String(sharedBuff, 0, sharedBuff.length, Charset.forName("ASCII"));
    }

    @Override
    public String toString() {
        return "Data{" +
                "int1=" + int1 +
                ", int2=" + int2 +
                ", bool1=" + bool1 +
                ", bool2=" + bool2 +
                ", str1='" + str1 + '\'' +
                ", str2='" + str2 + '\'' +
                ", node=" + node +
                '}';
    }

    public int getInt1() {
        return int1;
    }

    public int getInt2() {
        return int2;
    }

    public boolean isBool1() {
        return bool1;
    }

    public boolean isBool2() {
        return bool2;
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        if (int1 != data.int1) return false;
        if (int2 != data.int2) return false;
        if (bool1 != data.bool1) return false;
        if (bool2 != data.bool2) return false;
        if (str1 != null ? !str1.equals(data.str1) : data.str1 != null) return false;
        if (str2 != null ? !str2.equals(data.str2) : data.str2 != null) return false;
        return node != null ? node.equals(data.node) : data.node == null;

    }

    @Override
    public int hashCode() {
        int result = int1;
        result = 31 * result + int2;
        result = 31 * result + (bool1 ? 1 : 0);
        result = 31 * result + (bool2 ? 1 : 0);
        result = 31 * result + (str1 != null ? str1.hashCode() : 0);
        result = 31 * result + (str2 != null ? str2.hashCode() : 0);
        result = 31 * result + (node != null ? node.hashCode() : 0);
        return result;
    }

    @Override
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        int1 = input.readInt();
        int2 = input.readInt();
        str1 = input.readUTF();
        str2 = input.readUTF();
        bool1 = input.readBoolean();
        bool2 = input.readBoolean();
        node = new Node();
        node.readExternal(input);
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(int1);
        output.writeInt(int2);
        output.writeUTF(str1);
        output.writeUTF(str2);
        output.writeBoolean(bool1);
        output.writeBoolean(bool2);
        node.writeExternal(output);
    }
}
