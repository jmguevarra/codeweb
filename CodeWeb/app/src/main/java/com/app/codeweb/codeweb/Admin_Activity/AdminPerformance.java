package com.app.codeweb.codeweb.Admin_Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminPerformance extends Fragment {
    ArrayList<SrlStudents> load;
    BarChart barChart;
    ArrayList<String> studentID;
    ArrayList<String> position;
    ArrayList<BarEntry> barEntries;
    String[] legendString;
    public AdminPerformance() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);
        barChart = (BarChart) view.findViewById(R.id.graph);

        AsyncLoad();
        return view;
    }

    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Top", "Top");
        PostResponseAsyncTask task = new PostResponseAsyncTask(this.getActivity(), postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                    load = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                    float pos = 1;
                    barEntries = new ArrayList<>();
                    studentID = new ArrayList<>();
                    ArrayList<Integer>  points = new ArrayList<>();

                    for(SrlStudents d: load){
                        points.add(d.Points);
                        studentID.add(d.Student_ID);
                    }
                        float max = points.get(0);

                    for(Integer p: points){
                        pos = pos + 1;
                        barEntries.add(new BarEntry(pos, (p/max)*100));
                    }




                    legendString = studentID.toArray(new String[studentID.size()]);
                    BarDataSet dataset =  new BarDataSet(barEntries, "Total Score");
                    dataset.setColors(ColorTemplate.LIBERTY_COLORS);
                    BarData data = new BarData(dataset);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(R.color.colorPrimary);
                    barChart.getXAxis().setEnabled(false);
                    barChart.getAxisLeft().setAxisMaximum(100);
                    barChart.getAxisLeft().setAxisMinimum(0);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getLegend().setExtra(ColorTemplate.LIBERTY_COLORS, legendString);
                    barChart.getLegend().setTextSize(10f);
                    barChart.getLegend().setXEntrySpace(20f);
                    barChart.getLegend().setYEntrySpace(0.5f);
                    barChart.getLegend().setWordWrapEnabled(true);
                    Description d = new Description();
                    d.setEnabled(false);
                    barChart.setDescription(d);
                    barChart.setData(data);
                    barChart.setTouchEnabled(true);
                    barChart.setScaleEnabled(true);
                    barChart.setHighlightPerTapEnabled(true);
                    barChart.setDragEnabled(true);
                    barChart.setFitBars(true);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

}
