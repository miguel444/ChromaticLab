package com.example.cuia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
                Toast texto_iniciar = Toast.makeText(getBaseContext(),"Iniciando cámara ...",Toast.LENGTH_LONG);
                startActivity(new Intent(menu.this, MainActivity.class));
                texto_iniciar.show();

            case R.id.button3:
                Toast texto_config = Toast.makeText(getBaseContext(),"Configuración",Toast.LENGTH_SHORT);
                startActivity(new Intent(menu.this, Configuracion.class));
                texto_config.show();

            case R.id.button4:
                System.exit(0);

        }
    }
}

