package com.example.arduinobt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    private Button Desconectar;
    private TextView Estado;
    private ImageButton Izquierda, Derecha, Claxon, Acelerador, Freno;

    Handler BTIn;
    final int HandlerState = 0;
    private BluetoothAdapter BTAdapter = null;
    private BluetoothSocket BTSocket = null;
    private StringBuilder DataStringIn = new StringBuilder();

    private ConnectedThread MyBTCnx;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private static String Adress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTIn = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                if(msg.what == HandlerState)
                {
                    char MyChar = (char) msg.obj;
                }
            }
        };

        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        VerificarStadoBT();

        Desconectar = findViewById(R.id.Desconectar);
        Estado = findViewById(R.id.Estado);
        Izquierda = findViewById(R.id.Izquierda);
        Derecha = findViewById(R.id.Derecha);
        Claxon = findViewById(R.id.Claxon);
        Acelerador = findViewById(R.id.Acelerador);
        Freno = findViewById(R.id.Freno);

        Desconectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(BTSocket != null)
                {
                    try
                    {
                        BTSocket.close();
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(getApplicationContext(), "Error" , Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });

        Izquierda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        MyBTCnx.write("L");
                        break;

                    case MotionEvent.ACTION_UP:
                        MyBTCnx.write("S");
                        break;
                }
                return true;
            }
        });

        Derecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        MyBTCnx.write("R");
                        break;

                    case MotionEvent.ACTION_UP:
                        MyBTCnx.write("S");
                        break;
                }
                return true;
            }
        });

        Acelerador.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        MyBTCnx.write("F");
                        break;

                    case MotionEvent.ACTION_UP:
                        MyBTCnx.write("S");
                        break;
                }
                return true;
            }
        });

        Freno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                            MyBTCnx.write("B");
                            break;

                    case MotionEvent.ACTION_UP:
                        MyBTCnx.write("S");
                        break;
                }

                return true;
            }
        });

        Claxon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        MyBTCnx.write("V");
                        break;
                    case MotionEvent.ACTION_UP:
                        MyBTCnx.write("v");
                        break;
                }
                return true;
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException
    {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Intent intent = getIntent();

        Adress = intent.getStringExtra(DispositivosVinculados.EXTRA_DEVICE_ADRESS);

        BluetoothDevice device = BTAdapter.getRemoteDevice(Adress);

        try
        {
            BTSocket = createBluetoothSocket(device);
        }
        catch (IOException e)
        {
            Toast.makeText(getBaseContext(), "La creaccion del Socket fallo", Toast.LENGTH_LONG).show();
        }

        try
        {
            BTSocket.connect();
        }
        catch (IOException e)
        {
            try
            {
                BTSocket.close();
            } catch (IOException e2) {}
        }

        MyBTCnx = new ConnectedThread(BTSocket);
        MyBTCnx.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        try
        {
            BTSocket.close();
        }catch (IOException e2) {}
    }

    public void VerificarStadoBT()
    {
        if(BTAdapter == null)
        {
            Toast.makeText(getApplicationContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(BTAdapter.isEnabled())
            {
                Toast.makeText(getApplicationContext(), "Bluetooth activado", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent EnableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(EnableBT, 1);
            }
        }
    }

    private class ConnectedThread extends  Thread
    {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] byte_in = new byte[1];

            while(true)
            {
                try
                {
                    mmInStream.read(byte_in);
                    char ch = (char) byte_in[0];
                    BTIn.obtainMessage(HandlerState, ch).sendToTarget();
                }catch (IOException e){ break; }
            }
        }

        public void write(String input)
        {
            try
            {
                mmOutStream.write(input.getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getBaseContext(), "La conexion fallo!" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}