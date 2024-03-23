import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class CameraStream extends Application {
    
    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private ImageView imageView = new ImageView();
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        // Create a Stage and ImageView to display the camera stream
        HBox hbox = new HBox(imageView);
        Scene scene = new Scene(hbox);
        stage.setScene(scene);
        stage.show();

        // Initialize the haarcascade classifier
        CascadeClassifier classifier = new CascadeClassifier();
        classifier.load("path/to/haarcascade_frontalface_alt.xml"); // Make sure to use the correct path

        // Open the camera with VideoCapture
        VideoCapture camera = new VideoCapture(0);

        // AnimationTimer to refresh and display the camera stream
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                imageView.setImage(grabFrame(camera, classifier));
            }
        }.start();
    }
    
    private Image grabFrame(VideoCapture camera, CascadeClassifier classifier) {
        Mat frame = new Mat();
        if (camera.read(frame)) {
            // Here, you can process the frame for face detection with classifier if needed
            return mat2Img(frame);
        }
        return null;
    }

    private Image mat2Img(Mat mat) {
        MatOfByte bytes = new MatOfByte();
        Imgcodecs.imencode(".bmp", mat, bytes);
        InputStream inputStream = new ByteArrayInputStream(bytes.toArray());
        return new Image(inputStream);
    }
}
