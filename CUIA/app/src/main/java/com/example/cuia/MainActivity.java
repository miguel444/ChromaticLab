
package com.example.cuia;

import java.util.Collections;

import java.util.List;

import org.opencv.android.BaseLoaderCallback;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import org.opencv.core.Scalar;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;



import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity implements  CvCameraViewListener2, CompoundButton.OnCheckedChangeListener {
    private static final String  TAG= "OCVSample::Activity";


    private Mat                  mRgba;
       private ColorBlobDetector    mDetector;

    private Mat mRgbaF, mRgbaT;


    private Scalar daltonismo_seleccionado,relleno;
    private int index_camera = 0;


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
    private TextView valor_seek_bar;
    private TextView info_daltonismo;

    private ImageButton reverse_camera;



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
        mOpenCvCameraView.setCameraIndex(index_camera);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        on_off = (Switch) findViewById(R.id.switch2);
        on_off.setOnCheckedChangeListener(this);

        valor_seek_bar = (TextView)findViewById(R.id.textView6);

        if(Controller.idioma==0){
            valor_seek_bar.setText("Grado 100 / 100");
        }
        else
            valor_seek_bar.setText("Grade 100 / 100");




        if(Controller.default_value_daltonismo==2){
            switch (Controller.default_value_dicromatismo){
                case 0:
                    daltonismo_seleccionado = new Scalar(0,255,255);
                    ColorBlobDetector.mLowerBound = new Scalar(0,120,84);
                    ColorBlobDetector.mUpperBound = new Scalar(10,255,255);
                    Controller.info_daltonismo="DICROMATISMO - PROTANOPIA";
                    relleno = new Scalar(0,255,0);
                    ColorBlobDetector.cotaRojoClaro = new Scalar(200,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(256,255,255);
                    break;
                case 1:
                    daltonismo_seleccionado = new Scalar(255,255,0);
                    ColorBlobDetector.mLowerBound = new Scalar(49,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(128,255,255);
                    Controller.info_daltonismo="DICROMATISMO - DEUTERANOPIA";
                    relleno = new Scalar(255,0,0);

                    break;
                case 2:
                    daltonismo_seleccionado = new Scalar(255,0,255);
                    ColorBlobDetector.mLowerBound = new Scalar(137,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(180,255,255);
                    Controller.info_daltonismo="DICROMATISMO - TRITANOPIA";
                    relleno = new Scalar(255,0,0);

                    break;
            }
        }

        else if(Controller.default_value_daltonismo==1){
            switch (Controller.default_value_monocromatismo){
                case 0:
                    daltonismo_seleccionado = new Scalar(255,0,0);
                    ColorBlobDetector.mLowerBound = new Scalar(25,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(210,255,255);
                    Controller.info_daltonismo="MONOCROMATISMO - SOLO PERCIBE EL ROJO";
                    relleno = new Scalar(255,0,0);
                    break;
                case 1:
                    daltonismo_seleccionado = new Scalar(0,255,0);
                    ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(45,255,255);
                    ColorBlobDetector.cotaRojoClaro = new Scalar(138,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(255,255,255);
                    Controller.info_daltonismo="MONOCROMATISMO - SOLO PERCIBE EL VERDE";
                    relleno = new Scalar(0,255,0);
                    break;
                case 2:
                    daltonismo_seleccionado = new Scalar(0,0,255);
                    ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
                    ColorBlobDetector.mUpperBound = new Scalar(110,255,255);
                    ColorBlobDetector.cotaRojoClaro = new Scalar(200,50,50);
                    ColorBlobDetector.cotaRojoOscuro = new Scalar(255,255,255);
                    Controller.info_daltonismo="MONOCROMATISMO - SOLO PERCIBE EL AZUL";
                    relleno = new Scalar(0,0,255);
                    break;
            }
        }

        else {
            daltonismo_seleccionado = new Scalar(0,0,0);
            ColorBlobDetector.mLowerBound = new Scalar(0,50,50);
            ColorBlobDetector.mUpperBound = new Scalar(255,255,255);
            Controller.info_daltonismo="ACROMATISMO - SOLO PERCIBE EN BLANCO Y NEGRO";
            relleno = new Scalar(0,0,0);

        }

        info_daltonismo = (TextView)findViewById(R.id.textView5);
        info_daltonismo.setText(Controller.info_daltonismo);
        barra_seleccion = (SeekBar) findViewById(R.id.seekBar) ;
        barra_seleccion.setProgress(100);
        barra_seleccion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(Controller.idioma==0)
                    valor_seek_bar.setText("Grado "+progress+" / 100");
                else
                    valor_seek_bar.setText("Grade "+progress+" / 100");
                double nuevo_valor;
                if(Controller.default_value_daltonismo==2){
                    switch (Controller.default_value_dicromatismo){
                        case 0:
                            nuevo_valor = (25 * (progress/100.0));
                            ColorBlobDetector.mUpperBound = new Scalar(nuevo_valor,255,255);
                            Controller.info_daltonismo="DICROMATISMO - PROTANOPIA";

                            nuevo_valor = ((256-200) * (progress/100.0)+200);
                            ColorBlobDetector.cotaRojoClaro = new Scalar(nuevo_valor,50,50);

                            break;
                        case 1:
                            nuevo_valor = ((128 - 49) * (progress/100.0))+49;

                            ColorBlobDetector.mUpperBound = new Scalar(nuevo_valor,255,255);
                            Controller.info_daltonismo="DICROMATISMO - DEUTERANOPIA";

                            break;
                        case 2:
                            nuevo_valor = ((190 - 120) * (progress/100.0))+120;

                            ColorBlobDetector.mUpperBound = new Scalar(nuevo_valor,255,255);

                            Controller.info_daltonismo="DICROMATISMO - TRITANOPIA";
                            break;
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getBaseContext(),"Modificando grado ..",Toast.LENGTH_LONG);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        reverse_camera = (ImageButton) findViewById(R.id.imageButton);
        reverse_camera.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(index_camera==0){

                    mOpenCvCameraView.disableView();
                    mOpenCvCameraView.setCameraIndex(1);
                    mOpenCvCameraView.enableView();
                    index_camera=1;

                }
                else if (index_camera==1){

                    mOpenCvCameraView.setCameraIndex(0);
                    mOpenCvCameraView.disableView();
                    mOpenCvCameraView.enableView();
                    index_camera=0;

                }
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

            }

    public void onCameraViewStopped() {
        mRgba.release();
    }


    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();


        if(blob_detector){

            mDetector.process(mRgba);
            List<MatOfPoint> contours = mDetector.getContours();
            Imgproc.drawContours(mRgba, contours, -1, daltonismo_seleccionado,10);
            Imgproc.fillPoly(mRgba,contours,relleno);



        }

        Core.transpose(mRgba,mRgbaF);
        Imgproc.resize(mRgbaF, mRgbaT,mRgba.size(),0,0,0);
        Core.flip(mRgbaT,mRgba,1);

        if (index_camera==1)
            Core.flip(mRgba,mRgba,0);


        return mRgba;
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(on_off.isChecked()){
            blob_detector=true;
            info_daltonismo.setVisibility(View.VISIBLE);}
        else{
            blob_detector=false;

            info_daltonismo.setVisibility(View.INVISIBLE);}

    }
}
