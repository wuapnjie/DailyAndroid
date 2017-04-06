package com.xiaopo.flying.pretty;

/**
 * Created by snowbean on 16-12-7.
 */

public interface OnItemClickListener<T extends PrettyCell> {
  void onItemClick(T t, int position);
}
