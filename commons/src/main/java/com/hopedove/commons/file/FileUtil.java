package com.hopedove.commons.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*
 * 读取各类本地文件
 */
public class FileUtil {

    public static String readASC() {
        StringBuffer xml = new StringBuffer();
        Path path = Paths.get("/Users/liaochente/和度软件/客户.txt");
        try {
            List<String> lines = Files.readAllLines(path, Charset.forName("US-ASCII"));
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml.toString();
    }
//
//    public static void main(String[] args) {
//        readASC();
//    }
}
