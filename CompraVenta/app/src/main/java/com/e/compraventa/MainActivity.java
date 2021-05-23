package com.e.compraventa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        CandleStickChart candleStickChart = (CandleStickChart)findViewById(R.id.datachart);
        candleStickChart.setHighlightPerDragEnabled(true);
        candleStickChart.setDrawBorders(true);
        candleStickChart.setBorderColor(1);

        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(true);
        candleStickChart.requestDisallowInterceptTouchEvent(true);

        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setDrawGridLines(false);// disable x axis grid lines
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        Legend l = candleStickChart.getLegend();
        l.setEnabled(false);

        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<>();
        for(int i=100;i<2001;i=i+4){
            Random rd = new Random();
            float a = abs(rd.nextFloat());
            float c = abs(rd.nextFloat());
            float b = abs(rd.nextFloat());
            float d = abs(rd.nextFloat());
            yValsCandleStick.add(new CandleEntry(i+0, a*225.0f, b*219.84f, c*224.94f, d*221.07f));
            yValsCandleStick.add(new CandleEntry(i+1, b*228.35f, c*222.57f, d*223.52f, c*226.41f));
            yValsCandleStick.add(new CandleEntry(i+2, d*226.84f,  a*222.52f, b*225.75f, a*223.84f));
            yValsCandleStick.add(new CandleEntry(i+3, c*222.95f, a*217.27f, d*222.15f, b*217.88f));
        }


        CandleDataSet set1 = new CandleDataSet(yValsCandleStick,"Data set");

        set1.setColor(Color.rgb(80, 80, 80));
//        set1.setShadowColor(getResources().getColor(R.color.colorLightGrayMore));
        set1.setDecreasingColor(getResources().getColor(R.color.colorRed));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(getResources().getColor(R.color.colorAccent));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.LTGRAY);
        set1.setDrawValues(false);
        CandleData candleData = new CandleData(set1);
        candleStickChart.setData(candleData);
        candleStickChart.setVisibleXRangeMaximum(30);
        candleStickChart.invalidate();
    }

    public static class MyService extends IntentService {

        /**
         * @param name
         * @deprecated
         */
        public MyService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.d("Main.TAG", "onHandleIntent");
            final int port = 12345;
            ServerSocket listener = null;
            try {
                listener = new ServerSocket(port);
                Log.d("Main.TAG", String.format("listening on port = %d", port));
                while (true) {
                    Log.d("Main.TAG", "waiting for client");
                    Socket socket = listener.accept();
                    Log.d("Main.TAG", String.format("client connected from: %s", socket.getRemoteSocketAddress().toString()));
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintStream out = new PrintStream(socket.getOutputStream());
                    for (String inputLine; (inputLine = in.readLine()) != null;) {
                        Log.d("Main.TAG", "received");
                        Log.d("Main.TAG", inputLine);
                        StringBuilder outputStringBuilder = new StringBuilder("");
                        char inputLineChars[] = inputLine.toCharArray();
                        for (char c : inputLineChars)
                            outputStringBuilder.append(Character.toChars(c + 1));
//                        out.println(outputStringBuilder);
                    }
                }
            } catch(Exception e) {
                Log.d("Main.TAG", e.toString());
            }
        }
    }

}