import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.ModelTransferService;
import org.junit.jupiter.api.Test;

public class ModelTransferService_Test {

    @Test
    public void test() {
        Scale scale = new Scale();
        scale.setiP_Balanza("192.168.5.178");
        ModelTransferService service = new ModelTransferService();
        service.transferModelForScale(scale);
    }
}
