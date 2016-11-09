package ru.ok.android.marshallingcomparsion;

import android.app.Application;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.Assert;

import org.nustaq.serialization.FSTConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import ru.ok.android.marshallingcomparsion.flatbuffer.NodeFlat;
import ru.ok.android.marshallingcomparsion.flatbuffer.NodeMarshaller;
import ru.ok.android.marshallingcomparsion.model.Node;
import ru.ok.android.marshallingcomparsion.model.NodeFactory;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialInputStream;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialOutputStream;
import ru.ok.android.marshallingcomparsion.protobuff.NodeProtobuffProtos;
import ru.ok.android.marshallingcomparsion.protobuff.ProtobuffMarshaller;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ThreeTest extends ApplicationTestCase<Application> {

    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();
    static {
        fstConf.setShareReferences(false);

    }

    private static final String TAG = "SERIAL_TEST";
    private Node node;

    public ThreeTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        NodeFactory nodeFactory = new NodeFactory(8);
        node = nodeFactory.generateTree(10);
    }

    public void testOkSerial() throws IOException {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        SimpleSerialOutputStream ssos = new SimpleSerialOutputStream(baos);

        long start = System.currentTimeMillis();
        ssos.writeObject(getTestData());
        Log.d(TAG, "Ok Write: " + (System.currentTimeMillis() - start) + " ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Ok Bytes: " + buf.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        SimpleSerialInputStream ssis = new SimpleSerialInputStream(bais);

        start = System.currentTimeMillis();
        Object restoredData = ssis.readObject();
        Log.d(TAG, "Ok Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(getTestData(), restoredData);
    }

    private Node getTestData() {
        return node;
    }

    public void testJavaSerial() throws Exception {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        long start = System.currentTimeMillis();
        oos.writeObject(getTestData());
        Log.d(TAG, "Java Write: " + (System.currentTimeMillis() - start) + " ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Java Bytes: " + buf.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = new ObjectInputStream(bais);

        start = System.currentTimeMillis();
        Object restoredData = ois.readObject();
        Log.d(TAG, "Java Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(getTestData(), restoredData);
    }

    public void testFlatBufferSerial() throws Exception {
        long start = System.currentTimeMillis();
        ByteBuffer bb = NodeMarshaller.toByteBuffer(getTestData());
        Log.d(TAG, "Flat Write: " + (System.currentTimeMillis() - start) + " ms");

        Log.d(TAG, "Flat Bytes: " + bb.capacity());

        start = System.currentTimeMillis();
        NodeFlat nodeFlat = NodeFlat.getRootAsNodeFlat(bb);
        Object restoredObject = NodeMarshaller.unMarshal(nodeFlat);
        Log.d(TAG, "Flat Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(getTestData(), restoredObject);
    }

    public void testProtoBuffSerial() throws Exception {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        long start = System.currentTimeMillis();
        NodeProtobuffProtos.NodeProtobuff nodeProtobuff = ProtobuffMarshaller.toNodeProtobuff(getTestData());
        Log.d(TAG, "Proto Create: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        nodeProtobuff.writeTo(baos);
        Log.d(TAG, "Proto Write: " + (System.currentTimeMillis() - start) + "ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Proto Bytes: " + buf.length);

        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        start = System.currentTimeMillis();
        NodeProtobuffProtos.NodeProtobuff newProto = NodeProtobuffProtos.NodeProtobuff.parseFrom(bais);
        Log.d(TAG, "Proto Read: " + getTimePassed(start));

        Assert.assertEquals(nodeProtobuff, newProto);
    }

    public void testFstSerial() throws Exception {
        long start = System.currentTimeMillis();
        byte[] out = fstConf.asByteArray(getTestData());
        Log.d(TAG, "FST Write: " + getTimePassed(start));

        Log.d(TAG, "FST Bytes: " + out.length);

        start = System.currentTimeMillis();
        Node newNode = (Node) fstConf.asObject(out);
        Log.d(TAG, "FST Read: " + getTimePassed(start));

        Assert.assertEquals(getTestData(), newNode);
    }

    @NonNull
    private static String getTimePassed(long start) {
        return (System.currentTimeMillis() - start) + "ms";
    }
}