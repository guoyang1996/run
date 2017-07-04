package cn.edu.hit.run;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.hit.run.MainActivity;
import cn.edu.hit.run.R;
import cn.edu.hit.run.dao.PedometerDB;
import cn.edu.hit.run.domain.Step;
import cn.edu.hit.run.domain.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment extends Fragment implements OnClickListener {
    public static final int REQUEST_CODE_TO_TACK_PICTURE = 2201;
    public static final int REQUEST_CODE_TACK_PICTURE = 2202;
    public static final int REQUEST_CODE_TACK_PICTURE_ZOOM = 2204;
    public static final int REQUEST_CODE_FROM_ALBUM = 2203;
    public static final int REQUEST_CODE_FROM_ALBUM_ZOOM = 2205;
    private View view;
    // private LinearLayout sexLayout;


    private LinearLayout weightLayout;
    private LinearLayout sensitivyLayout;
    private LinearLayout lengthLayout;
    private LinearLayout nameLayout;
    private RadioButton rButton1;
    private RadioButton rButton2;

    private TextView weightText;
    private TextView sensitivyText;
    private TextView lengthText;
    private TextView nameText;
    private EditText editText;

    private AlertDialog.Builder dialog;
    private NumberPicker numberPicker;

    private PedometerDB pedometerDB;
    private User user = null;
   private Step step;
    //private ToRoundBitmap toRoundBitmap;
	private LinearLayout goalLayout;
	private TextView goalText;

    // private Intent pictureIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my, container, false);
        init();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pedometerDB.updateUser(user);
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        //toRoundBitmap = ToRoundBitmap.getInstance(getActivity());
        // sexLayout = (LinearLayout) view.findViewById(R.id.sex);
        weightLayout = (LinearLayout) view.findViewById(R.id.weight);
        sensitivyLayout = (LinearLayout) view.findViewById(R.id.sensitivy);
        lengthLayout = (LinearLayout) view.findViewById(R.id.lengh_step);
        nameLayout = (LinearLayout) view.findViewById(R.id.set_name);
        goalLayout=(LinearLayout) view.findViewById(R.id.goal);

        weightText = (TextView) view.findViewById(R.id.weight_);
        goalText = (TextView) view.findViewById(R.id.goalNumber);
        sensitivyText = (TextView) view.findViewById(R.id.sensitivy_);
        lengthText = (TextView) view.findViewById(R.id.lengh_step_);
        nameText = (TextView) view.findViewById(R.id.name_);
        rButton1 = (RadioButton) view.findViewById(R.id.male);
        rButton2 = (RadioButton) view.findViewById(R.id.female);

        pedometerDB = PedometerDB.getInstance(getActivity());

        goalLayout.setOnClickListener(this);
        weightLayout.setOnClickListener(this);
        sensitivyLayout.setOnClickListener(this);
        nameLayout.setOnClickListener(this);
        lengthLayout.setOnClickListener(this);
        rButton1.setOnClickListener(this);
        rButton2.setOnClickListener(this);

        if (MainActivity.myObjectId != null) {
            user = pedometerDB.loadUser(MainActivity.myObjectId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if(pedometerDB.loadSteps(MainActivity.myObjectId,sdf.format(new Date()))!=null)
            {
            	 step= pedometerDB.loadSteps(MainActivity.myObjectId,sdf.format(new Date()));
            }else{
            	step=new Step();
            	step.setDate(sdf.format(new Date()));
            	step.setUserId(user.getObjectId());
            	step.setNumber(user.getToday_step());
            	step.setId(1);
            	step.setWeight(Integer.valueOf((String) weightText.getText()));
            	 pedometerDB.saveStep(step);
            	}
            nameText.setText(user.getName());
            setSensitivity(user.getSensitivity());
            weightText.setText(String.valueOf(step.getWeight()));
            lengthText.setText(String.valueOf(user.getStep_length()));
            goalText.setText(String.valueOf(user.getGoal()));

            if (user.getGender().equals("男")) {
                rButton1.setChecked(true);
            } else {
                rButton2.setChecked(true);
            }
        } else {
            user = new User();
            user.setName(nameText.getText().toString());
            
            user.setSensitivity(10);
            user.setGender("男");
            user.setStep_length(Integer
                    .valueOf(lengthText.getText().toString()));
            user.setObjectId("1");
            MainActivity.myObjectId = user.getObjectId();
            pedometerDB.saveUser(user);
            // 将当前日期格式化为：yyyyMMdd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
           step = new Step();
            // 得到当天的日期
            step.setDate(sdf.format(new Date()));
            step.setUserId(MainActivity.myObjectId);
            step.setNumber(0);
            step.setWeight(Integer.valueOf(weightText.getText().toString()));
            pedometerDB.saveStep(step);
        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.goal:
            dialog = new AlertDialog.Builder(getActivity());
            editText = new EditText(getActivity());
            editText.setSingleLine(true);
            dialog.setView(editText);

            dialog.setTitle("修改今日目标");
            dialog.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            
                            if(!editText.getText().toString().equals(""))
                            {
                            	goalText.setText(editText.getText().toString());
                            	  user.setGoal(Integer.valueOf(editText.getText().toString()));
                            	  pedometerDB.updateUser(user);
                            }
                        }
                    });
            dialog.show();
            break;

            case R.id.set_name:
                dialog = new AlertDialog.Builder(getActivity());
                editText = new EditText(getActivity());
                editText.setSingleLine(true);
                dialog.setView(editText);

                dialog.setTitle("填写姓名");
                dialog.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                nameText.setText(editText.getText().toString());
                                user.setName(editText.getText().toString());
                                pedometerDB.updateUser(user);
                            }
                        });
                dialog.show();
                break;

            case R.id.weight:
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(200);
                numberPicker.setValue(Integer.parseInt(weightText.getText()
                        .toString()));
                numberPicker.setMinValue(30);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               weightText.setText(numberPicker.getValue() + "");
                               step.setWeight(numberPicker.getValue());
                               pedometerDB.updateStep(step);
                            }
                        });
                dialog.show();
                break;

            case R.id.sensitivy:
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(1);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                user.setSensitivity(numberPicker.getValue());
                                setSensitivity(numberPicker.getValue());
                                pedometerDB.updateUser(user);
                            }
                        });
                dialog.show();
                break;
            case R.id.lengh_step:
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(100);
                numberPicker.setValue(Integer.parseInt(lengthText.getText()
                        .toString()));
                numberPicker.setMinValue(15);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                lengthText.setText(numberPicker.getValue() + "");
                                user.setStep_length(numberPicker.getValue());
                                pedometerDB.updateUser(user);
                            }
                        });
                dialog.show();
                break;
            case R.id.male:

                user.setGender("男");
                pedometerDB.updateUser(user);
                break;
            case R.id.female:
                user.setGender("女");
                pedometerDB.updateUser(user);
                break;
        }
    }

    private void setSensitivity(int value) {
        switch (value) {
            case 1:
                sensitivyText.setText("一级");
                break;
            case 2:
                sensitivyText.setText("二级");
                break;
            case 3:
                sensitivyText.setText("三级");
                break;
            case 4:
                sensitivyText.setText("四级");
                break;
            case 5:
                sensitivyText.setText("五级");
                break;
            case 6:
                sensitivyText.setText("六级");
                break;
            case 7:
                sensitivyText.setText("七级");
                break;
            case 8:
                sensitivyText.setText("八级");
                break;
            case 9:
                sensitivyText.setText("九级");
                break;
            case 10:
                sensitivyText.setText("十级");
                break;
            default:
                break;
        }
    }
}