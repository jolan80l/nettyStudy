package com.jolan.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {

        //举例，IntBuffer，8种基本类型，除boolean都有
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for(int i = 0 ; i < intBuffer.capacity() ; i++){
            intBuffer.put(i * 2);
        }
        /**
         * flip操作：将buffer转换，读写切换
         * limit = position;
         * position = 0;
         * mark = -1;
         */
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
