import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 画像を3倍拡大して画面の中央に表示するスライドショーのJavaアプリケーション
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Slideshow App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800); // 600から800に変更
            frame.add(new SlideshowCanvas());
            frame.setVisible(true);
        });
    }
}

/**
 * スライドショーを描画するCanvasクラス
 */
class SlideshowCanvas extends Canvas {
    private BufferedImage[] images;
    private int currentImageIndex = 0;

    /**
     * 画像を読み込んで初期化
     */
    public SlideshowCanvas() {
        loadImages();
        startSlideshow();
    }

    /**
     * 画像を読み込む
     */
    private void loadImages() {
        images = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            String filename = "image" + (i + 1) + ".jpg";
            try {
                images[i] = ImageIO.read(new File(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * スライドショーを開始
     */
    private void startSlideshow() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentImageIndex = (currentImageIndex + 1) % images.length;
                repaint();
            }
        }, 0, 1000); // 切り替え間隔を1秒に設定
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage currentImage = images[currentImageIndex];
        double scale = 3.0; // 画像を3倍に拡大
        int scaledWidth = (int) (currentImage.getWidth() * scale);
        int scaledHeight = (int) (currentImage.getHeight() * scale);
        double anchorX = scaledWidth / 2.0;
        double anchorY = scaledHeight / 2.0;
        int currentSeconds = Calendar.getInstance().get(Calendar.SECOND);
        double rotateDegree = currentSeconds / 60.0 * 360.0;
        double rotateRadian = Math.toRadians(rotateDegree);
        AffineTransform transform = AffineTransform.getRotateInstance(rotateRadian, anchorX, anchorY);
        transform.scale(scale, scale); // 3倍に拡大
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
        g.drawImage(transformOp.filter(currentImage, null), getWidth() / 2 - scaledWidth / 2, getHeight() / 2 - scaledHeight / 2, null);
    }
}