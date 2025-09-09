import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;


public class Wallpaper 
{
    private static final Color[] DAY_COLORS = {Color.GREEN, Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW, Color.RED, Color.GREEN};
    Graphics2D g;
    BufferedImage image;
    File wallpaper;

    public Wallpaper() {
        image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        wallpaper = new File("image\\wallpaper.png");

        try {
            wallpaper.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void testChangeImage() {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1920, 1080);
        g.setColor(Color.RED);
        g.drawRect(500,500,50,50);
        g.drawString((new Date()).toString(), 300, 300);

        updateScreen();
    }

    public void update(String[] forecast) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1920, 1080);
        g.setFont(new Font("Consolas", 0, 20));

        for (int i = 0; i < forecast.length; i++) 
        {
            g.setColor(DAY_COLORS[i]);

            String[] lines = forecast[i].split("\n");
            int lineHeight = g.getFontMetrics().getHeight();

            for (int y = 0; y < lines.length; y++)
                g.drawString(lines[y], 50 + i * 270, 100 + y * lineHeight);
        }

        g.setColor(Color.BLUE);
        g.drawString("Updated: " + LocalDate.now() + "  " + LocalTime.now(), 20, 30);

        updateScreen();
    }

    private void updateScreen() {
        try {
            FileUtils.cleanDirectory(new File("C:/code/weatherBackground/image"));
            ImageIO.write(image, "png", wallpaper);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
