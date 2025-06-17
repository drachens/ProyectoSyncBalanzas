package integration;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.*;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class DeleteScaleProductServiceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public SyncDataLoader syncDataLoader() {return new SyncDataLoader();}

        @Bean
        public DeleteScaleProductService deleteScaleProductService() {return new DeleteScaleProductService(syncDataLoader());}

        @Bean
        public SyncDataDownloader syncDataDownloader() {return new SyncDataDownloader();}

        @Bean
        public ScaleDataReaderService scaleDataReaderService() {return new ScaleDataReaderService(syncDataDownloader());}

        @Bean
        public WriteDeleteFileService writeDeleteFileService() {return new WriteDeleteFileService();}

        @Bean
        public ProductComparisonService productComparisonService() {return new ProductComparisonService();}
    }

    private static final Logger logger = LoggerFactory.getLogger(DeleteScaleProductServiceTest.class);
    @Autowired
    private DeleteScaleProductService service;
    @Autowired
    private ScaleDataReaderService scaleDataReaderService;
    @Autowired
    private WriteDeleteFileService writeDeleteFileService;
    @Autowired
    private ProductComparisonService productComparisonService;

    private Scale scale,wrongScale;
    private List<Item> serverProducts;


    @BeforeEach
    void setUp() throws Exception {
        /**
         * BALANZAS
         */
        scale = Scale.builder()
                .store(72)
                .departamento(80)
                .iP_Balanza("192.168.2.52")
                .build();
        wrongScale = Scale.builder()
                .iP_Balanza("10.0.0.1")
                .store(72)
                .departamento(80)
                .build();

        /**
         * ITEMS
         */
        Item item1 = Item.builder()
                .plu_nbr(6044)
                .build();
        Item item2 = Item.builder()
                .plu_nbr(6075)
                .build();
        Item item3 = Item.builder()
                .plu_nbr(6077)
                .build();
        Item item4 = Item.builder()
                .plu_nbr(6080)
                .build();

        serverProducts = List.of(item1, item2, item3, item4);
    }

    @Test
    void test_procesoCompletoDeEliminadoExitoso() throws Exception {
        List<Integer> scaleProducts;
        try{
            scaleProducts = scaleDataReaderService.getProductFromScale(scale);
        } catch (Exception e) {
            logger.error(e.getMessage());
            scaleProducts = Collections.emptyList();
        }
        List<Integer> productsToDelete;
        try{
            productsToDelete = productComparisonService.compareProducts(serverProducts, scaleProducts,scale);
        }catch (Exception e) {
            logger.error(e.getMessage());
            productsToDelete = Collections.emptyList();
        }
        try{
            writeDeleteFileService.generateDeleteFile(productsToDelete,scale);
        }catch (Exception e) {
            logger.error(e.getMessage());
        }

        try{
            service.deleteFromScale(scale);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Test
    void test_eliminacionABalanzaInexistente(){
        List<Integer> scaleProducts;
        try{
            scaleProducts = scaleDataReaderService.getProductFromScale(wrongScale);
        } catch (Exception e) {
            logger.error(e.getMessage());
            scaleProducts = Collections.emptyList();
        }
        List<Integer> productsToDelete;
        try{
            productsToDelete = productComparisonService.compareProducts(serverProducts, scaleProducts,scale);
        }catch (Exception e) {
            logger.error(e.getMessage());
            productsToDelete = Collections.emptyList();
        }
        try{
            writeDeleteFileService.generateDeleteFile(productsToDelete,wrongScale);
        }catch (Exception e) {
            logger.error(e.getMessage());
        }

        try{
            service.deleteFromScale(scale);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
