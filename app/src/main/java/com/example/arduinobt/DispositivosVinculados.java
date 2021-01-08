package com.example.arduinobt;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class DispositivosVinculados extends AppCompatActivity
{
    private static final String TAG = "DispositivosVinculados";
    public static String EXTRA_DEVICE_ADRESS = "device_adress";

    private BluetoothAdapter BTAdapter;

    private ArrayAdapter ArrayBT;

    ListView ListaBT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_vinculados);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        VerificarStadoBT();

        ArrayBT = new ArrayAdapter(this, R.layout.dispositivos_encontrados);

        ListaBT = findViewById(R.id.ListaBT);

        ListaBT.setAdapter(ArrayBT);

        ListaBT.setOnItemClickListener(DeviceListener);

        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device: pairedDevices)
            {
                ArrayBT.add(device.getName() +"\n" +device.getAddress());
            }
        }
    }

    private AdapterView.OnItemClickListener DeviceListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String info = ((TextView) view).getText().toString();
            String Adress = info.substring(info.length() - 17);

            finishAffinity();

            Intent OpenNewActivity = new Intent(getApplicationContext(), MainActivity.class);
            OpenNewActivity.putExtra(EXTRA_DEVICE_ADRESS , Adress);
            startActivity(OpenNewActivity);
        }
    };

    public void VerificarStadoBT()
    {
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

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
}