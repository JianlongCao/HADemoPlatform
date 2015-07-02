package com.cisco.demo.comm;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

public class HTTPHelper implements Runnable{

    private static boolean                        running  = false;
    private LinkedBlockingDeque<HttpCmd> cmdLists = new LinkedBlockingDeque<>();
    private static HTTPHelper httpHelper = null;

    public static HTTPHelper Instance() {
        if(httpHelper == null) {
           httpHelper = new HTTPHelper();
        }
        return httpHelper;
    }

    private HTTPHelper() {

    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }
    @Override
    public void run() {
        while(running) {
            HttpCmd httpCmd = null;
            try {
                httpCmd = cmdLists.take();
            } catch (InterruptedException e) {
                continue;
            }
//            excuteHTTPCmd(httpCmd);
            excuteHTTPCmdAsync(httpCmd);
        }
    }

    public void addCmd(HttpCmd cmd) {
        if(!running) {
            this.start();
        }

        if(cmd != null) {
            cmdLists.add(cmd);
        }
    }

    public HttpResponse excuteHTTPCmd(HttpCmd cmd) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;
        System.out.println(cmd.toString());
        if(cmd.getMethod().equals("put")) {

            HttpPut httpput = new HttpPut(
                    cmd.getUri().toLowerCase());
            try {
                StringEntity input = new StringEntity(cmd.getEntity());
                input.setContentType("application/json");
                httpput.setEntity(input);
                response = client.execute(httpput);

            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            } catch (ClientProtocolException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if(cmd.getMethod().equalsIgnoreCase("get")){
            HttpGet httpGet = new HttpGet((cmd.getUri().toLowerCase()));
            try {
                StringEntity input = new StringEntity(cmd.getEntity());
                response = client.execute(httpGet);
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            } catch (ClientProtocolException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if(cmd.getMethod().equals("post")) {

            HttpPost httppost = new HttpPost(
                    cmd.getUri().toLowerCase());
            try {
                StringEntity input = new StringEntity(cmd.getEntity());
                input.setContentType("application/json");
                httppost.setEntity(input);
                response = client.execute(httppost);

            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            } catch (ClientProtocolException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return response;
    }

    public HttpResponse excuteHTTPCmdAsync(HttpCmd cmd) {
        HttpResponse response = null;
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        System.out.println(cmd.toString());
        if(cmd.getMethod().equals("put")) {
            HttpPut httpput = new HttpPut(
                    cmd.getUri().toLowerCase());
            try {
                StringEntity input = new StringEntity(cmd.getEntity());
                input.setContentType("application/json");
                httpput.setEntity(input);
                httpclient.execute(httpput, null);
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage());
            }
        } else if(cmd.getMethod().equals("get")) {
            HttpGet httpGet = new HttpGet(
                    cmd.getUri().toLowerCase());
            Future<HttpResponse> future = httpclient.execute(httpGet, null);
            try {
                 response = future.get();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
//        try {
//            httpclient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return response;
    }


}
