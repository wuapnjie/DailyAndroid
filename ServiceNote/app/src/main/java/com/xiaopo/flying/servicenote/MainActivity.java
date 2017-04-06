package com.xiaopo.flying.servicenote;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  private ClipServiceConnection connection;
  private ClipService.ClipBinder binder;
  private Intent serviceIntent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    connection = new ClipServiceConnection();
    TextView textView = (TextView) findViewById(R.id.tv_clipboard);
    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (binder != null) {
          binder.showContent();
        }
      }
    });
  }

  public void startService(View view) {
    serviceIntent = new Intent(this, ClipService.class);
    startService(serviceIntent);
    bindService(serviceIntent, connection, BIND_AUTO_CREATE);
  }

  public void stopService(View view) {
    unbindService(connection);
    stopService(serviceIntent);
  }

  private class ClipServiceConnection implements ServiceConnection {

    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      Log.d(TAG, "onServiceConnected: " + name.toString());
      Log.d(TAG, "onServiceConnected: " + service.getClass().getName());
      if (service instanceof ClipService.ClipBinder) {
        binder = (ClipService.ClipBinder) service;
      }
    }

    @Override public void onServiceDisconnected(ComponentName name) {
      Log.d(TAG, "onServiceDisconnected: " + name.toString());
    }
  }
}
