package unit;

import com.marsol.sync.domain.service.ProductComparisonService;
import com.marsol.sync.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductComparisonServiceTest {

    private ProductComparisonService service;

    @BeforeEach
    void setUp() {
        service = new ProductComparisonService();
    }

    /**
     * Test de productos en balanza pero no en servidor
     */
    @Test
    void testProductosParaEliminar(){
        Item item1 = Item.builder()
                .plu_nbr(1001)
                .build();
        Item item2 = Item.builder()
                .plu_nbr(1002)
                .build();
        Item item3 = Item.builder()
                .plu_nbr(1003)
                .build();
        List<Item> serverProducts = Arrays.asList(item1, item2, item3);
        List<Integer> scaleProducts = Arrays.asList(1001, 1002, 1003, 1004, 1005); //1004 y 1005 no estan en el servidor
        List<Integer> productosAEliminar = service.compareProducts(serverProducts, scaleProducts);
        assertEquals(2, productosAEliminar.size());
        assertTrue(productosAEliminar.contains(1004));
        assertTrue(productosAEliminar.contains(1005));
    }
    /**
     * Test de lista vacía si no hay diferencias
     */
    @Test
    void testSinProductosAEliminar(){
        Item item1 = Item.builder()
                .plu_nbr(1001)
                .build();
        Item item2 = Item.builder()
                .plu_nbr(1002)
                .build();
        List<Item> serverProducts = Arrays.asList(item1, item2);
        List<Integer> scaleProducts = Arrays.asList(1001,1002);
        List<Integer> productosAEliminar = service.compareProducts(serverProducts, scaleProducts);
        assertTrue(productosAEliminar.isEmpty());
    }
    /**
     * Test de manejo de null en inputs
     */
    @Test
    void testServerProductsIsNull(){
        List<Integer> scaleProducts = Arrays.asList(1001,1002);
        List<Integer> productosAEliminar = service.compareProducts(null, scaleProducts);
        assertTrue(productosAEliminar.isEmpty());
    }
    @Test
    void testScaleProductsIsNull(){
        Item item1 = Item.builder()
                .plu_nbr(1001)
                .build();
        Item item2 = Item.builder()
                .plu_nbr(1002)
                .build();
        List<Item> serverProducts = Arrays.asList(item1, item2);
        List<Integer> productsAEliminar = service.compareProducts(serverProducts, null);
        assertTrue(productsAEliminar.isEmpty());
    }
    /**
     * Test de lista con null interno
     */
    @Test
    void testConItemNullEnServerProducts(){
        Item item2 = Item.builder()
                .plu_nbr(1002)
                .build();
        List<Item> serverProducts = Arrays.asList(null, item2);
        List<Integer> scaleProducts = Arrays.asList(1001, 1002);
        List<Integer> productosAEliminar = service.compareProducts(serverProducts, scaleProducts);
        assertTrue(productosAEliminar.isEmpty());
    }
}
