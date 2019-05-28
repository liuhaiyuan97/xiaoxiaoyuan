package com.google.blockly.android.demo;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class GetTask extends AsyncTask {
    private int floor=1;
    private String user;
    public void setUser(String user){
        this.user=user;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        //通过网络访问服务器端实现登录
        try {
            String str = "http://60.205.183.222:8080/xiaoxiaoyuan/floorServlet?user="+user;
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //设置请求参数
            connection.setRequestProperty("contentType","utf-8");
            InputStream is = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String res = reader.readLine();
            if(res!=null){
                int r = Integer.parseInt(res);
                floor = r;
                Log.e("text0",""+floor);
            }

            reader.close();
            inputStreamReader.close();
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getFloor(){
        try {
            while(floor==1){
                Thread.sleep(1);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e("text1",""+floor);
        return floor;
    }
}
