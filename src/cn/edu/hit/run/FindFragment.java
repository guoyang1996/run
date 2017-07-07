package cn.edu.hit.run;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.TransportMode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.trace.model.PushMessage;

public class FindFragment extends Fragment {

	private View view;
	private MapView mapView;
	private BaiduMap baiduMap;
	// 定位相关声明  
    public LocationClient locationClient = null;  
    //自定义图标  
    BitmapDescriptor mCurrentMarker = null;  
	protected double locLatitude;
	protected double locLongititude;
  
    public BDLocationListener myListener = new BDLocationListener() {  
        @Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override  
        public void onReceiveLocation(BDLocation location) {  
            // map view 销毁后不在处理新接收的位置  
            if (location == null || mapView == null)  
                return;  
              
            MyLocationData locData = new MyLocationData.Builder()  
                    .accuracy(location.getRadius())  
                    // 此处设置开发者获取到的方向信息，顺时针0-360  
                    .direction(100).latitude(location.getLatitude())  
                    .longitude(location.getLongitude()).build();  
             //baiduMap.setMyLocationData(locData);    //设置定位数据  
              
              locLatitude = location.getLatitude();
              locLongititude= location.getLongitude();
            
                LatLng ll = new LatLng(location.getLatitude(),  
                        location.getLongitude());  
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 17);   //设置地图中心点以及缩放级别  
                baiduMap.animateMapStatus(u);  
         
        }  
    };
	private LBSTraceClient mTraceClient;
	private Trace mTrace;
	private OnTraceListener mTraceListener;  
	
	  
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		startTrace();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.view = inflater.inflate(R.layout.find, container, false);
		mapView = (MapView) view.findViewById(R.id.bmapView);
		baiduMap = mapView.getMap();
		 //开启定位图层  
        baiduMap.setMyLocationEnabled(true);  
       
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 设置为一般地图 
        locationClient = new LocationClient(this.getActivity().getApplicationContext()); // 实例化LocationClient类  
        locationClient.registerLocationListener(myListener); // 注册监听函数  
        this.setLocationOption();   //设置定位参数  
        locationClient.start(); // 开始定位  
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); // 设置为一般地图  
  
        // baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //设置为卫星地图  
        // baiduMap.setTrafficEnabled(true); //开启交通图  
		
		return view;
	}

	// 三个状态实现地图生命周期管理  
    @Override
	public void onDestroy() {  
        //退出时销毁定位  
        locationClient.stop();  
        baiduMap.setMyLocationEnabled(false);  
        // TODO Auto-generated method stub  
        super.onDestroy();  
        mapView.onDestroy();  
        mapView = null;  
     // 停止服务
        mTraceClient.stopTrace(mTrace, mTraceListener);
        // 停止采集
        mTraceClient.stopGather(mTraceListener);
    }
	
	  @Override
	public void onPause() {  
        // TODO Auto-generated method stub  
        super.onPause();  
        mapView.onPause();  
    }  
    
 @Override
	public void onResume() {  
        // TODO Auto-generated method stub  
        super.onResume();  
        mapView.onResume();  
    }  
  
    /** 
     * 设置定位参数 
     */  
    private void setLocationOption() {  
        LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true); // 打开GPS  
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式  
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02  
        option.setScanSpan(20); // 设置发起定位请求的间隔时间为5000ms  
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息  
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向  
          
        locationClient.setLocOption(option);  
    }  
  
    // 开启轨迹服务
	private void startTrace() {
		// 轨迹服务ID
		long serviceId = 145000;
		// 设备标识
		String entityName = "guoyang";
		// 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK
		// v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为
		// true，且需导入bos-android-sdk-1.0.2.jar。
		boolean isNeedObjectStorage = false;
		// 初始化轨迹服务
		mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
		// 初始化轨迹服务客户端
		 mTraceClient = new LBSTraceClient(this.getActivity()
				.getApplicationContext());
		// 定位周期(单位:秒)
		int gatherInterval = 2;
		// 打包回传周期(单位:秒)
		int packInterval = 60;
		// 设置定位和打包周期
		mTraceClient.setInterval(gatherInterval, packInterval);
		// 初始化轨迹服务监听器
		mTraceListener = new OnTraceListener() {
			@Override
			public void onBindServiceCallback(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			// 推送回调
			@Override
			public void onPushCallback(byte messageNo, PushMessage message) {
			}

			// 开启采集回调
			@Override
			public void onStartGatherCallback(int status, String message) {
			}

			// 开启服务回调
			@Override
			public void onStartTraceCallback(int status, String message) {
			}

			// 停止采集回调
			@Override
			public void onStopGatherCallback(int status, String message) {
			}

			// 停止服务回调
			@Override
			public void onStopTraceCallback(int status, String message) {
			}
		};
		// 开启服务
		mTraceClient.startTrace(mTrace, mTraceListener);
		// 开启采集
		mTraceClient.startGather(mTraceListener);
		
		
		
		// 绘制轨迹
		// 请求标识
		int tag = 1;

		// 创建历史轨迹请求实例
		HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag,
				serviceId, entityName);

		// 设置轨迹查询起止时间
		// 开始时间(单位：秒)
		long startTime = System.currentTimeMillis() / 1000 - 12 * 60 * 60;
		// 结束时间(单位：秒)
		long endTime = System.currentTimeMillis() / 1000;
		// 设置开始时间
		historyTrackRequest.setStartTime(startTime);
		// 设置结束时间
		historyTrackRequest.setEndTime(endTime);
		historyTrackRequest.setProcessed(true);
		historyTrackRequest.setProcessOption(new ProcessOption().setNeedDenoise(true)
				.setTransportMode(TransportMode.walking));
		// 初始化轨迹监听器
		OnTrackListener mTrackListener = new OnTrackListener() {
			// 历史轨迹回调
			@Override
			public void onHistoryTrackCallback(HistoryTrackResponse response) {
				Log.d("responsesize", response.getSize()+" ");
				List<TrackPoint> tps = response.getTrackPoints();
				
				List<LatLng> lls = new ArrayList<LatLng>();
				Log.d("response", response.message+" "+response.getEntityName());
				if(tps!=null&&tps.size()>0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					//标记起点和终点
					LatLng startNode = new LatLng(response.getStartPoint().getLocation().getLatitude(),
		        			response.getStartPoint().getLocation().getLongitude());
					Log.d("startTime", sdf.format(new Date(response.getStartPoint().getLocTime()*1000)));
					Log.d("endTime", sdf.format(new Date(response.getEndPoint().getLocTime()*1000)));
					
					//构建Marker图标  
			        BitmapDescriptor bitmap = BitmapDescriptorFactory  
			            .fromResource(R.drawable.start);  
			        //构建MarkerOption，用于在地图上添加Marker  
			        OverlayOptions option = new MarkerOptions()  
			            .position(startNode)  
			            .icon(bitmap);  
			        //在地图上添加Marker，并显示  
			        baiduMap.addOverlay(option);
		        	
		        	LatLng endNode = new LatLng(response.getEndPoint().getLocation().getLatitude(),
		        			response.getEndPoint().getLocation().getLongitude());
		        	 //构建Marker图标  
			        BitmapDescriptor bitmap1 = BitmapDescriptorFactory  
			            .fromResource(R.drawable.end);  
			        //构建MarkerOption，用于在地图上添加Marker  
			        OverlayOptions option1 = new MarkerOptions()  
			            .position(endNode)  
			            .icon(bitmap1);  
			        //在地图上添加Marker，并显示  
			        baiduMap.addOverlay(option1);
			        
//			        BitmapDescriptor bitm;
//			        OverlayOptions op;
					//绘制轨迹
					for(TrackPoint tp:tps){
						
						LatLng ll =new LatLng(tp.getLocation().getLatitude(),tp.getLocation().getLongitude());
						lls.add(ll);
						Log.d("tp", ll.latitude+"　"+ll.longitude);
						
//						bitm = BitmapDescriptorFactory  
//							    .fromResource(R.drawable.dot);  
//							//构建MarkerOption，用于在地图上添加Marker  
//						 op= new MarkerOptions()  
//							    .position(new LatLng(tp.getLocation().getLatitude(),tp.getLocation().getLongitude()))  
//							    .icon(bitm);  
//							//在地图上添加Marker，并显示  
//							baiduMap.addOverlay(op);
					}
					 if(lls.size()<2){
							Log.d("size2","=="+tps.size());
				        	//Toast.makeText(MainActivity.this, "您已经到达目的地！", Toast.LENGTH_LONG).show();
				        }else{
				        	OverlayOptions ooPolyline = new PolylineOptions().width(10)
				        	        .points(lls).color(Color.rgb(18, 150, 219));
				        	//Log.d("color", Color.RED+" ");
				        	Log.d("size","=="+lls.size());
				        	Polyline  mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
//				        	mPolyline.setColor();
			        	}
					
				}
				
			}
		};

		// 查询历史轨迹
		mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
	}  
	
}
