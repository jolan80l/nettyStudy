package com.jolan;

public class StringStudy {
    public static void main(String[] args) {
        String aaa = "xa x ";
        boolean a=aaa.contains("x");
        System.out.println(a);

        for(int i=0;i<aaa.length();i++){
            char c = aaa.charAt(i);
            if(c=='x'){
                System.out.println(c);
                continue;
            }
            System.out.println("------------");
        }

        int w = aaa.indexOf("x");
        System.out.println(w);

        int x = aaa.lastIndexOf("x");
        System.out.println(x);

        System.out.println(aaa.substring(0,2));

        String[] as = aaa.split("a");
        System.out.println(as);

        for(int i = 0;i<as.length;i++){
            System.out.println(as[i]);
        }

        for(String s : as){
            System.out.println(s);
        }

        String[] s={"s","u"};

        int[] h = {1,2,3};

        int[] h2 = new int[3];
        h2[0]=1;

        boolean x1 = aaa.startsWith("x");


        //aaa=aaa.replace("x", "b");
        System.out.println(aaa);

        System.out.println(aaa.trim());


    }
}
