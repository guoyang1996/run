package cn.edu.hit.run;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import cn.edu.hit.run.dao.PedometerDB;
import cn.edu.hit.run.domain.Step;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryFragment extends Fragment {
	
	private View view;
	private PedometerDB pedometerDB;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.history, container, false);
		
		BarChart barChart = (BarChart) view.findViewById(R.id.chart);
	
		barChart.setPadding(5, 10,5, 10);
		
		//1、基本设置
        XAxis xAxis=barChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.getAxisLeft().setDrawAxisLine(true);
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        //2、y轴和比例尺

        //barChart.setDescription("四个季度");// 数据描述

        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);

        Legend legend = barChart.getLegend();//比例尺
        legend.setEnabled(true);

        //3、x轴数据,和显示位置
        ArrayList<String> xValues = new ArrayList<String>();
       
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部
        
        //4、y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        //new BarEntry(20, 0)前面代表数据，后面代码柱状图的位置；
        
      
        //从数据库获取数据
        pedometerDB=PedometerDB.getInstance(this.getActivity());
		List<Step> alist = pedometerDB.loadListSteps();
		for(int i=6;i>=0;i--)
		{
			if(alist.size()-i>0){
				Step step=alist.get(alist.size()-i-1);
				String date = step.getDate();
				int number = step.getNumber();
				 xValues.add(date);
				 yValues.add(new BarEntry(number,6-i));
			}
			else{
				xValues.add(beforeToday(i));
				 yValues.add(new BarEntry(0,6-i));
			}
		}
		
        //5、设置显示的数字为整形
        BarDataSet barDataSet=new BarDataSet(yValues,"步数");
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                int n = (int) v;
                return n + "";
            }
        });
        //6、设置柱状图的颜色
        barDataSet.setColors(new int[]{Color.rgb(128, 191, 204), Color.rgb(82, 140, 153),
                Color.rgb(185, 255, 229), Color.rgb(255, 167, 164),Color.rgb(204, 141, 166),
                Color.rgb(128, 191, 204),Color.rgb(82, 140, 153)});
        //7、显示，柱状图的宽度和动画效果
        BarData barData = new BarData(xValues, barDataSet);
        barDataSet.setBarSpacePercent(40f);//值越大，柱状图就越宽度越小；
        barChart.animateY(2000);
        barChart.setData(barData); //
		
		return view;
	}
	private String beforeToday(int i) {
		Date today = new Date();
		 Date theday = new Date(today.getTime()-i*1000*60*60*24);
		 SimpleDateFormat sdf = new SimpleDateFormat("MMdd");  
	     String dstr = sdf.format(theday);  
		return dstr;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}
	


}