package com.marsol.sync.utils;

import com.marsol.sync.model.Notes;
import com.marsol.sync.model.structures.HeadersFilesHPRT;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NoteWriter {
   public String[] header;

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
            System.out.println("Nota escrita en: " + filepath);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }


}
