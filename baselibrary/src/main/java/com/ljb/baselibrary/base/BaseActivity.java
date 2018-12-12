package com.ljb.baselibrary.base;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ljb.baselibrary.dialog.BaseDialog;
import com.ljb.baselibrary.ioc.ViewUtils;
import com.ljb.baselibrary.utils.InstallUitls;

/**
 * Author      :ljb
 * Date        :2018/4/6
 * Description : 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Fragment mCurrentFragment;
    private final int GET_UNKNOWN_APP_SOURCES = 1111;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 实例化布局
        setContentView(getLayoutId());
        ViewUtils.inject(this);
        //实例化头部
        initTitle();
        //实例化View
        initView();

        //实例化数据
        initData();

    }

    /**
     * 返回一个布局id
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();
    /**
     * 实例化头部
     */
    protected abstract void initTitle();
    /**
     * 实例化View
     */
    protected abstract void initView();
    /**
     * 实例化数据
     */
    protected abstract void initData();






    protected void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        startActivityForResult(clazz, requestCode, null);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(clazz, requestCode, bundle);
    }

    protected <T extends View> T viewById(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     * @param addToBackTask 是否添加到返回栈
     */
    protected void changeFragmengt(Fragment targetFragment,boolean addToBackTask){
        if(mCurrentFragment==null){
            mCurrentFragment=new Fragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(targetFragment.isAdded()){
            transaction.hide(targetFragment).show(targetFragment);
        }else{
            // 0 为 控件ID
            transaction.hide(targetFragment).add(0,targetFragment);
        }
        if(addToBackTask){
            transaction.addToBackStack(null);
        }
        transaction.commit();
        mCurrentFragment =  targetFragment;
    }


    protected void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 创建弹窗
     * @param layoutId
     * @param width
     * @return
     */
    protected BaseDialog createDialog(int layoutId, float width) {
        int w;
        if (width == 0) {
            w = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            w = (int) (getResources().getDisplayMetrics().widthPixels * width);
        }
        return new BaseDialog.Builder(this)//
                .setContentView(layoutId)//
                .setCancelable(false)//
                .setWidthAndHeight(w,//
                        WindowManager.LayoutParams.WRAP_CONTENT).setCancelable(false)//
                .create();
    }
    protected void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
