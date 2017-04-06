package com.xiaopo.flying.pretty;

import android.support.v7.util.DiffUtil;
import java.util.List;

/**
 * The Cell Data Diff Callback
 * Created by snowbean on 16-9-11.
 */
public abstract class PrettyDiffCallback<T extends PrettyCell> extends DiffUtil.Callback {
  protected List<T> oldCellData;
  protected List<T> newCellData;

  public PrettyDiffCallback(List<T> newCellData) {
    this.newCellData = newCellData;
  }

  public PrettyDiffCallback(List<T> oldCellData, List<T> newCellData) {
    this.oldCellData = oldCellData;
    this.newCellData = newCellData;
  }

  @Override public int getOldListSize() {
    return oldCellData == null ? 0 : oldCellData.size();
  }

  @Override public int getNewListSize() {
    return newCellData == null ? 0 : newCellData.size();
  }

  @Override public abstract boolean areItemsTheSame(int oldItemPosition, int newItemPosition);

  @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return oldCellData.get(oldItemPosition).equals(newCellData.get(newItemPosition));
  }

  public List<T> getOldCellData() {
    return oldCellData;
  }

  public void setOldCellData(List<T> oldCellData) {
    this.oldCellData = oldCellData;
  }

  public List<T> getNewCellData() {
    return newCellData;
  }

  public void setNewCellData(List<T> newCellData) {
    this.newCellData = newCellData;
  }
}
