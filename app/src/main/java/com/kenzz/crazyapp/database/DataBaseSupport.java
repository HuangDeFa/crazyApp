package com.kenzz.crazyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.kenzz.crazyapp.annotations.ColumnField;
import com.kenzz.crazyapp.annotations.IgnoreField;
import com.kenzz.crazyapp.annotations.PrimaryKey;
import com.kenzz.crazyapp.annotations.TableName;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdefa on 27/08/2017.
 */

public class DataBaseSupport<T> implements IDataBaseSupport<T> {
    private SQLiteDatabase mSQLiteDatabase;
    private DataBaseSupportHelper mSupportHelper;
    @Override
    public void init(Context context) {
         String fileName=createDatabaseFile("joke").getAbsolutePath();
         mSupportHelper=new DataBaseSupportHelper(context,fileName);
         mSQLiteDatabase=mSupportHelper.getWritableDatabase();
    }
    private File createDatabaseFile(@NonNull String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dirFile = new File(Environment.getExternalStorageDirectory(), "essayJoke");
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            return new File(dirFile, fileName + ".db");
        } else {
            throw new RuntimeException("ExternalStorage has not be Mounted,can not create db file");
        }
    }

    @Override
    public int insert(T model) {
        TableName tableName = model.getClass().getAnnotation(TableName.class);
        ContentValues values = createInsertContentValues(model);
        return (int) mSQLiteDatabase.insert(tableName.tableName(), null, values);
    }

    private ContentValues createInsertContentValues(T model) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation fileAnnotation = field.getAnnotation(IgnoreField.class);
            if (fileAnnotation != null) {
                continue;
            }
            String columnName;
            fileAnnotation = field.getAnnotation(ColumnField.class);
            if (fileAnnotation != null) {
                columnName = ((ColumnField) fileAnnotation).columnName();
            }
            columnName = field.getName();
            if(columnName.equals("$change") || columnName.equals("serialVersionUID")){
                continue;
            }
            addFieldToContentValue(contentValues, columnName, field, model);
        }
        return contentValues;
    }

    private void addFieldToContentValue(ContentValues contentValues, String key, Field field, Object target) {
        try {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == String.class) {
                contentValues.put(key, (String) field.get(target));
            } else if (type == int.class) {
                contentValues.put(key, field.getInt(target));
            } else if (type == float.class) {
                contentValues.put(key, field.getFloat(target));
            } else if (type == long.class) {
                contentValues.put(key, field.getLong(target));
            } else if (type == byte.class) {
                contentValues.put(key, field.getByte(target));
            } else if (type == double.class) {
                contentValues.put(key, field.getDouble(target));
            } else if (type == short.class) {
                contentValues.put(key, field.getShort(target));
            } else if (type == boolean.class) {
                contentValues.put(key, field.getBoolean(target));
            } else if (type == char.class) {
                contentValues.put(key, field.getChar(target) + "");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean delete(T model) {
        TableName table = model.getClass().getAnnotation(TableName.class);
        Field[] fields = model.getClass().getDeclaredFields();
        String primaryKey = "";
        String primaryKeyValue = "";
        for (Field field : fields) {
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if (PrimaryKey != null) {
                primaryKey = PrimaryKey.value();
                field.setAccessible(true);
                try {
                    primaryKeyValue = field.get(model).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(primaryKeyValue)) {
            throw new RuntimeException("Can not delete from table " + table.tableName() + "with PrimaryKey is empty");
        }
        return mSQLiteDatabase.delete(table.tableName(), primaryKey + "=?", new String[]{primaryKeyValue}) > 0;
    }

    @Override
    public boolean delete(Class<T> clazz, int id) {
        TableName table = clazz.getAnnotation(TableName.class);
        Field[] fields = clazz.getDeclaredFields();
        String primaryKey = "";
        for (Field field : fields) {
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if (PrimaryKey != null) {
                primaryKey = PrimaryKey.value();
                break;
            }
        }
        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(id + "")) {
            throw new RuntimeException("Can not delete from table " + table.tableName() + "with PrimaryKey is empty");
        }
        return mSQLiteDatabase.delete(table.tableName(), primaryKey + "=?", new String[]{id + ""}) > 0;
    }

    @Override
    public boolean delete(Class<T> clazz, int[] ids) {
        TableName table = clazz.getAnnotation(TableName.class);
        Field[] fields = clazz.getDeclaredFields();
        String primaryKey = "";
        for (Field field : fields) {
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if (PrimaryKey != null) {
                primaryKey = PrimaryKey.value();
                break;
            }
        }
        if (TextUtils.isEmpty(primaryKey) || ids == null) {
            throw new RuntimeException("Can not delete from table " + table.tableName() + "with PrimaryKey is empty");
        }
        StringBuilder sb=new StringBuilder();
        sb.append("delete from table " )
                .append(table.tableName())
                .append("where ")
                .append(primaryKey)
                .append("in " );
        for (int id : ids) {
            sb.append(id);
            sb.append(",");
        }
       String sql=sb.substring(0,sb.length()-1);
        try {
            mSQLiteDatabase.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public int update(T model) {
        TableName tableName = model.getClass().getAnnotation(TableName.class);
        ContentValues values = createInsertContentValues(model);
        Field[] fields = model.getClass().getDeclaredFields();
        String primaryKey = "";
        String primaryKeyValue = "";
        for (Field field : fields) {
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if (PrimaryKey != null) {
                primaryKey = PrimaryKey.value();
                field.setAccessible(true);
                try {
                    primaryKeyValue = field.get(model).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(primaryKeyValue)) {
            throw new RuntimeException("Can not update table " + tableName.tableName() + "with PrimaryKey is empty");
        }
        return mSQLiteDatabase.update(tableName.tableName(), values, primaryKey + "=?", new String[]{primaryKeyValue});
    }

    @Override
    public List<T> queryAll(Class<T> clazz) {
        TableName table = clazz.getAnnotation(TableName.class);
        Cursor cursor = mSQLiteDatabase.query(table.tableName(), null, null, null, null, null, null);
        return queryImpl(clazz,cursor);
    }

    private void setColumnValue(Field field, Cursor cursor, T target) {
      try{
          field.setAccessible(true);
          Class<?> type = field.getType();
          ColumnField annotation = field.getAnnotation(ColumnField.class);
          int columnIndex=cursor.getColumnIndex(annotation!=null?annotation.columnName():field.getName());
          if (type == String.class) {
           field.set(cursor.getString(columnIndex),target);
          } else if (type == int.class) {
              field.setInt(target,cursor.getInt(columnIndex));
          } else if (type == float.class) {
              field.setFloat(target,cursor.getFloat(columnIndex));
          } else if (type == long.class) {
              field.setLong(target,cursor.getLong(columnIndex));
          } else if (type == byte.class) {
              field.setByte(target,cursor.getBlob(columnIndex)[0]);
          } else if (type == double.class) {
              field.setDouble(target,cursor.getDouble(columnIndex));
          } else if (type == short.class) {
              field.setShort(target,cursor.getShort(columnIndex));
          } else if (type == boolean.class) {
              field.setBoolean(target,Boolean.valueOf(cursor.getString(columnIndex)));
          } else if (type == char.class) {
            field.setByte(target,Byte.valueOf(cursor.getString(columnIndex)));
          }
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    @Override
    public T queryById(Class<T> clazz,int id) {
        TableName table = clazz.getAnnotation(TableName.class);
        Field[] fields = clazz.getDeclaredFields();
        String primaryKey = "";
        for (Field field : fields) {
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if (PrimaryKey != null) {
                primaryKey = PrimaryKey.value();
                break;
            }
        }
        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(id + "")) {
            throw new RuntimeException("Can not query from table " + table.tableName() + "with PrimaryKey is empty");
        }
        Cursor cursor = mSQLiteDatabase.query(table.tableName(),null,primaryKey+"=?",new String[]{id+""},null,null,null);
        List<T> modelList = queryImpl(clazz, cursor);
        return modelList.size()>0?modelList.get(0):null;
    }

    @Override
    public List<T> query(Class<T> clazz, ContentValues contentValues) {

        return null;
    }

    private List<T> queryImpl(Class<T> clazz,Cursor cursor){
        List<T> modelList=new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> columnFields=new ArrayList<>();
        for (Field field : fields) {
            IgnoreField annotation = field.getAnnotation(IgnoreField.class);
            if(annotation!=null){
                continue;
            }
            if(field.getName().equals("$change") || field.getName().equals("serialVersionUID")){
                continue;
            }
            columnFields.add(field);
        }
        T model;
        if(cursor!=null){
            while (cursor.moveToNext()){
                try {
                    model=clazz.newInstance();
                    for (Field columnField : columnFields) {
                        setColumnValue(columnField,cursor,model);
                    }
                    modelList.add(model);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return modelList;
    }
}
