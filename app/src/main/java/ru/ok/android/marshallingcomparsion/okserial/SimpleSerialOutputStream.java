package ru.ok.android.marshallingcomparsion.okserial;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmitry.trunin on 24.06.2015.
 */
public class SimpleSerialOutputStream extends DataOutputStream {

    private SerialCache serialCache = SerialCache.getInstance();

    public SimpleSerialOutputStream(OutputStream out) {
        super(out);
    }

    public void writeString(String s) throws IOException {
        if (s == null) {
            write(0);
        } else {
            // Workaround for bug https://jira.odkl.ru/browse/ANDROID-7507
            // In short DataOutputStream#writeUTF can't work with strings longer that 2^16
            if (s.length() < 65535) {
                write(1);
                writeUTF(s);
            } else {
                write(2);
                byte[] bytes = s.getBytes();
                writeInt(bytes.length);
                write(bytes);
            }
        }
    }

    public void writeCollection(Collection collection) throws IOException {
        writeBoolean(collection != null);
        if (collection != null) {
            final int size = collection.size();
            writeInt(size);
            for (Object obj : collection) {
                writeObject(obj);
            }
        }
    }

    public void writeStringList(List<String> list) throws IOException {
        writeBoolean(list != null);
        if (list != null) {
            final int size = list.size();
            writeInt(size);
            for (int i = 0; i < size; i++) {
                writeString(list.get(i));
            }
        }
    }

    public void writeStringMap(Map<String, ?> map) throws IOException {
        writeBoolean(map != null);
        if (map != null) {
            final int size = map.size();
            writeInt(size);
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                writeString(entry.getKey());
                writeObject(entry.getValue());
            }
        }
    }

    public <T extends Enum> void writeEnum(T value) throws IOException {
        writeBoolean(value != null);
        if (value != null) {
            writeInt(value.ordinal());
        }
    }

    public <T extends  Enum> void writeEnumList(List<T> list) throws IOException {
        writeBoolean(list != null);
        if (list != null) {
            final int size = list.size();
            writeInt(size);
            for (int i = 0; i < size; i++) {
                writeEnum(list.get(i));
            }
        }
    }

    public void writeDate(Date date) throws IOException {
        writeBoolean(date != null);
        if (date != null) {
            writeLong(date.getTime());
        }
    }

    public void writeObject(Object o) throws IOException {
        Serializer serializer = serialCache.get(o);
        writeInt(serializer.getId());
        //noinspection unchecked
        serializer.writeObject(this, o);
    }

}
