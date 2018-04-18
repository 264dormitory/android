package com.jacklee.clatclatter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class Model0 extends AppCompatActivity{
    private TextView model0_tv;
    private EditText model0_ed;
    private Button model0_commit;
    final PlayVoice playVoice = new PlayVoice(0);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model0);
        model0_ed=(EditText)findViewById(R.id.model0_ed);
        model0_tv=(TextView)findViewById(R.id.model0_tv);
        final int num1 = (int) ((Math.random() * 9 + 1) * 100);
        final int num2 = (int) ((Math.random() * 9 + 1) * 100);
        model0_tv.setText("请回答如下问题"+"\n"+num1+"+"+num2+"="+"?");
        model0_commit=(Button)findViewById(R.id.model0_commit);
        model0_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = model0_ed.getText().toString();
                if (!result.isEmpty()) {

                    int result1 = Integer.parseInt(result);
                    if (result1 == (num1 + num2)) {
                        playVoice.stopVoice();
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Model0.this);
                        builder.setTitle("提示")
                                .setMessage("回答错误，请重新输入!")
                                .setCancelable(false)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        model0_ed.setText("");
                                    }
                                }).show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Model0.this);
                    builder.setTitle("提示")
                            .setMessage("回答不能为空!")
                            .setCancelable(false)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            }


        });
    }
}
