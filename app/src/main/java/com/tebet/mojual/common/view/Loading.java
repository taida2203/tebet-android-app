package com.tebet.mojual.common.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.tebet.mojual.R;

public class Loading {
    //private SweetAlertDialog pDialog;
    private Context context;
    private String dialogtext;
    private AlertDialog pDialog = null;
    private ImageView image;
    private TextView Ttext;

    /*public Loading(Context context) {
        this.context = context;
        text = context.getString(R.string.loading);
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(text);
        pDialog.setContentText("Harap tunggu sebentar");
        pDialog.setCancelable(false);
    }

    public void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void setText(String text) {
        this.text = text;
    }
*/
    public Loading(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = inflater.inflate(R.layout.layout_loading, null);

        dialogtext = context.getString(R.string.loading);
        image = (ImageView) dialog.findViewById(R.id.imageView);
        Ttext = (TextView) dialog.findViewById(R.id.text);
        Ttext.setText(dialogtext);

        Glide.with(context)
                .load(R.drawable.loadingnew)
                .into(image);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(dialog);

        pDialog = builder.create();
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hidepDialog() {
//        if (pDialog.isShowing()){
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //Do something after 1500ms
        pDialog.dismiss();
//                }
//            }, 1500);
//        }
    }

    public boolean isShowing() {
        if (pDialog == null) return false;
        return pDialog.isShowing();
    }

    public void setDialogtext(String dialogtext) {
        this.dialogtext = dialogtext;
    }
}
