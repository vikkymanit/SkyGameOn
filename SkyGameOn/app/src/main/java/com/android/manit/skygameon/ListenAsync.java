package com.android.manit.skygameon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by manit on 20/11/16.
 */
public class ListenAsync extends AsyncTask implements MqttCallback{

    private MqttConnectOptions mqttConnectOptions;
    private static MqttClient cl;
    private Context mainContext;

    ListenAsync(Context context){
        mainContext = context;
    }

    @Override
    protected void onPreExecute(){
        mqttConnectOptions = new MqttConnectOptions();
        try {
            cl = new MqttClient("tcp://broker.hivemq.com:1883", "AndroidSubscriber", new MemoryPersistence());
            cl.connect(mqttConnectOptions);
            Log.d("AppDebug", "Connected");
            cl.subscribe("/temp/flashlight", 0);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        cl.setCallback(this);
        return null;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        publishProgress();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void onProgressUpdate(Object[] values) {
        Log.d("AppDebug", "on progress reached");
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mainContext, notification);
        r.play();
    }
}
