package my.dumc.dumc;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Aaron David on 8/2/2017.
 */

public class CustomCurl
{
    private final HttpURLConnection connection;

    public CustomCurl(final String urlString) throws IOException
    {
        URL url = new URL(urlString);

        String auth = "dumcapp:JesusLORD0fa11!";
        byte[] authEncByte = Base64.encode(auth.getBytes(), Base64.DEFAULT);
        String authEncString = new String(authEncByte);

        connection = (HttpsURLConnection)(url).openConnection();
        connection.setRequestProperty("Authorization", "Basic " + authEncString);
        connection.setRequestMethod("POST");
    }

    public String getIDFromLogin(String username, String password)
    {
        BufferedReader br = null;
        try
        {
            String params = "login="+username+"&password=" + java.net.URLEncoder.encode(password, "UTF-8");
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);
            writer.flush();
            writer.close();
            os.close();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            final StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line).append("\n");
            }

            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (br != null) br.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
