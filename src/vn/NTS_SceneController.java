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
        
        if (!isSpeaking) {
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
        btSpeak.setGraphic(ivSpeaker);
        btSpeak.setDisable(!state);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtResult.setStyle("-fx-text-inner-color: #6f6f6f;-fx-background-color:#FFFCA8");
        changeStateOfBtSpeak(true);
        
        txtNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
                txtNum.setText(newValue);
            }
            
            if (newValue.matches("^0+.*")) {
                newValue = newValue.replaceAll("^0+", "");
                txtNum.setText(newValue);
            }
            
            String textResult = NumToSpeech.getText(newValue);
            lbNumber.setText(NumToSpeech.displayString(newValue));            
            txtResult.setText(textResult);
        });
    }    
    
}
