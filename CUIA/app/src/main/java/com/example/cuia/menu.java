package com.example.cuia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class menu extends AppCompatActivity implements View.OnClickListener {

    private Button iniciar_camera,config,salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        iniciar_camera = (Button)findViewById(R.id.button);
        config = (Button)findViewById(R.id.button3);
        salir = (Button)findViewById(R.id.button4);

        iniciar_camera.setOnClickListener(this);
        config.setOnClickListener(this);
        salir.setOnClickListener(this);





    }

    private void startApp() {
        Intent intent = new Intent(menu.this, MainActivity.class);
        startActivity(intent);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button:
                if (ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {



                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            0);

                    //

                }

                else{
                    Toast texto_iniciar = Toast.makeText(getBaseContext(),"Iniciando cámara ...",Toast.LENGTH_SHORT);
                    startActivity(new Intent(menu.this, MainActivity.class));
                    texto_iniciar.setGravity(Gravity.BOTTOM, 0, 430);
                    texto_iniciar.show();
                }

                break;

            case R.id.button3:
                Toast texto_config = Toast.makeText(getBaseContext(),"Configuración",Toast.LENGTH_SHORT);
                startActivity(new Intent(menu.this, Configuracion.class));
                texto_config.show();
                break;

            case R.id.button4:
                System.exit(0);
                break;

        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),"Permiso permitido",Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast texto_iniciar = Toast.makeText(getBaseContext(),"Iniciando cámara ...",Toast.LENGTH_SHORT);
                    startActivity(new Intent(menu.this, MainActivity.class));
                    texto_iniciar.setGravity(Gravity.BOTTOM, 0, 430);
                    texto_iniciar.show();
                } else {
                    Toast.makeText(getBaseContext(),"Permiso denegado",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}

