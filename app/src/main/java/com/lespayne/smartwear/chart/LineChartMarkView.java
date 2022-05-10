package com.lespayne.smartwear.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.lespayne.smartwear.R;

import java.util.List;

/**
 * 曲线图弹框布局
 *
 */
@SuppressLint("ViewConstructor")
public class LineChartMarkView extends MarkerView {
    private LinearLayout markerViewLayout;
    private TextView tvDate;
    private TextView tvValue0;
    private TextView tvValue1;
    private ValueFormatter xAxisValueFormatter;

    public LineChartMarkView(Context context, ValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_markview);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvDate = findViewById(R.id.tv_date);
        tvValue0 = findViewById(R.id.tv_value0);
        tvValue1 = findViewById(R.id.tv_value1);
        markerViewLayout = findViewById(R.id.markerView_layout);
    }

    public void setDialogBackground(int background) {
        markerViewLayout.setBackgroundResource(background);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            if (dataSetList.size() == 1) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(0);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                tvValue0.setText(xAxisValueFormatter.getFormattedValue(e.getX()) + "：" + (int) y);
                tvValue1.setVisibility(GONE);
                tvDate.setText(dataSet.getLabel() + "");
            } else if (dataSetList.size() == 2) {
                for (int i = 0; i < dataSetList.size(); i++) {
                    LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                    //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                    float y = dataSet.getValues().get((int) e.getX()).getY();
                    if (i == 0) {
                        tvValue0.setText(dataSet.getLabel() + "：" + (int) y);
                    }
                    if (i == 1) {
                        tvValue1.setText(dataSet.getLabel() + "：" + (int) y);
                    }
                }
                tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX()));
            }
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2.0f), -getHeight() - 20);
    }
}
