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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


    public class MainActivity extends AppCompatActivity {
        private Button refresh;

        BluetoothAdapter mBluetoothAdapter;
        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice;
        OutputStream mmOutputStream;
        InputStream mmInputStream;
        Thread workerThread;
        byte[] readBuffer;
        int readBufferPosition;
        volatile boolean stopWorker;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


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



        refresh = (Button) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mBluetoothAdapter.isEnabled()){
                    startActivity(new Intent(getApplicationContext(),erroBluetooth.class));
                    overridePendingTransition(0,0);
                }


                try
                {
                    sendData("1111");
                }
                catch (IOException ex)
                {
                    showToast("SEND FAILED");
                }


            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        return true;
                    case R.id.auto:
                        startActivity(new Intent(getApplicationContext(),auto.class));
                        overridePendingTransition(0,0);
                        return true;

                }

                return false;

            }


        });







    }

        public void refreshData() {




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

        }



        private void showToast (String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }



        void beginListenForData()
        {
            final Handler handler = new Handler();
            final byte delimiter = 10; //This is the ASCII code for a newline character

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
        }

        void sendData(String msg) throws IOException
        {

            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write('A');
        }

        void closeBT() throws IOException
        {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        }




    }




