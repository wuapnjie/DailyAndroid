package com.xiaopo.flying.pretty;

import android.view.View;

/**
 * Created by snowbean on 16-9-10.
 */
public abstract class PrettyTypeViewHolder<T extends PrettyCell> extends PrettyViewHolder {
  protected T data;

  protected OnItemClickListener<T> itemClickListener;

  public PrettyTypeViewHolder(View itemView) {
    super(itemView);
  }

  @SuppressWarnings("unchecked") @Override public void onBind(PrettyCell data) {
    this.data = (T) data;
    bindCellData((T) data);
  }

  public abstract void bindCellData(T data);

  public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
    this.itemClickListener = itemClickListener;
  }


  public T getData() {
    return data;
  }
}
