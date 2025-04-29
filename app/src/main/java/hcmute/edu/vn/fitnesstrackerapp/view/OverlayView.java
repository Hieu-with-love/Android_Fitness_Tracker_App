package hcmute.edu.vn.fitnesstrackerapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class OverlayView extends View {
    private List<PointF> keypoints = new ArrayList<>();

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeypoints(List<PointF> keypoints) {
        this.keypoints = keypoints;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(8f);

        for (PointF point : keypoints) {
            canvas.drawCircle(point.x, point.y, 10f, paint);
        }
    }
}
