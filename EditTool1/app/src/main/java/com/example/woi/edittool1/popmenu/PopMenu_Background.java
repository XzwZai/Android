package com.example.woi.edittool1.popmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.woi.edittool1.EditTextActivity;
import com.example.woi.edittool1.MemoryManager;
import com.example.woi.edittool1.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by woi on 2016/10/25.
 */

public class PopMenu_Background extends PopupWindow {
    private View mMenuView;
    EditTextActivity editTextActivity;
    Map bg_map = new HashMap();
    int[] imageview_id;
    int[] drawable_id;
    public PopMenu_Background(Activity context,EditTextActivity editTextActivity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.editTextActivity = editTextActivity;
        imageview_id = new int[]{R.id.bg1,R.id.bg2,R.id.bg3};
        drawable_id = new int[]{R.drawable.bj1,R.drawable.bj2,R.drawable.bj3};
        mMenuView = inflater.inflate(R.layout.choosebackground, null);
        for(int i = 0;i < imageview_id.length;i++) {
            bg_map.put(imageview_id[i],drawable_id[i]);
            ImageView iv = (ImageView) mMenuView.findViewById(imageview_id[i]);
            Bitmap bitmap = MemoryManager.loadBitmap(drawable_id[i],200,300,1);
            iv.setImageBitmap(bitmap);
        }

        mMenuView.findViewById(R.id.bg1).setOnClickListener(choose_bg);
        mMenuView.findViewById(R.id.bg2).setOnClickListener(choose_bg);
        mMenuView.findViewById(R.id.bg3).setOnClickListener(choose_bg);

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

                int height = mMenuView.findViewById(R.id.choose_bg).getTop();
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

    View.OnClickListener choose_bg = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            int id = v.getId();
            int bg_id = (int) bg_map.get(id);
            editTextActivity.setBG(bg_id);
        }
    };
}
