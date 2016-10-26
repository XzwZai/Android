package com.example.woi.edittool1.popmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.woi.edittool1.EditTextActivity;
import com.example.woi.edittool1.R;

import java.util.HashMap;
import java.util.Map;


public class PopMenu_WordSize extends PopupWindow {
    private Button btn_add,btn_less,btn_bgcolor,btn_wordcolor;
    private View mMenuView;
    private SeekBar seekbar_size;
    private TextView word_size;
    EditTextActivity editTextActivity;
    Activity context;
    Map mapcolor = new HashMap();
    public PopMenu_WordSize(final Activity context, final EditText editText, final int size, final EditTextActivity editTextActivity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.editTextActivity = editTextActivity;
        this.context = context;
        mapcolor.put(R.id.black, Color.rgb(0,0,0));
        mapcolor.put(R.id.blue,Color.rgb(0,0,0xff));
        mapcolor.put(R.id.red,Color.rgb(0xff,0,0));
        mapcolor.put(R.id.yellow,Color.rgb(0xff,0xff,0));
        mapcolor.put(R.id.green,Color.rgb(0,0xff,0));
        mapcolor.put(R.id.white,Color.rgb(0xff,0xff,0xff));
        mapcolor.put(R.id.gray,Color.rgb(0x6,0x6,0x6));
        mapcolor.put(R.id.purple,Color.rgb(0xff,0x34,0xb3));
        mapcolor.put(R.id.brown,Color.rgb(0xcd,0x66,0x1d));
        mapcolor.put(R.id.lightblue,Color.rgb(0x63,0xb8,0xff));
        mMenuView = inflater.inflate(R.layout.word_type, null);
        btn_add = (Button) mMenuView.findViewById(R.id.add_size);
        btn_less = (Button) mMenuView.findViewById(R.id.less_size);
        seekbar_size = (SeekBar) mMenuView.findViewById(R.id.seekbar_size);
        word_size = (TextView) mMenuView.findViewById(R.id.word_size);

        seekbar_size.setProgress((size-15)*2);
        word_size.setText(size+"");
        //设置按钮监听
        mMenuView.findViewById(R.id.black).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.blue).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.red).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.yellow).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.green).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.white).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.gray).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.purple).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.brown).setOnClickListener(choosecolor);
        mMenuView.findViewById(R.id.lightblue).setOnClickListener(choosecolor);
        seekbar_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int num = seekBar.getProgress();
                word_size.setText((num/2+15) + "");
//                editText.setTextSize(num);
                editTextActivity.setSize((num/2+15));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(word_size.getText().toString());
                if(num <= 64) {
                    num++;
                    seekbar_size.setProgress((num-15)*2);
                }
                word_size.setText(num + "");
                editTextActivity.setSize(num);
            }
        });
        btn_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(word_size.getText().toString());
                if(num >= 2) {
                    num--;
                    seekbar_size.setProgress((num-15)*2);
                }

                word_size.setText(num + "");
//                editText.setTextSize(num);
//                seekbar_size.setProgress(num);
                editTextActivity.setSize(num);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.PopSeekBar).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
    View.OnClickListener choosecolor = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            int color = (int)mapcolor.get(id);
            editTextActivity.setWordColor(color);
        }
    } ;
}
