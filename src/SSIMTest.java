import org.opencv.core.*;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class SSIMTest {

    private static final double sigma = 4;

    private Mat first;

    private Mat second;

    private Mat out;

    private double ssim;

    public SSIMTest(Mat first, Mat second) {
        super();
        this.first = first;
        this.second = second;
        this.out = getMSSIM(this.first, this.second);
        Scalar mssim = Core.mean(this.out);
        this.ssim = mssim.val[0];

    }

    public static Mat getMSSIM(Mat i1, Mat i2) {
        double C1 = 6.5025, C2 = 58.5225;
        /***************************** INITS **********************************/
        int d = CvType.CV_32F;

        Mat I1 = i1.clone(), I2 = i2.clone();
        I1.convertTo(I1, d);            // cannot calculate on one byte large values
        I2.convertTo(I2, d);
        Mat I2_2 = I2.mul(I2);        // I2^2
        Mat I1_2 = I1.mul(I1);        // I1^2
        Mat I1_I2 = I1.mul(I2);        // I1 * I2

        /*************************** END INITS **********************************/

        Mat mu1 = new Mat(), mu2 = new Mat();                   // PRELIMINARY COMPUTING

        GaussianBlur(I1, mu1, new Size(11, 11), sigma);
        I1.release();
        GaussianBlur(I2, mu2, new Size(11, 11), sigma);
        I2.release();
        //LIBERAR

        Mat mu1_2 = mu1.mul(mu1);
        Mat mu2_2 = mu2.mul(mu2);
        Mat mu1_mu2 = mu1.mul(mu2);
        mu2.release();
        mu1.release();

        Mat sigma1_2 = new Mat(), sigma2_2 = new Mat(), sigma12 = new Mat();

        GaussianBlur(I1_2, sigma1_2, new Size(11, 11), sigma);
        I1_2.release();

        Core.subtract(sigma1_2, mu1_2, sigma1_2);
        //        sigma1_2 -= mu1_2;

        GaussianBlur(I2_2, sigma2_2, new Size(11, 11), sigma);
        I2_2.release();
        Core.subtract(sigma2_2, mu2_2, sigma2_2);
        //        sigma2_2 -= mu2_2;

        GaussianBlur(I1_I2, sigma12, new Size(11, 11), sigma);
        I1_I2.release();
        Core.subtract(sigma12, mu1_mu2, sigma12);
        //        sigma12 -= mu1_mu2;

        ///////////////////////////////// FORMULA ////////////////////////////////
        Mat t1 = new Mat(), t2 = new Mat(), t3 = new Mat();

        Core.multiply(mu1_mu2, new Scalar(2), mu1_mu2);
        Core.add(mu1_mu2, new Scalar(C1), t1);
        mu1_mu2.release();
        //t1 = 2 * mu1_mu2 + C1;

        Core.multiply(sigma12, new Scalar(2), sigma12);
        Core.add(sigma12, new Scalar(C2), t2);
        sigma12.release();
        //        t2 = 2 * sigma12 + C2;
        t3 = t1.mul(t2);                 // t3 = ((2*mu1_mu2 + C1).*(2*sigma12 + C2))

        Core.add(mu1_2, mu2_2, t1);
        mu1_2.release();
        mu2_2.release();
        Core.add(t1, new Scalar(C1), t1);
        //        t1 = mu1_2 + mu2_2 + C1;

        Core.add(sigma1_2, sigma2_2, t2);
        sigma1_2.release();
        sigma2_2.release();
        Core.add(t2, new Scalar(C2), t2);
        //        t2 = sigma1_2 + sigma2_2 + C2;
        //t1 = t1.mul(t2);
        t1 = t1.mul(t2);                 // t1 =((mu1_2 + mu2_2 + C1).*(sigma1_2 + sigma2_2 + C2))
        t2.release();

        Mat ssim_map = new Mat();
        Core.divide(t3, t1, ssim_map);
        //        divide(t3, t1, ssim_map);        // ssim_map =  t3./t1;
        t3.release();
        t1.release();

        Scalar mssim = Core.mean(ssim_map);   // mssim = average of ssim map
        //        ssim_map.release();
        return ssim_map;

    }

    public double getSsim() {
        return ssim;
    }

    public Mat getOut() {
        return out;
    }
}