package integration;

import com.marsol.sync.MainClass;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.DataExtractionService;
import com.marsol.sync.domain.service.ProductComparisonService;
import com.marsol.sync.domain.service.ScaleDataReaderService;
import com.marsol.sync.domain.service.WriteDeleteFileService;
import com.marsol.sync.model.Item;
import com.marsol.sync.utils.ConnectionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = MainClass.class)
@TestPropertySource("classpath:application-test.properties")
public class ProductComparisonServiceTest {
    @Autowired
    private DataExtractionService dataExtractionService;

    @Autowired
    private ProductComparisonService service;

    @Autowired
    private ScaleDataReaderService scaleDataReaderService;

    private Scale scale;

    @BeforeEach
    void setUp() {
        scale = Scale.builder()
                .store(72)
                .departamento(94)
                .iP_Balanza("10.107.127.109")
                .build();
    }

    @Test
    void test_compareServerProductsWithScaleProducts() throws IOException {
        List<Integer> scaleProducts;
        boolean ping = ConnectionTest.sendPingRequest(scale.getIp_Balanza());
        if(!ping){
            System.out.println("PING ERROR");
            return;
        }
        List<Item> serverProducts = dataExtractionService.getItems(72,94, true);
        try{
            scaleProducts = scaleDataReaderService.getProductFromScale(scale);
        }catch (Exception e){
            scaleProducts = Collections.emptyList();
            e.printStackTrace();
        }

        List<Integer> filteredProducts = service.compareProducts(serverProducts, scaleProducts);

        System.out.println(filteredProducts);
    }

    @Test
    void test_compareServerProductsWithScaleNonProducts(){
        List<Integer> scaleProducts = Collections.emptyList();
        List<Item> serverProducts = dataExtractionService.getItems(72,94, false);
        List<Integer> filteredProducts = service.compareProducts(serverProducts, scaleProducts);
        System.out.println(filteredProducts);
    }

    @Test
    void showItems(){
        List<Item> serverProducts = dataExtractionService.getItems(72,94, true);
        for(Item item : serverProducts){
            System.out.println(item.getPlu_nbr());
        }
    }
}
