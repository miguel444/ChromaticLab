
package com.example.cuia;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements  CvCameraViewListener2, CompoundButton.OnCheckedChangeListener {
    private static final String  TAG= "OCVSample::Activity";

    private boolean              mIsColorSelected = false;
    private Mat                  mRgba;
    private Scalar               mBlobColorRgba;
    private Scalar               mBlobColorHsv;
    private ColorBlobDetector    mDetector;
    private Mat                  mSpectrum;
    private Size                 SPECTRUM_SIZE;
    private Scalar               CONTOUR_COLOR;
    private Mat mRgbaF, mRgbaT;

    private Scalar daltonismo_seleccionado;


    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    private Switch on_off;
    private SeekBar barra_seleccion;
    private boolean blob_detector;
    private int valor_seek_bar;


    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        on_off = (Switch) findViewById(R.id.switch2);
        on_off.setOnCheckedChangeListener(this);

        if(Controller.default_value_daltonismo==2){
            switch (Controller.default_value_dicromatismo){
                case 0:
                    daltonismo_seleccionado = new Scalar(0,255,255);
                    ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(10,255,255);

                    ColorBlobDetector.cotaRojoClaro = new Scalar(220,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(256,255,255);
                    break;
                case 1:
                    daltonismo_seleccionado = new Scalar(255,255,0);
                    ColorBlobDetector.mLowerBound = new Scalar(49,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(128,255,255);

                    break;
                case 2:
                    daltonismo_seleccionado = new Scalar(255,0,255);
                    ColorBlobDetector.mLowerBound = new Scalar(120,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(190,255,255);
                    break;
            }
        }

        else if(Controller.default_value_daltonismo==1){
            switch (Controller.default_value_monocromatismo){
                case 0:
                    daltonismo_seleccionado = new Scalar(255,0,0);
                    ColorBlobDetector.mLowerBound = new Scalar(30,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(210,255,255);
                    break;
                case 1:
                    daltonismo_seleccionado = new Scalar(0,255,0);
                    ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(45,255,255);
                    ColorBlobDetector.cotaRojoClaro = new Scalar(138,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(255,255,255);
                    break;
                case 2:
                    daltonismo_seleccionado = new Scalar(0,0,255);
                    ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(110,255,255);
                    ColorBlobDetector.cotaRojoClaro = new Scalar(200,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(255,255,255);
                    break;
            }
        }

        else {
            daltonismo_seleccionado = new Scalar(0,0,0);
            ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
            ColorBlobDetector.mUpperBound = new Scalar(255,255,255);

        }


        barra_seleccion = (SeekBar) findViewById(R.id.seekBar) ;
        barra_seleccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valor_seek_bar = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getBaseContext(),"Modificando grado ..",Toast.LENGTH_LONG);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               Toast texto =  Toast.makeText(getBaseContext(),"Grado "+valor_seek_bar+" / "+ seekBar.getMax(),Toast.LENGTH_SHORT);
                texto.setGravity(Gravity.BOTTOM, 0, 430);
                texto.show();

            }
        });


    }



    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }



    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mDetector = new ColorBlobDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,0,255);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }
/*
    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv);

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);

        Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0, 0, Imgproc.INTER_LINEAR);

        mIsColorSelected = true;

        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }
*/
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();


        if(blob_detector){
            mDetector.process(mRgba);
            List<MatOfPoint> contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());
            Imgproc.drawContours(mRgba, contours, -1, daltonismo_seleccionado,-1);

            //Imgproc.fillPoly(mRgba,contours,tipo_dicromatismo);

            Mat colorLabel = mRgba.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba);


        }

        /*Mat mask1 = inputFrame.rgba();
        Mat output= inputFrame.rgba();

        if(blob_detector) {
            //Imgproc.cvtColor(mRgba, canny, Imgproc.COLOR_BGR2HSV);
            //Core.inRange(canny, new Scalar(110, 50, 50), new Scalar(130, 255, 255), mask1);

            //Core.inRange(canny, new Scalar(45, 20, 10), new Scalar(75, 255, 255), mask2);

            //Core.add(mask1,mask2,canny);

            Mat gaussian_output = new Mat();

            Imgproc.Canny(inputFrame.gray(), mask1, 80, 100);
            Imgproc.cvtColor(mask1, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
            //Imgproc.GaussianBlur(mask1, gaussian_output, new Size(5, 5), 5);

            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat gray = new Mat();
            Imgproc.cvtColor(output, gray, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.findContours(gray, contours, new Mat(), Imgproc.RETR_EXTERNAL,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            //System.out.println(contours.size());
            //output = new Mat(gaussian_output.size(),CvType.CV_8UC3);
            //Imgproc.cvtColor(gray,output,Imgproc.COLOR_GRAY2RGBA,4);

            Imgproc.drawContours(output, contours, -1, new Scalar(255, 0, 0), -1);




        }
        */


        //Imgproc.Canny(mRgba,canny,10,100);
        Core.transpose(mRgba,mRgbaF);
        Imgproc.resize(mRgbaF, mRgbaT,mRgba.size(),0,0,0);
        Core.flip(mRgbaT,mRgba,1);

        return mRgba;
    }

    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(on_off.isChecked())
            blob_detector=true;
        else
            blob_detector=false;
            mIsColorSelected=false;

    }
}
