package com.company;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Index idx = new Index("./src/data/");
        idx.serialIndexing();
    }
}
