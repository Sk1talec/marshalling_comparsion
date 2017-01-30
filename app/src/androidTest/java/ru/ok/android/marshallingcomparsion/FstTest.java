package ru.ok.android.marshallingcomparsion;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.nustaq.serialization.FSTConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

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

    public void testSerialMigrationFailure() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("Model");
        Class aClass = ctClass.toClass();

        Assert.assertNotNull(aClass.newInstance());
//        Model model = new Model();
//        model.list.add(10);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        fstConf.encodeToStream(baos, model);
//
//        Class<Model> clazz = Model.class;
//
//        fstConf.setClassLoader(new MyClassLoader());
//        model = (Model) fstConf.decodeFromStream(new ByteArrayInputStream(baos.toByteArray()));
//        Class.forName()
    }

    private class Model {
        private List<Number> list = new ArrayList<>();


    }
}
