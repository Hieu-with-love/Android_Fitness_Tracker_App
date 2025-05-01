package hcmute.edu.vn.fitnesstrackerapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import hcmute.edu.vn.fitnesstrackerapp.R;

public class ReviewActivity extends AppCompatActivity {
    private ImageView ivCaptured;
    private TextView tvPoseName;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ImageView iv = findViewById(R.id.ivCapturedImage);
        TextView tv = findViewById(R.id.tvPoseName);
        Button btnBack = findViewById(R.id.btnBack);

        // Lấy URI từ Intent
        String uriString = getIntent().getStringExtra("capturedImageUri");
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            iv.setImageURI(uri);
        }

        // TODO: Phân tích pose từ ảnh (nếu cần), set tv.setText(...)
        boolean prevFront = getIntent().getBooleanExtra("useFrontCamera", false);
        String exerciseName = getIntent().getStringExtra("exerciseName");
        if (exerciseName == null) {
            exerciseName = "Unknown";
        }
        tv.setText("Detected: " + exerciseName);

        btnBack.setOnClickListener(v -> finish());
    }

}
