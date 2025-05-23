package com.marsol.sync.application.controller;

import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.*;
import com.marsol.sync.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
public class DeleteProductsController {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProductsController.class);

    private final DataExtractionService dataExtractionService;
    private final DeleteScaleProductService deleteScaleProductService;
    private final ProductComparisonService productComparisonService;
    private final ScaleDataReaderService scaleDataReaderService;
    private final WriteDeleteFileService writeDeleteFileService;

    @Value("${directory.pendings}")
    private String directoryPendings;
    @Value("${directory.uploads}")
    private String directoryUploads;

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

        logger.info("Comenzando proceso de eliminación de productos balanza -> {}",scale.getIP_Balanza());
        List<Item> serverProducts = dataExtractionService.getItems(scale.getStore(),scale.getDepartamento(),scale.isEsAutoservicio());
        List<Integer> scaleProducts;
        try{
            scaleProducts = scaleDataReaderService.getProductFromScale(scale);
        }catch (Exception e){
            scaleProducts = Collections.emptyList();
        }
        List<Integer> productsToDelete = productComparisonService.compareProducts(serverProducts, scaleProducts);
        writeDeleteFileService.generateDeleteFile(productsToDelete,scale);
    }
}
