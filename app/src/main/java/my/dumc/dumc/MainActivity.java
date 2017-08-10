package my.dumc.dumc;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button)findViewById(R.id.buttonLogin);
        username = (EditText)findViewById(R.id.editUsername);
        password = (EditText)findViewById(R.id.editPassword);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        //Toast.makeText(MainActivity.this, "This is " + usernameString + " Using " + passwordString, Toast.LENGTH_SHORT).show();

        RunHttpRequests runHttpRequests = new RunHttpRequests();
        runHttpRequests.execute(usernameString, passwordString);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void makeRequest(String username, String password) throws IOException
    {
        /*CustomCurl customCurl = new CustomCurl(getString(R.string.individual_id_from_login_password));
        String response = customCurl.getIDFromLogin(username,password);*/
        MultipartUtility multipartUtility = new MultipartUtility(getString(R.string.individual_id_from_login_password));
        multipartUtility.addFormField("login",username);
        multipartUtility.addFormField("password",password);
        byte[] bytes = multipartUtility.finish();
        String response = new String(bytes, StandardCharsets.UTF_8);
        Log.v("PRINT RESPONSE","Response");
        printResponse(response);
    }

    public void printResponse(String response)
    {
        System.out.println(response);
    }

    private class RunHttpRequests extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] params)
        {
            try {
                makeRequest(params[0].toString(), params[1].toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
