package com.campuscoders.posterminalapp.presentation.payment.slip;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;

import com.campuscoders.posterminalapp.domain.model.ShoppingCart;
import com.campuscoders.posterminalapp.presentation.sale.views.ShoppingCartItems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

public class SlipPrinterBitmap {
    Date DateAndTime = new Date();
    ArrayList<ShoppingCart> shoppingCartList = new ArrayList<ShoppingCart>();

    String SlipHeaderFilePath = Environment.getExternalStorageDirectory() + File.separator + "SlipHeader.txt";
    String SlipSQLFilePath = Environment.getExternalStorageDirectory() + File.separator + "SlipSQL.txt";
    String SlipTailFilePath = Environment.getExternalStorageDirectory() + File.separator + "SlipTail.txt";

    public void generateSlip() {

        shoppingCartList = ShoppingCartItems.Companion.getShoppingCartList();
        String totalPrice = ShoppingCartItems.Companion.getTotalPrice();
        String totalKdv = ShoppingCartItems.Companion.getTotalTax();
        String customerName = ShoppingCartItems.Companion.getCustomerName();
        String orderNo = ShoppingCartItems.Companion.getOrderNo();
        String date = ShoppingCartItems.Companion.getDate();
        String time = ShoppingCartItems.Companion.getTime();


        try {
            FileOutputStream fos = new FileOutputStream(SlipHeaderFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            writer.write(Constants.ORTALA + "POS TERMINAL");
            writer.write("\n");
            writer.write(Constants.ORTALA + "PROTOKOL YOLU NO:45");
            writer.write("\n");
            writer.write(Constants.ORTALA + "10.YIL CADDESİ, 34010");
            writer.write("\n");
            writer.write(Constants.ORTALA + "REYHAN BİNASI KAT:8");
            writer.write("\n");
            writer.write(Constants.ORTALA + "MT BİLGİ TEKNOLOJİLERİ");
            writer.write("\n");
            writer.write(Constants.ORTALA + "BİRUNİ TEKNOPARK");
            writer.write("\n");
            writer.write(Constants.ORTALA + "TEL: (0212) 438 30 33");
            writer.write("\n");
            writer.write(Constants.ORTALA + " "); // Manuel Space
            writer.write("\n");
            writer.write(Constants.SOLA_YASLA + "Sipariş No:");
            writer.write(Constants.SOLA_YASLA + orderNo);
            writer.write("\n");
            writer.write(Constants.SOLA_YASLA + "Tarih: " + date);
            writer.write("\n");
            writer.write(Constants.SOLA_YASLA + "Saat: " + time);
            writer.write("\n");
            writer.write(Constants.ORTALA + " "); // Manuel Space
            writer.write("\n");
            writer.write(Constants.ORTALA + Constants.BORDER);
            writer.write("\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(SlipSQLFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            writer.write(Constants.SOLA_YASLA + "" + Constants.BOLD + "Ürün Adı");
            writer.write(Constants.ORTALA + String.format(Constants.PRODUCT_KDV_NAME, "KDV"));
            writer.write(Constants.SAGA_YASLA + String.format(Constants.PRODUCT_PRICE_NAME, "Fiyat"));
            writer.write("\n");
            for (int i = 0; i < shoppingCartList.size(); i++) {
                String urunAdi = shoppingCartList.get(i).getProductName();
                String kdvDegeri = shoppingCartList.get(i).getProductKdv();
                String fiyat = shoppingCartList.get(i).getProductPrice() + "," + shoppingCartList.get(i).getProductPriceCent();
                String adet = shoppingCartList.get(i).getProductQuantity();

                writer.write(Constants.SOLA_YASLA + "" + Constants.FONT48 + customFormat(adet + "x" + " " + urunAdi, 17));

                writer.write(Constants.ORTALA + String.format(Constants.PRODUCT_KDV, "%" + kdvDegeri));

                writer.write(Constants.SAGA_YASLA + String.format(Constants.PRODUCT_PRICE, fiyat));

                writer.write("\n");
            }

            writer.write(Constants.ORTALA + Constants.BORDER);
            writer.write("\n");
            writer.write(Constants.SAGA_YASLA + "" + Constants.FONTTOTALANDKDV + "TOPKDV: " + totalKdv);
            writer.write("\n");
            writer.write(Constants.SAGA_YASLA + "" + Constants.FONTTOTALANDKDV + "TOPLAM: " + totalPrice);
            writer.write("\n");
            writer.write(Constants.ORTALA + Constants.BORDER);
            writer.write("\n");


            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(SlipTailFilePath);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            writer.write(Constants.ORTALA + customerName);
            writer.write("\n");
            writer.write(Constants.ORTALA + "SATIŞ");
            writer.write("\n");
            writer.write(Constants.ORTALA + "VISA CREDIT");
            writer.write("\n");
            writer.write(Constants.ORTALA + "TC: 13549678650");
            writer.write("\n");
            writer.write(Constants.ORTALA + Constants.BORDER);
            writer.write("\n");
            writer.write(Constants.ORTALA + "TUTAR KARŞILIĞI MAL");
            writer.write("\n");
            writer.write(Constants.ORTALA + "VEYA");
            writer.write("\n");
            writer.write(Constants.ORTALA + "HİZMET ALDIM");
            writer.write("\n");
            writer.write(Constants.ORTALA + "PAROLA KULLANILMIŞTIR");
            writer.write("\n");
            writer.write(Constants.ORTALA + "BU BELGEYİ SAKLAYINIZ");
            writer.write("\n");
            writer.write(Constants.ORTALA + "(İŞ YERİ NÜSHASI)");
            writer.write("\n");

            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap slipBitmap = createBitmapFromTextFile(SlipHeaderFilePath, SlipSQLFilePath, SlipTailFilePath);
        SingletonBitmap.getInstance().setBitmap(slipBitmap);
    }


    private String customFormat(String text, int maxLength) {
        if (text.length() < maxLength) {
            int spacesToAdd = maxLength - text.length();
            StringBuilder formattedText = new StringBuilder(text);
            for (int i = 0; i < spacesToAdd; i++) {
                formattedText.append(" ");
            }
            return formattedText.toString();
        } else if (text.length() == maxLength) {
            return text;
        } else {
            String formattedText = text.substring(0, maxLength - 1) + ".";
            return formattedText;
        }
    }

    private int calculateTotalHeight(String filePath1, String filePath2, String filePath3) {
        int font24 = 0;
        int font48 = 0;
        int fontTotalAndKdv = 0;

        int totalHeight = 0;


        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
            BufferedReader reader2 = new BufferedReader(new FileReader(filePath2));
            BufferedReader reader3 = new BufferedReader(new FileReader(filePath3));
            String line;

            while ((line = reader1.readLine()) != null || (line = reader2.readLine()) != null || (line = reader3.readLine()) != null) {
                if (line != null) {

                    char tag2 = line.charAt(1);
                    char tag = line.charAt(0);


                    if (tag2 == Constants.BOLD) {
                        font48++;

                    } else if (tag2 == Constants.FONTTOTALANDKDV) {
                        fontTotalAndKdv++;


                    } else if (tag2 == Constants.FONT48) {
                        font48++;


                    } else if (tag==Constants.ORTALA || tag==Constants.SAGA_YASLA || tag==Constants.SOLA_YASLA){
                        font24++;
                    }

                     else {
                        font24++;

                    }

                }
            }

            System.out.println("48FONT" + font48);
            System.out.println("24FONT" + font24);
            System.out.println("TOTALKDVFONT" + fontTotalAndKdv);

            Paint paint2 = new Paint();
            paint2.setColor(Color.BLACK);
            paint2.setTextSize(Constants.FONT_SIZE_FOR_24_CHARACTERS);
            Paint.FontMetrics fm2 = paint2.getFontMetrics();
            totalHeight += (int) ((fm2.descent - fm2.ascent) * font24);

            Paint paint3 = new Paint();
            paint3.setColor(Color.BLACK);
            paint3.setTextSize(Constants.FONT_SIZE_FOR_48_CHARACTERS);
            Paint.FontMetrics fm3 = paint3.getFontMetrics();
            totalHeight += (int) ((fm3.descent - fm3.ascent) * font48);

            Paint paint4 = new Paint();
            paint4.setColor(Color.BLACK);
            paint4.setTextSize(Constants.FONTTOTALANDKDV);
            Paint.FontMetrics fm4 = paint4.getFontMetrics();
            totalHeight += (int) ((fm4.descent - fm4.ascent) * fontTotalAndKdv);

            System.out.println("TOTALKDVFONT" + totalHeight);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalHeight;
    }


    private Bitmap createBitmapFromTextFile(String filePath1, String filePath2, String filePath3) {
        int genislik = 384; // Slip genişliği
        int yukseklik = 1200; // Slip yüksekliği

        Bitmap slipBitmap = Bitmap.createBitmap(genislik, yukseklik, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(slipBitmap);
        Paint paint = new Paint();

        // Metin rengi, boyutu ve diğer özellikleri ayarla
        paint.setColor(Color.BLACK);
        paint.setTextSize(Constants.FONT_SIZE_FOR_24_CHARACTERS); // Yazı boyutunu ayarla
        Typeface monospaceTypeface = paint.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
        paint.setTypeface(monospaceTypeface);
        Typeface monospaceTypeface2 = paint.setTypeface(Typeface.create("monospace", Typeface.BOLD));

        Paint.FontMetrics fm = paint.getFontMetrics();

        int y = 0; // İlk satırın yüksekliği

        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
            BufferedReader reader2 = new BufferedReader(new FileReader(filePath2));
            BufferedReader reader3 = new BufferedReader(new FileReader(filePath3));
            String line;

            while ((line = reader1.readLine()) != null || (line = reader2.readLine()) != null || (line = reader3.readLine()) != null) {
                if (line != null) {
                    y += (int) (fm.descent - fm.ascent); // Satır yüksekliği hesapla

                    // İlk karakter tag'i temsil eder
                    char tag = line.charAt(0);

                    // İlk karakter tag'i temsil eder
                    char tag2 = line.charAt(1);


                    if (tag2 == Constants.BOLD) {
                        paint.setTypeface(monospaceTypeface2);
                        paint.setTextSize(Constants.FONT_SIZE_FOR_48_CHARACTERS);
                        line = line.substring(2); // Tag'i kaldır ve sadece metni al
                    } else if (tag2 == Constants.FONTTOTALANDKDV) {
                        paint.setTextSize(Constants.FONT_SIZE_FOR_TOTAL_AND_KDV);
                        paint.setTypeface(monospaceTypeface);
                        line = line.substring(2); // Tag'i kaldır ve sadece metni al
                    } else if (tag2 == Constants.FONT48) {
                        paint.setTextSize(Constants.FONT_SIZE_FOR_48_CHARACTERS);
                        paint.setTypeface(monospaceTypeface);
                        line = line.substring(2); // Tag'i kaldır ve sadece metni al
                    } else {
                        paint.setTextSize(Constants.FONT_SIZE_FOR_24_CHARACTERS);
                        paint.setTypeface(monospaceTypeface);
                        line = line.substring(1); // Tag'i kaldır ve sadece metni al
                    }


                    int xposition;

                    switch (tag) {
                        case Constants.ORTALA:
                            paint.setTextAlign(Paint.Align.CENTER);
                            xposition = canvas.getWidth() / 2; // Ortala
                            break;
                        case Constants.SOLA_YASLA:
                            paint.setTextAlign(Paint.Align.LEFT);
                            xposition = 0; // Sola yasla
                            break;
                        case Constants.SAGA_YASLA:
                            paint.setTextAlign(Paint.Align.RIGHT);
                            xposition = canvas.getWidth(); // Sağa yasla
                            break;
                        default:
                            paint.getTextAlign();
                            xposition = canvas.getWidth() / 1; // Sağa yasla
                            break;
                    }

                    canvas.drawText(line, xposition, y, paint);

                    y += (int) (fm.descent - fm.ascent) - 25; // Bir sonraki satırın yüksekliği
                }
            }

            reader1.close();
            reader2.close();
            reader3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return slipBitmap;
    }


    private static class Constants {

        static final char ORTALA = 0;
        static final char SOLA_YASLA = 1;
        static final char SAGA_YASLA = 2;
        static final char FONT48 = 4;
        static final char FONTTOTALANDKDV = 5;
        static final char BOLD = 6;
        static final char SPACE = 7;
        static final char HEADER = 8;
        static final char TAIL = 9;
        public static final String PRODUCT_KDV_NAME = "%21s";
        public static final String PRODUCT_KDV = "%12s";
        public static final String PRODUCT_PRICE = "%18s";
        public static final String PRODUCT_PRICE_NAME = "%18s";
        public static final String BORDER = "-----------------------";
        static final float FONT_SIZE_FOR_24_CHARACTERS = 26f;
        static final float FONT_SIZE_FOR_48_CHARACTERS = 14.99f;
        static final float FONT_SIZE_FOR_TOTAL_AND_KDV = 18f;


    }
}