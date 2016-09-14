/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
    private void btSpeak_click(ActionEvent event) throws FileNotFoundException, JavaLayerException, IOException, InterruptedException {
        Runnable task = () -> {
            try {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        changeStateOfBtSpeak(false);
                    }
                });
                isSpeaking = true;
                if (checkInternetConnection("google.com")) {
                    NumToSpeech.speakNumber(txtResult.getText());
                } else {
                    Platform.runLater(new Runnable() {
                    @Override public void run() {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText("Lỗi kết nối.");
                        alert.setContentText("Vui lòng kiểm tra lại kết nối mạng!");
                        alert.showAndWait();
                    }
                });
                }
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
            } catch (IOException ex) {
                Logger.getLogger(NTS_SceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        if (!isSpeaking && txtResult.getText().length() > 0) {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
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
    
    private Boolean checkInternetConnection(String strUrl) throws IOException{
        try(Socket socket = new Socket()) {
            int port = 80;
            InetSocketAddress socketAddress = new InetSocketAddress(strUrl, 80);
            socket.connect(socketAddress, 3000);
            
            return true;
        }
        catch(UnknownHostException unknownHost) {
            return false;
        }
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
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText("Ậy, sai rồi. Đừng nhập ẩu như vậy chứ!");
                    alert.setContentText("Bạn chỉ được nhập số nguyên dương. Nhập không đúng thì tui không có xử lý được đâu nha!");
                    alert.showAndWait();
                    newValue = newValue.substring(0, 30);
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
                    alert.setHeaderText("Ậy, từ từ nào.");
                    alert.setContentText("Bạn chỉ được nhập số có không quá 30 chữ số thôi nhé!");
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
            } else {
                lbNumber.setText("");
                txtResult.clear();
            }
        });
    }    
    
}
