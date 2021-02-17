import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Picture {

	static int id = 0;
	static ArrayList<String> ListImages = new ArrayList<>();
	String img_name;
	BufferedImage buf_img;
	int width;
	int height;
	
	public Picture(String name) {
	    this.img_name = name;
	    try {
	        buf_img = ImageIO.read(new File(img_name));
	    } catch (IOException ex) {
	        //
	    }
	}
	
	public Picture(int w, int h) {
	    this.width = w;
	    this.height = h;
	    buf_img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public int width() {
	    width = buf_img.getWidth();
	    return width;
	}
	
	public int height() {
	    height = buf_img.getHeight();
	    return height;
	}
	
	public Color get(int col, int row) {
	    Color color = new Color(buf_img.getRGB(col, row));
	    return color;
	}
	
	public void set(int col, int row, Color color) {
	    buf_img.setRGB(col, row, color.getRGB());
	}
	
	public void show() {
	    try {
	    	String imageAddres = ("image/" + id + ".png");
	        File saveAs = new File(imageAddres);
	        ImageIO.write(buf_img, "png", saveAs);
	        id++;
	        ListImages.add(imageAddres);
	       // Desktop.getDesktop().open(saveAs);
	    } catch (IOException ex) {
	        //
	    }
	}
}
