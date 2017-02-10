package com.my_utils_lib;

import android.app.ProgressDialog;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Ketan on 2/10/2017.
 */

public class M_utils {
    public static int tostLen_long = Toast.LENGTH_LONG;
    public static int tostLen_short = Toast.LENGTH_SHORT;
    private ProgressDialog progressDialog;
    public static String DateFormat_YYYY_MM_DDHHmm = "yyyy-MM-dd HH:mm:ss";
    public static String DateFormat_dd_MM_yyyy = "dd-MM-yyyy";
    public static int Device_height, Device_width;
    private Context _Context;

    public M_utils(Context c) {
        _Context = c;
        GetScreenHeightandWidth(c);
    }

    public void ShowToastMSG(String msg, int len) {
        Toast.makeText(_Context, msg, len).show();
    }

    // check internet connection
    public boolean isConnectingToInternet(boolean ShowMsg) {
        ConnectivityManager connectivity = (ConnectivityManager) _Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }


        }
        if (ShowMsg) {
            ShowToastMSG("No internet connection.", tostLen_short);
        }
        return false;
    }

    // Create progress dialog
    public void mProgressDialog(String msg, boolean isCancelable) {
        progressDialog = new ProgressDialog(_Context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCancelable);
        progressDialog.show();
    }

    // Dismiss progress dialog
    public void DismissmProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    // convert date format
    public String TimeFormatConverter(String CurrentFormat, String ConvertFormat, String currentdate) {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat(CurrentFormat);
        java.text.SimpleDateFormat simpleDateFormat1 = new java.text.SimpleDateFormat(ConvertFormat);

        try {
            return simpleDateFormat1.format(simpleDateFormat.parse(currentdate));
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;
    }

    // Devive Height and Width
    public void GetScreenHeightandWidth(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        Device_height = displaymetrics.heightPixels;
        Device_width = displaymetrics.widthPixels;

    }

    // String Url
    public void GetImageFromUrl(String Url, ImageView imageView) {
        Picasso.with(_Context).load(Url).into(imageView);
    }

    // From file
    public void GetImageFromFile(File file, ImageView imageView) {
        Picasso.with(_Context).load(file).into(imageView);
    }

    // Get Days different
    public int Date_Different(String Date_Format, String StartDate, String EndDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(Date_Format);

        try {

            Date date1 = simpleDateFormat.parse(StartDate);
            Date date2 = simpleDateFormat.parse(EndDate);

            return printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

      /*  System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);*/
        Calendar calendar = Calendar.getInstance();


        return (int) elapsedDays;

    }

    // Convert server time to local device time zone.....
    public String ConvertToLocalTime(String date1, String timezone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat_YYYY_MM_DDHHmm);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = null;
        try {
            date = simpleDateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        Calendar calendar = Calendar.getInstance();
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        String result = simpleDateFormat.format(date);
        return result;
    }

    public void SetCaptureOrientation(File imageFile) {
        int rotate = 0;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);

        try {

            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.v("", "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = _Context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = _Context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
