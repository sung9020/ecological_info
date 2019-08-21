package com.sung.local.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 *
 * @author 123msn
 * @since 2019-08-17
 */
@Slf4j
public class FileUtils {

    public static List<List<String>> readCsv(File file){
        List<List<String>> Data = new ArrayList<>();
        try{
            Files.lines(file.toPath(), Charset.forName("utf-8")).forEach(line ->{
                String[] rows = line.split( ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                Data.add(
                        Arrays.asList(rows).stream().map(str ->{
                            str.replaceAll("\"", "");
                            return str;
                        }).collect(Collectors.toList())
                );
            });
        }catch (IOException e){
            e.printStackTrace();
            log.error("# 파일 읽기 에러");
        }

        return Data;

    }

}
