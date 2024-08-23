package com.marsol.sync.model;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

import static com.sun.jna.Structure.*;

/*
    Esta clase se encarga de crear objetos de tipo TSDKDeviceInfo, utilizados por el SDK de
    comunicación con la balanza en su función de solicitar la información de un dispositivo ( balanza )
 */



public class TSDKDeviceInfo extends Structure {
    public int Addr;
    public int Port;
    public int ProtocolType;
    public byte[] DeviceNo = new byte[16];
    public int Version;
    public byte LanguageID;
    public byte KeyID;
    public short PLUStorage;
    public short Note1Storage;
    public short Note2Storage;
    public short Note3Storage;
    public short Note4Storage;
    public double PrinterKm;
    public int PrinterPaperCount;
    public byte[] Reserve = new byte[192];

   //Sobreescribir el metodo getFieldOrder de la clase Structure

    @Override
    protected List<String> getFieldOrder() {
    	return Arrays.asList("Addr", "Port", "ProtocolType", "DeviceNo", "Version", "LanguageID", "KeyID",
                "PLUStorage", "Note1Storage", "Note2Storage", "Note3Storage", "Note4Storage",
                "PrinterKm", "PrinterPaperCount", "Reserve");
    }
    @Override
    public String toString() {
        return String.format("Addr: %d, Port: %d, ProtocolType: %d, Version: %d, " +
                        "LanguageID: %d, KeyID: %d, PLUStorage: %d, Note1Storage: %d, " +
                        "Note2Storage: %d, Note3Storage: %d, Note4Storage: %d, PrinterKm: %f, " +
                        "PrinterPaperCount: %d",
                Addr, Port, ProtocolType,
                Version, LanguageID, KeyID, PLUStorage, Note1Storage,
                Note2Storage, Note3Storage, Note4Storage, PrinterKm,
                PrinterPaperCount);
    }
    public static void main(String[] args) {
        // Asegúrate de que la estructura no tenga padding adicional
        System.out.println("Size of TSDKDeviceInfo: " + new TSDKDeviceInfo().size() + " bytes");
    }

}
