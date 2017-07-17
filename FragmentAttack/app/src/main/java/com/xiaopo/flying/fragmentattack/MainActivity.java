package com.xiaopo.flying.fragmentattack;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
  private FrameLayout fragmentContainer;
  private OneFragment one;
  private TwoFragment two;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    one = OneFragment.newInstance();
    two = TwoFragment.newInstance();

    fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(R.id.fragment_container, one)
        .add(R.id.fragment_container, two)
        .hide(two)
        .commit();
  }

  public void showOne(View view) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (one.isVisible()) return;
    transaction.hide(two).show(one).commit();
  }

  public void showTwo(View view) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (two.isVisible()) return;
    transaction.hide(one).show(two).commit();
  }
}
