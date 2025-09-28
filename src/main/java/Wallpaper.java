import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;


public class Wallpaper 
{
    Graphics2D g;
    BufferedImage image;
    File wallpaper;
    int precipitation;

    public Wallpaper() {
        image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        wallpaper = new File("image\\wallpaper.png");
        precipitation = 0;

        try {
            wallpaper.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrecipitation(int p) {
        precipitation = p;
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

        try {
            Image img;
            if (precipitation > 70) 
                img = ImageIO.read(new File("backgrounds\\storm\\lightning.jpg"));
            else if (precipitation > 10)
                img = ImageIO.read(new File("backgrounds\\rain\\raindrops.jpg"));
            else
                img = ImageIO.read(new File("backgrounds\\sun\\sky.jpg"));

            g.drawImage(img, 0, 0, null);
        }
        catch(IOException e) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 1920, 1080);
        }        

        g.setColor(new Color(0,0,0,100));
        g.fillRoundRect(20, 60, 1890, 660, 70, 70);

        g.setFont(new Font("Cascadia Code", 0, 20));

        for (int i = 0; i < forecast.length; i++) 
        {

            String[] lines = forecast[i].split("\n");
            int lineHeight = g.getFontMetrics().getHeight();

            if (i > 0) {
                g.setColor(new Color(250, 250, 250, 175));
                g.fillRoundRect(18 + i * 270, 100, 5, 600, 5, 5);
            }

            g.setColor(new Color(140, 255, 140));
            for (int y = 0; y < lines.length; y++)
                g.drawString(lines[y], 37 + i * 270, 100 + y * lineHeight);
        }

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
