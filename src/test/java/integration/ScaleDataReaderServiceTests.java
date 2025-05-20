package integration;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.ScaleDataReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ScaleDataReaderServiceTests {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public ScaleDataReaderService scaleDataReaderService() {
            return new ScaleDataReaderService();
        }
    }

    @Autowired
    private ScaleDataReaderService service;

    @Test
    void shouldDownloadPLUFileFromScale(){

        Scale realScale = new Scale();
        realScale.setId(1);
        realScale.setiP_Balanza("192.168.3.111");
        realScale.setStore(72);
        realScale.setDepartamento(80);
        List<Integer> products = service.getProductFromScale(realScale);

        assertFalse(products.isEmpty(), "La balanza debería tener productos cargados");
        assertEquals(49, products.size());
    }
}
