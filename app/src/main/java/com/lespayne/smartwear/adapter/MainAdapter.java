package com.lespayne.smartwear.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lespayne.base.view.CustomCircleBar;
import com.lespayne.smartwear.R;
import com.lespayne.smartwear.model.MainBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.SelfHolder> {

    private List<MainBean> dataList;
    private Context context;

    public MainAdapter(Context context, List<MainBean> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public void setDataList(List<MainBean> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SelfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelfHolder(LayoutInflater.from(context).inflate(R.layout.item_histroy_layout, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SelfHolder holder, int position) {
        MainBean mainBean = dataList.get(position);
        holder.tvMainItemTime.setText(formatTime(mainBean.getTime()));
        holder.tvRecode.setText("姓名：" + mainBean.getRecord());
        holder.tvLength.setText("时间：" + mainBean.getLength());
        holder.tvNote.setText("备注：" + mainBean.getNote());

        float bodyTem=Float.parseFloat(mainBean.getBody());
        holder.pbAverageView.setPercent((int) bodyTem);
        holder.pbAverageView.setCustomText("体温");
        holder.pbAverageView.setUnit("℃");
        holder.pbAverageView.setProgressColor(context.getResources().getColor(R.color.y_FFC107));

        holder.pbNormalView.setPercent(Integer.parseInt(mainBean.getHeart()));
        holder.pbNormalView.setCustomText("心率");
        holder.pbNormalView.setUnit("BPM");
        holder.pbNormalView.setProgressColor(context.getResources().getColor(R.color.light));

        String[] split = mainBean.getHumidity().split("%");
        holder.pbView.setPercent(Integer.parseInt(split[0]));
        holder.pbView.setCustomText("湿度");
        holder.pbView.setUnit("RH");
        holder.pbView.setProgressColor(context.getResources().getColor(R.color.red_E4999F));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class SelfHolder extends RecyclerView.ViewHolder {
        private TextView tvMainItemTime;
        private TextView tvRecode;
        private TextView tvLength;
        private TextView tvNote;
        private CustomCircleBar pbAverageView;
        private CustomCircleBar pbNormalView;
        private CustomCircleBar pbView;

        public SelfHolder(@NonNull View itemView) {
            super(itemView);
            tvMainItemTime = itemView.findViewById(R.id.tv_main_item_time);
            pbAverageView = itemView.findViewById(R.id.pb_average_view);
            pbNormalView = itemView.findViewById(R.id.pb_normal_view);
            pbView = itemView.findViewById(R.id.pb_view);
            tvRecode = itemView.findViewById(R.id.tv_recode);
            tvLength = itemView.findViewById(R.id.tv_length);
            tvNote = itemView.findViewById(R.id.tv_note);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        return sdf.format(new Date(timeInMillis));
    }
}
