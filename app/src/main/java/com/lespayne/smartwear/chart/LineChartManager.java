package com.lespayne.smartwear.chart;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lespayne.smartwear.App;
import com.lespayne.smartwear.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图管理类
 */
public class LineChartManager {
    /**
     * X轴,   左侧Y轴,   右侧Y轴 自定义XY轴值,  图例,    限制线
     */
    private LineChart lineChart;
    private XAxis xAxis;
    private YAxis leftYAxis;
    private YAxis rightYAxis;
    private List<String> xData = new ArrayList<>();
    private List<Float> dataList = new ArrayList<>();
    private LineDataSet lineDataSet;

    public LineChartManager(LineChart lineChart) {
        this.lineChart = lineChart;
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initChart(lineChart);
    }

    private void initChart(LineChart lineChart) {
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //背景
        lineChart.setBackgroundColor(Color.WHITE);
        //设置chart是否可以触摸
        lineChart.setTouchEnabled(true);
        //设置是否可以拖拽
        lineChart.setDragEnabled(false);
        //设置是否可以缩放 x和y，默认true
        lineChart.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setDoubleTapToZoomEnabled(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //高亮
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setMaxHighlightDistance(500);
        //底部间距
        lineChart.setExtraBottomOffset(9f);
        //设置XY轴动画效果
        lineChart.animateY(1500);
        lineChart.animateX(500);
        Description description = new Description();
        description.setText("");
        description.setEnabled(false);
        lineChart.setDescription(description);
        //XY轴的设置
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        xAxis.setDrawGridLines(false);
        rightYAxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        //设置Y轴网格线为虚线
        leftYAxis.enableGridDashedLine(8f, 8f, 0f);
        leftYAxis.setStartAtZero(false);
        rightYAxis.setEnabled(false);

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        //是否显示
        legend.setEnabled(true);
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setHighLightColor(color);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(1f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        //设置填充颜色
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(App.getApp().getApplicationContext(), R.drawable.shape_bg));
        //设置曲线展示为圆滑曲线（如果不设置则默认折线）
        lineDataSet.setMode((mode == null) ? LineDataSet.Mode.LINEAR : mode);
    }

    /**
     * 设置X轴的显示值
     *
     * @param min        x轴最小值
     * @param max        x轴最大值
     * @param labelCount x轴的分割数量
     */
    public void setXAxisData(float min, float max, int labelCount) {
        xAxis.setAxisMinimum(min);
        xAxis.setAxisMaximum(max);
        xAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 自定义的 X轴显示内容
     *
     * @param labelCount x轴的分割数量
     */
    public void setXAxisData(final List<String> xAxisStr, int labelCount) {
        xAxis.setLabelCount(labelCount, false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xAxisStr.get((int) value % xAxisStr.size());
            }
        });
        lineChart.invalidate();
    }

    /**
     * 设置Y轴值
     */
    public void setYAxisData(float max, float min, int labelCount) {
        leftYAxis.setAxisMaximum(max);
        leftYAxis.setAxisMinimum(min);
        leftYAxis.setLabelCount(labelCount, false);

        rightYAxis.setAxisMaximum(max);
        rightYAxis.setAxisMinimum(min);
        rightYAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 自定义的 y轴显示内容
     *
     * @param labelCount y轴的分割数量
     */
    public void setYAxisData(final List<String> yAxisStr, int labelCount) {
        xAxis.setLabelCount(labelCount, false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return yAxisStr.get((int) value % yAxisStr.size());
            }
        });
        lineChart.invalidate();
    }


    /**
     * 设置描述信息
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }


    /**
     * 设置线条填充背景颜色
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }


    /**
     * 动态添加数据
     */
    public void addEntry(float y, String proStr) {

        Entry entry = new Entry(dataList.size(), y);
        LineData lineData = lineChart.getData();
        if (lineChart.getData() == null || lineChart.getData().getDataSetCount() == 0) {
            return;
        }
        xData.add(proStr);
        int count = Math.min(xData.size(), 6);
        xAxis.setLabelCount(count, false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int position = (int) value;
                if (position > 0 && position < xData.size()) {
                    return xData.get(position);
                } else if (position <= 0) {
                    return xData.get(0);
                } else {
                    return "";
                }
            }
        });

        Entry entrys = new Entry(lineDataSet.getEntryCount(), entry.getY());
        lineData.addEntry(entrys, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    /**
     * 展示曲线
     *
     * @param color 曲线颜色
     */
    public void showLineChart(int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Entry entry = new Entry(i, dataList.get(i));
            entries.add(entry);
        }

        xAxis.setLabelRotationAngle(-60);
        //设置是否绘制刻度
        xAxis.setDrawLabels(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(7f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int position = (int) value;
                if (position > 0 && position < xData.size()) {
                    return xData.get(position);
                } else if (position <= 0) {
                    return xData.get(0);
                } else {
                    return "";
                }
            }
        });

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftYAxis.setLabelCount(3);
        leftYAxis.setDrawLabels(true);
        leftYAxis.setDrawZeroLine(false);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setZeroLineColor(Color.GRAY);
        leftYAxis.setZeroLineWidth(0f);
        leftYAxis.setAxisLineWidth(0f);
        leftYAxis.setAxisLineColor(Color.GRAY);
        leftYAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value + "");
            }
        });

        lineDataSet = new LineDataSet(entries, "次/分钟");
        //LINEAR 折线图  CUBIC_BEZIER 圆滑曲线
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> leftYAxis.getAxisMinimum());
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    public void showLineChart(List<Float> yList,List<String> xList,int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < yList.size(); i++) {
            Entry entry = new Entry(i, yList.get(i));
            entries.add(entry);
        }

        xAxis.setLabelRotationAngle(-60);
        //设置是否绘制刻度
        xAxis.setDrawLabels(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(7f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int position = (int) value;
                if (position > 0 && position < xList.size()) {
                    return xList.get(position);
                } else if (position <= 0) {
                    return xList.get(0);
                } else {
                    return "";
                }
            }
        });

        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftYAxis.setLabelCount(3);
        leftYAxis.setDrawLabels(true);
        leftYAxis.setDrawZeroLine(false);
        leftYAxis.setDrawAxisLine(false);
        leftYAxis.setZeroLineColor(Color.GRAY);
        leftYAxis.setZeroLineWidth(0f);
        leftYAxis.setAxisLineWidth(0f);
        leftYAxis.setAxisLineColor(Color.GRAY);
        leftYAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ((int) value + "");
            }
        });

        lineDataSet = new LineDataSet(entries, "次/分钟");
        //LINEAR 折线图  CUBIC_BEZIER 圆滑曲线
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> leftYAxis.getAxisMinimum());
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(Context context, @DrawableRes int drawable) {
        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        mv.setDialogBackground(drawable);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

    public List<String> getXList() {
        return xData;
    }

    public List<Float> getTodayList() {
        return dataList;
    }
}
