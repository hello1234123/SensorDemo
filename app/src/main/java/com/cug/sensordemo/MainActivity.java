package com.cug.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener {
    public static final int MAX_SHOW_COUNT = 100;
    private SensorManager mSensorManager;
    private Sensor mAccSensor;//accelerometer sensor
    //==========================================================================================
    private LineChart mLineChart;
    private List<Entry> accXEntries;
    private List<Entry> accYEntries;
    private List<Entry> accZEntries;

    private List<Entry> accEntries;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //==========================================================================================
        mLineChart = (LineChart) findViewById(R.id.chart);
        accXEntries = new ArrayList<>();
        accYEntries = new ArrayList<>();
        accZEntries = new ArrayList<>();
        accEntries = new ArrayList<>();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        count++;
        accXEntries.add(new Entry(count, sensorEvent.values[0]));
        accYEntries.add(new Entry(count, sensorEvent.values[1]));
        accZEntries.add(new Entry(count, sensorEvent.values[2]));
        float acc = (float) Math.sqrt(sensorEvent.values[0] * sensorEvent.values[0] +
                sensorEvent.values[1] * sensorEvent.values[0] +
                sensorEvent.values[2] * sensorEvent.values[2]);
        accEntries.add(new Entry(count, acc));
        if (accXEntries.size() >= MAX_SHOW_COUNT) {
            accXEntries.remove(0);
            accYEntries.remove(0);
            accZEntries.remove(0);
            accEntries.remove(0);
        }
        LineDataSet accXLineDataSet = new LineDataSet(accXEntries, "accX");
        accXLineDataSet.setColor(Color.RED);

        LineDataSet accYLineDataSet = new LineDataSet(accYEntries, "accY");
        accYLineDataSet.setColor(Color.GREEN);

        LineDataSet accZLineDataSet = new LineDataSet(accZEntries, "accZ");
        accZLineDataSet.setColor(Color.BLUE);

        LineDataSet accLineDataSet = new LineDataSet(accEntries, "acc");
        accLineDataSet.setColor(Color.BLACK);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(accXLineDataSet);
        dataSets.add(accYLineDataSet);
        dataSets.add(accZLineDataSet);
        dataSets.add(accLineDataSet);
        LineData lineData = new LineData(dataSets);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
