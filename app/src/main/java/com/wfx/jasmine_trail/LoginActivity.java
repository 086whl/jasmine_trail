package com.wfx.jasmine_trail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView userName = findViewById(R.id.input_username);
                TextView userPassword = findViewById(R.id.input_password);
                String name=userName.getText().toString();
                String password = userPassword.getText().toString();
                if(name==null||password==null||name.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名与密码！",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        public void run(){
                            try{
                                Looper.prepare();
                                String loginInfo = new ConnectWeb().login(name, password);
                                if (loginInfo == null || loginInfo.equals("")){
                                    Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("username",loginInfo.trim() );
                                    startActivity(intent);
                                }
                                Looper.loop();
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                            }
                        }
                    }.start();

                }
            }
        });
        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView userName = findViewById(R.id.input_username);
                TextView userPassword = findViewById(R.id.input_password);
                String name=userName.getText().toString();
                String password = userPassword.getText().toString();
                if(name==null||password==null||name.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名与密码！",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        public void run(){
                            try{
                                Looper.prepare();
                                String registerResult = new ConnectWeb().register(name, password);
                                Toast.makeText(LoginActivity.this,registerResult,Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                            }
                        }
                    }.start();

                }
            }
        });
    }


}
