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
import java.util.ArrayList;
import java.util.List;

import ru.ok.android.marshallingcomparsion.flatbuffer.DataFlat;
import ru.ok.android.marshallingcomparsion.flatbuffer.DataFlats;
import ru.ok.android.marshallingcomparsion.flatbuffer.NodeFlat;
import ru.ok.android.marshallingcomparsion.flatbuffer.NodeMarshaller;
import ru.ok.android.marshallingcomparsion.model.Data;
import ru.ok.android.marshallingcomparsion.model.DataMarshaller;
import ru.ok.android.marshallingcomparsion.model.Node;
import ru.ok.android.marshallingcomparsion.model.NodeFactory;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialInputStream;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialOutputStream;
import ru.ok.android.marshallingcomparsion.protobuff.DataProtobuffProtos;
import ru.ok.android.marshallingcomparsion.protobuff.NodeProtobuffProtos;
import ru.ok.android.marshallingcomparsion.protobuff.ProtobuffMarshaller;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BigChunkTest extends ApplicationTestCase<Application> {

    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();
    public static final int NUM = 10000;

    static {
        fstConf.setShareReferences(false);
    }

    private static final String TAG = "SERIAL_TEST";
    private List<Data> datas = new ArrayList<>(NUM);

    public BigChunkTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        for (int i = 0; i < NUM; i++) {
            datas.add(Data.gen());
        }
    }

    public void testOkSerial() throws IOException {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        SimpleSerialOutputStream ssos = new SimpleSerialOutputStream(baos);

        long start = System.currentTimeMillis();
        ssos.writeCollection(datas);
        Log.d(TAG, "Ok Write: " + (System.currentTimeMillis() - start) + " ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Ok Bytes: " + buf.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        SimpleSerialInputStream ssis = new SimpleSerialInputStream(bais);

        start = System.currentTimeMillis();
        Object restoredData = ssis.readArrayList();
        Log.d(TAG, "Ok Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(datas, restoredData);
    }

    public void testJavaSerial() throws Exception {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        long start = System.currentTimeMillis();
        oos.writeObject(datas);
        Log.d(TAG, "Java Write: " + (System.currentTimeMillis() - start) + " ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Java Bytes: " + buf.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = new ObjectInputStream(bais);

        start = System.currentTimeMillis();
        Object restoredData = ois.readObject();
        Log.d(TAG, "Java Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(datas, restoredData);
    }

    public void testFlatBufferSerial() throws Exception {
        long start = System.currentTimeMillis();
        ByteBuffer bb = DataMarshaller.toByteBuffer(datas);
        Log.d(TAG, "Flat Write: " + (System.currentTimeMillis() - start) + " ms");

        Log.d(TAG, "Flat Bytes: " + bb.capacity());

        start = System.currentTimeMillis();
        DataFlats dataFlat = DataFlats.getRootAsDataFlats(bb);

        List<Data> restoredObject = DataMarshaller.unMarshal(dataFlat);
        Log.d(TAG, "Flat Read: " + (System.currentTimeMillis() - start) + " ms");

        Assert.assertEquals(datas, restoredObject);
    }

    public void testProtoBuffSerial() throws Exception {
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();
        long start = System.currentTimeMillis();
        DataProtobuffProtos.DataBundle dataBundle = ProtobuffMarshaller.toDataProtobuff(datas);
        Log.d(TAG, "Proto Create: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        dataBundle.writeTo(baos);
        Log.d(TAG, "Proto Write: " + (System.currentTimeMillis() - start) + "ms");

        byte[] buf = baos.toByteArray();
        Log.d(TAG, "Proto Bytes: " + buf.length);

        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        start = System.currentTimeMillis();
        DataProtobuffProtos.DataBundle newProto = DataProtobuffProtos.DataBundle.parseFrom(bais);
        Log.d(TAG, "Proto Read: " + getTimePassed(start));

        Assert.assertEquals(dataBundle, newProto);
    }

    public void testFstSerial() throws Exception {
        long start = System.currentTimeMillis();
        byte[] out = fstConf.asByteArray(datas);
        Log.d(TAG, "FST Write: " + getTimePassed(start));

        Log.d(TAG, "FST Bytes: " + out.length);

        start = System.currentTimeMillis();
        Object newNode = fstConf.asObject(out);
        Log.d(TAG, "FST Read: " + getTimePassed(start));

        Assert.assertEquals(datas, newNode);
    }

    @NonNull
    private static String getTimePassed(long start) {
        return (System.currentTimeMillis() - start) + "ms";
    }
}