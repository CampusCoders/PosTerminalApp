package com.campuscoders.posterminalapp.presentation.payment.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.campuscoders.posterminalapp.presentation.payment.api.POSAPIHelper;
import com.campuscoders.posterminalapp.presentation.payment.api.ByteUtil;
import com.campuscoders.posterminalapp.R;
import com.campuscoders.posterminalapp.presentation.SaleActivity;
import com.campuscoders.posterminalapp.presentation.payment.slip.SlipPrinter;
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NFCType extends Activity {

    TextView textViewMsg = null;
    private boolean bIsBack = false;
    private boolean isNFCReadingStopped = false;
    POSAPIHelper PosAPI = POSAPIHelper.getInstance();

    SlipPrinter slipPrinter = new SlipPrinter(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nfc_type);
        textViewMsg = (TextView) this.findViewById(R.id.textviewCardInfo);
    }

    private Handler handler = new Handler();

    private void showPopup() {
        // Dialog Oluştur
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.pop_up_sales_summary, null);

        // Dialog düzenini ve köşelerini yuvarlamak için
        builder.setView(dialogView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        Button buttonPrintSlip = dialogView.findViewById(R.id.buttonPrintSlip);
        TextView totalPrice = dialogView.findViewById(R.id.textViewSaleTotal);
        TextView orderNo = dialogView.findViewById(R.id.textViewOrderNo);

        totalPrice.setText(ShoppingCartItems.Companion.getproductTotalPrice());
        orderNo.setText(ShoppingCartItems.Companion.getOrderNo());

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NFCType.this.finish();
                Intent intent = new Intent(getApplicationContext(), SaleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        buttonPrintSlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slipPrinter.ClickAndPrint();
            }
        });
        dialog.show();
    }

    private Runnable nfcReadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (!bIsBack && !isNFCReadingStopped) {
                int result = readNFC();
                if (result == 0) {
                    isNFCReadingStopped = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPopup();
                        }
                    }, 3000);
                }
                handler.postDelayed(this, 1);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        disableFunctionLaunch(true);
        handler.postDelayed(nfcReadingRunnable, 1);
    }

    protected void onPause() {
        disableFunctionLaunch(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
        handler.removeCallbacks(nfcReadingRunnable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bIsBack = true;
    }

    public int readNFC() {
        synchronized (this) {
            Log.e("nfc", "heyp nfc Picc_Open start!");
            byte[] NfcData_Len = new byte[5];
            final byte[] Technology = new byte[25];
            byte[] NFC_UID = new byte[56];
            byte[] NDEF_message = new byte[500];

            int ret = PosAPI.NFCPicc(NfcData_Len, Technology, NFC_UID, NDEF_message);

            final int TechnologyLength = NfcData_Len[0] & 0xFF;
            int NFC_UID_length = NfcData_Len[1] & 0xFF;
            int NDEF_message_length = (NfcData_Len[3] & 0xFF) + (NfcData_Len[4] & 0xFF);
            byte[] NDEF_message_data = new byte[NDEF_message_length];
            byte[] NFC_UID_data = new byte[NFC_UID_length];

            System.arraycopy(NFC_UID, 0, NFC_UID_data, 0, NFC_UID_length);
            System.arraycopy(NDEF_message, 0, NDEF_message_data, 0, NDEF_message_length);
            String NDEF_message_data_str = new String(NDEF_message_data);
            String NDEF_str = null;

            if (!TextUtils.isEmpty(NDEF_message_data_str)) {
                NDEF_str = NDEF_message_data_str.substring(NDEF_message_data_str.indexOf("en") + 2, NDEF_message_data_str.length());
            }

            if (ret == 0) {
                PosAPI.SysBeep();
                if (!TextUtils.isEmpty(NDEF_str)) {
                    final String tmpStr = "TYPE: " + new String(Technology).substring(0, TechnologyLength) + "\n"
                            + "UID: " + ByteUtil.bytearrayToHexString(NFC_UID_data, NFC_UID_data.length) + "\n"
                            + NDEF_str;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            textViewMsg.setText(tmpStr);
                        }
                    });
                } else {
                    final String str = "TYPE: " + new String(Technology).substring(0, TechnologyLength) + "\n"
                            + "UID: " + ByteUtil.bytearrayToHexString(NFC_UID_data, NFC_UID_data.length) + "\n"
                            + NDEF_str;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            textViewMsg.setText(str);
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewMsg.setText("Lütfen Kartı Okutun");
                        return;
                    }
                });
            }
            return ret;
        }
    }

    private static final String DISABLE_FUNCTION_LAUNCH_ACTION = "android.intent.action.DISABLE_FUNCTION_LAUNCH";

    private void disableFunctionLaunch(boolean state) {
        Intent disablePowerKeyIntent = new Intent(DISABLE_FUNCTION_LAUNCH_ACTION);
        if (state) {
            disablePowerKeyIntent.putExtra("state", true);
        } else {
            disablePowerKeyIntent.putExtra("state", false);
        }
        sendBroadcast(disablePowerKeyIntent);
    }
}

