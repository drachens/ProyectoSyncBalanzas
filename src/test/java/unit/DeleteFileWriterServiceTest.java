package unit;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.DeleteFileWriterService;
import com.marsol.sync.domain.service.ScaleDataReaderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class DeleteFileWriterServiceTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        public DeleteFileWriterService deleteFileWriterService() {return new DeleteFileWriterService();}
    }
    @Autowired
    private DeleteFileWriterService service;

    private Scale dummyScale;
    private Scale dummyScale2;

    @Value("${directory.pendings}")
    private String pendingsDir;

    @BeforeEach
    public void setUp() throws IOException {
        dummyScale = Scale.builder()
                .departamento(94)
                .store(72)
                .build();
        Path testDir = Paths.get(pendingsDir);
        Files.createDirectories(testDir);
        dummyScale2 = Scale.builder()
                .departamento(98)
                .store(72)
                .build();
    }

    /**
    @AfterEach
    public void cleanUp() throws IOException {
        Path testDir = Paths.get(pendingsDir);
        if(Files.exists(testDir)) {
            Files.walk(testDir)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
     */

    @Test
    void testGenerarArchivoDeEliminacionCorrectamente() throws IOException {
        List<Integer> productsToDelete = List.of(1001,1002,1003,1004,1005,1006,1007);
        service.generateDeleteFile(productsToDelete, dummyScale);

        String expectedFilename = "pluDelete_72_94";
        Path filePath = Paths.get(pendingsDir, expectedFilename);

        assertTrue(Files.exists(filePath));
        List<String> lines = Files.readAllLines(filePath);
        assertFalse(lines.isEmpty());
    }

    /**
     * Lista de productos a eliminar vacia
     */
    @Test
    void testGenerarArchivoEliminacionProductosVacios() throws IOException {
        List<Integer> productsToDelete = Collections.emptyList();
        service.generateDeleteFile(productsToDelete, dummyScale2);

        String expectedFilename = "pluDelete_72_98";
        Path filePath = Paths.get(pendingsDir, expectedFilename);

        assertFalse(Files.exists(filePath));
    }
}
