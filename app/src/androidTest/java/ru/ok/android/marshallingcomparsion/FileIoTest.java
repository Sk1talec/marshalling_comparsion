package ru.ok.android.marshallingcomparsion;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.Pair;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.Assert;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileIoTest extends ApplicationTestCase<Application> {
    public FileIoTest() {
        super(Application.class);
    }

    public void testFileIo() throws Exception {
        List<Pair<String, Integer>> testData = new ArrayList<Pair<String, Integer>>() {{
            add(new Pair<>("OK", 63544));
            add(new Pair<>("ProtoBuff", 19715));
            add(new Pair<>("FST", 29373));
            add(new Pair<>("FlatBuffer", 131072));
            add(new Pair<>("Serializable", 99563));
        }};

        for (Pair<String, Integer> data : testData) {
            byte[] buffer = genBuffer(data.second);
            FileOutputStream fos = getContext().openFileOutput(data.first, Context.MODE_PRIVATE);
            try {
                long start = System.currentTimeMillis();
                fos.write(buffer);
                Log.d("DISK IO WRITE", data.second + ": " + (System.currentTimeMillis() - start) + " ms");
            } finally {
                fos.close();
            }
            FileInputStream fis = getContext().openFileInput(data.first);
            byte[] input = new byte[data.second];
            try {
                long start = System.currentTimeMillis();
                fis.read(input);
                Log.d("DISK IO READ", data.second + ": " + (System.currentTimeMillis() - start) + " ms");
            } finally {
                fis.close();
            }

            Assert.assertEquals(buffer.length, input.length);

            for (int i = 0; i < data.second; i++) {
                if (buffer[i] != input[i]) {
                    Assert.fail("Data is not equal: " + i);
                }
            }
        }
    }

    private byte[] genBuffer(int length) {
        Random rnd = new Random(0);
        byte[] out = new byte[length];
        rnd.nextBytes(out);
        return out;
    }

    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();

    public void testSerialException2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FSTObjectOutput objectOutput = fstConf.getObjectOutput(baos);
        objectOutput.flush();
        MyTestClass myTestClass = (MyTestClass) fstConf.getObjectInput(new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()))).readObject();
    }

    private static class MyTestClass implements Serializable {
        private final String someValue;

        MyTestClass(String someValue) {
            this.someValue = someValue;
        }
    }
}
