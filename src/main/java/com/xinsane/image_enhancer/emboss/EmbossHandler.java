package com.xinsane.image_enhancer.emboss;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static org.opencv.core.CvType.CV_8UC;

public class EmbossHandler {

    public static void handleImage(String srcPath, String destPath) {
        Mat src = Imgcodecs.imread(srcPath);
        Mat dest = new Mat(src.size(), CV_8UC(src.channels()));
        for (int y = 1; y < src.rows() - 1; y++) {
            for (int x = 1; x < src.cols() - 1; x++) {
                double[] src0 = src.get(y, x);
                double[] src1 = src.get(y+1, x); // 下方像素
                double[] src2 = src.get(y, x+1); // 右边像素
                double[] dest0 = new double[src.channels()];
                for (int i = 0; i < src.channels(); i++) {
                    double tmp = (src0[i] * 2 - src1[i] - src2[i]) / 2.0 + 128;
                    if (tmp < 0)
                        tmp = 0;
                    else if (tmp > 255)
                        tmp = 255;
                    dest0[i] = tmp;
                }
                dest.put(y, x, dest0);
            }
        }
        Imgcodecs.imwrite(destPath, dest);
    }

}
