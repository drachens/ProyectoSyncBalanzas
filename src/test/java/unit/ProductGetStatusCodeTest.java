package unit;

import com.marsol.sync.domain.service.DataExtractionService;
import com.marsol.sync.infraestructure.api.*;
import com.marsol.sync.model.Item;
import org.junit.Before;
import org.junit.Test; // <-- SOLO JUnit 4

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductGetStatusCodeTest {

    private DataExtractionService dataExtractionService;
    private ProductService productServiceMock;
    private InfonutService infonutServiceMock;
    private LayoutService layoutServiceMock;
    private ScaleService scaleServiceMock;

    @Before
    public void setup() {
        // Crear los mocks
        productServiceMock = mock(ProductService.class);
        infonutServiceMock = mock(InfonutService.class);
        layoutServiceMock = mock(LayoutService.class);
        scaleServiceMock = mock(ScaleService.class);

        // Instanciar DataExtractionService con los mocks
        dataExtractionService = new DataExtractionService(
                productServiceMock,
                infonutServiceMock,
                layoutServiceMock,
                scaleServiceMock

        );
    }

    @Test
    public void testGetAutoservicioItemsDept_FiltraSoloActivosConProductoReal() {
        // Mock JSON de Layout
        String layoutJSON = "[{\"plu\":6015}]";

        // Mock JSON de Productos
        String productJSON = "[" +
                "{" +
                "\"id_Items\":72104281," +
                "\"product_nbr\":1349963," +
                "\"store_nbr\":72," +
                "\"dept_nbr\":80," +
                "\"dept_subcatg_nbr\":21938," +
                "\"item_nbr\":104281," +
                "\"plu_nbr\":6015," +
                "\"upc_nbr\":206015000000," +
                "\"item1_desc\":\"JAMON PIERNA AHUMADO\"," +
                "\"brand_id\":17," +
                "\"brand_name\":\"LA CRIANZA\"," +
                "\"sell_uom_code\":\"KG\"," +
                "\"item_type_code\":20," +
                "\"fineline_nbr\":1700," +
                "\"item_status_code\":\"A\"," +
                "\"item_rplnshbl_ind\":\"Y\"," +
                "\"item_ord_eff_date\":\"2022-07-15\"," +
                "\"item_expire_date\":\"2049-12-31\"," +
                "\"cancel_whn_out_ind\":\"N\"," +
                "\"itm_valid_str_ind\":\"Y\"," +
                "\"backrm_scale_ind\":\"Y\"," +
                "\"item_scannable_ind\":\"Y\"," +
                "\"item_Cost\":6589," +
                "\"sell_price\":11560," +
                "\"cust_retail_amt\":11560," +
                "\"stock\":35," +
                "\"codigoTipoEtiqueta\":109," +
                "\"lastUpdateSmart\":\"2025-06-24T15:16:04\"" +
                "}" +
                "]";

        // Configurar mocks
        when(layoutServiceMock.getLayout(72, 80)).thenReturn(layoutJSON);
        when(productServiceMock.getItemsDept(72, 80)).thenReturn(productJSON);

        // Ejecutar método
        List<Item> result = dataExtractionService.getAutoservicioItemsDept(72, 80);

        // Validar resultados
        assertNotNull(result);
        assertEquals(1, result.size());

        Item item = result.get(0);
        assertEquals(6015, (int) item.getPlu_nbr());
        assertEquals("A", item.getItem_status_code());
        assertEquals("JAMON PIERNA AHUMADO", item.getItem1_desc());
        assertEquals("LA CRIANZA", item.getBrand_name());
        assertEquals(11560, item.getSell_price());
    }
}
