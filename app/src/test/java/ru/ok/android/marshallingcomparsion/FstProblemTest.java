package ru.ok.android.marshallingcomparsion;

import android.app.Application;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import ru.ok.android.marshallingcomparsion.model.Node;
import ru.ok.android.marshallingcomparsion.model.NodeFactory;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialInputStream;
import ru.ok.android.marshallingcomparsion.okserial.SimpleSerialOutputStream;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class FstProblemTest {
    private static final FSTConfiguration fstConf = FSTConfiguration.createAndroidDefaultConfiguration();
    public static final String MODEL = "Model";
    private CtClass ctClass;
    private ClassPool cp;

    @Before
    public void before() {
        cp = ClassPool.getDefault();
        ctClass = cp.makeClass(MODEL);
    }

    @After
    public void after() {
        ctClass.defrost();
    }

    @Test(expected = InvalidClassException.class)
    public void testJava() throws Exception {
        ctClass.addInterface(cp.makeClass("java.io.Serializable"));
        CtField field = CtField.make("public java.util.List list;", ctClass);
        CtField field2 = CtField.make("private static final long serialVersionUID = 1L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class1 = ctClass.toBytecode();

        ClassLoader clBefore = new MyClassLoader(class1, getClass().getClassLoader());
        Class<?> modelClazz = clBefore.loadClass(MODEL);
        Object model = modelClazz.newInstance();

        Field listField = modelClazz.getDeclaredField("list");
        listField.set(model, new ArrayList<>());
        List<String> list = (List<String>) listField.get(model);
        list.add("FooBar");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(model);

        Assert.assertTrue(baos.size() != 0);
        /**
         * Old model has been written to disk
         */
        ctClass.defrost();
        ctClass.removeField(field);
        ctClass.removeField(field2);
        field = CtField.make("public java.lang.String list;", ctClass);
        field2 = CtField.make("private static final long serialVersionUID = 2L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class2 = ctClass.toBytecode();
        final ClassLoader clAfter = new MyClassLoader(class2, getClass().getClassLoader());

        Thread.currentThread().setContextClassLoader(clAfter);
        model = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())){
            @Override
            protected Class<?> resolveClass(ObjectStreamClass osClass) throws IOException, ClassNotFoundException {
                return clAfter.loadClass(osClass.getName());
            }
        }.readObject();
        listField = clAfter.loadClass(MODEL).getField("list");
        String listStr = (String) listField.get(model);
        Assert.assertTrue(listStr == null);
    }


    @Test()
    public void testSuccess() throws Exception {
        ctClass.addInterface(cp.makeClass("java.io.Serializable"));
        CtField field = CtField.make("public java.util.List list;", ctClass);
        CtField field2 = CtField.make("private static long serialVersionUID = 1L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class1 = ctClass.toBytecode();

        ClassLoader clBefore = new MyClassLoader(class1, getClass().getClassLoader());
        Class<?> modelClazz = clBefore.loadClass(MODEL);
        Object model = modelClazz.newInstance();

        Field listField = modelClazz.getDeclaredField("list");
        listField.set(model, new ArrayList<>());
        List<String> list = (List<String>) listField.get(model);
        list.add("FooBar");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fstConf.encodeToStream(baos, model);

        Assert.assertTrue(baos.size() != 0);
        /**
         * Old model has been written to disk
         */
        ctClass.defrost();
        ctClass.removeField(field);
        ctClass.removeField(field2);
        field = CtField.make("public java.lang.String list;", ctClass);
        field2 = CtField.make("private static long serialVersionUID = 2L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class2 = ctClass.toBytecode();
        ClassLoader clAfter = new MyClassLoader(class2, getClass().getClassLoader());

        fstConf.setClassLoader(clAfter);
        model = fstConf.decodeFromStream(new ByteArrayInputStream(baos.toByteArray()));
        listField = clAfter.loadClass(MODEL).getField("list");
        String listStr = (String) listField.get(model);
        Assert.assertTrue(listStr == null);
    }

    @Test(expected = ClassCastException.class)
    public void testFailure() throws Exception {
        ctClass.addInterface(cp.makeClass("java.io.Serializable"));
        CtField field = CtField.make("public java.util.List list;", ctClass);
        CtField field2 = CtField.make("private static long serialVersionUID = 1L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class1 = ctClass.toBytecode();

        ClassLoader clBefore = new MyClassLoader(class1, getClass().getClassLoader());
        Class<?> modelClazz = clBefore.loadClass(MODEL);
        Object model = modelClazz.newInstance();

        Field listField = modelClazz.getDeclaredField("list");
        listField.set(model, new ArrayList<>());
        List<String> list = (List<String>) listField.get(model);
        list.add("FooBar");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fstConf.encodeToStream(baos, model);

        Assert.assertTrue(baos.size() != 0);
        /**
         * Old model has been written to disk
         */
        ctClass.defrost();
        ctClass.removeField(field);
        ctClass.removeField(field2);
        field = CtField.make("public java.lang.String list;", ctClass);
        field2 = CtField.make("private static long serialVersionUID = 2L;", ctClass);
        ctClass.addField(field);
        ctClass.addField(field2);
        byte[] class2 = ctClass.toBytecode();
        ClassLoader clAfter = new MyClassLoader(class2, getClass().getClassLoader());

        fstConf.setClassLoader(clAfter);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        model = fstConf.decodeFromStream(bais);
        listField = clAfter.loadClass(MODEL).getField("list");
        String listStr = (String) listField.get(model);
        Assert.assertTrue(listStr == null);
    }

    @Test
    public void testClassGenerator() throws Exception {
        ctClass.addInterface(cp.makeClass("java.io.Serializable"));
        CtField field = CtField.make("public java.util.List list;", ctClass);
        ctClass.addField(field);
        byte[] class1 = ctClass.toBytecode();

        ClassLoader clBefore = new MyClassLoader(class1, getClass().getClassLoader());
        Class<?> modelClazz = clBefore.loadClass(MODEL);
        Object model = modelClazz.newInstance();

        Field listField = modelClazz.getDeclaredField("list");
        listField.set(model, new ArrayList<>());
        List<String> list = (List<String>) listField.get(model);
        list.add("FooBar");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fstConf.encodeToStream(baos, model);

        Assert.assertTrue(baos.size() != 0);

        fstConf.setClassLoader(clBefore);
        model = fstConf.decodeFromStream(new ByteArrayInputStream(baos.toByteArray()));
        listField = model.getClass().getDeclaredField("list");
        list = (List<String>) listField.get(model);
        Assert.assertEquals(list.get(0), "FooBar");
    }

    private class MyClassLoader extends ClassLoader {
        public static final String CLASS_NAME = "Model";
        private final Class clazz;

        public MyClassLoader(byte[] class1, ClassLoader classLoader) {
            super(classLoader);
            clazz = defineClass(CLASS_NAME, class1, 0, class1.length);
        }

        @Override
        protected Class<?> findClass(String className) throws ClassNotFoundException {
            if (className.equals(CLASS_NAME)) {
                return clazz;
            }
            return super.findClass(className);
        }

        @Override
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            if (className.equals(CLASS_NAME)) {
                return clazz;
            }
            return super.loadClass(className);
        }
    }
}