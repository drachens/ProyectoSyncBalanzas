package unit;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.DeleteScaleProductService;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteScaleProductServiceTest {
    @Mock
    private SyncDataLoader mockSyncDataLoader;

    @InjectMocks
    private DeleteScaleProductService service;

    @TempDir
    Path tempDir;

    private Scale scale;
    private String filename;
    private Path path;

    @BeforeEach
    public void setUp() throws Exception {
        scale = Scale.builder()
                .store(72)
                .departamento(94)
                .iP_Balanza("10.10.10.10")
                .build();
        filename = "pluDelete_72_94";
        path = tempDir.resolve(filename);
    }

    @Test
    void testDeleteFromScale_archivoExisteEliminacionExitosa() throws Exception {
        //Simula el archivo con contenido
        Files.write(path, List.of("LFCode","1001","1002","1003"));

        //Inyectar path temporal
        Field field = DeleteScaleProductService.class.getDeclaredField("pendings");
        field.setAccessible(true);
        field.set(service, tempDir.toString()+File.separator);

        when(mockSyncDataLoader.deletePLU(anyString(),anyString())).thenReturn(true);

        //Act
        service.deleteFromScale(scale);

        //Assert
        assertFalse(Files.exists(path), "El archivo debería haber sido eliminado");
        verify(mockSyncDataLoader).deletePLU(path.toString(),"10.10.10.10");
    }

    @Test
    void testDeleteFromScale_archivoExisteEliminacionFalla() throws Exception {
        Files.write(path, List.of("LFCode","1001","1002","1003"));
        Field field = DeleteScaleProductService.class.getDeclaredField("pendings");
        field.setAccessible(true);
        field.set(service, tempDir.toString()+File.separator);
        when(mockSyncDataLoader.deletePLU(anyString(),anyString())).thenReturn(false);
        Exception ex = assertThrows(Exception.class, () -> service.deleteFromScale(scale));
        //Asserts
        assertTrue(ex.getMessage().contains("Error durante la eliminación de productos en balanza -> 10.10.10.10"));
    }

    @Test
    void testDeleteFromScale_archivoNoExiste() throws Exception {
        Field field = DeleteScaleProductService.class.getDeclaredField("pendings");
        field.setAccessible(true);
        field.set(service, tempDir.toString()+File.separator);
        FileNotFoundException ex = assertThrows(FileNotFoundException.class, () -> service.deleteFromScale(scale));
        //Asserts
        assertFalse(ex.getMessage().isEmpty());
    }

    @Test
    void testDeleteFromScale_archivoConErrorDeLectura() throws Exception {
        Files.write(path, List.of("LFCode","1001","1002","1003"));

        Field field = DeleteScaleProductService.class.getDeclaredField("pendings");
        field.setAccessible(true);
        field.set(service, tempDir.toString()+File.separator);


        //Spy sobre el servicio para simular error de lectura
        DeleteScaleProductService spyService = Mockito.spy(service);
        doReturn(-1L).when(spyService).contarLineasSinHeader(any());

        when(mockSyncDataLoader.deletePLU(anyString(),anyString())).thenReturn(true);

        //Assert
        assertDoesNotThrow(() -> spyService.deleteFromScale(scale));
        assertFalse(Files.exists(path));
    }

    @Test
    void testDeleteFromScale_errorAlEliminarArchivo() throws Exception {
        //Simula el archivo con contenido
        Files.write(path, List.of("LFCode","1001","1002","1003"));

        // Quitar permisos de escritura para provocar IOException en delete
        path.toFile().setWritable(false);

        //Inyectar path temporal
        Field field = DeleteScaleProductService.class.getDeclaredField("pendings");
        field.setAccessible(true);
        field.set(service, tempDir.toString()+File.separator);

        when(mockSyncDataLoader.deletePLU(anyString(),anyString())).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteFromScale(scale));

        assertTrue(Files.exists(path));
        // Restaurar permisos si planeas borrar el archivo después del test
        path.toFile().setWritable(true);
    }
}
