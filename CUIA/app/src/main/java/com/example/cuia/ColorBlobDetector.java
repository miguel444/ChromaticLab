package com.example.cuia;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorBlobDetector {

    public static Scalar mLowerBound = new Scalar(49,50,50);
    public static Scalar mUpperBound = new Scalar(107,255,255);

    public static Scalar cotaRojoClaro = new Scalar(49,50,50);
    public static Scalar cotaRojoOscuro = new Scalar(107,255,255);

    private static double mMinContourArea = 0.1;

    private Scalar mColorRadius = new Scalar(25,50,50,0);
    private Mat mSpectrum = new Mat();
    private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();

    // Cache
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat mMask = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();



    public void process(Mat rgbaImage) {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);
        if ((Controller.default_value_daltonismo==1 || Controller.default_value_dicromatismo==0) && Controller.default_value_daltonismo!=0 && Controller.default_value_monocromatismo!=0){
            Mat rojo1 = new Mat();
            Core.inRange(mHsvMat,cotaRojoClaro,cotaRojoOscuro,rojo1);
            Mat rojo2 = new Mat();
            Core.inRange(mHsvMat,mLowerBound,mUpperBound,rojo2);
            Core.bitwise_or(rojo1,rojo2,mMask);

        }
        else
            Core.inRange(mHsvMat, mLowerBound, mUpperBound, mMask);

        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            if (area > maxArea)
                maxArea = area;
        }


        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);
            }
        }
    }

    public List<MatOfPoint> getContours() {
        return mContours;
    }
}