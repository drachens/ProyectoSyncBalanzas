package com.marsol.sync.service.io;

import com.marsol.sync.model.Notes;
import com.marsol.sync.model.structures.HeadersFilesHPRT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NoteWriter {
   public String[] header;

   public static void writeNote(String filepath, Map<Integer,String> rows){
       String[] header = HeadersFilesHPRT.NoteHeader;
       try(BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))){
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
