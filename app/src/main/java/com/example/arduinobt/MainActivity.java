package com.example.arduinobt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private Button Conectar, Desconectar;
    private TextView Estado;
    private EditText IngresoTxt;
    private ImageButton Izquierda, Derecha, Claxon, Acelerador, Freno;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Conectar = findViewById(R.id.Conectar);
        Desconectar = findViewById(R.id.Desconectar);
        Estado = findViewById(R.id.Estado);
        IngresoTxt = findViewById(R.id.IngresoTxt);
        Izquierda = findViewById(R.id.Izquierda);
        Derecha = findViewById(R.id.Derecha);
        Claxon = findViewById(R.id.Claxon);
        Acelerador = findViewById(R.id.Acelerador);
        Freno = findViewById(R.id.Freno);

        Conectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                
            }
        });

        Desconectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Izquierda.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Derecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Claxon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Freno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Acelerador.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        Desconectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }
}