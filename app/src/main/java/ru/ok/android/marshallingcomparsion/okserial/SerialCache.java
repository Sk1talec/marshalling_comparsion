package ru.ok.android.marshallingcomparsion.okserial;

import java.io.IOException;
import java.util.HashMap;

public final class SerialCache {

    private static final SerialCache INSTANCE = new SerialCache();

    private HashMap<Class<?>, Serializer> serializers1 = new HashMap<>();
    private HashMap<Integer, Serializer> serializers2 = new HashMap<>();

    private Serializer resolveSerializer(Object o) throws IOException {
        Marshaller annotation = o.getClass().getAnnotation(Marshaller.class);
        //noinspection TryWithIdenticalCatches
        try {
            Serializer serializer = annotation.value().newInstance();
            serializers1.put(o.getClass(), serializer);
            serializers2.put(serializer.getId(), serializer);
            return serializer;
        } catch (InstantiationException e) {
            throw new IOException(e);
        } catch (IllegalAccessException e) {
            throw new IOException(e);
        }
    }

    public Serializer get(Object o) throws IOException {
        Serializer serializer = serializers1.get(o.getClass());
        if (serializer == null) {
            serializer = resolveSerializer(o);
        }
        return serializer;
    }

    public Serializer get(int serializerId) throws IOException {
        Serializer serializer = serializers2.get(serializerId);
        if (serializer == null) {
            serializer = resolveSerializer2(serializerId);
        }
        return serializer;
    }

    private Serializer resolveSerializer2(int serializerId) throws IOException {
        //noinspection TryWithIdenticalCatches
        throw new IOException();
    }

    public static SerialCache getInstance() {
        return INSTANCE;
    }
}
