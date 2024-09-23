package com.marsol.sync.utils;

import com.marsol.sync.controller.ScalesNetworkController;
import com.marsol.sync.model.Notes;
import com.marsol.sync.model.structures.HeadersFilesHPRT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NoteWriter {
   public String[] header;
    private static final Logger logger = LoggerFactory.getLogger(NoteWriter.class);

   public static void writeNote(String filepath, Map<Integer,String> rows){
       String[] header = HeadersFilesHPRT.NoteHeader;
       try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),StandardCharsets.UTF_8))){
            writer.write(String.join("\t", header));
            writer.newLine();
            for(Map.Entry<Integer,String> entry : rows.entrySet()){
                Notes nota = new Notes(entry.getKey(), entry.getValue());
                writer.write(nota.toString());
                writer.newLine();
            }
            logger.info("Nota escrita en: {}",filepath);
       } catch (IOException e) {
           logger.error("Error al escribir el archivo: {} {}",filepath,e.getMessage());
       }
   }


}
