//package com.iot.hahoang.iwatch5;
//
///**
// * Created by haha8 on 12/6/2017.
// */
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class StepCounter {
//
//    private SensorManager mSensorManager;
//    private Sensor mSensor;
//    private boolean mActivated;
//    private int mSteps = 0;
//    private int dataCounter = 0;
//
//    private float[] x_axis = new float[4];
//    private float[] y_axis = new float[4];
//    private float[] z_axis = new float[4];
//    private Integer mInterval = 0;
//    private float x_max = -99;
//    private float x_min = 99;
//    private float y_max = -99;
//    private float y_min = 99;
//    private float z_max = -99;
//    private float z_min = 99;
//
//    private float sample_new = Float.MAX_VALUE;
//    private float sample_old = Float.MAX_VALUE;
//
//    private float precision = Float.MAX_VALUE;
//
//    enum Active_Axis {
//        X,
//        Y,
//        Z,
//        NONE
//    }
//
//    private Active_Axis mActAxis = Active_Axis.NONE;
//
//    private float active_Axis_thresh = 0;
//
//    List<StepListener> stepListenerList;
//
//    public StepCounter(Context _context) {
//        mSensorManager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(this,mSensor,20000); // 20 ms
//
//        stepListenerList = new ArrayList<>();
//    }
//
//    public void setActivated(boolean _activated) {
//        mActivated = _activated;
//        if(!mActivated) {
//            mSteps = 0;
//            resetMinMax();
//            resetSamples();
//        }
//    }
//
//    public boolean getActivated() {
//        return mActivated;
//    }
//
//    public void subscribeStepListener(StepListener _listener) {
//        if(!stepListenerList.contains(_listener)) {
//            stepListenerList.add(_listener);
//        }
//    }
//
//    public void unSubscribeStepListener(StepListener _listener) {
//        if(stepListenerList.contains(_listener)) {
//            stepListenerList.remove(_listener);
//        }
//    }
//
//    private void resetSamples() {
//        sample_new = Float.MAX_VALUE;
//        sample_old = Float.MAX_VALUE;
//
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (mActivated) {
//
//
//            float x = event.values[0];
//            float y = event.values[1];
//            float z = event.values[2];
//
//            updateAxisValues(x, y, z);
//
//            dataCounter++;
//            mInterval++;
//            if(dataCounter>=50) {
//                //calc net threshold
//                dataCounter = 0;
//                mInterval = 0;
//                float x_thresh = (x_max + x_min) / 2;
//                float y_thresh = (y_max + y_min) / 2;
//                float z_thresh = (z_max + z_min) / 2;
//
//                // get highest peak
//                float x_peak2peak = Math.abs(x_max - x_min);
//                float y_peak2peak = Math.abs(y_max - y_min);
//                float z_peak2peak = Math.abs(z_max - z_min);
//
//                // calc most active axis
//                if(x_peak2peak > y_peak2peak && x_peak2peak > z_peak2peak) {
//                    active_Axis_thresh = x_thresh;
//                    mActAxis = Active_Axis.X;
//                    precision = x_peak2peak / 2;
//                    if(precision < 3) {
//                        precision = 3;
//                    }
//                } else
//                if(y_peak2peak > x_peak2peak && y_peak2peak > z_peak2peak) {
//                    active_Axis_thresh = y_thresh;
//                    mActAxis = Active_Axis.Y;
//                    precision = y_peak2peak / 2;
//                    if(precision < 3) {
//                        precision = 3;
//                    }
//                } else
//                if(z_peak2peak > y_peak2peak && z_peak2peak > x_peak2peak) {
//                    active_Axis_thresh = z_thresh;
//                    mActAxis = Active_Axis.Z;
//                    precision = z_peak2peak / 2;
//                    if(precision < 3) {
//                        precision = 3;
//                    }
//                }
//                resetMinMax();
//                resetSamples();
//            }
//
//            switch (mActAxis) {
//                case X:
//                    if(sample_new == Float.MAX_VALUE) {
//                        sample_new = x;
//                        sample_old = x;
//                    } else {
//                        sample_old = sample_new;
//                        if(Math.abs(x - sample_old) > precision) {
//                            sample_new = x;
//
//                            //STEP
//                            if(sample_old > active_Axis_thresh && sample_new < active_Axis_thresh && mInterval > 10) {
//                                mSteps++;
//                                mInterval = 0;
//                                for(StepListener listener : stepListenerList) {
//                                    listener.onNewStep();
//                                }
//                            }
//                        }
//                    }
//                    break;
//                case Y:
//                    if(sample_new == Float.MAX_VALUE) {
//                        sample_new = y;
//                        sample_old = y;
//                    } else {
//                        sample_old = sample_new;
//                        if(Math.abs(sample_new - y) > precision) {
//                            sample_new = y;
//
//                            //STEP
//                            if(sample_old > active_Axis_thresh && sample_new < active_Axis_thresh && mInterval > 10) {
//                                mSteps++;
//                                mInterval = 0;
//                                for(StepListener listener : stepListenerList) {
//                                    listener.onNewStep();
//                                }
//                            }
//                        }
//                    }
//                    break;
//                case Z:
//                    if(sample_new == Float.MAX_VALUE) {
//                        sample_new = z;
//                        sample_old = z;
//                    } else {
//                        sample_old = sample_new;
//                        if(Math.abs(sample_new - z) > precision) {
//                            sample_new = z;
//
//                            //STEP
//                            if(sample_old > active_Axis_thresh && sample_new < active_Axis_thresh && mInterval > 10) {
//                                mSteps++;
//                                mInterval = 0;
//                                for(StepListener listener : stepListenerList) {
//                                    listener.onNewStep();
//                                }
//                            }
//                        }
//                    }
//                    break;
//                case NONE:
//                    break;
//            }
//        }
//    }
//
//    public int getSteps() {
//        return mSteps;
//    }
//
//    private void resetMinMax() {
//        x_max = -99;
//        x_min = 99;
//        y_max = -99;
//        y_min = 99;
//        z_max = -99;
//        z_min = 99;
//    }
//
//    private float getXFiltered() {
//        return (x_axis[0]+x_axis[1]+x_axis[2]+x_axis[3])/4;
//    }
//
//    private float getYFiltered() {
//        return (y_axis[0]+y_axis[1]+y_axis[2]+y_axis[3])/4;
//    }
//
//    private float getZFiltered() {
//        return (z_axis[0]+z_axis[1]+z_axis[2]+z_axis[3])/4;
//    }
//
//    void updateAxisValues(float x, float y, float z) {
//
//        //set back
//        for(int i = 0; i < 3; i++) {
//            x_axis[i+1] = x_axis[i];
//            y_axis[i+1] = y_axis[i];
//            z_axis[i+1] = z_axis[i];
//        }
//
//        //set new values
//        x_axis[0] = x;
//        y_axis[0] = y;
//        z_axis[0] = z;
//
//        float x_filt = getXFiltered();
//        float y_filt = getYFiltered();
//        float z_filt = getZFiltered();
//
//        if(x_filt > x_max)
//            x_max = x_filt;
//        if(x_filt < x_min)
//            x_min = x_filt;
//        if(y_filt > y_max)
//            y_max = y_filt;
//        if(y_filt < y_min)
//            y_min = y_filt;
//        if(z_filt > z_max)
//            z_max = z_filt;
//        if(z_filt < z_min)
//            z_min = z_filt;
//    }
//
//    public float getStrideLength() {
//        boolean female = App.getCurrentUser().getSex();
//        int age = App.getCurrentUser().getAge();
//        float height = App.getCurrentUser().getHeight();
//        int weight = App.getCurrentUser().getWeight();
//        if(female) {
//            return -0.001f * age + 1.058f * height - 0.002f * weight - 0.129f;
//        } else {
//            return -0.002f * age + 0.760f * height - 0.001f * weight + 0.327f;
//        }
//    }
//
//    public float getCalsPerMeter(int weight) {
//        float bmr = 24*weight*4.184f*1000 / 3600 / 24;
//        float totalEnergy = 1.64f * weight;
//        float mechanicEnergy = 20.6f;
//        return (totalEnergy - bmr - mechanicEnergy) / 4.184f;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//}