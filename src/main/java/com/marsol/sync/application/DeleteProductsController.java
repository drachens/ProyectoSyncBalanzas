package com.marsol.sync.application;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.*;
import com.marsol.sync.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DeleteProductsController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductsController.class);

    private final DataExtractionService dataExtractionService;
    private final DeleteScaleProductService deleteScaleProductService;
    private final ProductComparisonService productComparisonService;
    private final ScaleDataReaderService scaleDataReaderService;
    private final WriteDeleteFileService writeDeleteFileService;

    @Autowired
    public DeleteProductsController(DataExtractionService dataExtractionService,
                                     DeleteScaleProductService deleteScaleProductService,
                                     ProductComparisonService productComparisonService,
                                     ScaleDataReaderService scaleDataReaderService,
                                     WriteDeleteFileService writeDeleteFileService){
        this.dataExtractionService = dataExtractionService;
        this.deleteScaleProductService = deleteScaleProductService;
        this.productComparisonService = productComparisonService;
        this.scaleDataReaderService = scaleDataReaderService;
        this.writeDeleteFileService = writeDeleteFileService;
    }

    public void deleteProducts(Scale scale){
        logger.info("Comenzando proceso de eliminación de productos balanza -> {}",scale.getiP_Balanza());
        List<Item> serverProducts = dataExtractionService.getItems(scale.getStore(),scale.getDepartamento(),scale.isEsAutoservicio());
        List<Integer> scaleProducts;
        List<Integer> productsToDelete;
        try{
            scaleProducts = scaleDataReaderService.getProductFromScale(scale);
        }catch (Exception e){
            scaleProducts = Collections.emptyList();
            logger.error("{}",e.getMessage());
        }
        try{
            productsToDelete = productComparisonService.compareProducts(serverProducts, scaleProducts,scale);
        }catch (Exception e){
            productsToDelete = Collections.emptyList();
            logger.error("{}",e.getMessage());
        }
        try{
            writeDeleteFileService.generateDeleteFile(productsToDelete,scale);
        }catch (Exception e){
            logger.error("{}",e.getMessage());
        }

        try{
            deleteScaleProductService.deleteFromScale(scale);
        }catch (Exception e){
            logger.error("{}",e.getMessage());
        }
    }

    public void deleteProducts2(Scale scale){
        logger.info("Comenzando proceso de eliminación2 de productos balanza -> {}",scale.getiP_Balanza());

        //antes necesito pasarle el archivo, por lo que debo generarlo arriba
        deleteScaleProductService.deleteFromScale2(scale);

    }

}

