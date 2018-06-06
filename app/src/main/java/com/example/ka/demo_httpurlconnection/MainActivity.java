package com.example.ka.demo_httpurlconnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private Button mButtonDo;
    private ProgressDialog mProgressDialog;
    private TextView mTextView;

    private AsyncTask mMyTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = MainActivity.this;
        mCLayout = findViewById(R.id.coordinator_layout);
        mButtonDo = findViewById(R.id.btn_do);
        mTextView = findViewById(R.id.tv);

        // Bật cuộn trong Textview
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("AsyncTask");
        mProgressDialog.setMessage("Please wait, we are downloading your file...");

        mButtonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Execute the async task
                mMyTask = new DownloadTask()
                        .execute(stringToURL("http://pastebin.com/raw/wgkJgazE"));
            }
        });
    }

    private class DownloadTask extends AsyncTask<URL,Void,String>{
        protected void onPreExecute(){
            mProgressDialog.show();
        }

        protected String doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                String line ="";
                while((line= bufferedReader.readLine())!=null){
                    stringBuffer.append(line);
                }

                return stringBuffer.toString();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                connection.disconnect();

                try{
                    bufferedReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String result){
            mProgressDialog.dismiss();
            mTextView.setText(result);
        }
    }

    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
