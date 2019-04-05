package com.shark.apollo.deeplearning.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChartUtils {
    public static final int DAILY = 0;
    public static final int WEEKLY = 1;
    public static final int MONTHLY = 2;

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     */
    public static void initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("No Data");
        // 不显示网格
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);

        // 不显示y轴两边的值
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.setTouchEnabled(true);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(16);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(0);

        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
//        yAxis.setXOffset(-20);
//        yAxis.setYOffset(-20);
        yAxis.setAxisMinimum(0);

//        Matrix matrix = new Matrix();
//        // x轴缩放1.5倍
//        matrix.postScale(1.5f, 1f);
//        // 在图表动画显示之前进行缩放
//        chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        chart.animateX(2000);
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartDatas(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.animateX(1500);
        } else {
            lineDataSet = new LineDataSet(values, "");
            lineDataSet.setColor(Color.parseColor("#FFFFFF"));
            lineDataSet.setCircleColor(Color.parseColor("#FF4081"));
            lineDataSet.setCircleRadius(4);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            // 不显示坐标点的小圆点
//            lineDataSet.setDrawCircles(false);
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(false);
            // 高亮显示数据
            lineDataSet.setHighlightEnabled(true);
            lineDataSet.setDrawHighlightIndicators(false);
            LineData data = new LineData(lineDataSet);
            chart.setData(data);
//            chart.invalidate();
            chart.animateX(3000);
//            chart.animateY(3000s);
//            chart.animateX(3000, new EasingFunction() {
//                @Override
//                public float getInterpolation(float input) {
//                    return (float) (input * 0.6);
//                }
//            });
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
                                            final int valueType) {
        chart.getXAxis().setValueFormatter((value, axis) ->
                xValuesProcess(valueType)[(int) value]);
        chart.invalidate();
        setChartDatas(chart, values);
    }

    /**
     * x轴数据处理
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
    private static String[] xValuesProcess(int valueType) {
        long currentTime = System.currentTimeMillis();

        if (valueType == DAILY) {
            String[] dayValues = new String[7];
            for (int i = 6; i >= 0; i--) {
                dayValues[i] = TimeUtils.timeToDate(currentTime, TimeUtils.dateFormat_day);
                currentTime -= (24 * 60 * 60 * 1000);
            }
            return dayValues;

        } else if (valueType == WEEKLY) {
            String[] weekValues = new String[7];
            weekValues[6] = "Now";
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            for (int i = 5; i >= 0; i--) {
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
                Date date = calendar.getTime();
                weekValues[i] = TimeUtils.weekDateToString(date, TimeUtils.DATE_FORMAT_WEEK);
            }
            return weekValues;

        } else if (valueType == MONTHLY) {
            String[] monthValues = new String[7];
            Calendar calendar = Calendar.getInstance();
            monthValues[6] = TransformUtils.intToMonth(calendar.get(Calendar.MONTH));
            for (int i = 6; i >= 0; i--) {
                monthValues[i] = TransformUtils.intToMonth(calendar.get(Calendar.MONTH));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            }
            return monthValues;
        }
        return new String[]{};
    }
}
