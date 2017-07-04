package cn.edu.hit.run;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hit.run.dao.PedometerDB;
import cn.edu.hit.run.domain.Step;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryList extends ListActivity {

	
	 private PedometerDB pedometerDB;
	private List<Map<String, Object>> mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		pedometerDB=PedometerDB.getInstance(this);
		mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
		
		}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Step> alist = pedometerDB.loadListSteps();
		for(int i=0;i<alist.size();i++)
		{
			Step step=alist.get(i);
			map = new HashMap<String, Object>();
			map.put("dateText", step.getDate());
			map.put("weightText", "当天体重为"+step.getWeight()+"kg");
			map.put("stepText", "您走了"+step.getNumber()+"步！");
			list.add(map);
		}
		
		return list;
	}
	
	// ListView 中某项被选中后的逻辑
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Log.v("MyListView4-click", (String)mData.get(position).get("dateText"));
	}
	
	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo(){
		new AlertDialog.Builder(this)
		.setTitle("我的listview")
		.setMessage("介绍...")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
	}
	
	
	
	public final class ViewHolder{
		public TextView dateText;
		public TextView weightText;
		public TextView stepText;
	}
	
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.historyitem, null);
				holder.dateText = (TextView)convertView.findViewById(R.id.dateText);
				holder.weightText = (TextView)convertView.findViewById(R.id.weightText);
				holder.stepText = (TextView)convertView.findViewById(R.id.stepText);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			holder.dateText.setText((String)mData.get(position).get("dateText"));
			holder.weightText.setText((String)mData.get(position).get("weightText"));
			holder.stepText.setText((String)mData.get(position).get("stepText"));
			return convertView;
		}
		
	}
	
}
