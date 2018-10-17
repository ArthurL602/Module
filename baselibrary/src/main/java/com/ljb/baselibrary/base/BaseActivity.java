package com.ljb.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ljb.baselibrary.ioc.ViewUtils;

/**
 * Author      :ljb
 * Date        :2018/4/6
 * Description : 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {
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
}
