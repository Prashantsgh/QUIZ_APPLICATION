package com.example.quiz_app_assg;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    AssetManager am;
    int noOfQues=0,pos=-1,score=0;
    int vis[] = new int[100];
    int quesOrder[]=new int[100];
    int finalans[]=new int[100];
    ListView lv;
    ArrayList<String> ques,ans;
    ArrayList<ArrayList<String>> opts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = this.getAssets();
        readQuestions();
        startQuiz();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = (String) adapterView.getItemAtPosition(i);
                if(selectedOption.equalsIgnoreCase("• " +ans.get(quesOrder[pos]))){
                    if(finalans[pos]==0){
                        finalans[pos]=1;
                        score+=1;
                    }
                    Toast.makeText(getBaseContext(), "Correct Answer", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(finalans[pos]==0){
                        finalans[pos]=1;
                    }
                    Toast.makeText(getBaseContext(), "Incorrect Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void readQuestions(){
        ques= new ArrayList<>();
        opts= new ArrayList<>();
        ans= new ArrayList<>();

        try {
            InputStream is = am.open("ques.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null){
                noOfQues+=1;
                ques.add(line);
                ArrayList<String> topt = new ArrayList<>();
                for(int x=0;x<4;x++){
                    line = reader.readLine();
                    topt.add("• " + line);
                }
                opts.add(topt);
                line = reader.readLine();
                ans.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setQuesOrder(){
        int temp;
        Random random = new Random();
        for(int x=0;x<noOfQues;x++){
            while(true){
                temp= random.nextInt(noOfQues);
                if(vis[temp]==0){
                    vis[temp]=1;
                    quesOrder[x]=temp;
                    break;
                }
            }
        }
    }

    public void setupQuestions(int i){
        TextView tv = (TextView) findViewById(R.id.question);
        tv.setText("Q " + (pos+1) + ": " +ques.get(i));

        Collections.shuffle(opts.get(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, opts.get(i));
        lv = (ListView) findViewById(R.id.options);
        lv.setAdapter(adapter);
    }

    public void nextques(View view) {
        pos+=1;
        if(pos>noOfQues){
            startQuiz();
            return;
        }
        else if(pos==noOfQues-1){
            Button bb = (Button) findViewById(R.id.next);
            bb.setText("SUBMIT TEST");
        }
        else if(pos==noOfQues){
            endTest();
            return;
        }
        setupQuestions(quesOrder[pos]);
    }

    private void startQuiz() {
        lv = (ListView) findViewById(R.id.options);
        lv.setPadding(10,10,10,10);
        TextView tv= (TextView) findViewById(R.id.question);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);

        for(int x=0;x<100;x++){
            vis[x]=quesOrder[x]=finalans[x]=0;
        }
        pos=-1;score=0;
        setQuesOrder();
        nextques(null);
        Button bb = (Button) findViewById(R.id.next);
        bb.setText("NEXT QUESTION");

    }

    public void endTest(){
        lv = (ListView) findViewById(R.id.options);
        lv.setAdapter(null);
        lv.setPadding(0,0,0,0);

        TextView tv= (TextView) findViewById(R.id.question);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText("SCORE: " + score);

        Button bb = (Button) findViewById(R.id.next);
        bb.setText("RETAKE TEST");
    }
}