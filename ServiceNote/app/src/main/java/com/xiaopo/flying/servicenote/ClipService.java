package com.xiaopo.flying.servicenote;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author wupanjie
 */

public class ClipService extends Service {
  private static final String TAG = ClipService.class.getSimpleName();
  private String content;

  private ClipBinder binder = new ClipBinder();
  //private ClipInterface.Stub stub = new ClipInterface.Stub() {
  //  @Override
  //  public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble,
  //      String aString) throws RemoteException {
  //
  //  }
  //};

  @Nullable @Override public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind: ");
    return binder;
  }

  @Override public void onCreate() {
    super.onCreate();
    Log.d(TAG, "onCreate: ");
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand: ");
    final ClipboardManager clipboardManager =
        (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    clipboardManager.addPrimaryClipChangedListener(
        new ClipboardManager.OnPrimaryClipChangedListener() {
          @Override public void onPrimaryClipChanged() {
            ClipData clipData = clipboardManager.getPrimaryClip();

            ClipData.Item item = clipData.getItemAt(0);
            content = item.getText().toString().trim();
            Log.d(TAG, "onPrimaryClipChanged: item-->" + content);
          }
        });
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy: ");
  }

  public class ClipBinder extends Binder {

    public void showContent() {
      Log.d(TAG, "showContent: " + content);
    }
  }
}
