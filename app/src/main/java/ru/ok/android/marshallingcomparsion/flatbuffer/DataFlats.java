// automatically generated, do not modify

package ru.ok.android.marshallingcomparsion.flatbuffer;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class DataFlats extends Table {
  public static DataFlats getRootAsDataFlats(ByteBuffer _bb) { return getRootAsDataFlats(_bb, new DataFlats()); }
  public static DataFlats getRootAsDataFlats(ByteBuffer _bb, DataFlats obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public DataFlats __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public DataFlat datas(int j) { return datas(new DataFlat(), j); }
  public DataFlat datas(DataFlat obj, int j) { int o = __offset(4); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int datasLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }

  public static int createDataFlats(FlatBufferBuilder builder,
      int datas) {
    builder.startObject(1);
    DataFlats.addDatas(builder, datas);
    return DataFlats.endDataFlats(builder);
  }

  public static void startDataFlats(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addDatas(FlatBufferBuilder builder, int datasOffset) { builder.addOffset(0, datasOffset, 0); }
  public static int createDatasVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startDatasVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endDataFlats(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishDataFlatsBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
};

