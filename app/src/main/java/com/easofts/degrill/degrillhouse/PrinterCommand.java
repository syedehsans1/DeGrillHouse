package com.easofts.degrill.degrillhouse;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class PrinterCommand extends AppCompatActivity {

    Button mainBtn;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    private DBHelper read;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";
    ListView order;
    ArrayList<sale> saleitems = new ArrayList<sale>();
    saleAdapter sadapter;
    String txtvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printsale);
        Intent i=getIntent();
        final int id=i.getIntExtra("OrderID",0);
        order=(ListView) findViewById(R.id.saleList);
        read=new DBHelper(getApplicationContext());
        saleitems=read.getsales(id);
        int total=0;
        txtvalue="           DE GRILL HOUSE\n" +
                        "FB:: https//www.facebook.com/degrillhouse\n\n" +
                        " Order #"+id+"  \n\n" +
                        "ItemID  Name              Price\n";
        for(int x=0;x<saleitems.size();x++){
            txtvalue+=  x+"  "+saleitems.get(x).getName()+"          "+saleitems.get(x).getPrice()+"\n\n";
            total=total+Integer.parseInt(saleitems.get(x).getPrice());
        }
        txtvalue+=      "           Total Bill:  "+total+"\n\n\n\n System Designed by: EA Software Solutions\n Contact: 0349-4637007\n\n\n";
        sadapter = new saleAdapter(this, saleitems);
        order.setAdapter(sadapter);
        mainBtn = (Button)findViewById(R.id.BtnPrint);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv=(TextView) findViewById(R.id.tv);
                tv.setText(txtvalue);
                IntentPrint(txtvalue);
                read.updatePaidStatus(id);
            }
        });
    }
    protected void InitPrinter(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("BlueTooth Printer")) //Note, you will need to change this to match the name of your device
                    {
                        Toast.makeText(this, "Printer Connected", Toast.LENGTH_LONG).show();
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            }
            else
            {
                value+="No Devices found";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
    }

    protected void beginListenForData(){
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("e", data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void IntentPrint(String txtvalue){
        byte[] buffer = txtvalue.getBytes();
        byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
        PrintHeader[3]=(byte) buffer.length;
        InitPrinter();
        /*if(PrintHeader.length>128)
        {
            value+="\nValue is more than 128 size\n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
        else
        {*/
            try
            {

                outputStream.write(txtvalue.getBytes());
                outputStream.close();
                //socket.close();
            }
            catch(Exception ex)
            {
                value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            }
        //}
    }
}
