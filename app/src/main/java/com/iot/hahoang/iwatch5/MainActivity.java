package com.iot.hahoang.iwatch5;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static java.lang.Math.abs;

public class MainActivity  extends BlunoLibrary {
	private Button buttonScan;
	private Button buttonSerialSend;

	private EditText serialSendText;
	private TextView serialReceivedText;

	private TextView txtEMG1;
	private TextView txtEMG2;
	private TextView txtBreath;

	private TextView txtData;

	private double xValue = 5d;
	private final LineGraphSeries<DataPoint> mSeriesEMG1=new LineGraphSeries<>();
	private final LineGraphSeries<DataPoint> mSeriesEMG2=new LineGraphSeries<>();
	private final LineGraphSeries<DataPoint> mSeriesBreath=new LineGraphSeries<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess();                                                        //onCreate Process by BlunoLibrary


		serialBegin(9600);                                                    //set the Uart Baudrate on BLE chip to 115200

		serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);    //initial the EditText of the received data
		serialSendText = (EditText) findViewById(R.id.serialSendText);            //initial the EditText of the sending data

		//initial the textview for display data
		txtEMG1 = (TextView) findViewById(R.id.txtEMG1);
		txtEMG2 = (TextView) findViewById(R.id.txtEMG2);
		txtBreath = (TextView) findViewById(R.id.txtBreath);

		txtData = (TextView) findViewById(R.id.txtData);

		buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);        //initial the button for sending the data
		buttonSerialSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				serialSend(serialSendText.getText().toString());                //send the data to the BLUNO
			}
		});

		buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device
		buttonScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
			}
		});

		GraphView graphEMG = (GraphView) findViewById(R.id.graphEMG);
		mSeriesEMG1.setColor(Color.BLUE);
		mSeriesEMG1.setTitle("EMG1");
		graphEMG.addSeries(mSeriesEMG1);

		mSeriesEMG2.setColor(Color.BLACK);
		mSeriesEMG2.setTitle("EMG2");
		graphEMG.addSeries(mSeriesEMG2);

		graphEMG.getViewport().setXAxisBoundsManual(true);
		graphEMG.getViewport().setMinX(0);
		graphEMG.getViewport().setMaxX(40);

		GraphView graphBreath = (GraphView) findViewById(R.id.graphBreath);
		mSeriesBreath.setColor(Color.GREEN);
		mSeriesBreath.setTitle("Breath");
		graphBreath.addSeries(mSeriesBreath);

		graphBreath.getViewport().setXAxisBoundsManual(true);
		graphBreath.getViewport().setMinX(0);
		graphBreath.getViewport().setMaxX(40);
	}

	protected void onResume() {
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();                                                        //onResume Process by BlunoLibrary
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);                    //onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		onPauseProcess();                                                        //onPause Process by BlunoLibrary
	}

	protected void onStop() {
		super.onStop();
		onStopProcess();                                                        //onStop Process by BlunoLibrary
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
	}

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {                                            //Four connection state
			case isConnected:
				buttonScan.setText("Connected");
				break;
			case isConnecting:
				buttonScan.setText("Connecting");
				break;
			case isToScan:
				buttonScan.setText("Scan");
				break;
			case isScanning:
				buttonScan.setText("Scanning");
				break;
			case isDisconnecting:
				buttonScan.setText("isDisconnecting");
				break;
			default:
				break;
		}
	}

	@Override
	public void onSerialReceived(String theString) { //Once connection data received, this function will be called
		serialReceivedText.append(theString); //append the received data to a list

		//analyze the received data list
		String lines[] = serialReceivedText.getText().toString().split("\\r?\\n");
		String lastOne = lines[lines.length - 1];
		System.out.println(lastOne); //get the last line from received data list only
		String[] list = lastOne.split(","); //split data by comma and store it to array

		double EMG1 = 0;
		double EMG2 = 0;
		double Breath = 0;
		try {
			EMG1 = ParseDouble(list[0].trim());
			EMG2 = ParseDouble(list[1].trim());

			Breath = ParseDouble(list[2].trim());

			txtEMG1.setText(EMG1 + "");
			txtEMG2.setText(EMG2 + "");

			txtBreath.setText(Breath + "");
		} catch (Exception e) {

		}
		xValue += 1d;
		mSeriesEMG1.appendData(new DataPoint(xValue, EMG1), true, 40);
		mSeriesEMG2.appendData(new DataPoint(xValue, EMG2), true, 40);
		mSeriesBreath.appendData(new DataPoint(xValue, Breath), true, 40);

	}

	double ParseDouble(String strNumber) {
		if (strNumber != null && strNumber.length() > 0) {
			try {
				return Double.parseDouble(strNumber);
			} catch (Exception e) {
				return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
			}
		} else return 0;
	}


}