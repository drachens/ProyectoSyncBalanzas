package com.marsol.sync.web.controller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marsol.sync.domain.model.Scale;
import com.marsol.sync.web.services.ScaleWebService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WebController {


    private List<String> ips = List.of("192.168.2.239");

    private final ScaleWebService scaleWebService;
    public WebController(ScaleWebService scaleWebService) {
        this.scaleWebService = scaleWebService;
    }


    @GetMapping("/")
    public String index(@RequestParam(name = "marca", required = false, defaultValue = "HPRT") String marca, Model model) {

        String balanzas = scaleWebService.obtenerBalanzas(marca);
        Type listType = new TypeToken<List<Scale>>() {}.getType();
        List<Scale> lista = new Gson().fromJson(balanzas, listType);

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
            Model model
    )
    {
        System.out.println("===> Entró al método procesarIps");
        System.out.println("ipSource: " + ipSource);
        System.out.println("ipDest: " + ipDest);
        System.out.println("store: " + store);
        System.out.println("depto: " + depto);

        try {
            boolean ok = scaleWebService.procesarIp(ipSource, ipDest, etiquetas, codigos, imagenes, plu, store, depto, sysparameters);
            model.addAttribute("mensaje", ok ? "Carga aplicada correctamente" : "Falló la carga");
        } catch (Exception e) {
            e.printStackTrace(); // Agrega esto para ver en consola qué pasa
            model.addAttribute("mensaje", "Error en la carga: " + e.getMessage());
        }

        return "resultado";
    }





}