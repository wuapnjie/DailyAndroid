package com.xiaopo.flying.scrollernote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  private PagerLinearLayout linearLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    linearLayout = (PagerLinearLayout) findViewById(R.id.pager_linear_layout);

    linearLayout.setAdapter(new PagerItemAdapter());
    linearLayout.setMinHeight(DipPixelUtils.dip2px(this, 140));
    linearLayout.setMaxHeight(DipPixelUtils.dip2px(this, 300));
    linearLayout.setMode(PagerLinearLayout.MODE_PAGER);
  }
}
