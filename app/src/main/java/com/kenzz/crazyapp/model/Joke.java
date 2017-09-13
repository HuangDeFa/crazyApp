package com.kenzz.crazyapp.model;

import com.kenzz.crazyapp.annotations.ColumnField;
import com.kenzz.crazyapp.annotations.PrimaryKey;
import com.kenzz.crazyapp.annotations.TableName;

/**
 * Created by ken.huang on 9/12/2017.
 */

@TableName(tableName = "tbl_Joke")
public class Joke {
    @ColumnField(columnName = "joke_id")
    @PrimaryKey("joke_id")
    private int Id;

    private String content;

    @ColumnField(columnName = "isRead")
    private boolean isReaded;
}
