package com.lespayne.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

public abstract class BaseAdapter<T, V extends ViewBinding>
        extends RecyclerView.Adapter<BaseHolder<V>> {
    private final ArrayList<T> data;

    public BaseAdapter(ArrayList<T> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BaseHolder<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseHolder<>(onBindingView(parent));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder<V> holder, int position) {
        onBindingData(holder, data.get(position), position);
    }

    protected abstract void onBindingData(BaseHolder<V> holder, T t, int position);

    protected abstract V onBindingView(ViewGroup viewGroup);

    @Override
    public int getItemCount() {
        return data.size();
    }
}
