package com.jacklee.clatclatter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义横线的switch类
 * Created by liming on 18-4-3.
 */

public class RowSwitchView extends LinearLayout {
    public static final String NAMESPACE ="http://schemas.android.com/apk/res/com.jacklee.clatclatter";
    private TextView rowName;
    private View view;

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

        String title = attrs.getAttributeValue(NAMESPACE, "title");
        initView();
        rowName.setText(title);
    }

    public RowSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public RowSwitchView(Context context) {
        this(context,null);

    }


    /**
     * 初始化文件布局
     */
    private void  initView() {
        view = View.inflate(getContext(), R.layout.row_switch, null);
        rowName = view.findViewById(R.id.row_name_text);          //获取自动更新的id
        addView(view);
    }
}
