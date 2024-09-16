import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.marsol.sync.MainClass;
import com.marsol.sync.model.Item;
import com.marsol.sync.service.api.ApiService;
import com.marsol.sync.service.api.AuthService;
import com.marsol.sync.service.api.InfonutService;
import com.marsol.sync.service.api.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainClass.class})
public class ProductFilterTest {
    @Mock
    private ApiService<Item> apiService;

    @Autowired
    private ProductService productService;

    @Value("${wm.endpoint.product.auth}")
    private String authEndpoint;

    @Value("${wm.endpoint.product}")
    private String wmEndpoint;

    @Value("${wm.endpoint.product.user}")
    private String user;

    @Value("${wm.product.credential.pssw}")
    private String pssw;
    @Autowired
    private InfonutService infonutService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(ProductFilterTest.class);
        productService.authEndpoint = authEndpoint;
        productService.wmEndpoint = wmEndpoint;
        productService.user = user;
        productService.pssw = pssw;
    }

    @Test
    public void testProductFilter() {
        String response1 = infonutService.getInfonut(57,93);
        String response2 = productService.getItemsDept(57,93);
        JsonArray filteredArray = new JsonArray();
        Map<Integer,Void> MapReal = new HashMap<>();
        JsonArray jsonArray = JsonParser.parseString(response1).getAsJsonArray();
        for(JsonElement jsonElement : jsonArray) {
            JsonObject item = jsonElement.getAsJsonObject();
            int plu_nbr = item.get("plu_nbr").getAsInt();
            MapReal.put(plu_nbr,null);
        }
        JsonArray jsonArray2 = JsonParser.parseString(response2).getAsJsonArray();
        for(JsonElement jsonElement : jsonArray2) {
            JsonObject item = jsonElement.getAsJsonObject();
            int plu_nbr = item.get("plu_nbr").getAsInt();
            if(MapReal.containsKey(plu_nbr)) {
                filteredArray.add(item);
            }
        }
        System.out.println(jsonArray.size());
        System.out.println(jsonArray2.size());
        System.out.println(filteredArray.size());
        Gson gson = new Gson();
        String filteredJson = gson.toJson(filteredArray);
        System.out.println(filteredJson);
    }
}
