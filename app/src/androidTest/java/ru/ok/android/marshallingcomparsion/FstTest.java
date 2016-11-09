package ru.ok.android.marshallingcomparsion;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.nustaq.serialization.FSTConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class FstTest extends ApplicationTestCase<Application> {

    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();
    static {
        fstConf.setShareReferences(false);

    }

    public FstTest() {
        super(Application.class);
    }

    public void testSerialException() throws Exception {
        Exception exception = new IllegalArgumentException();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fstConf.encodeToStream(baos, exception);

        Assert.assertTrue(baos.size() != 0);

        Exception newException = (Exception) fstConf.decodeFromStream(new ByteArrayInputStream(baos.toByteArray()));
        Assert.assertEquals(exception.getMessage(), newException.getMessage());
    }
}
