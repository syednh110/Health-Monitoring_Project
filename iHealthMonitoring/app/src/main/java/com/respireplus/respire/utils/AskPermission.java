package com.respireplus.respire.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asif on 13/4/18.
 */

public class AskPermission {
    Context context;

    public AskPermission(Context context) {
        this.context = context;
    }

    public boolean isPermissionAllowed(String permission){
        if (ActivityCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public boolean askPermission(String[] permissions, int requestCode){
        List<String> pms=new ArrayList<>();
        for (String permission:permissions){
            if(!isPermissionAllowed(permission)){
                pms.add(permission);
            }
        }
        ActivityCompat.requestPermissions((Activity)context, permissions, requestCode);
        for (String permission:pms){
            if (!isPermissionAllowed(permission)){
                return false;
            }
        }
        return true;
    }

}