package com.example.woi.edittool1.popmenu;

import android.app.Activity;
import android.content.Context;
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


public class PopMenu_WordSize extends PopupWindow {
    private Button btn_add,btn_less,btn_bgcolor,btn_wordcolor;
    private View mMenuView;
    private SeekBar seekbar_size;
    private TextView word_size;
    private PopMenu_Color popMenu_color;
    public PopMenu_WordSize(final Activity context, final EditText editText, final int size, final EditTextActivity editTextActivity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.word_type, null);
        btn_add = (Button) mMenuView.findViewById(R.id.add_size);
        btn_less = (Button) mMenuView.findViewById(R.id.less_size);
        btn_bgcolor = (Button) mMenuView.findViewById(R.id.choose_bgcolor);
        btn_wordcolor = (Button) mMenuView.findViewById(R.id.choose_wordcolor);
        seekbar_size = (SeekBar) mMenuView.findViewById(R.id.seekbar_size);
        word_size = (TextView) mMenuView.findViewById(R.id.word_size);

        seekbar_size.setProgress((size-15)*2);
        word_size.setText(size+"");
        //设置按钮监听
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
//                editText.setTextSize(num);
//                seekbar_size.setProgress(num);
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
        btn_bgcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu_color = new PopMenu_Color(context,editText,1,editTextActivity);
                popMenu_color.showAtLocation(context.findViewById(R.id.activity_edit), Gravity.BOTTOM | Gravity.CENTER,0,0);
            }
        });
        btn_wordcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu_color = new PopMenu_Color(context,editText,0,editTextActivity);
                popMenu_color.showAtLocation(context.findViewById(R.id.activity_edit), Gravity.BOTTOM | Gravity.CENTER,0,0);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
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
}
