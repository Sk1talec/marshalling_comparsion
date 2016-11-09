package ru.ok.android.marshallingcomparsion;

import android.app.Application;

import junit.framework.Assert;

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import ru.ok.android.marshallingcomparsion.model.Node;
import ru.ok.android.marshallingcomparsion.model.NodeFactory;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialInputStream;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialOutputStream;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();

    @Test
    public void testSerialException() throws Exception {
        Exception exception = new IllegalArgumentException();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fstConf.encodeToStream(baos, exception);

        Assert.assertTrue(baos.size() != 0);

        Exception newException = (Exception) fstConf.decodeFromStream(new ByteArrayInputStream(baos.toByteArray()));
        Assert.assertEquals(exception.getMessage(), newException.getMessage());
    }
}