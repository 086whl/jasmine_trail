package com.wfx.jasmine_trail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView name;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonStart=findViewById(R.id.start);
        Button buttonClearAll=findViewById(R.id.clearAll);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                Toast.makeText(MainActivity.this,"开始记录轨迹！",Toast.LENGTH_SHORT).show();
                intent.setClass(MainActivity.this, LocationMarkerMoveActivity.class);
                startActivity(intent);

            }
        });
        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    public void run(){
                        try{
                            //向服务器端添加轨迹记录
                            new ConnectWeb().clearAll();
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {

                        }
                    }
                }.start();
                Toast.makeText(MainActivity.this,"清除全部轨迹成功！",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        supportInvalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent();
        switch (item.getItemId()) {
            case R.id.menu_home:

                break;
            case R.id.menu_list:
                intent.setClass(MainActivity.this, MoveActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_my:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}