package cn.edu.hit.run.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 这是一个实现了信号监听的记步的类
 * 这是从谷歌找来的一个记步的算法，看不太懂
 *
 */
public class StepDetector implements SensorEventListener {

	public static int CURRENT_SETP = 0;//当前的步数
	public static float SENSITIVITY = 5; // SENSITIVITY灵敏度
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	//添加参数判断是否连续走动10步以上，创建一个一维数组存放走前十步时的时间
	//以及一个循环控制变量
	private long[] check=new long[10];
	/**
	 * 最后加速度方向
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	
	/**
	 * 传入上下文的构造函数
	 * 
	 * @param context
	 */
	public StepDetector(Context context) {
		super();
		int h = 480;
		mYOffset = h * 0.5f;//240
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));//这个数值计算出来大概是-6.12
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));//这个数值计算出来是-2
		for(int i=0;i<10;i++)
		{
			check[i]=0;
		}
	}

	//当传感器检测到的数值发生变化时就会调用这个方法
	//通过一定的逻辑来判断是否走了一步，如果走了，CURRENT_SETP就加1
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {//如果是加速度传感器
				
				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];//240加x（y、z）方向上的加速度乘以2
					vSum += v;//累和
				}
				int k = 0;
				float v = vSum / 3;
				//把v和 mLastValues[k]比较，如果v大，则directions得1，
				//否则（即v比 mLastValues[k]）小，则再把v和mLastValues[k]比较，如果v比较小，则directions得-1
				//否则（即v和mLastValues[k]相等），directions得0
				//即判断当期的速度波形的方向，是上升的还是下降的
				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {//如果方向发生了改变
					// Direction changed
					int extType = (direction > 0 ? 0 : 1); // minumum or
															// maximum?    
					//Direction 大于0时到达波谷，否则为波峰
					mLastExtremes[extType][k] = mLastValues[k];//把上次的v赋值为最近一次的极值，即波峰或波谷的值
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);//波峰波谷做差

					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);//是否和之前的一样大
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);//之前的差值是否一样大
						boolean isNotContra = (mLastMatch != 1 - extType);//不懂？

						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							end = System.currentTimeMillis();//时间判断，ps：end-start本来是500，被我修改成了1000，以便观察效果
							if (end - start > 500) {// 此时判断为走了一步
								for(int i=0;i<9;i++)
								{
									check[i]=check[i+1];
									//Log.i(i+"aa",check[i]+"");
								}
								check[9]=end;
								if(check[9]-check[0]<10000)
								{
									CURRENT_SETP++;
								}
								mLastMatch = extType;
								start = end;
							}
						} else {
							mLastMatch = -1;//由于时间因素，当前数值的变化并不承认，也就是没有匹配上
						}
					}
					mLastDiff[k] = diff;//把当前的波峰波谷间的差值存下来，便于下次继续判断
				}
				mLastDirections[k] = direction;//存下当前波形的方向
				mLastValues[k] = v;//存下当前的加速度值
			}
		}
	}
	//当传感器的精度发生变化时就会调用这个方法，在这里没有用
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

}
