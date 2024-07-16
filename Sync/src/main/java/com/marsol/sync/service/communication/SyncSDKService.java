package com.marsol.sync.service.communication;

//import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SyncSDKService {


	private final SyncSDK sdkInstance;

    static {
        // Configurar la ruta de la biblioteca nativa
        System.setProperty("jna.library.path", "src/main/resources/win32-x86-64");
    }

	@Autowired
	public SyncSDKService(SyncSDK sdkInstance) {
		this.sdkInstance = sdkInstance;
	}

	public boolean initializeSDK() {
		boolean init = sdkInstance.SDK_Initialize();
		try {
			if (init) {
				System.out.println("Conexión exitosa!");
				return init;
			} else {
				System.out.println("Fallo de conexión");
				System.out.println(init);
				return init;
			}
		}catch (Exception e){
			System.out.println("Error");
			return init;
			}

	}
}
