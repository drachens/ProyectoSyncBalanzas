import com.google.gson.Gson;
import com.marsol.sync.model.Log;
import com.marsol.sync.service.api.LogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class LogsTest {

    private String dateTimeFormatter = "yyyy-MM-dd HH:mm:ss";

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private LogService logService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogs() {
        //HORA
        LocalDateTime nowMinus2Hours = LocalDateTime.now().minusHours(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatter);
        String dateTimeFormated = nowMinus2Hours.format(formatter);

        int store = 72;
        int deptartamento = 93;
        String accionBalanza = "Carga de productos";
        int cantidadCambios = 44;
        String ipBalanza = "10.105.197.125";
        String fechaHora = dateTimeFormated;
        String resultado = "LOL";

        Log log = new Log(
                0,
                store,
                deptartamento,
                accionBalanza,
                cantidadCambios,
                ipBalanza,
                fechaHora,
                resultado
        );
        logService.createLog(log);
    }
}
