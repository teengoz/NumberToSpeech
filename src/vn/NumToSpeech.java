/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Tee
 */
public class NumToSpeech {
    private static String[] textNum = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", ""};
    private static String[] textSpecialNum1 = {"mươi", "mốt", "", "", "tư", "lăm"};
    private static String[] textLevel1 = {"", "mươi", "trăm"};
    private static String[] textLevel2 = {"tỷ", "nghìn", "triệu", ""};
    private static String specialWord0x = "lẻ";
    private static String specialWord1x = "mười";
    
    public static String getText(String source) {
        if (validString(source)) {
            //Ket qua tra ve sau khi chuyen tu so sang chuoi la mot mang cac chuoi
            List<String> textList = NumToText(source);
            String result = "";
            if (textList.size() > 0) {
                result = String.join(" ", textList);
                result = result.substring(0, 1).toUpperCase() + result.substring(1);
            }
            return result;
        }
        return "Không hợp lệ";
    }
    
    private static Boolean validString(String source) {
        //Kiem tra chuoi nhap vao la chuoi so hop le va do dai > 0
        return source.matches("^\\d+$") && (source.length() > 0);
    }
    
    public static String displayString(String source) {
        //Tach chuoi so thanh cac bo 3 de hien thi
        List<String> listSring = StringToArray3(source);
        Collections.reverse(listSring);
        return String.join(",", listSring);
    }
    
    private static List<String> StringToArray3(String source) {
        //Tach chuoi so thanh cac bo 3 de xu ly chuyen doi
        List<String> temp = new ArrayList<String>();
        Integer n = 0;
        
        for (int i = source.length() - 3; i >= -2; i=i-3) {
            if (i < 0) {
                temp.add(source.substring(0, i + 3));
            } else {
                temp.add(source.substring(i, i + 3));
            }
        }
        return temp;
    }
    
    private static List<String> NumToText(String source) {
        //Xu ly theo tung bo 9 de tuan hang don vi Ty
        List<String> textFromNum = Go9ToText(source);
        return textFromNum;
    }
    
    private static List<String> Go9ToText(String source) {
        List<String> groups = StringToArray3(source);
        List<String> textFromNum = new ArrayList<>();
        for (int i = groups.size()-1; i >=0 ; i--) {
            String group = groups.get(i);
            List<String> textOf3 = Go3ToText(group);
            Integer idx = (i==0) ? 3 : i%3;
            String level2OfNum = "";
            
            //Don vi cap do 2 cua cac chu so la Ty, Trieu, Nghin
            //idx dung de xac dinh vi tri cua bo 3 dang xet trong bo 9
            if (textOf3.size() <= 0 && (idx % 3 != 0)) {
                level2OfNum = "";
            } else {
                level2OfNum = textLevel2[idx];
            }
                
            textFromNum.addAll(textOf3);
            if (level2OfNum != "") {
                textFromNum.add(level2OfNum);
            }
        }
        return textFromNum;
    }
    
    private static List<String> Go3ToText(String strGo3) {
        List<String> textFromGo3 = new ArrayList<>();
        
        Integer numGo3 = Integer.parseInt(strGo3);
        if (numGo3 == 0) {
            return textFromGo3;
        } else {
            Integer hundreds = numGo3/100;
            Integer tens = (numGo3%100)/10;
            Integer units = numGo3%10;
            Boolean hasHundreds = (strGo3.length() == 3);
            Boolean hasTens = (strGo3.length() >= 2);
            
            /*** Xu ly hang tram ***/
            if (hasHundreds) {
                textFromGo3.add(textNum[hundreds]);
                textFromGo3.add(textLevel1[2]); //trăm
                if (tens == 0 && units !=0) {
                    textFromGo3.add(specialWord0x); //linh
                }
            }
            /**********************/
            
            /*** Xu ly hang chuc ***/
            if (tens > 1) {
                textFromGo3.add(textNum[tens]);
                textFromGo3.add(textLevel1[1]); //mươi
            }
            else if (tens == 1) {
                textFromGo3.add(specialWord1x); //mười
            }
            /************************/
            
            /*** Xu ly hang don vi ***/
            if (units > 0) {
                if (tens >= 2 && (units == 1 || units == 4 || units == 5)) {
                    textFromGo3.add(textSpecialNum1[units]);
                }
                else if (tens == 1 && units == 5) {
                    textFromGo3.add(textSpecialNum1[units]);
                }
                else if (tens == 0 && units == 4) {
                    textFromGo3.add(textSpecialNum1[units]);
                }
                else {
                    textFromGo3.add(textNum[units]);
                }
            }
            /************************/
        }
        return textFromGo3;
    }
    
    public static void speakNumber(String text) throws MalformedURLException, JavaLayerException, UnsupportedEncodingException {
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String req = "https://code.responsivevoice.org/getvoice.php?t=" + encodedText + "&tl=vi&sv=&vn=&pitch=0.5&rate=0.5&vol=1";
        InputStream input = read(new URL(req));
        if (input != null) {
            Player player = new Player(input);
            player.play();
        } else {
            return;
        }
    }
    
    private static InputStream read(URL url) {
        InputStream result = null;
        try {
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            //Dat gia tri time out cho ket noi
            httpcon.setConnectTimeout(500);
            httpcon.setReadTimeout(10000);
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            result = httpcon.getInputStream();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
