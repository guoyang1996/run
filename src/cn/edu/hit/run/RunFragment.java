package cn.edu.hit.run;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.hit.run.ui.RoundProgressBar;
import cn.edu.hit.run.dao.PedometerDB;
import cn.edu.hit.run.domain.Step;
import cn.edu.hit.run.service.StepDetector;
import cn.edu.hit.run.service.StepService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//跑步界面
public class RunFragment extends Fragment{

	private View view;
	//private CircleBar circleBar;
	private RoundProgressBar roundProgressBar;
	//private TextView runTextView;
	private int total_step = 0;
	private Thread thread;
	private int Type = 1;
	private int calories = 0;
	private ImageButton mapButton;
	private int step_length = 50;
	private int weight = 70;
	private Step step = null;
	private cn.edu.hit.run.domain.User user = null;
	private PedometerDB pedometerDB;
	private SimpleDateFormat sdf;
	private String today;
	private TextView goalText;
	//private boolean flag = true;// 来判断第三个页面是否开启动画
	
	@SuppressLint("HandlerLeak")
	//利用Handler来实现UI线程的更新
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			   total_step = StepDetector.CURRENT_SETP;
			   roundProgressBar.invalidate();
				roundProgressBar.setProgress(total_step);
			}
				
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.view=inflater.inflate(R.layout.run, container, false);
		init();
		mThread();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		saveDate();
	}
	private void saveDate() {
		user = pedometerDB.loadUser(MainActivity.myObjectId);
		Log.i("s",""+user.getGoal());
		step = pedometerDB.loadSteps(MainActivity.myObjectId, today);
		step.setNumber(StepDetector.CURRENT_SETP);
		user.setToday_step(StepDetector.CURRENT_SETP);
		pedometerDB.updateUser(user);
		pedometerDB.updateStep(step);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		saveDate();
	}
	
	private void mThread() {
		if (thread == null) {

			thread = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (StepService.flag) {
							Message msg = new Message();
							handler.sendMessage(msg);
						}
					}
				}
			});
			thread.start();
		}
	}

	private void init() {
		
		sdf = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
		today = sdf.format(new Date());
		goalText=(TextView)view.findViewById(R.id.achieveGoal);
		roundProgressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
		roundProgressBar.setGoal(1000);
		roundProgressBar.setProgress(StepDetector.CURRENT_SETP);
		
		pedometerDB = PedometerDB.getInstance(getActivity());
		user = pedometerDB.loadUser(MainActivity.myObjectId);
		//Log.i("runinit",""+user.getGoal());
//		Toast.makeText(getActivity(), MainActivity.myObjectId+"--" ,
//				Toast.LENGTH_LONG).show();
		if (MainActivity.myObjectId != null) {
			step_length = user.getStep_length();
			StepDetector.SENSITIVITY = user.getSensitivity();
			StepDetector.CURRENT_SETP = user.getToday_step();
			roundProgressBar.setGoal(user.getGoal());
		} else {
			Toast.makeText(getActivity(), "user is null", Toast.LENGTH_SHORT)
					.show();
		}
		Intent intent = new Intent(getActivity(), StepService.class);
		getActivity().startService(intent);

//		mapButton=(ImageButton) view.findViewById(R.id.mapButton);
//		mapButton.setOnClickListener(this);
		//sharekey = (ImageView) view.findViewById(R.id.title_pedometer);
		//ShareSDK.initSDK(getActivity());
		//sharekey.setOnClickListener(this);
		
	}

//	@Override
//	public void onClick(View arg0) {
//		Intent intent = new Intent(this.getActivity(),com.baidu.trackshow.MainActivity.class);
//		
//		startActivity(intent);
//		Message msg = new Message();
//		handler.sendMessage(msg);
//
//		
//	}

	
	/*public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.title_pedometer:
			OnekeyShare oks = new OnekeyShare();
			oks.setNotification(R.drawable.pedometer,
					"ShareSDK notification content");
			oks.setText("今天已经走了" + total_step + "步");
			oks.setSilent(false);
			oks.disableSSOWhenAuthorize();
			oks.setDialogMode();
			// 显示
			oks.show(getActivity());
			break;
		case R.id.progress_pedometer:
			if (Type == 1) {
				Type = 2;
			} else if (Type == 2) {
				String address = "http://www.weather.com.cn/data/cityinfo"
						+ "/101010100.html";
				queryFromServer(address);
				flag = true;
				Type = 3;
			} else if (Type == 3) {
				Type = 1;
			}
			Message msg = new Message();
			handler.sendMessage(msg);
			break;
		default:
			break;
		}
	}
	}*/
}
