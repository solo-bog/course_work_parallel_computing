package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try{
            if(args.length < 2) throw new Exception("Enter data directory path and searched words");
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        Index idx = new Index();
        idx.serialIndexing(args[0]);
        idx.search(Arrays.asList(args[1].split(",")));
        idx.clear();
        idx.parallelIndexing(args[0],4);
        idx.search(Arrays.asList(args[1].split(",")));
    }
}
