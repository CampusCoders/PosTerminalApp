package com.campuscoders.posterminalapp.presentation.payment.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.campuscoders.posterminalapp.R;
import com.campuscoders.posterminalapp.presentation.SaleActivity;
import com.campuscoders.posterminalapp.presentation.payment.slip.SlipPrinter;
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;

import com.campuscoders.posterminalapp.POSAPIHelper;

public class MCRType extends Activity {
    public byte track1[] = new byte[250];
    public byte track2[] = new byte[250];
    public byte track3[] = new byte[250];
    private final static int MSG_MSR_OPEN_FLAG = 0;
    private final static int MSG_MSR_INFO_FLAG = 1;
    private final static int MSG_MSR_CLOSE_FLAG = 2;
    private final static String MSG_MSR_INFO = "msg_msr_info";
    TextView textViewMsg = null;
    boolean isQuit = false;
    boolean isOpen = false;
    int ret = -1;
    int checkCount = 0;
    int successCount = 0;
    int failCount = 0;
    private int RESULT_CODE = 0;
    PosApiHelper PosAPI = PosApiHelper.getInstance();
    SlipPrinter slipPrinter = new SlipPrinter(this);
    private Context mContext;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mcr_type);
        textViewMsg = findViewById(R.id.textViewCardInfo);
        mContext = MCRType.this;
    }

    protected void onPause() {
        disableFunctionLaunch(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
        isQuit = true;
    }

    protected void onResume() {
        disableFunctionLaunch(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
        isQuit = false;
        isOpen = false;
        m_MSRThread = new MSR_Thread();
        m_MSRThread.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        m_MSRThread.interrupt();
        isOpen = false;
        PosAPI.CloseMCR();
    }

    private void updateUI() {
        textViewMsg.setText(R.string.swipe);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {//SCREEN_ORIENTATION_PORTRAIT
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);    //SCREEN_ORIENTATION_LANDSCAPE
        }
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

        totalPrice.setText(ShoppingCartItems.Companion.getproductTotalPrice());
        orderNo.setText(ShoppingCartItems.Companion.getOrderNo());

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MCRType.this.finish();
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

    MSR_Thread m_MSRThread = null;

    public class MSR_Thread extends Thread {
        private boolean m_bThreadFinished = false;

        public boolean isThreadFinished() {
            return m_bThreadFinished;
        }

        public void run() {
            synchronized (this) {
                if (!isOpen) {
                    int reset1 = PosAPI.OpenMCR();   //02 c1 01
                    if (reset1 == 0) {
                        Message msg = new Message();
                        msg.what = MSG_MSR_OPEN_FLAG;
                        handler.sendMessage(msg);
                        isOpen = true;
                    } else {
                        Message msg = new Message();
                        msg.what = MSG_MSR_CLOSE_FLAG;
                        handler.sendMessage(msg);
                    }
                }
                while (!isQuit && isOpen) {
                    int temp = -1;
                    PosAPI.OpenMCR();
                    while (temp != 0 && !isQuit) {
                        try {
                            temp = PosAPI.CheckMCR();
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isQuit) {
                        return;
                    }
                    checkCount++;
                    Arrays.fill(track1, (byte) 0x00);
                    Arrays.fill(track2, (byte) 0x00);
                    Arrays.fill(track3, (byte) 0x00);
                    ret = PosAPI.ReadMCR((byte) 0, (byte) 0, track1, track2, track3);  //c1  07
                    if (ret > 0) {
                        RESULT_CODE = 0;
                        successCount++;
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        String string = "";
                        if (ret <= 7) {
                            if ((ret & 0x01) == 0x01) {
                                string = "track1:" + new String(track1).trim();
                            }
                            if ((ret & 0x02) == 0x02) {
                                string = string + "\n\ntrack2:" + new String(track2).trim();
                            }
                            if ((ret & 0x04) == 0x04) {
                                string = string + "\n\ntrack3:" + new String(track3).trim();
                            }
                        } else {
                            RESULT_CODE = -1;
                            string = "Lib_MsrRead check data error";
                            failCount++;
                        }
                        b.putString(MSG_MSR_INFO, string);
                        msg.setData(b);
                        msg.what = MSG_MSR_INFO_FLAG;
                        handler.sendMessage(msg);
                        PosAPI.SysBeep();
                        isQuit = true;

                    } else {
                        RESULT_CODE = -1;
                        failCount++;
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString(MSG_MSR_INFO, "Lib_MsrRead fail");
                        msg.setData(b);
                        msg.what = MSG_MSR_INFO_FLAG;
                        handler.sendMessage(msg);
                        isQuit = true;

                    }
                }
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MSR_CLOSE_FLAG:
                    MCRType.this.finish();
                    break;
                case MSG_MSR_OPEN_FLAG:
                    updateUI();
                    break;
                case MSG_MSR_INFO_FLAG:
                    Bundle b = msg.getData();
                    String strInfo = b.getString(MSG_MSR_INFO);
                    if (RESULT_CODE == -1) {
                        textViewMsg.setText("İşlem Onaylanamadı \nTekrar Deneyiniz");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MCRType.this.finish();
                            }
                        }, 3000);
                    } else {
                        textViewMsg.setText("İşlem Onaylandı" + strInfo);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showPopup();
                            }
                        }, 3000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private static final String DISABLE_FUNCTION_LAUNCH_ACTION = "android.intent.action.DISABLE_FUNCTION_LAUNCH";

    private void disableFunctionLaunch(boolean state) {
        Intent disablePowerKeyIntent = new Intent(
                DISABLE_FUNCTION_LAUNCH_ACTION);
        if (state) {
            disablePowerKeyIntent.putExtra("state", true);
        } else {
            disablePowerKeyIntent.putExtra("state", false);
        }
        sendBroadcast(disablePowerKeyIntent);
    }

}

