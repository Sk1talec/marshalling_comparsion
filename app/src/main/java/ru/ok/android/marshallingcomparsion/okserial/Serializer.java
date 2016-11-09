package ru.ok.android.marshallingcomparsion.okserial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T> {
    void writeObject(SimpleSerialOutputStream out, T o) throws IOException;

    T readObject(SimpleSerialInputStream in) throws IOException;

    int getId();
}
