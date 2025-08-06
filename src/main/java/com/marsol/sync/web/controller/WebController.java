package com.marsol.sync.web.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.infraestructure.integration.SyncDataDownloader;
import com.marsol.sync.infraestructure.integration.SyncDataLoader;
import com.marsol.sync.utils.ConnectionTest;
import com.marsol.sync.web.services.ScaleWebService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WebController {


    private List<String> ips = List.of("192.168.2.239");

    private final SyncDataLoader syncDataLoader;
    private final SyncDataDownloader syncDataDownloader;
    private final ScaleWebService scaleWebService;
    public WebController(SyncDataLoader syncDataLoader, SyncDataDownloader syncDataDownloader, ScaleWebService scaleWebService) {
        this.syncDataLoader = syncDataLoader;
        this.syncDataDownloader = syncDataDownloader;
        this.scaleWebService = scaleWebService;
    }


    @GetMapping("/")
    public String index(@RequestParam(name = "marca", required = false, defaultValue = "HPRT") String marca, Model model) throws IOException {


        String balanzas = scaleWebService.obtenerBalanzas(marca);


        Type listType = new TypeToken<List<Scale>>() {}.getType();
        List<Scale> lista = new Gson().fromJson(balanzas, listType);

        for (Scale scale : lista) {
            if(!ConnectionTest.sendPingRequest(scale.getiP_Balanza())){
                scale.setStatus("0");
            }else {
                scale.setStatus("1");
            }
        }


        model.addAttribute("marca", marca); // para que el input mantenga el valor



        model.addAttribute("balanzas", lista);

        return "index";
    }

    @PostMapping("/cargar")
    public String cargarArchivo(@RequestParam("archivo") MultipartFile archivo,
                                @RequestParam(name = "marca", required = false, defaultValue = "HPRT") String marca,
                                Model model) {
        if (archivo.isEmpty()) {
            model.addAttribute("mensaje", "Por favor, seleccione un archivo.");
            return "cargar";
        }

        // Aquí puedes procesar el archivo Excel
        // Por ejemplo, guardarlo o leer su contenido con Apache POI



        model.addAttribute("mensaje", "Archivo cargado exitosamente: " + archivo.getOriginalFilename());
        return "cargar";
    }

    @GetMapping("/cargar")
    public String mostrarFormularioCarga() {
        return "cargar";
    }

    @GetMapping("/probar")
    public String probarIP(@RequestParam(name = "ip") String ip, Model model) throws IOException, InterruptedException {

        // Aquí podrías hacer alguna verificación de la IP (ping, validación, etc.)
        // Por ahora simplemente se pasa al modelo

        Path tempFile = Files.createTempFile(null, ".txt");
// Escribe en el archivo
        syncDataDownloader.downloadAdvancedBarcodes(tempFile.toString(), ip);
// Luego léelo
        syncDataLoader.loadAdvancedBarcodes(tempFile.toString(), ip);
// Si quieres, elimínalo manualmente después
        Files.deleteIfExists(tempFile);



        model.addAttribute("ipProbar", ip);
        model.addAttribute("resultado", "Conexión exitosa"); // Puedes cambiar esto con lógica real

        return "resultadoProbar"; // Thymeleaf buscará resultadoProbar.html
    }




    @GetMapping("/procesarIps")
    public String procesarIps(
            @RequestParam(name = "ipSource") String ipSource,
            @RequestParam(name = "ipDest") String ipDest,
            @RequestParam(name = "store") Integer store,
            @RequestParam(name = "depto") Integer depto,
            @RequestParam(name = "etiquetas", required = false) String etiquetas,
            @RequestParam(name = "codigos", required = false) String codigos,
            @RequestParam(name = "imagenes", required = false) String imagenes,
            @RequestParam(name = "plu", required = false) String plu,
            @RequestParam(name = "sysparameters", required = false) String sysparameters,
            @RequestParam(name = "esAutoservicio", required = false) Boolean esAutoservicio,
            Model model
    )
    {
        System.out.println("===> Entró al método procesarIps");
        System.out.println("ipSource: " + ipSource);
        System.out.println("ipDest: " + ipDest);
        System.out.println("store: " + store);
        System.out.println("depto: " + depto);

        try {
            boolean ok = scaleWebService.procesarIp(ipSource, ipDest, etiquetas, codigos, imagenes, plu, store, depto, sysparameters, esAutoservicio);
            model.addAttribute("mensaje", ok ? "Carga aplicada correctamente" : "Falló la carga");
            model.addAttribute("ipInfo", "Balanza IP: " + ipDest);

        } catch (Exception e) {
            e.printStackTrace(); // Agrega esto para ver en consola qué pasa
            model.addAttribute("mensaje", "Error en la carga: " + e.getMessage());
        }

        return "resultado";
    }


    @GetMapping("/resultado-demo")
    public String resultadoDemo(Model model) {
        model.addAttribute("mensaje", "Carga aplicada correctamente");
        model.addAttribute("ipInfo", "Balanza IP: 10.105.197.19"); // Cambia la IP según necesites
        return "resultado";
    }







}