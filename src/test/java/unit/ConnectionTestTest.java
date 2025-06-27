package unit;

import com.marsol.sync.domain.service.WriteDeleteFileService;
import com.marsol.sync.utils.ConnectionTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ConnectionTestTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        public ConnectionTest connectionTest() {return new ConnectionTest();}
    }
    @Autowired
    ConnectionTest connTest;


    @Test
    public void testConnection() throws IOException {
        connTest.sendPingRequest("192.168.2.95");
    }
}
