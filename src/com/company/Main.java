package com.company;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Index idx = new Index("./src/data/");
        try{
        File file = new File("./src/data/0_2.txt");
        idx.indexFile(file);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
