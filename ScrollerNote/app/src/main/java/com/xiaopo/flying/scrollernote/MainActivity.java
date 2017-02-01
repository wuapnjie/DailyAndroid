package com.xiaopo.flying.scrollernote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoPicker;

public class MainActivity extends AppCompatActivity {
  private PagerLinearLayout linearLayout;
  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    linearLayout = (PagerLinearLayout) findViewById(R.id.pager_linear_layout);
    recyclerView = (RecyclerView) findViewById(R.id.photo_list);

    linearLayout.setAdapter(new PagerItemAdapter());
    linearLayout.setMode(PagerLinearLayout.MODE_PAGER);
    linearLayout.setMinHeight(DipPixelUtils.dip2px(this, 150));
    linearLayout.setMaxHeight(DipPixelUtils.dip2px(this, 275));

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
      }, Define.DEFAULT_REQUEST_PERMISSION_CODE);
    } else {
      startLoad();
    }
  }

  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == Define.DEFAULT_REQUEST_PERMISSION_CODE
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
      startLoad();
    }
  }

  private void startLoad() {
    PhotoPicker.newInstance().inflate(recyclerView, new GridLayoutManager(this, 4));
  }
}
