/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author Tee
 */
public class NTS_SceneController implements Initializable {
    
    @FXML
    private Label lbResult;
    @FXML
    private Label lbNumber;
    @FXML
    private TextField txtNum;
    @FXML
    private TextField txtResult;
    @FXML
    private Button btSpeak;
    private Boolean isSpeaking = false;
    
    @FXML
    private void btSpeak_click(ActionEvent event) throws FileNotFoundException, JavaLayerException, IOException {
        Runnable task = () -> {
            try {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        changeStateOfBtSpeak(false);
                    }
                });
                isSpeaking = true;
                NumToSpeech.speakNumber(txtResult.getText());
                isSpeaking = false;
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        changeStateOfBtSpeak(true);
                    }
                });
            } catch (MalformedURLException ex) {
                Logger.getLogger(NTS_SceneController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(NTS_SceneController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(NTS_SceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        
        if (!isSpeaking && txtResult.getText().length() > 0) {
            Thread thread = new Thread(task);
            thread.start();
        }
    }
    
    private void changeStateOfBtSpeak(Boolean state) {
        String imgSrc = state ? "/img/speaker_black.png" : "/img/loading.gif";
        Image speakerImage = new Image(getClass().getResourceAsStream(imgSrc));
        ImageView ivSpeaker = new ImageView(speakerImage);
        ivSpeaker.setFitHeight(16);
        ivSpeaker.setFitWidth(16);
        String strBtSpeak = state ? "Đọc" : " Đang đọc";
        btSpeak.setGraphic(ivSpeaker);
        btSpeak.setText(strBtSpeak);
        btSpeak.setDisable(!state);
        txtNum.setDisable(!state);
        txtNum.requestFocus();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtResult.setStyle("-fx-text-inner-color: #6f6f6f;-fx-background-color:#FFFCA8");
        changeStateOfBtSpeak(true);

        txtNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                //Xu ly khi nhap chuoi so co chua ky tu khac so
                if (!newValue.matches("\\d*")) {
                    newValue = newValue.replaceAll("[^\\d]", "");
                    txtNum.setText(newValue);
                }
                
                //Xu ly chu so dau tien cua chuoi la 0
                if (newValue.matches("^0+.*")) {
                    newValue = newValue.replaceAll("^0+", "");
                    txtNum.setText(newValue);
                }
                
                //Gioi han do dai chu so duoc phep nhap, gioi han boi do dai textfield
                if (newValue.length() > 30) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Vượt độ dài cho phép.");
                    alert.setContentText("Chỉ được nhập số có không quá 30 chữ số!");
                    alert.showAndWait();
                    newValue = newValue.substring(0, 30);
                    txtNum.setText(newValue);
                }
                
                String sourceNumber = txtNum.getText();
                if (sourceNumber.length() > 0) {
                    String textResult = NumToSpeech.getText(sourceNumber);
                    lbNumber.setText(NumToSpeech.displayString(sourceNumber));            
                    txtResult.setText(textResult);
                }
            }
        });
    }    
    
}
