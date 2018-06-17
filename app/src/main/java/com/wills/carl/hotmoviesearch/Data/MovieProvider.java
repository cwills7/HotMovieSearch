package com.wills.carl.hotmoviesearch.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    //Data Matching URI's
    public static final int CODE_MOVIE = 111;
    public static final int CODE_MOVIE_WITH_ID = 112;
    public static final int CODE_VIDEOS_WITH_ID = 222;

    private static final UriMatcher myUriMatcher = buildUriMatcher();
    private MovieDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cur;

        switch (myUriMatcher.match(uri)) {
            case CODE_MOVIE: {
                cur = mDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1
                );
                break;
            }
            case CODE_MOVIE_WITH_ID: {
                String normalizedId = uri.getLastPathSegment();
                String[] selArgs = new String[]{normalizedId};

                cur = mDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        strings,
                        MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                        selArgs,
                        null,
                        null,
                        s1
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("URI not supported: " + uri);
        }

        cur.setNotificationUri(getContext().getContentResolver(), uri);
        return cur;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long _id;
        switch(myUriMatcher.match(uri)){
            case CODE_MOVIE: {
                _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        if (_id != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int numDeleted;
        if (null == s) s = "1";

        switch(myUriMatcher.match(uri)){

            case CODE_MOVIE: {
                numDeleted = mDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        s,
                        strings
                );
                break;
            }

            case CODE_MOVIE_WITH_ID: {
                String normalizedId = uri.getLastPathSegment();
                String[] selArgs = new String[]{normalizedId};

                numDeleted = mDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        "id = ?",
                        selArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (numDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
