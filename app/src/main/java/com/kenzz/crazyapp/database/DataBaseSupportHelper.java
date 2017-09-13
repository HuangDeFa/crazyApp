package com.kenzz.crazyapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kenzz.crazyapp.annotations.ColumnField;
import com.kenzz.crazyapp.annotations.IgnoreField;
import com.kenzz.crazyapp.annotations.PrimaryKey;
import com.kenzz.crazyapp.annotations.TableName;
import com.kenzz.crazyapp.model.Joke;

import java.lang.reflect.Field;

/**
 * Created by ken.huang on 9/12/2017.
 *
 */

public class DataBaseSupportHelper extends SQLiteOpenHelper {

    public DataBaseSupportHelper(Context context,String name){
        super(context,name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //create the data base and table;
     createTable(Joke.class,db);
    }

    private  void createTable(Class<?> clazz,SQLiteDatabase db) {
        StringBuilder sb=new StringBuilder();
        TableName tableName = clazz.getAnnotation(TableName.class);
        sb.append("create table ");
        sb.append(tableName.tableName());
        sb.append("(");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.getAnnotation(IgnoreField.class)!=null){
                continue;
            }
            PrimaryKey PrimaryKey = field.getAnnotation(PrimaryKey.class);
            if(PrimaryKey !=null){
                sb.append(PrimaryKey.value())
                        .append(" integer primary key autoincrement,");
                continue;
            }
            String columnName="";
            ColumnField ColumnField = field.getAnnotation(ColumnField.class);
            columnName= ColumnField !=null? ColumnField.columnName():field.getName();
            if(columnName.equals("$change") || columnName.equals("serialVersionUID")){
                continue;
            }
            sb.append(columnName);
            if(field.getType()==String.class){
              sb.append(" TEXT,");
            }else if(field.getType()==int.class){
                sb.append(" INTEGER,");
            }else if(field.getType()==short.class || field.getType()==byte.class){
                sb.append(" SMALLINT,");
            }else if(field.getType()==float.class){
                sb.append(" FLOAT,");
            }else if(field.getType()==double.class){
                sb.append(" DOUBLE,");
            }else if(field.getType()==boolean.class){
                sb.append(" BOOLEAN,");
            }else if(field.getType()==char.class){
                sb.append(" varchar(5),");
            }else if(field.getType()==long.class){
                sb.append(" BIGINT,");
            }
        }
        sb.append(")");
        sb.setCharAt(sb.lastIndexOf(","),' ');
        //"create table person(id integer primary key autoincrement,name varchar(64),address varchar(64))";
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
