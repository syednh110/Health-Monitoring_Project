package com.respireplus.respire.apis;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.respireplus.respire.models.User;

/**
 * Created by namdar on 8/5/18.
 */
@Dao
public interface MyDao {
    @Insert
    void register(User... user);

    @Query("SELECT COUNT(id) FROM users WHERE mobile=:mobile")
    int getUserCount(String mobile);

    @Query("SELECT * FROM users WHERE mobile=:mobile AND password=:password")
    User login(String mobile, String password);
}
