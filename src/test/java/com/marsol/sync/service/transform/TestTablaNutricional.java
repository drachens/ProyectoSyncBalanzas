package com.marsol.sync.service.transform;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.model.Infonut;
import com.marsol.sync.utils.TablaNutricionalCondition;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TestTablaNutricional {
    @Test
    public void setUp() throws Exception {
        String url = "C:\\Users\\Drach\\Desktop\\MARSOL\\APIWalmart\\jsons_674\\json_infonut_674_98\\json_infonut_674_98";
        Gson gson = new Gson();
        try(FileReader fr = new FileReader(url)){
            Type infonutListType = new TypeToken<List<Infonut>>(){}.getType();
            List<Infonut> infonutList = gson.fromJson(fr, infonutListType);
            Infonut infonut = infonutList.get(0);
            TablaNutricionalCondition.checkCondition(infonut);
        }



    }
}
