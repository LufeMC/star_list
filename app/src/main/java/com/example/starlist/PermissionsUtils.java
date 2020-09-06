package com.example.starlist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtils {

    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    public static final int REQUEST_PERMISSION_INTERNET = 1;

    public static boolean checkAndRequestPermissions(Activity activity) {
        int permissionInternet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        List<String> listPermissionNeeded = new ArrayList<>();

        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
                Toast.makeText(activity, "Interet permission is necessary to run this app", Toast.LENGTH_SHORT).show();
            }

            listPermissionNeeded.add(Manifest.permission.INTERNET);
        }

        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                    REQUEST_PERMISSION_MULTIPLE);
            return false;
        }

        return true;
    }

    public static void requestInternetPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {
                Toast.makeText(activity, "Interet permission is necessary to run this app", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.INTERNET}, REQUEST_PERMISSION_INTERNET);
            } else {
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.INTERNET}, REQUEST_PERMISSION_INTERNET);
            }
        } else {
            Log.d("Permision granted", "Permission already granted");
        }
    }

    public static boolean hasPermission(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context  != null && permissions != null) {
            for (String permission: permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

}
