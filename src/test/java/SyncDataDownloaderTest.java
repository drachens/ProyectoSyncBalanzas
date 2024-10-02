import com.marsol.sync.MainClass;
import com.marsol.sync.service.communication.SyncDataDownloader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SyncDataDownloaderTest {

    @Test
    public void testDownloadParameters(){
        SyncDataDownloader sync = new SyncDataDownloader();
        sync.downloadSystemParameters("testParameters","10.105.197.125");
        //sync.downloadCustomBarcode("testCustomBarcode","10.105.197.125");
    }
}
