package com.xiaopo.flying.pretty;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snowbean on 16-9-10.
 */
public abstract class PrettyAdapter extends RecyclerView.Adapter {
  private static final String TAG = "PrettyAdapter";

  protected List<PrettyCell> prettyCell;

  @Override public int getItemViewType(int position) {
    return prettyCell.get(position).getDataBindingLayout();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return createCell(itemView, viewType);
  }

  protected abstract PrettyViewHolder createCell(View itemView, int viewType);

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    PrettyCell data = prettyCell.get(position);

    if (holder == null || data == null) {
      Log.e(TAG, "onBindViewHolder: the holder is null or the data is null");
      return;
    }

    if (holder instanceof PrettyViewHolder) {
      ((PrettyViewHolder) holder).onBind(data);
    }
  }

  public void swapItems(PrettyDiffCallback diffCallback) {
    swapItems(diffCallback, false);
  }

  @SuppressWarnings("unchecked")
  public void swapItems(final PrettyDiffCallback diffCallback, boolean needAsync) {
    if (diffCallback == null) return;
    if (diffCallback.getOldCellData() == null) {
      diffCallback.setOldCellData(prettyCell);
    }
    final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

    if (prettyCell != null) {
      this.prettyCell.clear();
      this.prettyCell.addAll(diffCallback.getNewCellData());
      diffResult.dispatchUpdatesTo(this);
    } else {
      refreshData(diffCallback.getNewCellData());
    }
  }

  //public void refreshData(List<PrettyCell> data) {
  //  if (prettyCell != null) {
  //    prettyCell.clear();
  //    prettyCell.addAll(data);
  //  } else {
  //    prettyCell = data;
  //  }
  //
  //  notifyDataSetChanged();
  //}

  public <T extends PrettyCell> void refreshData(List<T> data) {
    if (prettyCell != null) {
      prettyCell.clear();
      prettyCell.addAll(data);
    } else {
      prettyCell = new ArrayList<>();
      prettyCell.addAll(data);
    }

    notifyDataSetChanged();
  }

  public <T extends PrettyCell> void addData(List<T> data) {
    if (data == null) return;
    if (prettyCell == null) {
      refreshData(data);
      return;
    }
    final int oldCount = getItemCount();
    prettyCell.addAll(data);

    notifyItemInserted(oldCount);
  }

  @Override public int getItemCount() {
    return prettyCell == null ? 0 : prettyCell.size();
  }

  public List<PrettyCell> getPrettyCell() {
    return prettyCell;
  }
}
