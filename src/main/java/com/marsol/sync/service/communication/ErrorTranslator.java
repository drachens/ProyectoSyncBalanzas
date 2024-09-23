package com.marsol.sync.service.communication;

public class ErrorTranslator {

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case SyncSDKDefine.SDK_Err_Success:
                return "Operación exitosa.";
            case SyncSDKDefine.SDK_Err_Progress:
                return "En progreso.";
            case SyncSDKDefine.SDK_Err_Terminate:
                return "Operación terminada.";
            case SyncSDKDefine.SDK_Err_ProtocolTypeNotSupport:
                return "Tipo de protocolo no soportado.";
            case SyncSDKDefine.SDK_Err_DataTypeNotSupport:
                return "Tipo de dato no soportado.";
            case SyncSDKDefine.SDK_Err_CannotOpenInputFile:
                return "No se puede abrir el archivo de entrada.";
            case SyncSDKDefine.SDK_Err_ImportDataFailed:
                return "Error al importar los datos.";
            case SyncSDKDefine.SDK_Err_CommDataFailed:
                return "Error en los datos de comunicación.";
            case SyncSDKDefine.SDK_Err_ParseDataFailed:
                return "Error al analizar los datos.";
            case SyncSDKDefine.SDK_Err_CannotCreateOutputFile:
                return "No se puede crear el archivo de salida.";
            case SyncSDKDefine.SDK_Err_ParamError:
                return "Error en los parámetros.";
            case SyncSDKDefine.SDK_Err_CannotConnect:
                return "No se puede conectar.";
            case SyncSDKDefine.SDK_Err_NotHaveThisCommand:
                return "No se reconoce este comando.";
            case SyncSDKDefine.SDK_Err_DataOutOfRange:
                return "Datos fuera de rango.";
            case SyncSDKDefine.SDK_Err_WriteError:
                return "Error al escribir.";
            case SyncSDKDefine.SDK_Err_DiskNotEnough:
                return "Espacio en disco insuficiente.";
            case SyncSDKDefine.SDK_Err_ReadError:
                return "Error al leer.";
            case SyncSDKDefine.SDK_Err_PackageNoError:
                return "Paquete sin errores.";
            case SyncSDKDefine.SDK_Err_CommandError:
                return "Error de comando.";
            case SyncSDKDefine.SDK_Err_DataError:
                return "Error de datos.";
            case SyncSDKDefine.SDK_Err_MissPaper:
                return "Falta papel.";
            case SyncSDKDefine.SDK_Err_MD5CheckError:
                return "Error en la verificación MD5.";
            case SyncSDKDefine.SDK_Err_ErrorPackageLenError:
                return "Error en la longitud del paquete de error.";
            case SyncSDKDefine.SDK_Err_PackageLenError:
                return "Error en la longitud del paquete.";
            case SyncSDKDefine.SDK_Err_DataErrNotErrCode:
                return "Error en los datos (no es un código de error).";
            case SyncSDKDefine.SDK_Err_HaveNoThisLabelNo:
                return "No existe esta etiqueta.";
            case SyncSDKDefine.SDK_Err_MD5WaitTimeOut:
                return "Tiempo de espera de MD5 agotado.";
            case SyncSDKDefine.SDK_Err_DeviceNoRespond:
                return "El dispositivo no responde.";
            case SyncSDKDefine.SDK_Err_FileSizeError:
                return "Error en el tamaño del archivo.";
            case SyncSDKDefine.SDK_Err_HotkeyTypeNotSupport:
                return "Tipo de tecla rápida no soportado.";
            case SyncSDKDefine.SDK_Err_FileFormatError:
                return "Error en el formato del archivo.";
            case SyncSDKDefine.SDK_Err_FileTooSmall:
                return "Archivo demasiado pequeño.";
            case SyncSDKDefine.SDK_Err_FileTooLarge:
                return "Archivo demasiado grande.";
            case SyncSDKDefine.SDK_Err_FileNameErr:
                return "Error en el nombre del archivo.";
            case SyncSDKDefine.SDK_Err_NoThisErrorCode:
                return "Código de error no reconocido.";
            default:
                return "Código de error desconocido.";
        }
    }
}
