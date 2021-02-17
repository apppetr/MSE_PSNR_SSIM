import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
public class GraphicPanel extends JPanel {
    private int width;
    private int height;
    public int xcoord = (int) (main.xa * 20);
    public int ycoord = (int) (main.ya * 20);
    public int c = 3;
    public double xsrednee = 0, ysrednee = 0, summa_a = 0, summa_a2 = 0, summa_b = 0, coefb = 0, coefa = 0;
    public static double[][] tmp;
    public static double[][] newmassiv;
    
    public void paint(Graphics g)
    {
        super.paint(g);
        width = getWidth(); // сохраняем текущую ширину панели
        height = getHeight(); // и высоту
        drawGrid(g); // рисуем сетку
        drawAxis(g); // рисуем оси
        calculations(main.massiv);
        tochkas(g);
    }
    
    public static double[][] sorting(double [][] inputsorting, int checkingvalue) {
    	tmp = new double [1][1];
			for (int i = inputsorting.length - 1; i > 0; i--) {
			for (int m = 0; m < i; m++) {
				if (inputsorting[m][checkingvalue] >= inputsorting[m + 1][checkingvalue]) {
					for (int j = 0; j < 2; j++) {
						tmp [0][0] = inputsorting[m][j];
						inputsorting[m][j] = inputsorting[m + 1][j];
						inputsorting[m + 1][j] = tmp[0][0];
					}
				}
			}
		}
    	return inputsorting;
    }
    
    public void calculations(double inputmassiv[][]) {
    	for (int i = 0; i < inputmassiv.length; i ++) {
    		xsrednee += inputmassiv[i][0];
    		ysrednee += inputmassiv[i][1];
    	}
    	xsrednee = xsrednee/inputmassiv.length;
    	ysrednee = ysrednee/inputmassiv.length;
    	for (int i = 0; i < inputmassiv.length; i ++) {
    		summa_a += (inputmassiv[i][0] - xsrednee) * (inputmassiv[i][1] - ysrednee);
    		summa_a2 += (inputmassiv[i][0] - xsrednee) * (inputmassiv[i][0] - xsrednee);
    	}
    	coefa = summa_a / summa_a2;
    	coefb = ysrednee - coefa * xsrednee;
    	//System.out.println(coefa);
    	//System.out.print(coefb);
    }
    
    
    
    public void tochkas(Graphics g) {
    for (int m = 0; m< main.massiv.length; m++) {

    	double x= width/2 + (main.massiv[m][0] * 20 - 3);
    	double y= height/2 - (main.massiv[m][1] * 20 + 3);
    	double n= main.massiv[m][2];
    	
    	if (n==1) g.setColor(Color.black); else g.setColor(Color.BLUE);

    	g.fillOval((int) x, (int) y, 6, 6);
    }
    newmassiv = sorting(main.massiv, 0);
    g.setColor(Color.RED);
    g.drawLine((int) (width/2 + (-100) * 20), (int) (height/2 - (coefa * -100 + coefb) * 20), (int) (width/2 + (100) * 20), (int) (height/2 - (coefa * 100 + coefb) * 20));
    //g.drawLine(arg0, arg1, arg2, arg3);
    }
    
    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);  //задаем серый цвет

        for(int x=width/2; x<width; x+=20){  // цикл от центра до правого края 
            g.drawLine(x, 0, x, height);    // вертикальная линия
        }

        for(int x=width/2; x>0; x-=20){  // цикл от центра до леваого края
            g.drawLine(x, 0, x, height);   // вертикальная линия
        }

        for(int y=height/2; y<height; y+=20){  // цикл от центра до верхнего края
            g.drawLine(0, y, width, y);    // горизонтальная линия
        }

        for(int y=height/2; y>0; y-=20){  // цикл от центра до леваого края
            g.drawLine(0, y, width, y);    // горизонтальная линия
        }
    }

    private void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(width/2, 0, width/2, height);
        g.drawLine(0, height/2, width, height/2);
    }
}