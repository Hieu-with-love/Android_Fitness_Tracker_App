package hcmute.edu.vn.fitnesstrackerapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class OverlayView extends View {
    private Pose currentPose;
    private int imageWidth, imageHeight;
    private float scaleFactor, offsetX, offsetY;
    private final Paint paintCircle = new Paint();
    private final Paint paintLine   = new Paint();



    public OverlayView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setStrokeWidth(8f);
        paintCircle.setColor(Color.GREEN);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(4f);
        paintLine.setColor(Color.YELLOW);
    }

    /** Gọi trước mỗi lần nhận pose mới để set kích thước ảnh gốc */
    public void setImageSourceInfo(int width, int height) {
        this.imageWidth  = width;
        this.imageHeight = height;
        computeTransform();
    }

    /** Thiết lập pose và invalidate để onDraw() chạy lại */
    public void setPose(Pose pose) {
        this.currentPose = pose;
        postInvalidate();
    }

    /** Tính scale và offset dựa trên kích thước view và ảnh */
    private void computeTransform() {
        if (imageWidth == 0 || imageHeight == 0) return;
        float viewW = getWidth();
        float viewH = getHeight();
        // Giữ aspect ratio, có thể crop 2 chiều
        scaleFactor = Math.max(viewW / (float)imageWidth,
                viewH / (float)imageHeight);
        offsetX = (viewW - imageWidth * scaleFactor) / 2f;
        offsetY = (viewH - imageHeight * scaleFactor) / 2f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        computeTransform();
    }

    /** Chuyển PointF từ hệ ảnh gốc → hệ view :contentReference[oaicite:2]{index=2} */
    private PointF toViewCoords(PointF p) {
        float x = p.x * scaleFactor + offsetX;
        float y = p.y * scaleFactor + offsetY;
        return new PointF(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentPose == null) return;
        List<PoseLandmark> lmList = currentPose.getAllPoseLandmarks();
        if (lmList.isEmpty()) return;

        // Vẽ điểm mốc
        for (PoseLandmark lm : lmList) {
            PointF pt = toViewCoords(lm.getPosition());
            canvas.drawCircle(pt.x, pt.y, 8f, paintCircle);
        }
        // Vẽ các xương nối ví dụ :contentReference[oaicite:3]{index=3}
        drawLine(canvas, lmList, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW);
        drawLine(canvas, lmList, PoseLandmark.LEFT_ELBOW,    PoseLandmark.LEFT_WRIST);
        drawLine(canvas, lmList, PoseLandmark.RIGHT_SHOULDER,PoseLandmark.RIGHT_ELBOW);
        drawLine(canvas, lmList, PoseLandmark.RIGHT_ELBOW,   PoseLandmark.RIGHT_WRIST);
    }

    private void drawLine(Canvas c, List<PoseLandmark> lmList, int idx1, int idx2) {
        PointF p1 = toViewCoords(lmList.get(idx1).getPosition());
        PointF p2 = toViewCoords(lmList.get(idx2).getPosition());
        c.drawLine(p1.x, p1.y, p2.x, p2.y, paintLine);
    }
}
