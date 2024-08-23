package com.marsol.sync.service.communication;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

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
    public byte BoardType;
    public byte[] PrintEnableDate = new byte[7];
    public byte[] PrinterKm = new byte[8];
    public int PrinterPaperCount;
    public byte EthernetMode;
    public byte[] MacAddr = new byte[17];
    public byte[] Reserve = new byte[174];

    public TSDKDeviceInfo() {
    }

    protected List getFieldOrder() {
        return Arrays.asList("Addr", "Port", "ProtocolType", "DeviceNo", "Version", "LanguageID", "KeyID", "PLUStorage", "Note1Storage", "Note2Storage", "Note3Storage", "Note4Storage", "BoardType", "PrintEnableDate", "PrinterKm", "PrinterPaperCount", "EthernetMode", "MacAddr", "Reserve");
    }
}
