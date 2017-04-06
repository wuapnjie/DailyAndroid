package com.xiaopo.flying.pretty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by snowbean on 16-9-10.
 */
public abstract class PrettyViewHolder extends RecyclerView.ViewHolder {

  public PrettyViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void onBind(PrettyCell data);

  protected Context context() {
    return itemView.getContext();
  }

  protected int position() {
    return getAdapterPosition();
  }
}
