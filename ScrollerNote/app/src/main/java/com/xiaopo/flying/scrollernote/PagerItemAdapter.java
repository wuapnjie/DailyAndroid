package com.xiaopo.flying.scrollernote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author wupanjie
 */

public class PagerItemAdapter extends PagerLinearLayout.Adapter {
  @Override public View createView(ViewGroup parent) {
    return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nico, parent, false);
  }

  @Override public void bindView(final View view, final int position) {
    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(view.getContext(), "Position --> " + position, Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override public int getItemCount() {
    return 10;
  }
}
