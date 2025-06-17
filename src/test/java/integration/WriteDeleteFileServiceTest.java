package integration;

import com.marsol.sync.MainClass;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.DataExtractionService;
import com.marsol.sync.domain.service.ProductComparisonService;
import com.marsol.sync.domain.service.ScaleDataReaderService;
import com.marsol.sync.domain.service.WriteDeleteFileService;
import com.marsol.sync.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MainClass.class)
@TestPropertySource("classpath:application-test.properties")
public class WriteDeleteFileServiceTest {

    @Autowired
    private DataExtractionService dataExtractionService;

    @Autowired
    private ProductComparisonService productComparisonService;

    @Autowired
    private ScaleDataReaderService scaleDataReaderService;

    @Autowired
    private WriteDeleteFileService service;

    private Scale scale;
    private int store;
    private int dept;
    private List<Item> serverProduct;

    private String pathFile = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\pendings\\pluDelete_72_94";
    @BeforeEach
    void setUp() throws Exception {
        scale = Scale.builder()
                .iP_Balanza("10.107.127.109")
                .store(72)
                .departamento(94)
                .build();
        store = scale.getStore();
        dept = scale.getDepartamento();

        serverProduct = dataExtractionService.getItems(store,dept,true);
    }

    @Test
    void test_existingListProductToDelete() throws Exception {
        List<Integer> scaleProdut = scaleDataReaderService.getProductFromScale(scale);
        List<Integer> deleteProducts = productComparisonService.compareProducts(serverProduct,scaleProdut,scale);
        service.generateDeleteFile(deleteProducts,scale);
        Path path = Paths.get(pathFile);
        assertTrue(Files.exists(path));
    }

    @Test
    void test_emptyListOfProductToDelete() throws Exception {

        Scale scale2 = Scale.builder()
                .store(72)
                .departamento(80)
                .iP_Balanza("10.107.127.120")
                .build();

        List<Integer> scaleProduct = Collections.emptyList();
        List<Item> serverProduct2 = dataExtractionService.getItems(scale2.getStore(),scale2.getDepartamento(),false);
        List<Integer> deleteProducts = productComparisonService.compareProducts(serverProduct2,scaleProduct,scale);

        String pathFile2 = "C:\\Users\\sistemas\\Desktop\\MARSOL\\HPRT\\pendings\\pluDelete_72_80";

        Path path = Paths.get(pathFile2);

        service.generateDeleteFile(deleteProducts,scale2);
        assertFalse(Files.exists(path));
    }
}
