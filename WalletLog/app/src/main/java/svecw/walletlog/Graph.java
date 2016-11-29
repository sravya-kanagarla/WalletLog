package svecw.walletlog;
/**
 * Created by ramyateja.karicharla on 03-02-2016.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class Graph extends AppCompatActivity {
    ArrayList<Integer> allMonthsTotal;
    String expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }
    private ArrayList<BarDataSet> getDataSet() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            allMonthsTotal = bundle.getIntegerArrayList("allMonthsTotals");
            expense = bundle.getString("expense");
        } else {
            allMonthsTotal = null;
            expense = null;
        }

        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(allMonthsTotal.get(0).floatValue(), 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(allMonthsTotal.get(1).floatValue(), 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(allMonthsTotal.get(2).floatValue(), 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(allMonthsTotal.get(3).floatValue(), 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(allMonthsTotal.get(4).floatValue(), 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(allMonthsTotal.get(5).floatValue(), 5); // Jun
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(allMonthsTotal.get(6).floatValue(), 6); // July
        valueSet1.add(v1e7);
        BarEntry v1e8 = new BarEntry(allMonthsTotal.get(7).floatValue(), 7); // Aug
        valueSet1.add(v1e8);
        BarEntry v1e9 = new BarEntry(allMonthsTotal.get(8).floatValue(), 8); // Sep
        valueSet1.add(v1e9);
        BarEntry v1e10 = new BarEntry(allMonthsTotal.get(9).floatValue(), 9); // Oct
        valueSet1.add(v1e10);
        BarEntry v1e11 = new BarEntry(allMonthsTotal.get(10).floatValue(), 10); // Nov
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(allMonthsTotal.get(11).floatValue(), 11); // Dec
        valueSet1.add(v1e12);

            BarDataSet barDataSet1 = new BarDataSet(valueSet1, expense.toUpperCase());
            barDataSet1.setColors(ColorTemplate.PASTEL_COLORS);
            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JULY");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }
}
