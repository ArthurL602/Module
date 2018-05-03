package com.ljb.baselibrary.ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author      :ljb
 * Date        :2018/4/5
 * Description :
 */
public class ViewUtils {

    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    /**
     * 兼容上面三个方法，object --> 反射需要执行的类
     *
     * @param finder
     * @param object
     */
    private static void inject(ViewFinder finder, Object object) {
        injectField(finder, object);
        injectEvent(finder, object);
    }

    /**
     * 注入事件
     *
     * @param finder
     * @param object
     */
    private static void injectEvent(ViewFinder finder, Object object) {


        // 1. 获取类里面的所有方法
        Class<?> clazz = object.getClass();
        //获取类中的所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 2. 获取onClick里面的value值
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                CheckNet checkNet = method.getAnnotation(CheckNet.class);
                boolean isNetWork = (checkNet != null);
                // 3. findViewById找到View
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);
                    if (view != null) {
                        // 4. View.setOnclickListener
                        view.setOnClickListener(new DeclareOnclickListener(method, object, isNetWork));
                    }
                }
            }
        }


    }

    /**
     * 注入属性
     *
     * @param finder
     * @param object
     */
    private static void injectField(ViewFinder finder, Object object) {
        //1. 获取类里面的所有属性
        Class<?> clazz = object.getClass();
        //找到所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //2. 获取ViewById的里面的value值
                int viewId = viewById.value();
                //3. findViewById找到View
                View view = finder.findViewById(viewId);
                field.setAccessible(true);
                if (view != null) {
                    try {
                        //4. 动态注入找到的Vie
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class DeclareOnclickListener implements View.OnClickListener {
        private Method mMethod;
        private Object mObject;
        private boolean mIsNetWork;

        public DeclareOnclickListener(Method method, Object object, boolean isNetWork) {
            mMethod = method;
            mObject = object;
            mIsNetWork = isNetWork;
        }

        @Override
        public void onClick(View v) {
            if (mIsNetWork && !isNetWork(v.getContext())) {
                Toast.makeText(v.getContext(), "没有网络", Toast.LENGTH_SHORT).show();
                return;
            }
            mMethod.setAccessible(true);
            try {
                // 5. 反射执行方法
                mMethod.invoke(mObject, v);
            } catch (Exception e) {
                try {
                    //处理无参函数报错的问题
                    mMethod.invoke(mObject);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }
        }

        private boolean isNetWork(Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();

        }
    }


}
