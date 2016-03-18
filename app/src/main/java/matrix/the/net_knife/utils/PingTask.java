package matrix.the.net_knife.utils;

/**
 * Created by JoaoLuiz on 17/03/2016.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class PingTask extends AsyncTask<String, String, Void>
{
    private String hostAdress;
    private TextView resultText;
    private ProgressDialog progress;

    public PingTask(String host, View view, Context context)
    {
        hostAdress = host;
        resultText = (TextView) view;
        progress   = new ProgressDialog(context);
    }

    @Override
    protected Void doInBackground(String... params) {
        String pingCommand = "su -c /data/data/matrix.the.net_knife/busybox ping -c 5";
        String hostName = hostAdress;
        String cmd = pingCommand + " " + hostName;

        try
        {
            System.out.println(cmd);
            Process p = Runtime.getRuntime().exec(cmd);
//            p.waitFor();
//            progress.setTitle("Status do Ping");
//            progress.setMessage("Atingindo alvos...");
//            progress.show();


            int len;
            if ((len = p.getErrorStream().available()) > 0)
            {
                System.out.println(p.exitValue());
                byte[] buf = new byte[len];
                p.getErrorStream().read(buf);
                System.out.println("Command error:\t\"" + new String(buf) + "\"");
                publishProgress(new String(buf));
            }

            InputStream input = p.getInputStream();
            System.out.println(input.read());
            BufferedReader in = new BufferedReader(new InputStreamReader(input));

            StringBuffer buffer = new StringBuffer();
            String line = "aaa";
            publishProgress(line);
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
                System.out.println(line);
                publishProgress(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        String bufferStr = values[0].toString();
        String lastString = resultText.getText().toString();
        resultText.setText(lastString + "\n" + bufferStr);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        progress.setMessage("Finalizado.");
//        progress.dismiss();
        super.onPostExecute(aVoid);
    }
}
