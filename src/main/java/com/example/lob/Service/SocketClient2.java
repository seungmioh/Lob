package com.example.lob.Service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketClient2  extends Thread{
    private Socket msocket;

    @Override
    public void run() {
        super.run();
        try {
            msocket= IO.socket("192.168.0.103:9999");
            msocket.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // Socket서버에 connect 된 후, 서버로부터 전달받은 'Socket.EVENT_CONNECT' Event 처리.
    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = new JSONObject();
            try {
                data.put("key1", "value1");
                data.put("key2", "value2");
                msocket.emit("reqMsg", data);
            } catch(JSONException e) {
                e.printStackTrace();
            }
            // your code...
        }
    };
    // 서버로부터 전달받은 'chat-message' Event 처리.
    public Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            // your code...
        }
    };
}