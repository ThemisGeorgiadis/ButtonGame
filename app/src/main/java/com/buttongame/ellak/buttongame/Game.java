package com.buttongame.ellak.buttongame;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends Activity implements View.OnClickListener {

    int rand = 0;
    Timer timer = null;
    TimerTask task = null;
    TextView tv;
    TextView tv2;
    int counter = 0;
    int lcounter =0;
    int tmp = 0;
    Button[] buttons = new Button[9];
    Button exit;
    Button scshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttons[0]= (Button) findViewById(R.id.button);
        buttons[1]= (Button) findViewById(R.id.button2);
        buttons[2]= (Button) findViewById(R.id.button3);
        buttons[3]= (Button) findViewById(R.id.button4);
        buttons[4]= (Button) findViewById(R.id.button5);
        buttons[5]= (Button) findViewById(R.id.button6);
        buttons[6]= (Button) findViewById(R.id.button7);
        buttons[7]= (Button) findViewById(R.id.button8);
        buttons[8]= (Button) findViewById(R.id.button9);
        exit = (Button) findViewById(R.id.exitbt);
        scshow = (Button) findViewById(R.id.scorebt);
        exit.setOnClickListener(this);
        scshow.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("hits: "+String.valueOf(counter));
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setText("lost hits: "+String.valueOf(lcounter));
        for(int i = 0 ; i < 9 ; i++){
            buttons[i].setOnClickListener(this);
            buttons[i].setVisibility(View.INVISIBLE);
        }
        task = new TimerTask() {
            @Override
            public void run() {
                rand = new Random().nextInt(9);
                DoShow(rand);

            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(task,2000,1000);
        task = new TimerTask() {
            @Override
            public void run() {
                myHandler.post(myRun);
            }
        };
        timer.schedule(task,500,1000);
    }


    public void DoShow(int num){
        Message mes = new Message();
        Bundle bu = new Bundle();
        bu.putInt("random",num);
        mes.setData(bu);
        myHandler.sendMessage(mes);
    }

    Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage (Message mess){
            Bundle bu = mess.getData();
            int rand = bu.getInt("random");
            buttons[rand].setVisibility(View.VISIBLE);
        }
    };



     Runnable myRun = new Runnable() {
        @Override
        public void run() {
            if(buttons[rand].getVisibility()!=View.INVISIBLE) {
                buttons[rand].setVisibility(View.INVISIBLE);
                lcounter++;
                tv2.setText("lost hits: " + String.valueOf(lcounter));
                if(lcounter==20){
                    submitScore();
                }
            }
        }
    };



    @Override
    public void onClick(View v) {
        if(v == scshow){
            Intent inte = new Intent(this,ScoreView.class);
            startActivity(inte);
        }
        else if(v == exit){
            timer.cancel();
            timer.purge();
            finish();
        }
        else{
            tmp = counter;
            counter++;
            tv.setText("hits: " + String.valueOf(counter));
            v.setVisibility(View.INVISIBLE);
        }

    }

    public void submitScore(){
        Intent intent = new Intent(this,ScoreSubmit.class);
        Bundle bu = new Bundle();
        bu.putInt("score",counter);
        intent.putExtras(bu);
        startActivity(intent);
    }

    @Override
    protected void onRestart(){
        lcounter = 0;
        counter = 0;
        tv.setText("hits: "+counter);
        tv2.setText("lost hits: "+lcounter);
        super.onRestart();
    }


}


