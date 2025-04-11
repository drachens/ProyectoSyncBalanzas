import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.LabelsTransferService;
import org.junit.jupiter.api.Test;

public class LabelsTransferService_Test {

    @Test
    public void testLabelsTransferService() {
        Scale scale = new Scale();
        //scale.setIp_Balanza("192.168.5.178");
        scale.setiP_Balanza("192.168.5.179");
        LabelsTransferService service = new LabelsTransferService();
        service.processLabelForScale(scale);
    }
}
