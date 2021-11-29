package com.jolan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Collection {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        List<List<Integer>> list2 = new LinkedList();

        for(int i=0;i<10;i++){
            List<Integer> list3 = new LinkedList();
            for(int j=0;j<10;j++){
                list3.add(j);
            }
            list2.add(list3);
        }

        for(int i=0;i<10;i++){
            list1.add(i+"");
        }

        System.out.println(list1.size());
        list1.set(2,"zhangsan");
        for (String s:list1) {
            System.out.print(s);
        }
        System.out.println();
        Object[] objects = list1.toArray();
        String s="";
        for(Object o:objects){
            s=s+","+(String) o;

        }
        //s = null;
        if(s!=null&&s.startsWith(",")){
            s=s.replaceFirst(",","");
        }

        System.out.print(s);
        System.out.println();


    }
}
