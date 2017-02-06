package com.xiaopo.flying.constrainmaster;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
  private ConstraintLayout constraintLayout;
  private ConstraintSet applyConstraintSet = new ConstraintSet();
  private ConstraintSet resetConstraintSet = new ConstraintSet();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    constraintLayout = (ConstraintLayout) findViewById(R.id.main);
    resetConstraintSet.clone(constraintLayout);
    applyConstraintSet.clone(this,R.layout.activity_main2);
  }

  public void onApplyClick(View view) {
    TransitionManager.beginDelayedTransition(constraintLayout);

    applyConstraintSet.applyTo(constraintLayout);
  }

  public void onResetClick(View view) {
    TransitionManager.beginDelayedTransition(constraintLayout);
    resetConstraintSet.applyTo(constraintLayout);
  }
}
