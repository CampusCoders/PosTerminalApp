package com.campuscoders.posterminalapp.presentation.payment.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.campuscoders.posterminalapp.presentation.payment.api.ByteUtil;
import com.campuscoders.posterminalapp.presentation.payment.api.POSAPIHelper;
import com.campuscoders.posterminalapp.R;
import com.campuscoders.posterminalapp.presentation.payment.api.RESP;
import com.campuscoders.posterminalapp.presentation.payment.api.SEND;
import com.campuscoders.posterminalapp.presentation.SaleActivity;
import com.campuscoders.posterminalapp.presentation.payment.slip.SlipPrinter;
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ICCType extends Activity implements View.OnClickListener {
    private final String TAG = "ICCActivity-Cagri";
    private AlertDialog alertDialog;
    private Button btnSingleTest = null;
    public byte dataIn[] = new byte[512];
    public byte ATR[] = new byte[40];
    public byte vcc_mode = 1;
    private int ret;
    TextView tv_msg = null;

    POSAPIHelper PosAPI = POSAPIHelper.getInstance();

    SlipPrinter slipPrinter = new SlipPrinter(this);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_icc_type);

        tv_msg = (TextView) this.findViewById(R.id.textviewCardInfo);

        btnSingleTest = (Button) findViewById(R.id.buttonStart);
        btnSingleTest.setOnClickListener(ICCType.this);

    }

    String strInfo = "";

    private Handler handler = new Handler();

    private boolean isCardRead = false;

    @Override
    protected void onStart() {
        super.onStart();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (null != IccThread && !IccThread.isThreadFinished()) {
                    return;
                }
                if (isCardRead) {
                    return;
                }
                IccThread = new ICC_Thread((byte) 0);
                IccThread.start();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

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

        totalPrice.setText(ShoppingCartItems.Companion.getTotalPrice());
        orderNo.setText(ShoppingCartItems.Companion.getOrderNo());

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ICCType.this.finish();
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

    void startICC(byte slot) {
        int j;
        for (j = 0; j < 1; j++) {
            ret = 1;
            if (slot == 0) {
                ret = PosAPI.CheckICC(slot);
                if (ret != 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            tv_msg.setText("Kart Giriniz");
                        }
                    });
                    Log.e(TAG, "ICC_Check failed!");
                    return;
                }
            }

            ret = PosAPI.OpenICC(slot, vcc_mode, ATR);
            if (ret != 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        tv_msg.setText("Yanlış Giriş");
                    }
                });
                Log.e(TAG, "ICC_Open failed!");

                return;
            }

            Log.e(TAG, "atrString = " + ByteUtil.bytearrayToHexString(ATR, 40));
            String atrString = "";
            for (int i = 0; i < ATR.length; i++) {
                atrString += Integer.toHexString(Integer.valueOf(String.valueOf(ATR[i]))).replaceAll("f", "");
            }
            Log.e(TAG, "atrString = " + atrString);

            byte cmd[] = new byte[4];
            short lc = 0;
            short le = 0;

            if (slot == 0) {
                cmd[0] = 0x00;            //0-3 cmd
                cmd[1] = (byte) 0xA4;
                cmd[2] = 0x04;
                cmd[3] = 0x00;
                lc = 0x0E;
                le = 1;
                String sendmsg = "1PAY.SYS.DDF01";
                dataIn = sendmsg.getBytes();
            } else {
                cmd[0] = 0x00;            //0-3 cmd
                cmd[1] = (byte) 0xA4;
                cmd[2] = 0x00;
                cmd[3] = 0x00;
                lc = 0x02;
                le = 0x00;
                String sendmsg = "2FE2";
                dataIn = hexStringToByteArray(sendmsg);
            }

            SEND APDUSend = new SEND(cmd, lc, dataIn, le);
            RESP APDUResp = null;
            byte[] resp = new byte[516];

            ret = PosAPI.CommandICC(slot, APDUSend.getBytes(), resp);
            if (0 == ret) {
                APDUResp = new RESP(resp);
                strInfo = ByteUtil.bytearrayToHexString(APDUResp.DataOut, APDUResp.LenOut) + "\n\nSWA:"
                        + ByteUtil.byteToHexString(APDUResp.SWA) + " SWB:" + ByteUtil.byteToHexString(APDUResp.SWB);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PosAPI.SysBeep();
                        tv_msg.setText(strInfo);
                        btnSingleTest.setVisibility(View.GONE);
                        Handler handler = new Handler();
                        isCardRead = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do it after success
                                showPopup();
                            }
                        }, 3000);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_msg.setText("Veri Bulunamadı");
                    }
                });
                Log.e(TAG, "ICC_Command failed!");
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "A26 sum = " + j);
    }

    //Converting a string of hex character to bytes
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    ICC_Thread IccThread = null;

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
    }

    private boolean m_bThreadFinished = false;

    public class ICC_Thread extends Thread {
        byte testMode;
        private int iWaitSecond = 1;
        public boolean isThreadFinished() {
            return m_bThreadFinished;
        }
        public ICC_Thread(byte mode) {
            testMode = mode;
        }
        byte slot = 0;

        public void run() {
            synchronized (this) {
                m_bThreadFinished = false;
                startICC(testMode);
                m_bThreadFinished = true;
            }
            Log.e("ICCThread[ run ]", "run() end");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

