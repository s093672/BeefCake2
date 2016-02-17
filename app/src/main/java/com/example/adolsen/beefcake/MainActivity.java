package com.example.adolsen.beefcake;

import android.app.Activity;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button exitButton;
    private Button addBlock;
    private EditText blockName;
    private TextView appTitle;
    private LinearLayout mainMenu;
    private ArrayList<String> blocks =  new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenu = (LinearLayout) findViewById(R.id.mainMenu);
        readFile();





        appTitle = (TextView) findViewById(R.id.appTitle);


        blockName = (EditText) findViewById(R.id.blockName);
        addBlock = (Button) findViewById(R.id.addBlock);
        addBlock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = blockName.getEditableText().toString();
                if (str.length() < 1) {
                    str = "Name Missing";
                }
                final String name = str.substring(0, 1).toUpperCase() + str.substring(1);
                writeFile(name);
                readFile();
            }
        });

        exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDestroy();
            }
        });
    }



    public void onDestroy() {
        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void writeFile(String text){
        String filename = "myfile";

        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            BufferedWriter r = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data = "BlockB\nBlockc";


            for(int i = 0; i<blocks.size()-1;i++){
                data+=blocks.get(i)+"\n";
            }
            data+=text;

            Log.d("Input", data);
            r.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void readFile(){
        String filename = "myfile";
        blocks.clear();
        try {
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            String line = "test";
            Log.d("Output", line);
            while ((line = r.readLine()) != null) {
                if(line.startsWith("Block")){

                    blocks.add(line);
                }
            }
            r.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainMenu.removeAllViews();
        for (int i = 0;i<blocks.size();i++){
            Log.d("add",Integer.toString(i));
            final int x = i;
            final LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            mainMenu.addView(ll);
            Button mainMenuButton = new Button(this);
            mainMenuButton.setAllCaps(false);
            mainMenuButton.setText(blocks.get(i));
            mainMenuButton.setWidth(500);
            mainMenuButton.setHeight(100);
            ll.addView(mainMenuButton);
            mainMenuButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this, BlockActivity.class);
                    myIntent.putExtra("name", Integer.toString(x));
                    MainActivity.this.startActivity(myIntent);
                }
            });
            Button deleteBlock = new Button(mainMenu.getContext());
            deleteBlock.setAllCaps(false);
            ll.addView(deleteBlock);
            deleteBlock.setText("Delete");
            deleteBlock.setWidth(100);
            deleteBlock.setHeight(50);
            deleteBlock.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    blocks.remove(x);
                    ll.removeAllViews();

                }
            });
        }

    }



}
