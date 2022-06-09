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
//            //允许输出流，即允许上传
//            conn.setDoOutput(true);
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

    public String getInfo(){
        String url = path + "/trailInfo/getInfo";
        String str = connWebByGet(url);
        return str;
    }

    public String addInfo(String trailInfo){
        String url = path + "/trailInfo/addInfo/"+trailInfo;
        String str = connWebByPost(url);
        return str;
    }

    public String clearAll(){
        String url = path + "/trailInfo/clearAll";
        String str = connWebByPost(url);
        return str;
    }

//    //获取用户订单
//    public List<BillEntity> getBillList(String uid){
//        List<BillEntity> mylist = new ArrayList<BillEntity>();
//        String url = path + "billAction.action?type=list&uid="+uid;
//        String str=connWeb(url);
//        System.out.println("str:"+str);
//        try{
//            JSONObject job = new JSONObject(str);
//            JSONArray jay = job.getJSONArray("blist");
//            for (int i=0;i<jay.length();i+=1){
//                JSONObject temp = (JSONObject) jay.get(i);
//                BillEntity be = new BillEntity();
//                be.setId(temp.getInt("id"));
//                be.setState(temp.getString("state"));
//                be.setBtime(temp.getString("btime"));
//                be.setBtype(temp.getString("btype"));
//                be.setCtime(temp.getString("ctime"));
//                List<GoodsListEntity> glist = new ArrayList<GoodsListEntity>();
//                JSONArray gl = temp.getJSONArray("glist");
//                for(int j=0;j<gl.length();j+=1){
//                    GoodsListEntity ge = new GoodsListEntity();
//                    JSONObject gtemp = (JSONObject) gl.get(j);
//                    ge.setGname(gtemp.getString("gname"));
//                    ge.setGnum(gtemp.getInt("gnum"));
//                    glist.add(ge);
//                }
//                be.setGlist(glist);
//                mylist.add(be);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return mylist;
//    }
}
