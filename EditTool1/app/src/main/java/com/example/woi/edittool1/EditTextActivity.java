package com.example.woi.edittool1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.woi.edittool1.popmenu.PopMenu_AddTag;
import com.example.woi.edittool1.popmenu.PopMenu_WordSize;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;


public class EditTextActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_PIC = 2;
    static final int REQUEST_EXTERNAL_STORAGE = 3;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    static float mInsertedImgWidth;
    float x = 0, y = 0, x1, y1;
    String mCurrentPhotoPath;
    RecordFile recordFile = new RecordFile();
    PopMenu_WordSize popMenu_wordSize;
    PopMenu_AddTag popMenu_AddTag;
    TextWatcher textWatcher;
    EditText edit;
    Button[] btns;
    LinearLayout layout;
    Uri imageUri;
    Bitmap bitmap,bitmaphalf = null;
    int wordsize = 20;
    int wordcolor = Color.BLACK, bgcolor;
    String local_file;
    int softinputheight = 0;
    int screenwidth = 0;
    int screenheight = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    public EditTextActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        Bundle bundle = this.getIntent().getExtras();
        local_file = bundle.getString(StartEditActivity.SAVE_PATH);
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }



    private Bitmap drawableToBitamp(Drawable drawable)
    {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 初始化edit,textwathcer,为edit设置setOnTouchListener
     */
    private void init() {
        int permission_storage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission_storage != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                    REQUEST_EXTERNAL_STORAGE//请求码
            );
        }
        layout = (LinearLayout) findViewById(R.id.activity_edit);
        layout.getBackground().setAlpha(77);
        bitmap = drawableToBitamp(layout.getBackground());
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();

                layout.getWindowVisibleDisplayFrame(r);

                //获取当前界面可视部分
                if (screenwidth == 0 || screenheight == 0) {
                    screenwidth = r.right;
                    screenheight = r.bottom;
                }
                softinputheight = screenheight - r.bottom;
                if(softinputheight >= screenheight/3) {
                    if(bitmaphalf == null) {
                        Matrix mx = new Matrix();
                        mx.postScale(1,1);
                        bitmaphalf = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight()*r.bottom/screenheight,mx,true);
                    }
                    Drawable d = new BitmapDrawable(bitmaphalf);
                    layout.setBackground(d);
                }
                else {
                    Drawable d = new BitmapDrawable(bitmap);
                    layout.setBackground(d);
                }
            }
        });
        edit = (EditText) findViewById(R.id.edit_all);
        btns = new Button[4];
        btns[0] = (Button) findViewById(R.id.Btn_Picture);
        btns[1] = (Button) findViewById(R.id.Btn_Camera);
        btns[2] = (Button) findViewById(R.id.Btn_Word);
        btns[3] = (Button) findViewById(R.id.Btn_Save);
        ViewTreeObserver vto = edit.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int mImgViewWidth;
                edit.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mImgViewWidth = edit.getWidth();
                mInsertedImgWidth = mImgViewWidth * 0.8f;
            }
        });
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                int type = 1;
                int time = 0;
                for (int i = 0; i < before; i++) {
                    recordFile.deleteContent(start + i);
                }
                if (count > 0) {
                    SpannableString ss = new SpannableString(s.toString().substring(start, start + count));
                    edit.removeTextChangedListener(this);
                    while (s.toString().length() > length - count) {
                        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
                        edit.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        time++;
                    }
                    if (time < count) {
                        type = 2;
                    }

                    //ss.setSpan(new MaskFilterSpan(new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER)), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //ss.setSpan(new MaskFilterSpan(new EmbossMaskFilter(new float[]{1,1,3}, 1.5f, 8, 3)), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(wordcolor), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan(wordsize, true), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //edit.append(ss);
                    edit.getText().insert(edit.getSelectionStart(), ss);
                    edit.addTextChangedListener(this);

                    for (int i = 0; i < count; i++) {
                        recordFile.addContent(start + i, type, s.toString().charAt(start + i), wordsize, wordcolor);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edit.addTextChangedListener(textWatcher);
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float error = 5;
                LinearLayout layout = (LinearLayout) findViewById(R.id.activity_edit);
                layout.setFocusable(true);
                layout.setFocusableInTouchMode(true);
                InputMethodManager im = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x1 = event.getX();
                        y1 = event.getY();
                        if (x - x1 > error || x - x1 < -error || y - y1 > error || y - y1 < -error) {
                            edit.setFocusable(false);
                            layout.requestFocus();
                            im.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } else {
                            edit.setFocusable(true);
                            edit.setFocusableInTouchMode(true);
                            edit.requestFocus();
                            im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 拍照按钮的触发事件，调用系统的拍照activity，将拍的照片存起来，之后进入onActivityResult
     *
     * @param view
     */
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * 选择图片的书法事件
     *
     * @param view
     */
    public void choosepicture(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PIC);
    }

    /**
     * 选择字体按钮的触发事件，弹出选择菜单
     *
     * @param view
     */
    public void choosewordtype(View view) {
        int loc;
        loc = edit.getSelectionStart();
        if (loc >= 1) {
            wordsize = recordFile.getstyle(loc - 1)[2];
            wordcolor = recordFile.getstyle(loc - 1)[1];
        }

        popMenu_wordSize = new PopMenu_WordSize(EditTextActivity.this, edit, wordsize, this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_edit);
        edit.setFocusable(false);
        layout.requestFocus();
        InputMethodManager im = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        popMenu_wordSize.showAtLocation(EditTextActivity.this.findViewById(R.id.activity_edit), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }


    /**
     *
     * @param view
     */
    public void addTag(View view) {

        popMenu_AddTag = new PopMenu_AddTag(EditTextActivity.this, recordFile);
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_edit);
        edit.setFocusable(false);
        layout.requestFocus();
        InputMethodManager im = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        popMenu_AddTag.showAtLocation(EditTextActivity.this.findViewById(R.id.activity_edit), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }





    /**
     * Save按钮的触发事件
     *
     * @param view
     */
    public void saveedit(View view) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String f;
        f = local_file + "/" + timeStamp + ".txt";
        File file = new File(f);
        recordFile.savetofile(file);

        loadRecordFile(file);

    }


    /**
     * 将图片添加到edittext
     * 若为选择图片事件的返回，还将选择的图片存起来
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PIC) if (data == null) {
                Toast.makeText(this, "failedddddddddddd", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = geturi(data);
                //Uri uri = data.getData();
                //get uri absolutepath
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                //copy to app area
                copyfile(new File(path));
                imageUri = Uri.fromFile(new File(path));
                Bitmap bitmap = getOriginalBitmap(imageUri);
                SpannableString ss = getBitmapMime(bitmap, imageUri);
                insertIntoEditText(ss);

            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File file = new File(mCurrentPhotoPath);
                if (!file.exists()) {
                    Toast.makeText(this, "failed to create photo", Toast.LENGTH_SHORT).show();
                }
                imageUri = Uri.fromFile(file);
                Bitmap bitmap = getOriginalBitmap(imageUri);
                SpannableString ss = getBitmapMime(bitmap, imageUri);
                insertIntoEditText(ss);
            }
        }
    }


    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }


    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();
        // 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
        if (imgWidth >= mInsertedImgWidth) {
            float scale = (float) mInsertedImgWidth / imgWidth;
            Matrix mx = new Matrix();
            mx.setScale(scale, scale);
            pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
        }
        String smile = uri.getPath();
        SpannableString ss = new SpannableString(smile);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void insertIntoEditText(SpannableString ss) {
        // 先获取Edittext中原有的内容
        String all;
        String enter = new String("\r\n");

        edit.removeTextChangedListener(textWatcher);
        Editable et = edit.getText();
        int start = edit.getSelectionStart();

        // 设置ss要添加的位置
        //et.insert(start, );
        et.insert(start, enter);
        et.insert(start + enter.length(), enter);
        et.insert(start + 2 * enter.length(), ss);
        et.insert(start + ss.length() + 2 * enter.length(), enter);
        et.insert(start + ss.length() + 3 * enter.length(), enter);
        // 把et添加到Edittext中
        edit.setText(et);
        // 设置Edittext光标在最后显示
        all = enter + enter + ss + enter + enter;
        edit.setSelection(start + all.length());
        for (int i = 0; i < all.length(); i++) {
            recordFile.addContent(start + i, 0, all.charAt(i), wordsize, wordcolor);
        }
        edit.addTextChangedListener(textWatcher);
    }

    private Bitmap getOriginalBitmap(Uri photoUri) {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            ContentResolver conReslv = getContentResolver();
            // 得到选择图片的Bitmap对象
            bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
        } catch (Exception e) {
            //Log.e( , "Media.getBitmap failed", e);
        }
        return bitmap;
    }


    /**
     * 根据时间创造一个file并返回
     *
     * @return file
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String filepath;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File f = new File(local_file, "/image/");
        if (!f.exists()) {
            f.mkdirs();
        }
        filepath = f.getAbsolutePath() + '/' + timeStamp + ".jpg";
        File image = new File(filepath);
        if (!image.exists()) {
            image.createNewFile();
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();
        }
        return image;
    }

    /**
     * 在选择完图片后使用
     * 将图片复制到指定目录下
     *
     * @param file
     */
    private void copyfile(File file) {
        File aimfile = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[4000];
        int count = 0;
        int onetime = 0;
        try {
            aimfile = createImageFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (aimfile != null) {
            try {
                fos = new FileOutputStream(aimfile);
                fis = new FileInputStream(file);
                while ((onetime = fis.read(buffer)) > 0) {
                    count += onetime;
                    fos.write(buffer, 0, onetime);
                }
                fis.close();
                fos.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "image file not find", Toast.LENGTH_SHORT);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据file将内容加载到edittext中
     *
     * @param file
     */

    public void loadRecordFile(File file) {
        SpannableString ss = null;
        try {
            RecordPart rp = null, rp2 = null;
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            LinkedList<RecordPart> content;
            content = (LinkedList<RecordPart>) ois.readObject();
            edit.removeTextChangedListener(textWatcher);
            edit.setText("");
            Iterator<RecordPart> it = content.iterator();
            while (it.hasNext()) {
                rp = it.next();
                if (rp.type == 2) {
                    rp2 = it.next();
                    ss = new SpannableString(rp.c + "" + rp2.c);
                    ss.setSpan(new AbsoluteSizeSpan(rp.size, true), 0, ss.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edit.append(ss);
                } else {
                    ss = new SpannableString(rp.c + "");
                    ss.setSpan(new ForegroundColorSpan(rp.color), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new AbsoluteSizeSpan(rp.size, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    edit.append(ss);
                }
            }
            edit.addTextChangedListener(textWatcher);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置当前输入size
     *
     * @param Size
     */
    public void setSize(int Size) {
        wordsize = Size;
    }

    /**
     * 设置当前输入颜色
     *
     * @param color
     */
    public void setWordColor(int color) {
        wordcolor = color;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EditText Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }
}
