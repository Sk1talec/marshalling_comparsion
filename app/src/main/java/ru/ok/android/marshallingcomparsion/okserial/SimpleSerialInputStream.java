package ru.ok.android.marshallingcomparsion.okserial;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dmitry.trunin on 24.06.2015.
 */
public class SimpleSerialInputStream extends DataInputStream {
    private SerialCache serialCache = SerialCache.getInstance();

    public SimpleSerialInputStream(InputStream in) {
        super(in);
    }

    public String readString() throws IOException {
        int mark = read();
        if (mark == 0) {
            return null;
        } else if (mark == 1) {
            return readUTF();
        } else if (mark == 2) { // See SimpleSerialOutputStream#writeString() for details.
            int length = readInt();
            byte[] buffer = new byte[length];
            int readLength = read(buffer);
            if (length != readLength) {
                throw new IOException("Expected length(" + length + ") != read length (" + readLength + " )");
            }
            return new String(buffer);

        }
        return null;
    }

    public <T> ArrayList<T> readArrayList() throws IOException {
        ArrayList<T> list = null;
        if (readBoolean()) {
            final int size = readInt();
            list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final T obj = (T) readObject();
                list.add(obj);
            }
        }
        return list;
    }

    public <V, T extends Collection<V>> void readCollection(T outCollection) throws IOException {
        if (readBoolean()) {
            final int size = readInt();
            for (int i = 0; i < size; ++i) {
                //noinspection unchecked
                outCollection.add((V) readObject());
            }
        }
    }

    public <T> void readArrayList(ArrayList<T> outList) throws IOException {
        readArrayList(outList, true /*skipNull*/);
    }

    public <T> void readArrayList(ArrayList<T> outList, boolean skipNulls) throws IOException {
        if (readBoolean()) {
            final int size = readInt();
            outList.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                final T obj = (T) readObject();
                if (obj != null || !skipNulls) {
                    outList.add(obj);
                }
            }
        }
    }

    public ArrayList<String> readStringArrayList() throws IOException {
        ArrayList<String> list = null;
        if (readBoolean()) {
            final int size = readInt();
            list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                list.add(readString());
            }
        }
        return list;
    }

    public void readStringArrayList(ArrayList<String> outList)
            throws IOException {
        if (readBoolean()) {
            final int size = readInt();
            outList.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                outList.add(readString());
            }
        }
    }

    public <T> HashMap<String, T> readStringHashMap() throws IOException {
        HashMap<String, T> map = null;
        if (readBoolean()) {
            final int size = readInt();
            map = new HashMap<>(size * 4 / 3 + 1);
            for (int i = 0; i < size; i++) {
                final String key = readString();
                final T value = (T) readObject();
                map.put(key, value);
            }
        }
        return map;
    }

    public <T> void readStringHashMap(HashMap<String, T> map) throws IOException {
        if (readBoolean()) {
            final int size = readInt();
            for (int i = 0; i < size; i++) {
                final String key = readString();
                final T value = (T) readObject();
                map.put(key, value);
            }
        }
    }

    public <T extends Enum> T readEnum(Class<T> klass) throws IOException {
        if (!readBoolean()) {
            return null;
        }
        final int ordinal = readInt();
        final T[] values = klass.getEnumConstants();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IOException("Ordinal out of range for enum class: "
                    + klass.getName());
        }
        return values[ordinal];
    }

    public <T extends Enum> ArrayList<T> readEnumArrayList(Class<T> klass) throws IOException {
        if (!readBoolean()) {
            return null;
        }
        final int size = readInt();
        final ArrayList<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(readEnum(klass));
        }
        return list;
    }

    public Date readDate() throws IOException {
        if (readBoolean()) {
            return new Date(readLong());
        }
        return null;
    }


    public <T> T readObject() throws IOException {
        int serializerId = readInt();
        Serializer serializer = serialCache.get(serializerId);
        //noinspection unchecked
        return (T) serializer.readObject(this);
    }

}
