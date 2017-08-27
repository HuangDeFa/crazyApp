package com.kenzz.crazyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangdefa on 26/08/2017.
 *
 *  用于处理App crash时的Helper并收集错误信息
 */

public class ExceptionHelper {

    private MyExceptionHandler mMyExceptionHandler;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private Context mContext;

    private ExceptionHelper(){}
    private static ExceptionHelper mExceptionHelper;
    public static ExceptionHelper getExceptionHelper(){
        if(mExceptionHelper==null){
            synchronized (ExceptionHelper.class){
                if(mExceptionHelper==null){
                    mExceptionHelper=new ExceptionHelper();
                }
            }
        }
        return mExceptionHelper;
    }

    public void init(Context context){
        mContext=context;
        mMyExceptionHandler=new MyExceptionHandler();
        mDefaultExceptionHandler=Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mMyExceptionHandler);
    }


     class MyExceptionHandler implements Thread.UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread t, Throwable e) {
           //TODO: save error message to file or other handler,eg:close all activity then exit app.
           String fileName= saveErrorMessageToFile(e);
            cacheFileName(fileName);
            mDefaultExceptionHandler.uncaughtException(t,e);
        }

         private String saveErrorMessageToFile(Throwable e) {
             String fileName = null;
             StringBuffer sb = new StringBuffer();

             for (Map.Entry<String, String> entry : obtainSimpleInfo()
                     .entrySet()) {
                 String key = entry.getKey();
                 String value = entry.getValue();
                 sb.append(key).append(" = ").append(value).append("\n");
             }

             sb.append(obtainErrorMessage(e));

             if (Environment.getExternalStorageState().equals(
                     Environment.MEDIA_MOUNTED)) {
                 File dir = new File(mContext.getFilesDir() + File.separator + "crash"
                         + File.separator);

                 // 先删除之前的异常信息
                 if (dir.exists()) {
                     deleteDir(dir);
                 }

                 // 再从新创建文件夹
                 if (!dir.exists()) {
                     dir.mkdir();
                 }
                 try {
                     fileName = dir.toString()
                             + File.separator
                             + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                     FileOutputStream fos = new FileOutputStream(fileName);
                     fos.write(sb.toString().getBytes());
                     fos.flush();
                     fos.close();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
             return fileName;
         }

         private Map<String,String> obtainSimpleInfo(){
             Map<String,String> map=new HashMap<>();
             PackageManager mPackageManager = mContext.getPackageManager();
             PackageInfo mPackageInfo = null;
             try {
                 mPackageInfo = mPackageManager.getPackageInfo(
                         mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
             } catch (PackageManager.NameNotFoundException e) {
                 e.printStackTrace();
             }
             map.put("versionName", mPackageInfo.versionName);
             map.put("versionCode", "" + mPackageInfo.versionCode);
             map.put("MODEL", "" + Build.MODEL);
             map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
             map.put("PRODUCT", "" + Build.PRODUCT);
             map.put("MOBLE_INFO", obtainMobileInfo());
             return map;
         }

         /**
          * 返回当前日期根据格式
          **/
         private String getAssignTime(String dateFormatStr) {
             DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
             long currentTime = System.currentTimeMillis();
             return dataFormat.format(currentTime);
         }

         private void cacheFileName(String fileName){
             SharedPreferences sharedPreferences = mContext.getSharedPreferences("crash_file", Context.MODE_PRIVATE);
             SharedPreferences.Editor editor = sharedPreferences.edit();
             editor.putString("crash_file_name",fileName);
             editor.commit();
         }

         public File getCrashFile(){
             SharedPreferences sharedPreferences = mContext.getSharedPreferences("crash_file", Context.MODE_PRIVATE);
             String fileName = sharedPreferences.getString("crash_file_name", null);
             File file=new File(fileName);
             if(file.exists()){
                 return file;
             }
             return null;
         }

         private String obtainMobileInfo(){
             StringBuilder sb=new StringBuilder();
             try {
                 Field[] fields = Build.class.getDeclaredFields();
                 for (Field field : fields) {
                     field.setAccessible(true);
                    String name= field.getName();
                    String value= field.get(null).toString();
                     sb.append(name+" = "+value);
                     sb.append("\n");
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
             return sb.toString();
         }

         private String obtainErrorMessage(Throwable e){
             StringWriter stringWriter=new StringWriter();
             PrintWriter writer=new PrintWriter(stringWriter);
             e.printStackTrace(writer);
             return writer.toString();
         }

         /**
          * 递归删除目录下的所有文件及子目录下所有文件
          *
          * @param dir 将要删除的文件目录
          * @return boolean Returns "true" if all deletions were successful. If a
          * deletion fails, the method stops attempting to delete and returns
          * "false".
          */
         private boolean deleteDir(File dir) {
             if (dir.isDirectory()) {
                 String[] children = dir.list();
                 // 递归删除目录中的子目录下
                 for (int i = 0; i < children.length; i++) {
                     boolean success = deleteDir(new File(dir, children[i]));
                     if (!success) {
                         return false;
                     }
                 }
             }
             // 目录此时为空，可以删除
             return true;
         }
     }
}
