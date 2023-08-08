package com.example.aqua;

//https://demat.tistory.com/19 참고 소스
//https://choidev-1.tistory.com/71 - node, socket 설치


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    Button Btn_Get_Value;
    TextView Text_Tem, Text_Hum, Text_WaterTem, Text_Light;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Text_Hum = findViewById(R.id.Text_Hum);
        Text_Tem = findViewById(R.id.Text_Tem);
        Text_WaterTem = findViewById(R.id.Text_WaterTem);
        Text_Light = findViewById(R.id.Text_Light);

        Btn_Get_Value = findViewById(R.id.Btn_Get_Value);

        Connect();

        Btn_Get_Value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            get_data();

            }//onclidk

        });

    }

    void Connect() {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");//안드로이드 avd사용시 로컬 호스트는 이 주소사용
            mSocket.connect();//위 주소로 연결
            Log.d("SOCKET", "Connection success : " + mSocket.id());
            mSocket.on("check_con", new Emitter.Listener() {
                //서버가 'check_con'이벤트를 일으킨경우
                @Override
                public void call(Object... args) {//args에 서버가 보내온 json데이터 들어간다
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = (JSONObject) args[0];
                                Toast.makeText(getApplicationContext(), "서버 연결 완료!", Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            Text_Tem.setText("오류");
            Text_Hum.setText("오류");
            Text_Light.setText("오류");
            Text_WaterTem.setText("오류");
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }//Connect end

    void get_data(){
        if(mSocket!=null){//연결된경우에만 보내기 가능
            JSONObject data=new JSONObject();//서버에게 줄 데이터를 json으로 만든다
            try{
                data.put("","get_data");//위에서 만든 json에 키와 값을 넣음
                mSocket.emit("msg",data);//서버에게 msg 이벤트 일어나게 함

                mSocket.on("msg_to_client", new Emitter.Listener() {
                    //서버가 msg_to_client이벤트 일으키면 실행

                    @Override
                    public void call(Object... args) {//args에 서버가 보낸 데이터 들어감
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject data=(JSONObject) args[0];
                                    Text_Tem.setText(data.getString("tem"));
                                    Text_Hum.setText(data.getString("hum"));
                                    Text_Light.setText(data.getString("lig"));
                                    Text_WaterTem.setText(data.getString("w_tem"));

                                    Toast.makeText(getApplicationContext(), "성공!",
                                            Toast.LENGTH_LONG).show();
                                }catch (Exception e){
                                    Text_Tem.setTextColor(Color.RED);
                                    Text_Tem.setText("오류 !!");

                                    Text_Hum.setTextColor(Color.RED);
                                    Text_Hum.setText("오류 !!");

                                    Text_Light.setTextColor(Color.RED);
                                    Text_Light.setText("오류 !!");

                                    Text_WaterTem.setTextColor(Color.RED);
                                    Text_WaterTem.setText("오류 !!");

                                    Toast.makeText(getApplicationContext(), e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }catch(Exception e) {
                Text_Tem.setText("오류");
                Text_Hum.setText("오류");
                Text_Light.setText("오류");
                Text_WaterTem.setText("오류");
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast
                        .LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

    }


}

