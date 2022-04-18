package cn.edu.bistu.cs.ir.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean isDirExists(String parent, String... names){
        File f = Paths.get(parent, names).toFile();
        return f.isDirectory()&&f.exists();
    }
}
