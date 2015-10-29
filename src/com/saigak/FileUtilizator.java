package com.saigak;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 27.10.15.
 */
public class FileUtilizator {
    public static String RamadaSenderTemporaryFolder = "RamadaSenderTemporaryFolder/";
    private static List<String> files = new ArrayList<>();

    static {
        File file = new File(FileUtilizator.RamadaSenderTemporaryFolder);
        if(!file.exists()) {
            file.mkdir();
        }
    }

    public static void putFile(String name) {
        files.add(name);
    }

    public static void clean() {
        for (String file : files) {
            new File(RamadaSenderTemporaryFolder + file).delete();
        }
        files.clear();
    }
}
