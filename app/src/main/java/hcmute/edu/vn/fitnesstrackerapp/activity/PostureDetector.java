package hcmute.edu.vn.fitnesstrackerapp.activity;

import android.graphics.PointF;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostureDetector {
    /**
     * Nhận diện tư thế từ một Pose, trả về tên bài tập.
     */
    public static String detect(Pose pose) {
        Map<Integer, PointF> pts = extractPoints(pose);
        if (isPlank(pts)) {
            return "Plank";
        } else if (isDonkeyKick(pts, true)) {
            return "Donkey Kick (Right)";
        } else if (isDonkeyKick(pts, false)) {
            return "Donkey Kick (Left)";
        } else {
            return "Unknown";
        }
    }

    private static Map<Integer, PointF> extractPoints(Pose pose) {
        Map<Integer, PointF> map = new HashMap<>();
        for (PoseLandmark lm : pose.getAllPoseLandmarks()) {
            map.put(lm.getLandmarkType(), lm.getPosition());
        }
        return map;
    }

    // Plank: vai-hip-knee thẳng hàng
    private static boolean isPlank(Map<Integer, PointF> pts) {
        if (!pts.containsKey(PoseLandmark.LEFT_SHOULDER) ||
                !pts.containsKey(PoseLandmark.LEFT_HIP) ||
                !pts.containsKey(PoseLandmark.LEFT_KNEE) ||
                !pts.containsKey(PoseLandmark.RIGHT_SHOULDER) ||
                !pts.containsKey(PoseLandmark.RIGHT_HIP) ||
                !pts.containsKey(PoseLandmark.RIGHT_KNEE)) {
            return false;
        }
        double angleL = angleBetween(
                pts.get(PoseLandmark.LEFT_SHOULDER),
                pts.get(PoseLandmark.LEFT_HIP),
                pts.get(PoseLandmark.LEFT_KNEE)
        );
        double angleR = angleBetween(
                pts.get(PoseLandmark.RIGHT_SHOULDER),
                pts.get(PoseLandmark.RIGHT_HIP),
                pts.get(PoseLandmark.RIGHT_KNEE)
        );
        return Math.abs(angleL - 180) < 12 && Math.abs(angleR - 180) < 12;
    }

    // Donkey Kick: hip-knee-ankle thẳng hàng, sideFlag = true bên phải
    private static boolean isDonkeyKick(Map<Integer, PointF> pts, boolean sideFlag) {
        int hip = sideFlag ? PoseLandmark.RIGHT_HIP : PoseLandmark.LEFT_HIP;
        int knee = sideFlag ? PoseLandmark.RIGHT_KNEE : PoseLandmark.LEFT_KNEE;
        int ankle = sideFlag ? PoseLandmark.RIGHT_ANKLE : PoseLandmark.LEFT_ANKLE;
        // kiểm tra tồn tại
        if (!pts.containsKey(hip) || !pts.containsKey(knee) || !pts.containsKey(ankle)) {
            return false;
        }
        double angle = angleBetween(
                pts.get(hip), pts.get(knee), pts.get(ankle)
        );
        return Math.abs(angle - 180) < 15;
    }

    /**
     * Tính góc (A-B-C) tại điểm B
     */
    private static double angleBetween(PointF A, PointF B, PointF C) {
        double vABx = A.x - B.x, vABy = A.y - B.y;
        double vCBx = C.x - B.x, vCBy = C.y - B.y;
        double dot = vABx * vCBx + vABy * vCBy;
        double magAB = Math.hypot(vABx, vABy);
        double magCB = Math.hypot(vCBx, vCBy);
        if (magAB == 0 || magCB == 0) return 0;
        double cos = dot / (magAB * magCB);
        cos = Math.max(-1, Math.min(1, cos));
        return Math.toDegrees(Math.acos(cos));
    }
}
