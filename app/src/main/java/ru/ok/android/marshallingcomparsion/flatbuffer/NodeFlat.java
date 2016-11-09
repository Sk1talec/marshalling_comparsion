// automatically generated, do not modify

package ru.ok.android.marshallingcomparsion.flatbuffer;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class NodeFlat extends Table {
  public static NodeFlat getRootAsNodeFlat(ByteBuffer _bb) { return getRootAsNodeFlat(_bb, new NodeFlat()); }
  public static NodeFlat getRootAsNodeFlat(ByteBuffer _bb, NodeFlat obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public NodeFlat __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int depth() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }
  public boolean mutateDepth(int depth) { int o = __offset(4); if (o != 0) { bb.putInt(o + bb_pos, depth); return true; } else { return false; } }
  public NodeFlat children(int j) { return children(new NodeFlat(), j); }
  public NodeFlat children(NodeFlat obj, int j) { int o = __offset(6); return o != 0 ? obj.__init(__indirect(__vector(o) + j * 4), bb) : null; }
  public int childrenLength() { int o = __offset(6); return o != 0 ? __vector_len(o) : 0; }

  public static int createNodeFlat(FlatBufferBuilder builder,
      int depth,
      int children) {
    builder.startObject(2);
    NodeFlat.addChildren(builder, children);
    NodeFlat.addDepth(builder, depth);
    return NodeFlat.endNodeFlat(builder);
  }

  public static void startNodeFlat(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addDepth(FlatBufferBuilder builder, int depth) { builder.addInt(0, depth, 0); }
  public static void addChildren(FlatBufferBuilder builder, int childrenOffset) { builder.addOffset(1, childrenOffset, 0); }
  public static int createChildrenVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startChildrenVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static int endNodeFlat(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
  public static void finishNodeFlatBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
};

