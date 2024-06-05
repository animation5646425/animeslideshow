import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * スライドショーを表示するアプリケーションのクラス
 */
public class SlideshowApp {
    private List<String> imageFiles;  // 画像ファイルのリスト
    private int currentIndex = 0;     // 現在の画像のインデックス
    private BufferedImage currentImage; // 現在表示されている画像
    private Canvas canvas;

    /**
     * アプリケーションのエントリーポイント
     * @param args コマンドライン引数（未使用）
     */
    public static void main(String[] args) {
        new SlideshowApp().perform();
    }

    /**
     * コンストラクタで画像ファイルのリストを初期化
     */
    public SlideshowApp() {
        imageFiles = new ArrayList<>();
        imageFiles.add("image1.jpg");
        imageFiles.add("image2.jpg");
        imageFiles.add("image3.jpg");
        // 追加の画像ファイルをここに追加
        loadCurrentImage();
    }

    /**
     * スライドショーを実行
     */
    public void perform() {
        JFrame mainFrame = new JFrame("SlideshowApp");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);
        canvas = new Canvas() {
            /**
             * 画像を描画。
             * @param g グラフィックスオブジェクト
             */
            public void paint(Graphics g) {
                drawImage(g);
            }
        };
        mainFrame.add(canvas);
        mainFrame.setVisible(true);

        TimerTask task = new TimerTask() {
            public void run() {
                nextImage();
                canvas.repaint();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 3000); // 3秒ごとに画像を変更
    }

    /**
     * 現在の画像を描画
     * @param g グラフィックスオブジェクト
     */
    public void drawImage(Graphics g) {
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        }
    }

    /**
     * 次の画像に切り替え
     */
    public void nextImage() {
        currentIndex = (currentIndex + 1) % imageFiles.size();
        loadCurrentImage();
    }

    /**
     * 現在の画像を読み込む
     */
    public void loadCurrentImage() {
        try {
            currentImage = ImageIO.read(new File(imageFiles.get(currentIndex)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}