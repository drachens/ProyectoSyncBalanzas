package integration;

import com.marsol.sync.MainClass;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = MainClass.class)
@TestPropertySource("classpath:application-test.properties")
public class DeleteScaleProductServiceTest {

    @Autowired
    private DataExtractionService dataExtractionService;

    @Autowired
    private ProductComparisonService productComparisonService;

    @Autowired
    private ScaleDataReaderService scaleDataReaderService;

    @Autowired
    private WriteDeleteFileService writeDeleteFileService;

    @Autowired
    private DeleteScaleProductService service;

    private Scale scale;

    @BeforeEach
    void setUp() throws Exception {
        scale = Scale.builder()
                .store(72)
                .departamento(94)
                .iP_Balanza("10.107.127.109")
                .build();
    }

    @Test
    void test_procesoCompletoDeEliminadoExitoso(){

    }
}
