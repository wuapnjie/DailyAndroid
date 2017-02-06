package com.xiaopo.flying.activitylifenote;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {
  private static final String TAG = "SecondActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: ");
    setContentView(R.layout.activity_second);
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.d(TAG, "onConfigurationChanged: ");
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Log.d(TAG, "onRestoreInstanceState: ");
  }

  @Override protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart: ");
  }

  @Override protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: ");
  }

  @Override protected void onPostResume() {
    super.onPostResume();
    Log.d(TAG, "onPostResume: ");
  }

  @Override protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause: ");
  }

  @Override protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop: ");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: ");
  }

  @Override protected void onRestart() {
    super.onRestart();
    Log.d(TAG, "onRestart: ");
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(TAG, "onSaveInstanceState: ");
  }
}
