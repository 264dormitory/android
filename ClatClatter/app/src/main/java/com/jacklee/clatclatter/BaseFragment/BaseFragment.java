package com.jacklee.clatclatter.BaseFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 李世杰创建
 * 为了解决使用fragment造成的页面重叠问题
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果activity页面被销毁，但是保存的销毁页面栈不为空，则进行fragment栈的判断
        //因为在activity被销毁的时候，系统不会自动保存fragment栈内的信息
        //所以进行手动判断保存
        Log.d(TAG, "onCreate: ");
        if(savedInstanceState != null)
        {
            Log.d(TAG, "onCreate: if");
            boolean flag = savedInstanceState.getBoolean("flag");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if(flag)
                transaction.show(this);  //如果存在，则显示已有的fragment
            else
                transaction.hide(this);  //否则对原有的fragment进行隐藏
            transaction.commit();
        }
    }

    //用于设置状态位，如果activity已经被销毁则保存当前的fragment栈的状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: " + isHidden());
        outState.putBoolean("flag", isHidden());
    }
}
