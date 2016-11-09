// automatically generated, do not modify

package ru.ok.android.marshallingcomparsion.flatbuffer;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class DataFlat extends Table {
  public static DataFlat getRootAsDataFlat(ByteBuffer _bb) { return getRootAsDataFlat(_bb, new DataFlat()); }
  public static DataFlat getRootAsDataFlat(ByteBuffer _bb, DataFlat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public DataFlat __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int i1() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public boolean mutateI1(int i1) { int o = __offset(4); if (o != 0) { bb.putInt(o + bb_pos, i1); return true; } else { return false; } }
  public int i2() { int o = __offset(6); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public boolean mutateI2(int i2) { int o = __offset(6); if (o != 0) { bb.putInt(o + bb_pos, i2); return true; } else { return false; } }
  public String s1() { int o = __offset(8); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer s1AsByteBuffer() { return __vector_as_bytebuffer(8, 1); }
  public String s2() { int o = __offset(10); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer s2AsByteBuffer() { return __vector_as_bytebuffer(10, 1); }
  public boolean b1() { int o = __offset(12); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean mutateB1(boolean b1) { int o = __offset(12); if (o != 0) { bb.put(o + bb_pos, (byte)(b1 ? 1 : 0)); return true; } else { return false; } }
  public boolean b2() { int o = __offset(14); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }
  public boolean mutateB2(boolean b2) { int o = __offset(14); if (o != 0) { bb.put(o + bb_pos, (byte)(b2 ? 1 : 0)); return true; } else { return false; } }
  public NodeFlat node() { return node(new NodeFlat()); }
  public NodeFlat node(NodeFlat obj) { int o = __offset(16); return o != 0 ? obj.__init(__indirect(o + bb_pos), bb) : null; }

  public static int createDataFlat(FlatBufferBuilder builder,
      int i1,
      int i2,
      int s1,
      int s2,
      boolean b1,
      boolean b2,
      int node) {
    builder.startObject(7);
    DataFlat.addNode(builder, node);
    DataFlat.addS2(builder, s2);
    DataFlat.addS1(builder, s1);
    DataFlat.addI2(builder, i2);
    DataFlat.addI1(builder, i1);
    DataFlat.addB2(builder, b2);
    DataFlat.addB1(builder, b1);
    return DataFlat.endDataFlat(builder);
  }

  public static void startDataFlat(FlatBufferBuilder builder) { builder.startObject(7); }
  public static void addI1(FlatBufferBuilder builder, int i1) { builder.addInt(0, i1, 0); }
  public static void addI2(FlatBufferBuilder builder, int i2) { builder.addInt(1, i2, 0); }
  public static void addS1(FlatBufferBuilder builder, int s1Offset) { builder.addOffset(2, s1Offset, 0); }
  public static void addS2(FlatBufferBuilder builder, int s2Offset) { builder.addOffset(3, s2Offset, 0); }
  public static void addB1(FlatBufferBuilder builder, boolean b1) { builder.addBoolean(4, b1, false); }
  public static void addB2(FlatBufferBuilder builder, boolean b2) { builder.addBoolean(5, b2, false); }
  public static void addNode(FlatBufferBuilder builder, int nodeOffset) { builder.addOffset(6, nodeOffset, 0); }
  public static int endDataFlat(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

