package com.jolan.nio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可以让文件直接在内存中修改，操作系统不需要再拷贝一次
 *
 */
public class MappedByteBuffer {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("raf.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 参数1：读写模式
         * 参数2：可以修改的起始位置
         * 参数3：映射到内存的大小，即可以将文件的多少个字节映射到内存，也就是可以直接修改的范围
         */
        java.nio.MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        //第0个位置修改成H，第3个位置修改成9
        map.put(0, (byte)'H');
        map.put(3, (byte)'9');

        randomAccessFile.close();

        System.out.println("修改成功");
    }
}
