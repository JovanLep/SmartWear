package com.lespayne.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BaseHolder<V extends ViewBinding> extends RecyclerView.ViewHolder {
    public BaseHolder(@NonNull V viewBinding) {
        super(viewBinding.getRoot());
    }
}
