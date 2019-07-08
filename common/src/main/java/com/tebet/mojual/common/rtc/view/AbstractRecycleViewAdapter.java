package com.tebet.mojual.common.rtc.view;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.tebet.mojual.common.R;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> dataSet;
    private OnRecycleViewClick<T> onActionClick;
    protected Activity context;

    public AbstractRecycleViewAdapter(Activity context, List<T> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public AbstractRecycleViewAdapter(Activity context) {
        this.context = context;
        dataSet = new ArrayList<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(getBaseContentLayout(), parent, false);
        FrameLayout placeHolder = v.findViewById(R.id.placeHolder);
        context.getLayoutInflater().inflate(getContentLayout(viewType), placeHolder);
        return getViewHolder(v, viewType);
    }

    protected abstract int getContentLayout(int viewType);

    protected int getBaseContentLayout() {
        return R.layout.project_item;
    }

    protected abstract BaseViewHolder getViewHolder(View v, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).item = getItem(position);
        if (((BaseViewHolder) holder).btnDelete != null) {
            ((BaseViewHolder) holder).btnDelete.setVisibility(canDelete(position) ? View.VISIBLE : View.GONE);
        }
        onBindInstanceItemView(holder, position);
    }

    public abstract void onBindInstanceItemView(RecyclerView.ViewHolder holder, int position);

    public abstract boolean canDelete(int position);

    public abstract boolean canShowDetail();

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItem(T item) {
        dataSet.add(item);
        notifyDataSetChanged();
    }

    public void addItems(@NonNull List<T> dataSet) {
        if (this.dataSet == null) {
            this.dataSet = new ArrayList<>();
        }
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void setItems(@NonNull List<? extends T> dataSet) {
        this.dataSet = new ArrayList<>();
        this.dataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        dataSet.remove(position);
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int indexOf = dataSet.indexOf(item);
        if (indexOf != -1) {
            dataSet.remove(indexOf);
            notifyDataSetChanged();
        }
    }


    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }

    public List<T> getItems() {
        return dataSet;
    }

    public void setOnActionClick(OnRecycleViewClick onActionClick) {
        this.onActionClick = onActionClick;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView btnDelete;
        protected FrameLayout flRoot;
        protected FrameLayout placeHolder;
        T item;

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            btnDelete = itemView.findViewById(R.id.button_remove);
            flRoot = itemView.findViewById(R.id.flRoot);
            placeHolder = itemView.findViewById(R.id.placeHolder);

            if (btnDelete != null) {
                btnDelete.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_remove) {
                if (onActionClick != null) {
                    onActionClick.onRemoveClick(item);
                }
                removeItem(item);
            } else {
                if (onActionClick != null && canShowDetail()) {
                    onActionClick.onShowDetailClick(item);
                }
            }
        }
    }

    public interface OnRecycleViewClick<T> {

        void onRemoveClick(T removeObject);

        void onShowDetailClick(T detailObject);
    }
}
