import com.marsol.sync.service.images.Transfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransferTest {

    @Test
    public void testTransfer() {
        String targetUrl = "http://192.168.2.33:5000/upload";
        String imagePath = "C:\\Users\\sistemas\\Desktop\\imagenes\\PLU_94_00001.jpg";
        Transfer.uploadImage(targetUrl,imagePath);
    }
}
