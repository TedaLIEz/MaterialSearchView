/*
 * Copyright (c) 2016.  TedaLIEz <aliezted@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hustunique.jianguo.materialsearchview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JianGuo on 11/8/16.
 * Db Helper for {@link SearchHistoryDb}
 */
public class HistoryDbHelper {
    private static SearchHistoryDb mHistoryDb;
    private static SQLiteDatabase db;
    private static HistoryDbHelper dbHelper;
    private static final String TAG = "HistoryDbHelper";
    static HistoryDbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new HistoryDbHelper(context);
        }
        return dbHelper;
    }

    private HistoryDbHelper(Context context) {
        mHistoryDb = new SearchHistoryDb(context);
    }

    private static void open() {
        db = mHistoryDb.getWritableDatabase();
    }

    private static void close() {
        db.close();
    }

    void addItem(SearchHistory history) {
        if (!checkByText(history.keywords)) {
            insertItem(history);
        } else {
            update(history);
        }
    }

    private void update(SearchHistory history) {
        long id = history._id;
        ContentValues values = new ContentValues();
        values.put(SearchHistoryDb.SEARCH_HISTORY_COLUMN_TIMES, history.searchTimes);
        String selection = SearchHistoryDb.SEARCH_HISTORY_COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        open();
        int rows = db.update(SearchHistoryDb.SEARCH_HISTORY_TABLE, values, selection, selectionArgs);
        Log.i(TAG, "update data rows: " + rows);
        close();
    }

    void delete(SearchHistory history) {
        long id = history._id;
        open();
        int rst = db.delete(SearchHistoryDb.SEARCH_HISTORY_TABLE, SearchHistoryDb.SEARCH_HISTORY_COLUMN_ID + "=?", new String[] {Long.toString(id)});
        Log.i(TAG, "delete data rows: " + rst);
        close();
    }

    private void insertItem(SearchHistory history) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SearchHistoryDb.SEARCH_HISTORY_COLUMN_TEXT, history.keywords);
        contentValues.put(SearchHistoryDb.SEARCH_HISTORY_COLUMN_TIMES, history.searchTimes);
        open();
        history._id = db.insert(SearchHistoryDb.SEARCH_HISTORY_TABLE, null, contentValues);
        Log.i(TAG, "insert data status: " + history._id);
        close();
    }

    private boolean checkByText(String text) {
        open();
        String query = "SELECT * FROM " + SearchHistoryDb.SEARCH_HISTORY_TABLE
                + " WHERE " + SearchHistoryDb.SEARCH_HISTORY_COLUMN_TEXT + " =?";
        try (Cursor cursor = db.rawQuery(query, new String[]{text})) {
            return cursor.moveToFirst();
        } finally {
            close();
        }
    }


    List<SearchHistory> getAllHistories() {
        List<SearchHistory> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SearchHistoryDb.SEARCH_HISTORY_TABLE +
                " ORDER BY " + SearchHistoryDb.SEARCH_HISTORY_COLUMN_TIMES + " DESC ";
        open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchHistory history = new SearchHistory(cursor.getString(1), cursor.getInt(2), cursor.getLong(0));
                list.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        Log.i(TAG, "find all data size " + list.size());
        return list;
    }

    public static void clearDatabase() {
        open();
        db.delete(SearchHistoryDb.SEARCH_HISTORY_TABLE, null, null);
        close();
    }

}
