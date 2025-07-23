package com.marsol.sync.web.services;

import com.marsol.sync.application.DeleteProductsController;
import com.marsol.sync.application.LabelTransfererController;
import com.marsol.sync.application.SystemParametersController;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.domain.service.ImagesTransferService;
import com.marsol.sync.domain.service.LabelsTransferService;
import com.marsol.sync.infraestructure.api.ScaleService;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScaleWebService {

    private final ScaleService scaleService;
    private final LabelTransfererController labelTransfererController;
    private final SyncDataDownloader syncDataDownloader;
    private final SyncDataLoader syncDataLoader;
    private final ImagesTransferService imagesTransferService;
    private final SystemParametersController systemParametersController;
    private final DeleteProductsController deleteProductsController;


    public ScaleWebService(ScaleService scaleService, LabelsTransferService labelsTransferService, LabelTransfererController labelTransfererController, SyncDataDownloader syncDataDownloader, SyncDataLoader syncDataLoader, ImagesTransferService imagesTransferService, SystemParametersController systemParametersController, DeleteProductsController deleteProductsController) {
        this.scaleService = scaleService;
        this.labelTransfererController = labelTransfererController;
        this.syncDataDownloader = syncDataDownloader;
        this.syncDataLoader = syncDataLoader;
        this.imagesTransferService = imagesTransferService;
        this.systemParametersController = systemParametersController;
        this.deleteProductsController = deleteProductsController;
    }

    public String obtenerMensaje() {
        // Aquí podría haber lógica, cálculos, llamadas externas, etc.
        return "¡Mensaje dinámico generado por el servicio!";
    }

    public String obtenerBalanzas(String marca){
        String balanzas = scaleService.getScalesByMarca(marca);
        return balanzas;

    }


    //IP SOURCE ES DESDE DONDE QUIERO COPIAR, DESDE QUE BALANZA!
    //IP DST ES LA BALANZA A LA CUAL QUIERO COPIARLEA DATA!
    public Boolean procesarIp(String ipSource,
                              String ipDst,
                              String etiquetas,
                              String codigos,
                              String imagenes,
                              String plu,
                              Integer store,
                              Integer depto,
                              String sysparameters) throws IOException {

        if (ipSource == null || ipDst == null) {
            System.out.println("IP de origen o destino no puede ser null");
            return false;
        }

        //Si está pinchado la opcion etiquetas / labels
        //para la carga de etiquetasa, hay que tener los 16 formatos; como siempre son los mismos prefiero rescatarlos del servidor
        //que esta descargando de cada balanzas la mismas etiquetas; osea no tiene sentido, asi que ira al servidor a rescatar las etiquetas

        //OK
            if (etiquetas != null) {
            // Procesar etiquetas
            labelTransfererController.loadLabels(ipDst);
        }

        //CODIGOS DE BARRA AVANZADOS
        //OBTENGO LOS CODIGOS DE BARRA DE UNA BALANZA
        //OK
        //ESTA FUNCIOANNDO AVECES NUEVAMENTE.!!!
        if (codigos != null) {
            Path tempFile = null;

            try {
                tempFile = Files.createTempFile("advancedcode", ".txt");

                // Descargar en el archivo
                syncDataDownloader.downloadAdvancedBarcodes(tempFile.toString(), ipSource);

                // Cargar desde el mismo archivo
                syncDataLoader.loadAdvancedBarcodes(tempFile.toString(), ipDst);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Buenas prácticas: restaurar el estado de interrupción
                System.err.println("Operación interrumpida: " + e.getMessage());
            } finally {
                if (tempFile != null) {
                    try {
                        Files.deleteIfExists(tempFile);
                        System.out.println("Archivo temporal eliminado: " + tempFile);
                    } catch (IOException e) {
                        System.err.println("Error al eliminar archivo temporal: " + e.getMessage());
                    }
                }
            }
        }

        //DEBO COMPROBAR ESTO EN EL LAB
        if (imagenes != null) {
            // Procesar imágenes
            // MISMA LOGICA ANTERIOR, IRÁ A BUSCAR LAS IMAGENES AL SERVIDOR YA QUE ACCEDER A UN EQUIPO Y SACAR LA DATA..
            Scale scaleTemp = new Scale();
            scaleTemp.setiP_Balanza(ipDst);
            scaleTemp.setDepartamento(depto);
            scaleTemp.setStore(store);
            imagesTransferService.cargarLayout(scaleTemp);
            //necesito tienda, departamento e ip

        }

        //OK
        if (plu != null) {
            Path pluFile = null;
            List<Path> notasFiles = new ArrayList<>();

            try {
                // Crear archivo temporal para PLU

                //Scale scaleTemp = new Scale();

                //deleteProductsController.deleteProducts(scaleTemp);

                pluFile = Files.createTempFile("plu", ".txt");

                // Descargar archivo PLU
                syncDataDownloader.downloadPLU(pluFile.toString(), ipSource);
                syncDataLoader.loadPLU(pluFile.toString(), ipDst);
                // Crear y descargar 4 archivos de notas
                for (int i = 1; i <= 4; i++) {
                    Path notaFile = Files.createTempFile("note" + i, ".txt");
                    notasFiles.add(notaFile);

                    // Descargar nota correspondiente
                    syncDataDownloader.downloadNote(i, notaFile.toString(), ipSource);
                    syncDataLoader.loadNotes(notaFile.toString(),ipDst,i);
                }

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                // Eliminar archivo PLU
                try {
                    if (pluFile != null) Files.deleteIfExists(pluFile);
                } catch (IOException e) {
                    System.err.println("Error al eliminar archivo PLU: " + e.getMessage());
                }
                // Eliminar notas
                for (Path nota : notasFiles) {
                    try {
                        if (nota != null) Files.deleteIfExists(nota);
                    } catch (IOException e) {
                        System.err.println("Error al eliminar archivo de nota: " + e.getMessage());
                    }
                }
            }
        }

        if(sysparameters !=null){
            systemParametersController.loadSystemParameters(ipDst);
        }

        return true;
    }


}
