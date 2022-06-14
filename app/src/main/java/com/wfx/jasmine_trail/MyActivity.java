package com.wfx.jasmine_trail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {
    private String username;
    private TextView currentName;
    private TextView myNameInput;
    private TextView oldPasswordInput;
    private TextView newPasswordInput;
    private TextView confirmPasswordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Button updateUserNameButton=findViewById(R.id.updateUserNameButton);
        Button updatePasswordButton=findViewById(R.id.updatePasswordButton);
        Button returnButton=findViewById(R.id.returnButton);
        Intent intent=getIntent();
        username = intent.getStringExtra("usernameMy");

        currentName = findViewById(R.id.currentName);
        oldPasswordInput = findViewById(R.id.oldPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        currentName.setText(username+"的个人中心");

        myNameInput = findViewById(R.id.myNameInput);
        myNameInput.setText(username);


        //修改用户名
        updateUserNameButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String updateUsername = myNameInput.getText().toString();
                        if (updateUsername!=null&&!"".equals(updateUsername)){
                            new Thread(){
                                public void run(){
                                    try{
                                        Looper.prepare();
                                        String result = new ConnectWeb().updateUsername(username, updateUsername);
                                        Toast.makeText(MyActivity.this,result,Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        currentName.setText(updateUsername+"的个人中心");
                                    }
                                }
                            }.start();
                        }
                    }
                }
        );

        //修改密码
        updatePasswordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String op = oldPasswordInput.getText().toString();
                        String np = newPasswordInput.getText().toString();
                        String cp = confirmPasswordInput.getText().toString();
                        //判空
                        if (op!=null&&np!=null&&cp!=null&&!"".equals(op)&&!"".equals(np)&&!"".equals(cp)){
                            //确认密码
                            if (np.equals(cp)){
                                new Thread(){
                                    public void run(){
                                        try{
                                            Looper.prepare();
                                            String result = new ConnectWeb().updatePassword(username, op, np);
                                            Toast.makeText(MyActivity.this,result,Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }finally {
                                        }
                                    }
                                }.start();

                            }else{
                                Toast.makeText(MyActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MyActivity.this,"请检查输入！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        //返回主页面
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MyActivity.this, MainActivity.class);
                intent.putExtra("username",username.trim() );
                startActivity(intent);
            }
        });
    }

}
