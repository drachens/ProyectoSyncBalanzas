package integration;

import com.marsol.sync.MainClass;
import com.marsol.sync.application.controller.DeleteProductsController;
import com.marsol.sync.domain.model.Scale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = MainClass.class)
@TestPropertySource("classpath:application-test.properties")
public class DeleteProductsControllerTest {

    @Autowired
    DeleteProductsController controller;

    private Scale scale;

    @BeforeEach
    void setUp() {
        scale = Scale.builder()
                .store(72)
                .departamento(94)
                .IP_Balanza("10.107.127.1099")
                .isEsAutoservicio(true)
                .build();
    }

    @Test
    void test(){
        controller.deleteProducts(scale);
    }
}
