package com.example.teste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static java.lang.Integer.parseInt;


public class auto extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    ImageButton Automatico;
    ImageButton horasLuzDiaMais;
    ImageButton horasLuzDiaMenos;
    ImageButton regasDiaMais;
    ImageButton regasDiaMenos;
    ImageButton horasVentilacaoDiaMais;
    ImageButton horasVentilacaoDiaMenos;
    TextView horasLuzDia;
    TextView regasDia;
    TextView horasVentilacaoDia;

    boolean modoAutomatico = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Automatico = findViewById(R.id.Automatico);
        horasLuzDia = findViewById(R.id.horasLuzDia);
        horasLuzDiaMais = findViewById(R.id.horasLuzDiaMais);
        horasLuzDiaMenos = findViewById(R.id.horasLuzDiaMenos);
        regasDia = findViewById(R.id.regasDia);
        regasDiaMais = findViewById(R.id.regasDiaMais);
        regasDiaMenos = findViewById(R.id.regasDiaMenos);
        horasVentilacaoDia = findViewById(R.id.horasVentilacaoDia);
        horasVentilacaoDiaMais = findViewById(R.id.horasVentilacaoDiaMais);
        horasVentilacaoDiaMenos = findViewById(R.id.horasVentilacaoDiaMenos);

        if(!mBluetoothAdapter.isEnabled()){
            startActivity(new Intent(getApplicationContext(),erroBluetooth.class));
            overridePendingTransition(0,0);
        }

        try {
            findBT("HC-05");
            openBT();
        } catch (IOException e) {
            startActivity(new Intent(getApplicationContext(),erroBluetooth.class));
            overridePendingTransition(0,0);
            e.printStackTrace();
        }

        

        horasLuzDiaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence s = horasLuzDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);
                if(s2 == 2){
                    String s3 = Integer.toString(4);
                    horasLuzDia.setText(s3);
                    horasLuzDiaMenos.setVisibility(View.VISIBLE);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(6);
                    horasLuzDia.setText(s3);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(8);
                    horasLuzDia.setText(s3);
                    horasLuzDiaMais.setVisibility(View.GONE);
                }

            }
        });

        horasLuzDiaMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence s = horasLuzDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);

                if(s2 == 8){
                    String s3 = Integer.toString(6);
                    horasLuzDia.setText(s3);
                    horasLuzDiaMais.setVisibility(View.VISIBLE);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(4);
                    horasLuzDia.setText(s3);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(2);
                    horasLuzDia.setText(s3);
                    horasLuzDiaMenos.setVisibility(View.GONE);
                }

            }
        });

        regasDiaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence s = regasDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);
                if(s2 == 2){
                    String s3 = Integer.toString(4);
                    regasDia.setText(s3);
                    regasDiaMenos.setVisibility(View.VISIBLE);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(6);
                    regasDia.setText(s3);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(8);
                    regasDia.setText(s3);
                    regasDiaMais.setVisibility(View.GONE);
                }

            }
        });

        regasDiaMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence s = regasDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);

                if(s2 == 8){
                    String s3 = Integer.toString(6);
                    regasDia.setText(s3);
                    regasDiaMais.setVisibility(View.VISIBLE);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(4);
                    regasDia.setText(s3);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(2);
                    regasDia.setText(s3);
                    regasDiaMenos.setVisibility(View.GONE);
                }

            }
        });

        horasVentilacaoDiaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence s = horasVentilacaoDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);
                if(s2 == 2){
                    String s3 = Integer.toString(4);
                    horasVentilacaoDia.setText(s3);
                    horasVentilacaoDiaMenos.setVisibility(View.VISIBLE);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(6);
                    horasVentilacaoDia.setText(s3);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(8);
                    horasVentilacaoDia.setText(s3);
                    horasVentilacaoDiaMais.setVisibility(View.GONE);
                }
            }
        });

        horasVentilacaoDiaMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence s = horasVentilacaoDia.getText();
                String s1 = s.toString();
                int s2 = Integer.parseInt(s1);

                if(s2 == 8){
                    String s3 = Integer.toString(6);
                    horasVentilacaoDia.setText(s3);
                    horasVentilacaoDiaMais.setVisibility(View.VISIBLE);
                }
                if(s2 == 6){
                    String s3 = Integer.toString(4);
                    horasVentilacaoDia.setText(s3);
                }
                if(s2 == 4){
                    String s3 = Integer.toString(2);
                    horasVentilacaoDia.setText(s3);
                    horasVentilacaoDiaMenos.setVisibility(View.GONE);
                }

            }
        });


        Automatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modoAutomatico){

                    try
                    {
                        sendData("2222");
                    } catch (IOException ex) {
                        showToast("SEND FAILED");
                    }

                    modoAutomatico = false;
                    Automatico.setImageResource(R.mipmap.autooff_foreground);

                    CharSequence s = horasLuzDia.getText();           //--
                    String s1 = s.toString();                        // Horas Luz Dia ... Para o Botao menos.mais nao aparecer novamente
                    int s2 = Integer.parseInt(s1);                  //--

                    CharSequence t = regasDia.getText();              //--
                    String t1 = t.toString();                        //
                    int t2 = Integer.parseInt(t1);                  //--

                    CharSequence u = horasVentilacaoDia.getText();    //--
                    String u1 = u.toString();                        //
                    int u2 = Integer.parseInt(u1);                  //--

                    if(s2 != 8){
                        horasLuzDiaMais.setVisibility(View.VISIBLE);
                    }
                    if(s2 != 2){
                        horasLuzDiaMenos.setVisibility(View.VISIBLE);
                    }

                    if(t2 != 8){
                        regasDiaMais.setVisibility(View.VISIBLE);
                    }
                    if(t2 != 2){
                        regasDiaMenos.setVisibility(View.VISIBLE);
                    }

                    if(u2 != 8){
                        horasVentilacaoDiaMais.setVisibility(View.VISIBLE);
                    }
                    if(u2 != 2){
                        horasVentilacaoDiaMenos.setVisibility(View.VISIBLE);
                    }

                }
                else{

                    CharSequence s = horasVentilacaoDia.getText();    //--
                    String s1 = s.toString();                        //
                    int s2 = Integer.parseInt(s1);                  //--

                    CharSequence t = regasDia.getText();              //--
                    String t1 = t.toString();                        //
                    int t2 = Integer.parseInt(t1);                  //--

                    CharSequence u = horasLuzDia.getText();           //--
                    String u1 = u.toString();                        // Horas Luz Dia ... Para enviar dados para a placa
                    int u2 = Integer.parseInt(u1);                  //--

                    try
                    {
                        sendData("3" + s2 + t2 + u2);
                    } catch (IOException ex) {
                        showToast("SEND FAILED");
                    }

                    modoAutomatico = true;
                    Automatico.setImageResource(R.mipmap.autoon_foreground);
                    showToast("Modo Automático ativado");
                    horasLuzDiaMais.setVisibility(View.GONE);
                    horasLuzDiaMenos.setVisibility(View.GONE);
                    regasDiaMais.setVisibility(View.GONE);
                    regasDiaMenos.setVisibility(View.GONE);
                    horasVentilacaoDiaMais.setVisibility(View.GONE);
                    horasVentilacaoDiaMenos.setVisibility(View.GONE);
                }




            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.auto);

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                switch(menuItem.getItemId()){
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(),Info.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.auto:
                        return true;
                }

                return false;

            }


        });
    }
    void findBT(String nome)
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            showToast("No Bluetooth Adapter Available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            showToast("Bluetooth desligado");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(nome))
                {
                    mmDevice = device;
                    break;
                }
            }
        }


    }


    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();



        showToast("Bluetoth Ligado");
    }



    private void showToast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    String beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        final String[] dataFinal = {"null"};

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            showToast(data);
                                            dataFinal[0] = data;
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }


            }
        });
        workerThread.start();
        return dataFinal[0];
    }

    void sendData(String msg) throws IOException
    {

        mmOutputStream.write(msg.getBytes());
        mmOutputStream.write('A');
        showToast("Data enviada");
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        showToast("Bluetooth Fechado");
    }



}