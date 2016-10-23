package com.example.woi.edittool1.popmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.woi.edittool1.EditTextActivity;
import com.example.woi.edittool1.R;


public class PopMenu_Color extends PopupWindow {
    private View mMenuView;
    Button btn_red,btn_blue,btn_green,btn_white,btn_black;
    EditText edit;

    public PopMenu_Color(final Activity context, final EditText editText, int type, final EditTextActivity editTextActivity) {
        super(context);
        edit = editText;
        View.OnClickListener bgcolorchooser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.black:
                        edit.setBackgroundColor(Color.rgb(0,0,0));
                        break;
                    case R.id.white:
                        edit.setBackgroundColor(Color.rgb(0xff,0xff,0xff));
                        break;
                    case R.id.green:
                        edit.setBackgroundColor(Color.rgb(0,0xff,0));
                        break;
                    case R.id.red:
                        edit.setBackgroundColor(Color.rgb(0xff,0,0));
                        break;
                    case R.id.blue:
                        edit.setBackgroundColor(Color.rgb(0,0,0xff));
                        break;
                }
            }
        };
        View.OnClickListener wordcolorchooser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.black:

                        editTextActivity.setWordColor(Color.rgb(0,0,0));
                        //edit.setTextColor(Color.rgb(0,0,0));
                        break;
                    case R.id.white:
                        editTextActivity.setWordColor(Color.rgb(0xff,0xff,0xff));
                        //edit.setTextColor(Color.rgb(0xff,0xff,0xff));
                        break;
                    case R.id.green:
                        editTextActivity.setWordColor(Color.rgb(0,0xff,0));
                        //edit.setTextColor(Color.rgb(0,0xff,0));
                        break;
                    case R.id.red:
                        editTextActivity.setWordColor(Color.rgb(0xff,0,0));
                        //edit.setTextColor(Color.rgb(0xff,0,0));
                        break;
                    case R.id.blue:
                        editTextActivity.setWordColor(Color.rgb(0,0,0xff));
                        //edit.setTextColor(Color.rgb(0,0,0xff));
                        break;
                }
            }
        };
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.word_color, null);
        btn_black = (Button) mMenuView.findViewById(R.id.black);
        btn_blue = (Button) mMenuView.findViewById(R.id.blue);
        btn_green = (Button) mMenuView.findViewById(R.id.green);
        btn_white = (Button) mMenuView.findViewById(R.id.white);
        btn_red = (Button) mMenuView.findViewById(R.id.red);
        if(type == 1) {
            btn_black.setOnClickListener(bgcolorchooser);
            btn_blue.setOnClickListener(bgcolorchooser);
            btn_green.setOnClickListener(bgcolorchooser);
            btn_white.setOnClickListener(bgcolorchooser);
            btn_red.setOnClickListener(bgcolorchooser);
        }
        else{
            btn_black.setOnClickListener(wordcolorchooser);
            btn_blue.setOnClickListener(wordcolorchooser);
            btn_green.setOnClickListener(wordcolorchooser);
            btn_white.setOnClickListener(wordcolorchooser);
            btn_red.setOnClickListener(wordcolorchooser);
        }


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
