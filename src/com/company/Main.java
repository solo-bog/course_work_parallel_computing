package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String inputs = "film,very,love";
        Index idx = new Index();
        idx.serialIndexing("./src/data/");
        idx.clear();
        idx.parallelIndexing("./src/data/",2);
        idx.search(Arrays.asList(inputs.split(",")));
    }
}
