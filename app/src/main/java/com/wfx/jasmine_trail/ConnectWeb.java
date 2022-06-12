package com.wfx.jasmine_trail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectWeb {
    public static String path="http://124.222.188.244/jasmine_trail_server";
//    public static String path="http://192.168.0.102:8088/jasmine_trail_server";
    //访问数据库获取数据
    private String connWebByGet(String url){

        //连接对象
        HttpURLConnection conn=null;
        InputStream is=null;
        String resultData="";

        try{
            //URL对象
            URL obUrl = new URL(url);
            //打开连接
            conn = (HttpURLConnection) obUrl.openConnection();
            //允许输入流，即允许下载
            conn.setDoInput(true);
            //不使用缓冲
            conn.setUseCaches(false);
            //使用get请求
            conn.setRequestMethod("GET");
            //获取输入流，此时才真正建立连接

            is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(isr);
            String inputLine="";
            while((inputLine= bufferedReader.readLine())!=null){
                resultData+=inputLine+"\n";
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is!=null){
                try{
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (conn!=null){
                conn.disconnect();
            }
        }
        return resultData;
    }

    //访问数据库获取数据
    private String connWebByPost(String url){

        //连接对象
        HttpURLConnection conn=null;
        InputStream is=null;
        String resultData="";

        try{
            //URL对象
            URL obUrl = new URL(url);
            //打开连接
            conn = (HttpURLConnection) obUrl.openConnection();
            //允许输入流，即允许下载
            conn.setDoInput(true);
            //允许输出流，即允许上传
            conn.setDoOutput(true);
            //不使用缓冲
            conn.setUseCaches(false);
            //使用post请求
            conn.setRequestMethod("POST");
            //获取输入流，此时才真正建立连接
            is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(isr);
            String inputLine="";
            while((inputLine= bufferedReader.readLine())!=null){
                resultData+=inputLine+"\n";
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is!=null){
                try{
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (conn!=null){
                conn.disconnect();
            }
        }
        return resultData;
    }

    public String getInfo(String username){
        String url = path + "/trailInfo/getInfo/"+username;
        String str = connWebByGet(url);
        return str;
    }

    public String login(String username,String password){
        String url = path + "/trailInfo/login/"+username+"/"+password;
        String str = connWebByPost(url);
        return str;
    }

    public String register(String username,String password){
        String url = path + "/trailInfo/register/"+username+"/"+password;
        String str = connWebByPost(url);
        return str;
    }

    public String addInfo(String username,String trailInfo){
        String url = path + "/trailInfo/addInfo/"+username+"/"+trailInfo;
        String str = connWebByPost(url);
        return str;
    }

    public String clearAll(String username){
        String url = path + "/trailInfo/clearAll/"+username;
        String str = connWebByPost(url);
        return str;
    }

}
