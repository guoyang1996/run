package cn.edu.hit.run.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.math.BigDecimal;
/**
 * 这是继承view的一个重新绘图的圆圈的一个类
 */
public class RoundProgressBar extends View {

	private  int goal = 8000;
     private int process = 5050;
   
     public void setProgress(int total_step) {
 		this.process=total_step;
 	}
	public RoundProgressBar(Context context) {
        super(context);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //实现点击界面切换
    String [] styleValues = { "Num", "Cal" }; //选择两个界面 步数和卡路里
    int currentStyleIndex = 0; //当前界面为NUM

    //ontouchevent方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentStyleIndex++;
            if (currentStyleIndex > (styleValues.length - 1)) {
                currentStyleIndex = 0;
            }
            postInvalidate();
            return true;
        }
        return result;
    }
    
    public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
		this.postInvalidate();
	}

	 //绘图
    public void onDraw(Canvas canvas){
    	
        //基本信息和数据
        int centre_x = getWidth()/2; //获取圆心的x坐标
        int centre_y = getHeight()/2;
        int radius = centre_x < centre_y ? centre_x : centre_y;
        String String_Process = Integer.toString(process);

        double cal = process/20.0;  //消耗卡路里数 每20步1卡
        float f = (float) (cal/256);  //一碗米饭225卡
        BigDecimal b = new BigDecimal(f);
        float  food  = b.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue(); //保留两位小数
        String String_Cal = Double.toString(cal);

        //画底层灰色圆形
        Paint paint_gray;
        paint_gray = new Paint();
        paint_gray.setColor(Color.parseColor("#a1a3a6"));
        paint_gray.setStyle(Style.STROKE);//设置空心
        paint_gray.setStrokeWidth(30);
        canvas.drawCircle(centre_x,centre_y,radius-20f,paint_gray);

        //画上层蓝色扇形
        Paint paint_blue = new Paint();
        paint_blue.setColor(Color.parseColor("#6DCAEC"));
        paint_blue.setStyle(Style.STROKE);
        paint_blue.setStrokeWidth(30);
        RectF oval = new RectF(centre_x-radius+20f,centre_y-radius+20f,centre_x+radius-20f,centre_y+radius-20f);  //用于定义的圆弧的形状和大小的界限
        if(goal!=0) {
        	canvas.drawArc(oval,-90,360*process/goal,false,paint_blue);
        }else{
        	canvas.drawArc(oval,-90,360*process/1,false,paint_blue);
        }
        

        //写文字
        //中间文字画笔
        Rect bounds = new Rect();
        Paint paint_text_Process = new Paint();
        paint_text_Process.setColor(Color.parseColor("#6DCAEC"));
        paint_text_Process.setTextSize(100);
        paint_text_Process.getTextBounds(String_Process,0,String_Process.length(),bounds);

        //设置不同界面文字
        String upText = null;
        String downText = null;
        String styleSelected = styleValues[currentStyleIndex];
        if (styleSelected.equals("Num")) {
            upText = "步    数";
            downText = "目 标 : "+goal;
            float textWidth = paint_text_Process.measureText(String_Process);   //测量字体宽度，根据字体的宽度设置在圆环中间
            canvas.drawText(String_Process , oval.centerX()-textWidth/2, oval.centerY()+bounds.height()/2,paint_text_Process);

        }
        else if (styleSelected.equals("Cal")) {
            upText = "卡 路 里 （卡）";
            downText = "约为"+food+"碗米饭";
            float textWidth = paint_text_Process.measureText(String_Cal);   //测量字体宽度，根据字体的宽度设置在圆环中间
            canvas.drawText(String_Cal , oval.centerX()-textWidth/2, oval.centerY()+bounds.height()/2,paint_text_Process);
        }

        //上下文字画笔
        Paint paint_text_Context = new Paint();
        paint_text_Context.setColor(Color.parseColor("#a1a3a6"));
        paint_text_Context.setTextSize(36);


        int distance = (radius-20)/2;//文字间的距离
        paint_text_Context.getTextBounds(upText, 0, upText.length(), bounds);
        float textWidthU = paint_text_Context.measureText(upText);
        canvas.drawText(upText , oval.centerX() - textWidthU / 2, oval.centerY()+bounds.height()/2- distance,paint_text_Context);

        paint_text_Context.getTextBounds(downText, 0, downText.length(), bounds);
        float textWidthD = paint_text_Context.measureText(downText);
        canvas.drawText(downText , oval.centerX() - textWidthD / 2, oval.centerY()+bounds.height()/2+distance,paint_text_Context);
    }

}
