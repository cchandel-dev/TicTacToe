package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {//had to get to lightbulb
    private TextView PlayerOneLabelx,PlayerTwoLabelx,PlayerOneCountx,PlayerTwoCountx,Leaderboardx;//labels
    private Button[] positions =new Button[9];//all of the the positions
    private Button rst, savebutton, loadbutton;//reset, save and load button
    private int PlayerOneCount,PlayerTwoCount, roundCount;//scores and round
    private boolean firstPlayer;
    private int[] gamestate ={-1,-1,-1,-1,-1,-1,-1,-1,-1};//-1 = empty, 0 = player 1, 1 = player 2
    private int[][]wins = {
            {0,1,2},{3,4,5},{6,7,8},//rows
            {0,3,6},{1,4,7},{2,5,8},//columns
            {0,4,8},{2,4,6}//diagonals
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tie Java variables to XML elements
        PlayerOneCountx = (TextView) findViewById(R.id.PlayerOneCount);
        PlayerTwoCountx = (TextView) findViewById(R.id.PlayerTwoCount);
        Leaderboardx = (TextView) findViewById(R.id.LeaderBoard);
        rst = (Button) findViewById(R.id.rstbtn);
        String buttonidmaker;
        int resource;
        for (int i = 0; i < positions.length; i++){
            buttonidmaker = "btn"+i;
            resource = getResources().getIdentifier(buttonidmaker,"id",getPackageName());
            positions[i]=(Button) findViewById(resource);
            positions[i].setOnClickListener(this);
        }
        firstPlayer = true;
        PlayerOneCount = 0;
        PlayerTwoCount = 0;
        roundCount=0;
    }

    @Override
    public void onClick(View v) {
       if(!((Button)v).getText().toString().equals("")){return;}
       String buttonID = v.getResources().getResourceEntryName(v.getId());
       int gameStatePointer =Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));
       if(firstPlayer){
           gamestate[gameStatePointer] = 0;
       }else if(!firstPlayer){
           gamestate[gameStatePointer] = 1;
       }
       if(checkWin()) {
           roundCount =0;
           Toast toast;
           String msg="";
            if (firstPlayer){
                PlayerOneCount++;
                msg= "Player 1 Won!";
            }
            else if (!firstPlayer){
                PlayerTwoCount++;
                msg= "Player 2 Won!";
            }
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG); // initiate the Toast with context, message and duration for the Toast
            toast.show(); // display the Toast
           reset();
       }
       firstPlayer =!firstPlayer;
       roundCount++;
        rst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PlayerOneCount=0;
                PlayerTwoCount=0;
                reset();
                updatePlayerScore();
                updateBoard();
            }
        });
        if(roundCount>8){
            roundCount=0;
            Toast toast1 = Toast.makeText(getApplicationContext(), "That round was tied", Toast.LENGTH_LONG); // initiate the Toast with context, message and duration for the Toast
            toast1.show(); // display the Toast
            reset();
        }
        updatePlayerScore();
        updateBoard();
    }

    public boolean checkWin(){
        boolean status = false;
        for(int i = 0; i < wins.length; i++){
            if(gamestate[wins[i][0]]==gamestate[wins[i][1]] && gamestate[wins[i][1]] ==gamestate[wins[i][2]] && gamestate[wins[i][0]]!=-1) {
                status = true;
            }
        }
        return status;
    }
    public void reset(){
        firstPlayer =true;
        for(int i=0; i<gamestate.length;i++) {
            gamestate[i] = -1;
        }
       //call update count labels and update button texts here
    }
    public void  updatePlayerScore(){
        PlayerOneCountx.setText(Integer.toString(PlayerOneCount));
        PlayerTwoCountx.setText(Integer.toString(PlayerTwoCount));
        if(PlayerOneCount>PlayerTwoCount) {
            Leaderboardx.setText("Player One is in the Lead");
        }
        else if(PlayerOneCount<PlayerTwoCount)
            {
                Leaderboardx.setText("Player Two is in the Lead");
            }
        else{
            Leaderboardx.setText("");
        }
    }
    public void updateBoard(){
        for(int i =0; i < positions.length; i++){
            switch(gamestate[i]){
                case -1:positions[i].setText("");
                        break;
                case 0: positions[i].setText("X");
                        positions[i].setTextColor(Color.parseColor("#F4A033"));
                        break;
                case 1: positions[i].setText("O");
                        positions[i].setTextColor(Color.parseColor("#73F1F5"));
                        break;
            }

        }
    }



}