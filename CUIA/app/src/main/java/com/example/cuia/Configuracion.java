package com.example.cuia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class Configuracion extends AppCompatActivity  {

    private Spinner spinner1,spinner2,spinner3;
    private TextView tv,tipos_monocromatismo;


    private static final String[] paths = {"Acromático","Monocromático", "Dicromático"};

    private static final String[] paths2 = {"Protonopia", "Deuteranopia","Tritanopia"};

    private static final String[] paths3 = {"Solo percibe color rojo", "Solor percibe color verde","Solo percible color azul"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }





        spinner1 = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths2);

        spinner3 = (Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(Configuracion.this, android.R.layout.simple_spinner_item,paths3);

        tv = (TextView)findViewById(R.id.textView3);
        tipos_monocromatismo = (TextView)findViewById(R.id.textView4);



        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(Controller.default_value_daltonismo);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(Controller.default_value_dicromatismo);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
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

    }





}