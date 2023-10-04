package com.campuscoders.posterminalapp.presentation.payment.api;

import android.graphics.Bitmap;

public class POSAPIHelper {

    private static POSAPIHelper instance;

    private POSAPIHelper() {}

    public static POSAPIHelper getInstance() {
        if (instance == null) {
            instance = new POSAPIHelper();
        }
        return instance;
    }

    public int CheckICC(byte slot) {
        return 0;
    }

    public int OpenICC(byte slot, byte vcc_mode, byte[] ATR) {
        return 0;
    }



    public int CommandICC(byte slot, byte[] sendBuf, byte[] recBuf) {
        return 0;
    }

    public void SysBeep() {

    }

    public void SysSetLedMode(int led, int mode) {

    }

    public void CloseMCR() {

    }

    public int OpenMCR() {
        return 0;
    }

    public int CheckMCR() {
        return 0;
    }

    public int ReadMCR(byte a, byte b, byte[] c, byte[] d, byte[] e) {
        return 0;
    }

    public int NFCPicc(byte[] a, byte[] b, byte[] c, byte[] d) {
        return 0;
    }

    public int PrintInit(int a, int b, int c, int d) {
        return 0;
    }

    public void PrintSetGray(int a) {

    }

    public int PrintBitmap(Bitmap bitmap) {
        return 0;
    }

    public int PrintStart() {
        return 0;
    }

}
