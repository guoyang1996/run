package cn.edu.hit.run;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;

import cn.edu.hit.run.dao.PedometerDB;
import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.os.Build;

public class MainActivity extends FragmentActivity implements OnClickListener{
	public static String myObjectId;
	private ViewPager mViewPager;//选择器
	private FragmentPagerAdapter mAdapter;//适配器
	private List<Fragment> mFragments;//碎片清单

	private LinearLayout mTabFind;//发现界面的线性布局
	private LinearLayout mTabRun;//跑步界面的线性布局
	private LinearLayout mTabMy;//我的界面的线性布局
	private LinearLayout mTabHistory;//历史界面的线性布局
	
	private ImageButton mFind;//发现的图标按钮
	private ImageButton mRun;//跑步的图标按钮
	private ImageButton mMy;//我的图标按钮
	private ImageButton mHistory;//历史图标按钮
	private PedometerDB pd;
	
	private int flag;//历史记录转换界面标志
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        pd = PedometerDB.getInstance(this);
        flag=0;
        //判断用户是否进行注册过
        if (pd.loadFirstUser() == null) {
        } else {
            myObjectId = pd.loadFirstUser().getObjectId();
        }
        
        initView();
		initEvent();
		
		setSelect(2);
    }

//初始化监听事件
    private void initEvent()
	{
		mTabRun.setOnClickListener(this);
		mTabFind.setOnClickListener(this);
		mTabMy.setOnClickListener(this);
		mTabHistory.setOnClickListener(this);
	}
//初始化各类视图控件
    private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

		mTabFind = (LinearLayout) findViewById(R.id.id_tab_find);
		mTabRun = (LinearLayout) findViewById(R.id.id_tab_run);
		mTabMy = (LinearLayout) findViewById(R.id.id_tab_my);
		mTabHistory = (LinearLayout) findViewById(R.id.id_tab_history);
	
		 mFind= (ImageButton) findViewById(R.id.id_tab_find_img);
		mRun = (ImageButton) findViewById(R.id.id_tab_run_img);
		mMy = (ImageButton) findViewById(R.id.id_tab_my_img);
		mHistory = (ImageButton) findViewById(R.id.id_tab_history_img);
		

		mFragments = new ArrayList<Fragment>();
		Fragment mFind = new FindFragment();
		Fragment mRun = new RunFragment();
		Fragment mMy = new MyFragment();
		Fragment mHistory= new HistoryFragment();
		//Fragment mHistoryList= new HistoryListView();
		
		//mFragments.add(mHistoryList);
		mFragments.add(mHistory);
		mFragments.add(mFind);
		mFragments.add(mRun);
		mFragments.add(mMy);
		
		
		//以匿名类的形式创建适配器
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			
			@Override
			public void onPageSelected(int arg0)
			{
				int currentItem = mViewPager.getCurrentItem();
				setTab(currentItem);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
    
    @Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.id_tab_history:
			setSelect(0);
			break;
		case R.id.id_tab_find:
			setSelect(1);
			break;
		case R.id.id_tab_run:
			setSelect(2);
			break;
		case R.id.id_tab_my:
			setSelect(3);
			break;
		
		default:
			break;
		}
	}
//选择显示的界面
	private void setSelect(int i)
	{
		setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void setTab(int i)
	{
		resetImgs();
		//根据索引显示
		switch (i)
		{
		case 0:
			mHistory.setImageResource(R.drawable.tab_history_pressed);
			break;
		case 1:
			mFind.setImageResource(R.drawable.tab_find_pressed);
			break;
		case 2:
			mRun.setImageResource(R.drawable.tab_run_pressed);
			break;
		case 3:
			mMy.setImageResource(R.drawable.tab_my_pressed);
			break;
		}
	}

	/**
	 * 重置底色
	 */
	private void resetImgs()
	{
		mFind.setImageResource(R.drawable.tab_find_normal);
		mRun.setImageResource(R.drawable.tab_run_normal);
		mMy.setImageResource(R.drawable.tab_my_normal);
		mHistory.setImageResource(R.drawable.tab_history_normal);
	}

}
