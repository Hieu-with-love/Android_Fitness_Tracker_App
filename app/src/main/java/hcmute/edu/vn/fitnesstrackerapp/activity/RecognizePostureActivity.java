package hcmute.edu.vn.fitnesstrackerapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hcmute.edu.vn.fitnesstrackerapp.R;
import hcmute.edu.vn.fitnesstrackerapp.view.OverlayView;


public class RecognizePostureActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private PreviewView previewView;
    private OverlayView overlayView;
    private Executor cameraExecutor;
    private PoseDetector poseDetector;

    private ImageButton btnFlipCamera;

    private boolean useFrontCamera = false;

    private String currentExercise="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_posture);

        previewView = findViewById(R.id.previewView);
        overlayView = findViewById(R.id.overlayView);
//        tvPoseResult = findViewById(R.id.tvPoseResult);

        btnFlipCamera = findViewById(R.id.btnFlipCamera);
        btnFlipCamera.setOnClickListener(v -> {
            useFrontCamera = !useFrontCamera;
            startCamera();
        });
        PoseDetectorOptions options =
                new PoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);
        cameraExecutor = ContextCompat.getMainExecutor(this);


        startCamera();
        // Kết hợp timer chụp ảnh sau 20s
        new Handler(Looper.getMainLooper()).postDelayed(this::captureAndReview, 20_000);

    }


    private void captureAndReview() {
        // 1. Lấy bitmap preview và overlay
        Bitmap previewBitmap = previewView.getBitmap();
        if (previewBitmap == null) return;
        Bitmap outputBitmap = previewBitmap.copy(previewBitmap.getConfig(), true);
        Canvas canvas = new Canvas(outputBitmap);
        overlayView.draw(canvas);

        try {
            // 2. Tạo file trong cache/images
            File imagesDir = new File(getCacheDir(), "images");
            if (!imagesDir.exists()) imagesDir.mkdirs();
            File imageFile = new File(imagesDir, "capture.png");

            FileOutputStream fos = new FileOutputStream(imageFile);
            outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush(); fos.close();

            // 3. Lấy URI qua FileProvider
            Uri uri = FileProvider.getUriForFile(
                    this,
                    "hcmute.edu.vn.fitnesstrackerapp.fileprovider",
                    imageFile
            );

            // 4. Bắn Intent chỉ truyền URI
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("capturedImageUri", uri.toString());
            intent.putExtra("useFrontCamera", useFrontCamera);
            intent.putExtra("exerciseName", currentExercise);
            // cấp quyền đọc URI tạm thời
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("RecognizePosture", "bind camera use cases failed", e);
            }
        }, cameraExecutor);
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        // Preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Image analysis
        ImageAnalysis analysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        analysis.setAnalyzer(cameraExecutor, this::analyzeImage);

        // Chọn camera trước/sau
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(useFrontCamera
                        ? CameraSelector.LENS_FACING_FRONT
                        : CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                analysis
        );
    }


    @SuppressLint("UnsafeOptInUsageError")
    private void analyzeImage(ImageProxy imageProxy) {
        if (imageProxy.getImage() == null) {
            imageProxy.close();
            return;
        }
        // Trước hết, cập nhật kích cỡ ảnh gốc cho overlay :contentReference[oaicite:4]{index=4}
        overlayView.setImageSourceInfo(
                imageProxy.getWidth(),
                imageProxy.getHeight()
        );

        InputImage image = InputImage.fromMediaImage(
                imageProxy.getImage(),
                imageProxy.getImageInfo().getRotationDegrees()
        );
        poseDetector.process(image)
                .addOnSuccessListener(this::onPoseDetected)
                .addOnCompleteListener(task -> imageProxy.close());
    }


    private void onPoseDetected(Pose pose) {
        // Cập nhật overlay với pose mới
        overlayView.setPose(pose);
        // TODO: phân tích pose để nhận diện plank, donkey kick, ...
        currentExercise = PostureDetector.detect(pose);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                finish();
            }
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        poseDetector.close();
    }
}