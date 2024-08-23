package com.marsol.sync.service.communication;

public class SyncSDKDefine {
    public static final int Sync_Action_DownLoad = 0;
    public static final int Sync_Action_UpLoad = 1;
    public static final int Sync_Action_Delete = 2;
    public static final int SDK_ProtocolType_None = 0;
    public static final int SDK_ProtocolType_Sttp = 1;
    public static final int Sync_DataType_nil = 65535;
    public static final int Sync_DataType_PLU = 0;
    public static final int Sync_DataType_Department = 1;
    public static final int Sync_DataType_Unit = 2;
    public static final int Sync_DataType_Hotkey = 3;
    public static final int Sync_DataType_CustomBarcode = 4;
    public static final int Sync_DataType_Note1 = 5;
    public static final int Sync_DataType_Note2 = 6;
    public static final int Sync_DataType_Note3 = 7;
    public static final int Sync_DataType_Note4 = 8;
    public static final int Sync_DataType_HeaderInfo = 9;
    public static final int Sync_DataType_FooterInfo = 10;
    public static final int Sync_DataType_Advertisement = 11;
    public static final int Sync_DataType_SystemOptions = 12;
    public static final int Sync_DataType_Time = 13;
    public static final int Sync_DataType_Operator = 14;
    public static final int Sync_DataType_Wifi = 15;
    public static final int Sync_DataType_UPDeleteWaterRecord = 17;
    public static final int Sync_CK1_Parameter = 19;
    public static final int Sync_CK1_HotKey = 20;
    public static final int Sync_DataType_STTPLog = 21;
    public static final int Sync_DataType_ScaleWeight = 23;
    public static final int Sync_DataType_NewChangePriceLog = 24;
    public static final int Sync_DataType_UpLogFileZip = 25;
    public static final int Sync_DataType_YHMsg = 26;
    public static final int Sync_DataType_UpDBFile = 27;
    public static final int Sync_DataType_Font = 4096;
    public static final int Sync_DataType_Firmware = 4097;
    public static final int Sync_DataType_ADModule = 4098;
    public static final int Sync_DataType_PrinterModule = 4099;
    public static final int Sync_DataType_Label = 8192;
    public static final int Sync_DataType_LabelBackGround = 8193;
    public static final int Sync_DataType_LabelFile = 8194;
    public static final int Sync_DataType_ZipFile = 12288;
    public static final int Sync_DataType_CK1_Hotkey = 12289;
    public static final int Sync_DataType_CK1_Program = 12290;
    public static final int Sync_DataType_TouchParamSetting = 12291;
    public static final int Sync_DataType_TouchScaleHotKeySetting = 12292;
    public static final int SDK_Err_Success = 0;
    public static final int SDK_Err_Progress = 1;
    public static final int SDK_Err_Terminate = 2;
    public static final int SDK_Err_ProtocolTypeNotSupport = 256;
    public static final int SDK_Err_DataTypeNotSupport = 257;
    public static final int SDK_Err_CannotOpenInputFile = 258;
    public static final int SDK_Err_ImportDataFailed = 259;
    public static final int SDK_Err_CommDataFailed = 260;
    public static final int SDK_Err_ParseDataFailed = 261;
    public static final int SDK_Err_CannotCreateOutputFile = 262;
    public static final int SDK_Err_ParamError = 263;
    public static final int SDK_Err_CannotConnect = 264;
    public static final int SDK_Err_NotHaveThisCommand = 265;
    public static final int SDK_Err_DataOutOfRange = 266;
    public static final int SDK_Err_WriteError = 267;
    public static final int SDK_Err_DiskNotEnough = 268;
    public static final int SDK_Err_ReadError = 269;
    public static final int SDK_Err_PackageNoError = 270;
    public static final int SDK_Err_CommandError = 271;
    public static final int SDK_Err_DataError = 272;
    public static final int SDK_Err_MissPaper = 273;
    public static final int SDK_Err_MD5CheckError = 274;
    public static final int SDK_Err_ErrorPackageLenError = 275;
    public static final int SDK_Err_PackageLenError = 276;
    public static final int SDK_Err_DataErrNotErrCode = 277;
    public static final int SDK_Err_HaveNoThisLabelNo = 278;
    public static final int SDK_Err_MD5WaitTimeOut = 279;
    public static final int SDK_Err_DeviceNoRespond = 280;
    public static final int SDK_Err_FileSizeError = 281;
    public static final int SDK_Err_HotkeyTypeNotSupport = 288;
    public static final int SDK_Err_FileFormatError = 289;
    public static final int SDK_Err_FileTooSmall = 290;
    public static final int SDK_Err_FileTooLarge = 291;
    public static final int SDK_Err_FileNameErr = 292;
    public static final int SDK_Err_NoThisErrorCode = 511;
    public static final String SDK_DateTimeFormat = "YYYY-MM-DD HH:mm:SS";
    public static final int SDK_Task_Failed = -1;

    public SyncSDKDefine() {
    }

    public static int ipToLong(String strIp) {
        int[] ip = new int[4];
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        ip[0] = Integer.parseInt(strIp.substring(0, position1));
        ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
        ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
        ip[3] = Integer.parseInt(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
}
