package com.jacklee.clatclatter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.SwitchCompat;

/**
 * 自定义横线的switch类
 * Created by liming on 18-4-3.
 */

public class RowSwitchView extends LinearLayout {
    public static final String NAMESPACE = "http://schemas.android.com/apk/res/com.jacklee.clatclatter";
    private static final String TAG      = "RowSwtichView";
    private TextView rowName;
    private TextView remineTextView;
    private View view;
    private SwitchCompat rowSwtich;
    private View delimiter;                     //分隔符　　

    public RowSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //读取布局文件中给自定义控件使用的属性
        //参数1：布局的命名空间
        //参数2：布局中控件的属性
        //参数3：默认值，如果在读取控件的属性没有读取到时，就返回默认的值
        /**
         * title为控件的自定义属性
         * 自定义属性步骤：
         * 1、在res/values下新建一个attrs.xml
         * 2、在自定义类中通过attrs去获取这些属性，如attrs.getAttributeIntValue(NAMESPACE, "backgroundres", 0);
         * 3、在布局文件中使用这些属性时，需要声明命名空间
         * 4、  xmlns:abc="http://schemas.android.com/apk/res/com.xxx.myphonesafe"
         *  注意以下三点：
         * abc：可以随便更换为其他字符，但是在下面使用属性时必须保证和你更换后的字符串相同
         * com.xxx.myphonesafe：对应的是你在清单文件中的包名，必须和清单文件一致，不能随意更改
         * NAMESPACE：命名空间必须和在布局中使用的命名空间一致
         */
        //获取布局中的属性值

        String title           = attrs.getAttributeValue(NAMESPACE, "title");
        String isShowDelimiter = attrs.getAttributeValue(NAMESPACE, "isShowDelimiter");
        Log.i(TAG, "初始化界面相关信息");
        initView();
        rowName.setText(title);
        if (Boolean.parseBoolean(isShowDelimiter))
            delimiter.setVisibility(View.VISIBLE);      //设置下划线是可见
        else
            delimiter.setVisibility(View.INVISIBLE);    //设置下滑性不可见
    }

    public RowSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public RowSwitchView(Context context) {
        this(context,null);

    }

    /**
     * switch开关事件的回调
     * @param listener
     */
    public void setOnClickListener(final switchClickListener listener) {
        rowSwtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchListener();
            }
        });
    }

    /**
     * 点击开关事件接口
     */
    public interface switchClickListener {
        void switchListener();
    }

    /**
     * 设置remind_text_view
     * @param text
     */
    public void setText(String text) {
        remineTextView.setText(text);
    }

    /**
     * 判断switch是否被点击
     * @return
     */
    public boolean isChecked() {
        if (rowSwtich.isChecked())
            return true;                //被点击

        return false;                   //没有被点击
    }

    /**
     * 初始化文件布局
     */
    private void  initView() {
        view           = View.inflate(getContext(), R.layout.row_switch, null);
        rowName        = view.findViewById(R.id.row_name_text);
        remineTextView = view.findViewById(R.id.remin_text_view);
        rowSwtich      = view.findViewById(R.id.switch_0);               //获取Switch控件
        delimiter      = view.findViewById(R.id.delimiter);              //获取下划线
        addView(view);
    }
}
