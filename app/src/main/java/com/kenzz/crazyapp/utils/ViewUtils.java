package com.kenzz.crazyapp.utils;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

import com.kenzz.crazyapp.annotations.OnClickEvent;
import com.kenzz.crazyapp.annotations.ViewById;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by huangdefa on 26/08/2017.
 *  仿xUtils 自定义IOC
 *  使用依赖注入方式给设置view和onclick事件
 */

public class ViewUtils {

    static class ViewFinder{
        private Object mObject;
        private SparseArray<View> mViewSparseArray;
        ViewFinder(Object object){
            mObject=object;
            mViewSparseArray=new SparseArray<>();
        }
        View findView(int viewId){
            if(mViewSparseArray.indexOfKey(viewId)>=0){
                return mViewSparseArray.get(viewId);
            }
            View view;
            if(mObject instanceof Activity){
                view= ((Activity)mObject).findViewById(viewId);
            }else if(mObject instanceof View){
                view= ((View)mObject).findViewById(viewId);
            }
            else{
                throw  new IllegalArgumentException("Must find view in Activity or view");
            }
            mViewSparseArray.put(viewId,view);
           return view;
        }
    }

    public static void inject(Activity activity){
     inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }

    private static void inject(ViewFinder viewFinder,Object object){
        injectField(viewFinder,object);
        injectEvent(viewFinder,object);
    }

    /**
     *  通过反射设置点击事件
     * @param viewFinder
     * @param object
     */
    private static void injectEvent(ViewFinder viewFinder, Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method:methods){
            OnClickEvent annotation = method.getAnnotation(OnClickEvent.class);
            if(annotation!=null){
                int[] viewIds=annotation.value();
                for (int viewId:viewIds) {
                    View view = viewFinder.findView(viewId);
                    if(view!=null) {
                        view.setOnClickListener(new DeclaredClickEvent(method, object));
                    }
                }
            }
        }
    }

   static class DeclaredClickEvent implements View.OnClickListener{
        Method mMethod;
        Object mObject;
        public DeclaredClickEvent(Method method,Object object) {
            this.mMethod=method;
            this.mObject=object;
        }

        @Override
        public void onClick(View v) {
              mMethod.setAccessible(true);
            try {
                mMethod.invoke(mObject,v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过反射获取被注解标记的属性 并将view对象注入。
     * @param viewFinder
     * @param object
     */
    private static void injectField(ViewFinder viewFinder, Object object) {
        Class<?> clazz=object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            ViewById annotation = field.getAnnotation(ViewById.class);
            if(annotation!=null){
                int viewId = annotation.value();
                View view = viewFinder.findView(viewId);
                if(view!=null){
                    field.setAccessible(true);
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
