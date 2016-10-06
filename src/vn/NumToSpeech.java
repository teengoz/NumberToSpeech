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
    private static String[] textLevel2 = {"tỷ", "ngàn", "triệu", ""};
    private static String specialWord0x = "lẻ";
    private static String specialWord1x = "mười";
    
    public static String getText(String source) {
//        if (source.matches("^0+.*") && !source.matches("^0$")) 
//            source = source.replaceAll("^0+", "");
        if (validPosNum(source)) {
            //Ket qua tra ve sau khi chuyen tu so sang chuoi la mot mang cac chuoi
            String result = numToText(source);
            result = result.substring(0, 1).toUpperCase() + result.substring(1);
            return result;
        }
        return "Không hợp lệ";
    }
    
    private static String numToText(String source) {
        //Xu ly theo tung bo 9 de tuan hang don vi Ty
        String textFromNum = go9ToText(source);
        return textFromNum;
    }
    
    public static String go9ToText(String source) {
        List<String> groups = stringToArray3(source);
        String textFromNum = "";
        for (int i = groups.size()-1; i >=0 ; i--) {
            String group = groups.get(i);
            String textOf3 = go3ToText(group);
            Integer idx = (i==0) ? 3 : i%3;
            String level2OfNum = "";
            
            //Don vi cap do 2 cua cac chu so la Ty, Trieu, Nghin
            //idx dung de xac dinh vi tri cua bo 3 dang xet trong bo 9
            if (textOf3.length() <= 0 && (idx % 3 != 0)) {
                level2OfNum = "";
            } else {
                level2OfNum = textLevel2[idx];
            }
            if (!textOf3.isEmpty())    
                textFromNum += (textFromNum.isEmpty()) ? textOf3 : " " + textOf3;
            if (level2OfNum != "")
                textFromNum += (textFromNum.isEmpty()) ? level2OfNum : " " + level2OfNum;
        }
        return textFromNum;
    }
    
    public static String go3ToText(String strGo3) {
        //Xu ly cho moi bo 3 so
        String textFromGo3 = "";
        
        if (!tryParseInt(strGo3)) {
            return textFromGo3;
        } else {
            Integer numGo3 = Integer.parseInt(strGo3);
            Integer length = strGo3.length();
            
            if (length > 1 && numGo3 == 0) {
                return textFromGo3;
            }
            
            /*** Xu ly hang tram ***/
            //textFromGo3.addAll(hundredsToString(strGo3.length(), hundreds, tens, units));
            String strHundreds = hundredsToString(strGo3);
            /**********************/
            
            /*** Xu ly hang chuc ***/
            String strTens = tensToString(strGo3);
            /************************/
            
            /*** Xu ly hang don vi ***/
            String strUnits = unitsToString(strGo3);
            /************************/
            
            textFromGo3 += strHundreds;
            if (!strTens.isEmpty())
                textFromGo3 += (textFromGo3.isEmpty()) ? strTens : " " + strTens;
            if (!strUnits.isEmpty())
                textFromGo3 += (textFromGo3.isEmpty()) ? strUnits : " " + strUnits;
        }
        return textFromGo3;
    }
    
    public static String hundredsToString(String strGo3) {
        Integer numGo3 = Integer.parseInt(strGo3);
        Integer hundreds = numGo3/100;
        Integer length = strGo3.length();        

        //Xu ly cho hang tram
        String strHundreds = "";
        if (length == 3 && numGo3 > 99) {
            strHundreds = textNum[hundreds] + " " + textLevel1[2]; //"n" trăm
        }
        return strHundreds;
    }
    
    public static String tensToString(String strGo3) {
        Integer numGo3 = Integer.parseInt(strGo3);
        Integer tens = (numGo3%100)/10;
        Integer units = numGo3%10;
        Integer length = strGo3.length();        

        //Xu ly cho hang chuc
        String strTens = "";
        if (length == 1 || numGo3 < 10) {
            return strTens;
        }
        else if (tens > 1) {
            strTens = textNum[tens] + " " + textLevel1[1];//"n" mươi
        }
        else if (tens == 1) {
            strTens = specialWord1x; //mười
        }
        else if (tens == 0 && units !=0) {
            strTens = specialWord0x; //lẻ
        }
        return strTens;
    }
    
    public static String unitsToString(String strGo3) {
        Integer numGo3 = Integer.parseInt(strGo3);
        Integer hundreds = numGo3/100;
        Integer tens = (numGo3%100)/10;
        Integer units = numGo3%10;
        Integer length = strGo3.length();
        
        //Xu ly cho hang don vi
        String strUnits = "";
        if (length > 1) {
            if (tens >= 2 && (units == 1 || units == 5)) {
                strUnits = textSpecialNum1[units];
            }
            else if (tens == 1 && units == 5) {
                strUnits = textSpecialNum1[units];
            }
            else if (units > 0) {
                strUnits = textNum[units];
            }
        }
        else if (length == 1) {
            strUnits = textNum[units];
        }
        return strUnits;
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
    
    public static Boolean validPosNum(String source) {
        //Kiem tra chuoi nhap vao la chuoi so nguyen > 0
        if (source.matches("^[1-9]\\d*$") || source.matches("^0$")) {
            return true;
        }
        return false;
    }
    
    public static String displayString(String source) {
        //Tach chuoi so thanh cac bo 3 de hien thi
        List<String> listSring = stringToArray3(source);
        Collections.reverse(listSring);
        return String.join(",", listSring);
    }
    
    private static List<String> stringToArray3(String source) {
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
    
    private static boolean tryParseInt(String value) {  
        try {  
            Integer.parseInt(value);  
            return true;  
        } catch (NumberFormatException e) {  
            return false;  
        }  
    }
}
