package com.batmad.quizsmile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Tm on 01.05.2017.
 */

public class InfoDialog {
    private Dialog dialog;
    private Activity activity;
    private int current_level;

    InfoDialog(Activity activity, String str){
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setOwnerActivity(activity);

        Button okBtn = (Button) dialog.findViewById(R.id.info_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView text = (TextView) dialog.findViewById(R.id.info_text);
        text.setText(str);
    }

    public void show(){
        dialog.show();
    }

    public void CreateShopBtn(){
        Button shopBtn = (Button) dialog.findViewById(R.id.info_shop_btn);
        shopBtn.setVisibility(View.VISIBLE);
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(activity, BankActivity.class);
                activity.startActivity(intent);
            }
        });
    }
}
