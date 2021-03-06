package com.asiainfo.util;

import com.asiainfo.codis.schema.CodisHash;
import com.asiainfo.codis.schema.DataSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class ExternalDataLoader {
    private static Logger logger = Logger.getLogger(ExternalDataLoader.class);

    public static List<CodisHash> loadSchema(String filePath) {
        List<CodisHash> result = new ArrayList();


        Gson gson = new GsonBuilder().create();

        try {
            result = gson.fromJson(FileUtils.readFileToString(new File(filePath), "UTF-8"), new TypeToken<List<CodisHash>>() {}.getType());
        } catch (IOException e) {
            logger.error(e);
        }


        return result;
    }


    private static DataSchema generateSchema(String key, String value) {
        DataSchema dataSchema = new DataSchema(key);
        String tablePrimaryKey = "";
        int tablePrimaryKeyIndex = -1;

        String[] headers = StringUtils.splitByWholeSeparator(value, ",");//TODO parameterize ","

        for (int i = 0; i < headers.length; i++) {
            if (StringUtils.startsWith(headers[i], "*")) {//TODO parameterize "*"

                tablePrimaryKey = StringUtils.substringAfter(headers[i], "*");
                headers[i] = tablePrimaryKey;
                tablePrimaryKeyIndex = i;
                break;
            }
        }

        if (StringUtils.isEmpty(tablePrimaryKey)) {
            logger.error("Can not find primary key from " + key);
        }

        dataSchema.setTablePrimaryKey(tablePrimaryKey).setHeader(headers).setTablePrimaryKeyIndex(tablePrimaryKeyIndex);

        return dataSchema;
    }


    public static void loadBigFile(String filePath) {
        LineIterator it = null;

        try {
            it = FileUtils.lineIterator(new File(filePath), "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    void largeFileIO(String inputFile, String outputFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);// 10M缓存
            FileWriter fw = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                fw.append(line + " ");
            }
            in.close();
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //largeFileIO("/Users/peng/tmp/mv.log");


        List<String> lines = FileUtils.readLines(new File("/Users/peng/SandBox/Dev/Stream/CodisImport/conf/schema.properties"), "UTF-8");

        for (String ling : lines) {
            System.out.println(ling + "===");
        }

    }
}
