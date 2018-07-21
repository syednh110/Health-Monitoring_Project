package com.respireplus.respire.apis;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.respireplus.respire.models.User;

/**
 * Created by namdar on 8/5/18.
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MyDao getMyDao();
}
