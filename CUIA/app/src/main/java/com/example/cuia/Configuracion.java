package com.example.cuia;


import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.utils.Converters;


public class Configuracion extends AppCompatActivity  {

    private Spinner spinner1,spinner2,spinner3;
    private TextView tv,tipos_monocromatismo, daltonismo;
    private ImageButton idioma;


    private static final String[] paths = {"Acromático","Monocromático", "Dicromático"};

    private static final String[] paths2 = {"Protonopia", "Deuteranopia","Tritanopia"};

    private static final String[] paths3 = {"Solo percibe color rojo", "Solor percibe color verde","Solo percible color azul"};

    private static final String[] paths4 = {"It only perceives red colour", "It only perceives green colour","It only perceives blue colour"};

    private static final String[] paths5 = {"Achromatic","Monochromacy", "Dichromacy"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        idioma = (ImageButton) findViewById(R.id.imageButton2);

        daltonismo = (TextView)findViewById(R.id.textView2);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        final ArrayAdapter<String>adapter = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths);

        final ArrayAdapter<String>adapter5 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths5);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths2);

        spinner3 = (Spinner)findViewById(R.id.spinner3);
        final ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths3);

        final ArrayAdapter<String>adapter4 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tv = (TextView)findViewById(R.id.textView3);
        tipos_monocromatismo = (TextView)findViewById(R.id.textView4);



        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (Controller.idioma==0){
            spinner1.setAdapter(adapter);
            spinner3.setAdapter(adapter3);

            idioma.setImageResource(R.drawable.espana);
            daltonismo.setText("Seleccione el tipo de daltonismo :");
            tipos_monocromatismo.setText("Seleccione el tipo de monocromatismo :");
            tv.setText("Seleccione el tipo de dicromatismo :");
            setTitle("Configuración");


        }
        else{
            spinner1.setAdapter(adapter5);
            spinner3.setAdapter(adapter4);
            idioma.setImageResource(R.drawable.uk);
            daltonismo.setText("Select the type of colour blindness :");
            tipos_monocromatismo.setText("Select the type of monochromatism :");
            tv.setText("Select the type of dichromatism :");
            setTitle("Settings");


        }

        spinner1.setSelection(Controller.default_value_daltonismo);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(Controller.default_value_dicromatismo);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3.setSelection(Controller.default_value_monocromatismo);

        if(Controller.default_value_daltonismo==2){
            tv.setVisibility(View.VISIBLE);
            spinner2.setVisibility(View.VISIBLE);
        }
        else if(Controller.default_value_daltonismo==1){
            tipos_monocromatismo.setVisibility(View.VISIBLE);
            spinner3.setVisibility(View.VISIBLE);
        }


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Controller.default_value_dicromatismo=0;

                        break;
                    case 1:

                        Controller.default_value_dicromatismo=1;

                        break;
                    case 2:

                        Controller.default_value_dicromatismo=2;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Controller.default_value_monocromatismo=0;

                        break;
                    case 1:

                        Controller.default_value_monocromatismo=1;

                        break;
                    case 2:

                        Controller.default_value_monocromatismo=2;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        spinner2.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                        spinner3.setVisibility(View.GONE);
                        tipos_monocromatismo.setVisibility(View.GONE);
                        Controller.default_value_daltonismo=0;
                        Controller.info_daltonismo="ACROMATISMO";
                        break;
                    case 1:
                        spinner2.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                        spinner3.setVisibility(View.VISIBLE);
                        tipos_monocromatismo.setVisibility(View.VISIBLE);

                        Controller.default_value_daltonismo=1;
                        break;


                    case 2:
                        spinner3.setVisibility(View.GONE);
                        tipos_monocromatismo.setVisibility(View.GONE);
                        spinner2.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        Controller.default_value_daltonismo=2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       idioma.setOnClickListener(new ImageButton.OnClickListener(){

           @Override
           public void onClick(View v) {
               if (Controller.idioma == 0) {
                   Controller.idioma = 1;
                   idioma.setImageResource(R.drawable.uk);
                   daltonismo.setText("Select the type of colour blindness :");
                   tipos_monocromatismo.setText("Select the type of monochromatism :");
                   tv.setText("Select the type of dichromatism :");
                   spinner1.setAdapter(adapter5);
                   spinner3.setAdapter(adapter4);
                   spinner1.setSelection(Controller.default_value_daltonismo);
                   spinner2.setSelection(Controller.default_value_dicromatismo);
                   spinner3.setSelection(Controller.default_value_monocromatismo);
                   setTitle("Settings");


               } else {
                   Controller.idioma = 0;
                   idioma.setImageResource(R.drawable.espana);
                   daltonismo.setText("Seleccione el tipo de daltonismo :");
                   tipos_monocromatismo.setText("Seleccione el tipo de monocromatismo :");
                   tv.setText("Seleccione el tipo de dicromatismo :");
                   spinner3.setAdapter(adapter3);
                   spinner1.setAdapter(adapter);
                   spinner1.setSelection(Controller.default_value_daltonismo);
                   spinner2.setSelection(Controller.default_value_dicromatismo);
                   spinner3.setSelection(Controller.default_value_monocromatismo);
                   setTitle("Configuración");

               }
           }


       });

    }





}