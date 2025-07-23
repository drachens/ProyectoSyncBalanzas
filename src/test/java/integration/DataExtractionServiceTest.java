package integration;

import com.marsol.sync.MainClass;
import com.marsol.sync.domain.service.DataExtractionService;
import com.marsol.sync.infraestructure.api.*;
import com.marsol.sync.model.Advertising;
import com.marsol.sync.model.Item;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootTest(classes = MainClass.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class DataExtractionServiceTest {

    @Nested
    @ContextConfiguration(classes = DataExtractionService.class)
    class TestConfiguration {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public AuthService authService() {
            return new AuthService(restTemplate());
        }

        @Bean
        public ApiService apiService() {
            return new ApiService(restTemplate(), authService());
        }

        @Bean
        public InfonutService infonutService() {
            return new InfonutService(apiService());
        }

        @Bean
        public ProductService productService() {
            return new ProductService(apiService(), infonutService());
        }

        @Bean
        public LayoutService layoutService() {
            return new LayoutService(restTemplate());
        }

        @Bean
        public ScaleService scaleService() {
            return new ScaleService(apiService(), restTemplate());
        }


        @Bean
        public DataExtractionService dataExtractionService() {
            return new DataExtractionService(productService(), infonutService(), layoutService(), scaleService());
        }
    }
        @Autowired
        private DataExtractionService service;

        @Test
        void test_getItemsDept(){
            List<Item> items = service.getItemsDept(72,94);
            int count = items.size();
            System.out.println("Total productos: "+count);
        }

        @Test
        void test_getAutoservicioItemsDept(){
            List<Item> items = service.getAutoservicioItemsDept(57,94);
            int count = items.size();
            System.out.println("Total productos: "+count);
        }

        @Test
        void test_getItemsNewFunctionAutoservicio(){
            List<Item> items = service.getItems(72,94,true);
            int count = items.size();
            System.out.println("Total productos: "+count);
        }

        @Test
        void test_getItemsNewFunctionNoAutoservicio(){
            List<Item> items = service.getItems(72,94,false);
            int count = items.size();
            System.out.println("Total productos: "+count);
        }


}
