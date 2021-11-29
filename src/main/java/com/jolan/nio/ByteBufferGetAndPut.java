package com.jolan.nio;

import java.nio.ByteBuffer;

public class ByteBufferGetAndPut {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('龙');
        byteBuffer.putShort((short) 4);

        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}
