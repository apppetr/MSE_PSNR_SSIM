import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;

public class main {
	public static String SourseImg = "0.png";
	public static String ResultImg = "1.png";
	public static double MSECoef = 0.0;
	public static double PSNRCoef = 0.0;
	public static ArrayList<Double> ListOfMSE = new ArrayList<>();
	public static ArrayList<Double> ListOfPSNR = new ArrayList<>();
	public static ArrayList<Double> ListOfSSIM = new ArrayList<>();


	public static GraphicPanel graphicPanel;
	public static double xa, ya;
	public static int finalresult;

	public static double [][] massiv;

	public static void main(String[] args) {
	    // ������ ��������
	    Picture picture = new Picture("test.jpg");
	    // �������� � �������
	    int width = picture.width();
	    int height = picture.height();

	    for(int i = 0; i < 10; i ++){
			Picture pictureR = getPictureWithNoise(picture, width, height);
		}


		// ������� ������������
		for(int j = 0; j < Picture.ListImages.size(); j ++)
		{
			CalculateCoefs(picture, width, height, Picture.ListImages.get(j));
		}

		System.out.println("MSE " + ListOfMSE.toString());
		System.out.println("PSNR " + ListOfPSNR.toString());
		System.out.println("SSIM " + ListOfSSIM.toString());
		System.out.println("����������� ���������� MSE + PSNR = " + getSpearman(ListOfMSE,ListOfPSNR));
		System.out.println("����������� ���������� MSE + SSIM = " + getSpearman(ListOfMSE,ListOfSSIM));
		System.out.println("����������� ���������� PSNR + SSIM = " + getSpearman(ListOfPSNR,ListOfSSIM));

		massiv = new double[ListOfMSE.size()][3];
			for (int j=0; j<ListOfMSE.size();j++) {
				massiv[j][0] = ListOfMSE.get(j) ;
			}
		for (int j=0; j<ListOfPSNR.size();j++) {
			massiv[j][1] = ListOfPSNR.get(j) ;
		}
		for (int j=0; j<ListOfSSIM.size();j++) {
			massiv[j][2] = ListOfSSIM.get(j) ;
		}

		for (int i=0; i<massiv.length; i++) {
			for (int j=0; j<massiv[i].length;j++) {
				System.out.print(massiv[i][j]+" ");
			}
			System.out.println("");
		}

		createFrame();
	}
	public static double getSpearman(List<Double> list1, List<Double> list2)
	{
		SpearmansCorrelation correlation = new SpearmansCorrelation();
		double c = correlation.correlation(getArray(list1),getArray(list2));

		return c;
	}

	public static double[] getArray(List<Double> list)
	{
		double[] array = new double[list.size()];

		int index = 0;
		for(double d : list)
			array[index++] = d;

		return array;

	}

	private static void CalculateCoefs(Picture picture, int width, int height, String pictureR) {
		double MSE = 0;
		Picture pictureResult = new Picture(pictureR);
		// �������� � �������
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// �������� ������ ������� � ��������
				Color c1 = picture.get(i, j);
				Color c2 = pictureResult.get(i, j);
				// �� ������ ������� ������� ������
				double temp = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed());
				temp += (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen());
				temp += (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
				MSE += temp / (width * height);
			}
		}
		//������� ����������� PSNR
		PSNRCoef = 10.0 * Math.log10((255 * 255) / MSE);
		// ������� ������

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat Source = Imgcodecs.imread("test.jpg");
		Mat Result = Imgcodecs.imread(pictureR);

		//������� ���������� SSIM MS SIM
		Double resultSSIM = new SSIMTest(Source, Result).getSsim();

		ListOfMSE.add(MSE);
		ListOfPSNR.add(PSNRCoef);
		ListOfSSIM.add(resultSSIM);
	}

	private static Picture getPictureWithNoise(Picture picture, int width, int height) {
		// ������� ������ �������� � ������ �� �����������
		Picture pictureR = new Picture(width, height);

		// ���
		int noise = 30;

		// �� ������ ������� ��������� ���
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				// �������� ���� �������
				Color color = picture.get(col, row);
				// ����������� ��� �� ������ ���� (����� �������� ���� �������� �� ��� �����)
				int r = color.getRed() + (int)Math.ceil((Math.random() * noise)-noise/2);
				int g = color.getGreen() + (int)Math.ceil((Math.random() * noise)-noise/2);
				int b = color.getBlue() + (int)Math.ceil((Math.random() * noise)-noise/2);
				// ����������� ���� ����� �� �������
				if (r > 255) r = 255;
				if (g > 255) g = 255;
				if (b > 255) b = 255;
				if (r < 0) r = 0;
				if (g < 0) g = 0;
				if (b < 0) b = 0;
				// ��������� �������
				pictureR.set(col, row, new Color(r, g, b));
			}
		}
		pictureR.show();
		return pictureR;
	}

	public static void createFrame() {
		JFrame frame, frametree;
		frame = new JFrame("�����: 1 - ������, -1 - �����");
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		graphicPanel = new GraphicPanel();
		graphicPanel.setBackground(Color.WHITE);
		frame.add(graphicPanel);
		frame.setVisible(true);
	}
}

