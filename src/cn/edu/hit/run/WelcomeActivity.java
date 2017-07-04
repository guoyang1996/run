package cn.edu.hit.run;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * 这是开启程序的第一个启动页面
 */
public class WelcomeActivity extends Activity {
	//private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//页面设置为无标题
		setContentView(R.layout.welcome);
			//设置一个计时器，在此页面上停留3秒然后跳转到主页面,
			 (new Timer()).schedule(new TimerTask() {
			 public void run() {
			 Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
			 startActivity(intent);
			 finish();
			 }
			 }, 3000);
		}
	}

