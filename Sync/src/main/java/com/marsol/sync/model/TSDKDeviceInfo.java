package com.marsol.sync.model;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;


@Structure.FieldOrder({"Addr", "Port", "ProtocolType", "DeviceNo", "Version", "LenguageID", "KeyID",
    "PLUStorage", "Note1Storage", "Note2Storage", "Note3Storage", "Note4Storage",
    "PrinterKm", "PrinterPaperCount", "Reserve"})
public class TSDKDeviceInfo extends Structure {
    public int Addr;
    public int Port;
    public int ProtocolType;
    public byte[] DeviceNo = new byte[16];
    public int Version;
    public byte LenguageID;
    public byte KeyID;
    public short PLUStorage;
    public short Note1Storage;
    public short Note2Storage;
    public short Note3Storage;
    public short Note4Storage;
    public double PrinterKm;
    public int PrinterPaperCount;
    public byte[] Reserve = new byte[200];

   //Sobreescribir el metodo getFieldOrder de la clase Structure
    public TSDKDeviceInfo() {

    }

    @Override
    protected List<String> getFieldOrder() {
    	return Arrays.asList("Addr", "Port", "ProtocolType", "DeviceNo", "Version", "LenguageID", "KeyID",
                "PLUStorage", "Note1Storage", "Note2Storage", "Note3Storage", "Note4Storage",
                "PrinterKm", "PrinterPaperCount", "Reserve");
    }
}
