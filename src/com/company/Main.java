package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String inputs = "problem,very";
        Index idx = new Index("./src/data/");
        idx.serialIndexing();
        idx.search(Arrays.asList(inputs.split(",")));
    }
}
