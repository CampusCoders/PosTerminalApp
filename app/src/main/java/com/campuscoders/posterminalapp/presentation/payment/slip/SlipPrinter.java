package com.campuscoders.posterminalapp.presentation.payment.slip;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.campuscoders.posterminalapp.POSAPIHelper;
import com.campuscoders.posterminalapp.Print;
import com.campuscoders.posterminalapp.R;


public class SlipPrinter extends Thread {
    private Context context;

    public String tag = "PrintActivity-Cagri";

    int ret = -1;

    private boolean m_bThreadFinished = true;

    POSAPIHelper PosAPI = POSAPIHelper.getInstance();

    SlipPrinter printThread = null;

    public SlipPrinter(Context context) {
        this.context = context;
    }

    SlipPrinterBitmap slip = new SlipPrinterBitmap();

    private Bitmap makeSmallerBitmap(Bitmap image, Integer maxSize) {
        Integer width = image.getWidth();
        Integer height = image.getHeight();
        Double bitmapRatio = (width.doubleValue() / height.doubleValue());

        if (bitmapRatio > 1) {
            width = maxSize;
            Double scaledHeight = width / bitmapRatio;
            height = scaledHeight.intValue();
        } else {
            height = maxSize;
            Double scaledWidth = height * bitmapRatio;
            width = scaledWidth.intValue();
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void ClickAndPrint() {
        slip.generateSlip();
        if (printThread != null && !printThread.isThreadFinished()) {
            Log.e(tag, "Thread is still running...");
            return;
        }

        synchronized (this) {

            Resources resources = context.getResources();
            ret = PosAPI.PrintInit(2, 24, 24, 0);
            PosAPI.PrintSetGray(5);

            Print.PrintSetFont((byte) 24, (byte) 24, (byte) 2);

            Bitmap bit = SingletonBitmap.getInstance().getBitmap();
            PosAPI.PrintBitmap(bit);

            Bitmap bmp1 = BitmapFactory.decodeResource(resources, R.drawable.banner_black);
            Bitmap bmp2 = makeSmallerBitmap(bmp1, 384);
            ret = PosAPI.PrintBitmap(bmp2);
            ret = PosAPI.PrintStart();

        }
    }

    public boolean isThreadFinished() {
        return m_bThreadFinished;
    }
}



