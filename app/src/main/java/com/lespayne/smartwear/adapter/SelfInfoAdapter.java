package com.lespayne.smartwear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lespayne.smartwear.R;
import com.lespayne.smartwear.model.SelfBean;

import java.util.List;

public class SelfInfoAdapter extends RecyclerView.Adapter<SelfInfoAdapter.SelfHolder> {

    private List<SelfBean> dataList;
    private Context context;

    public SelfInfoAdapter(Context context, List<SelfBean> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SelfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelfHolder(LayoutInflater.from(context).inflate(R.layout.item_self_layout, parent, false));
    }

    public void setDataList(List<SelfBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(@NonNull SelfHolder holder, int position) {
        SelfBean selfBean = dataList.get(position);
        if (selfBean.isSex()) {
            holder.rightTv.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.cbBoy.setChecked(selfBean.getRightContent().equals("1"));
            holder.cbGril.setChecked(!selfBean.getRightContent().equals("1"));
            holder.cbBoy.setEnabled(selfBean.getRightContent().equals("1"));
            holder.cbGril.setEnabled(!selfBean.getRightContent().equals("1"));
        } else {
            holder.rightTv.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.rightTv.setText(selfBean.getRightContent() == null ? "" : selfBean.getRightContent());
        }
        holder.leftTv.setText(selfBean.getLeftTip() == null ? "" : selfBean.getLeftTip());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class SelfHolder extends RecyclerView.ViewHolder {
        private TextView leftTv;
        private TextView rightTv;
        private LinearLayout rightLayout;
        private CheckBox cbBoy;
        private CheckBox cbGril;

        public SelfHolder(@NonNull View itemView) {
            super(itemView);
            leftTv = itemView.findViewById(R.id.left_tip);
            rightTv = itemView.findViewById(R.id.right_content);
            rightLayout = itemView.findViewById(R.id.right_layout);
            cbBoy = itemView.findViewById(R.id.cb_boy);
            cbGril = itemView.findViewById(R.id.cb_gril);
        }
    }

}
