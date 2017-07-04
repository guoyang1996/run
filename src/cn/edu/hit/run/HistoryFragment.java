package cn.edu.hit.run;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.hit.run.dao.PedometerDB;
import cn.edu.hit.run.domain.Step;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryFragment extends Fragment implements OnClickListener{
	private LinearLayout tLayout;
	private View view;
	private PedometerDB pedometerDB;
	private SimpleDateFormat sdf;
	private String date1;
	private Step step;
	private TextView weightView;
	private TextView fweightView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.history, container, false);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		insert();
	}
	private void insert() {
		// TODO Auto-generated method stub
		
	}
	private void init() {
		tLayout = (LinearLayout) view.findViewById(R.id.date_image);
		tLayout.setOnClickListener(this);
		pedometerDB = PedometerDB.getInstance(getActivity());

		sdf = new SimpleDateFormat("yyyyMMdd");
		date1 = sdf.format(new Date());
		
		weightView=(TextView)view.findViewById(R.id.weight);
		fweightView=(TextView)view.findViewById(R.id.firstweight);

		step = pedometerDB.loadSteps(MainActivity.myObjectId, date1);
		if (step == null) {
			step = new Step();
		}
		weightView.setText(""+step.getWeight());
		step = pedometerDB.loadFirstStep();
		if (step == null) {
			step = new Step();
		}
		fweightView.setText(""+step.getWeight());
	}

	@Override
	public void onClick(View arg0) {
//		Intent intent = new Intent(this.getActivity(),HistoryList.class);
//		startActivity(intent);
		

	}
}