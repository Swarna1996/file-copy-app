package lk.ijse.dep10.app.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;

public class MainFormController {

    public Button btnReset;
    @FXML
    private Button btnCopy;

    @FXML
    private Button btnSelectDestination;

    @FXML
    private Button btnSelectFile;


    @FXML
    private Label lblSelectedDestination;

    @FXML
    private Label lblSelectedFile;

    @FXML
    private ProgressBar prgBar;

    private File openedFilePath;
    private File filePathToBePaste;
    private String openedFileName;

    private double openFileSize=0.0;
    private double copiedFileSize=0.0;
    public void initialize(){

    }
    @FXML
    void btnCopyOnAction(ActionEvent event) throws IOException {
        FileInputStream fis = new FileInputStream(openedFilePath);
        File pasteFilePath = new File(filePathToBePaste + File.separator + openedFileName);

        FileOutputStream fos = new FileOutputStream(pasteFilePath);
//        System.out.println(openFileSize);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
//                    Thread.sleep(500);
                    byte[] buffer = new byte[1024]; //10kb
                    int read = fis.read(buffer);
                    if (read==-1)break;
                    fos.write(buffer,0,read);
                    copiedFileSize += read;

                    updateProgress(copiedFileSize,openFileSize);
//
                    System.out.println("copied file size : "+copiedFileSize);
                }
                return null;
            }
        };
        new Thread(task).start();

        prgBar.progressProperty().bind(task.progressProperty());

        System.out.println("copied");
        fis.close();
        fos.close();
    }

    @FXML
    void btnSelectDestinationOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select the destination to paste the file");
        File file = directoryChooser.showDialog(btnSelectFile.getScene().getWindow());
        if (file==null)return;
        filePathToBePaste = file;
        lblSelectedDestination.setText(String.valueOf(file));

    }

    @FXML
    void btnSelectFileOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File to Copy");
        File file = fileChooser.showOpenDialog(btnSelectFile.getScene().getWindow());
        if (file==null)return;
        openedFilePath = file;
        lblSelectedFile.setText(String.valueOf(file));
        openedFileName = file.getName();
        openFileSize = file.length();
    }


    public void btnResetOnAction(ActionEvent actionEvent) {
        lblSelectedDestination.setText("empty");
        lblSelectedFile.setText("empty");
    }
}
