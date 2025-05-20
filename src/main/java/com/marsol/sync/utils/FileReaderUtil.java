package com.marsol.sync.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileReaderUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileReaderUtil.class);
    public static <T> List<T> readFileAndMap(String filename, Function<String[], T> mapper) {
        List<T> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split("\t");
                if (values.length > 0) {
                    try {
                        T item = mapper.apply(values);
                        result.add(item);
                    } catch (Exception e) {
                        logger.debug("Error procesando linea {}",e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el archivo: " + filename, e);
        }
        return result;
    }
}
