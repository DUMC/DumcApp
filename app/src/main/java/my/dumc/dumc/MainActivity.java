package my.dumc.dumc;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.InetAddress;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText username;
    EditText password;
    Button loginButton;
    Integer id;
    AlertDialog.Builder builder;
    AlertDialog alert;
    ProgressDialog asyncDialog;

    CustomCurl customCurl;
    XMLParser xmlParser;

    private TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues()
    {
        loginButton = (Button)findViewById(R.id.buttonLogin);

        String s1 = username.getText().toString();
        String s2 = password.getText().toString();

        if(s1.equals("")|| s2.equals(""))
        {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        asyncDialog = new ProgressDialog(MainActivity.this);

        asyncDialog.setTitle("Logging In");
        asyncDialog.setMessage("Just a moment...");

        username = (EditText)findViewById(R.id.editUsername);
        password = (EditText)findViewById(R.id.editPassword);

        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();

        loginButton.setOnClickListener(this);
    }

    public boolean isInternetAvailable()
    {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void onClick(View v)
    {
        id = null;
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        RunHttpRequests runHttpRequests = new RunHttpRequests();
        runHttpRequests.execute(usernameString, passwordString);

        while(id == null)
        {
            //show progress bar
            asyncDialog.show();
        }

        if(id != 0)
        {
            username.setText("");
            password.setText("");
            asyncDialog.dismiss();
            Toast.makeText(this,"Success!",Toast.LENGTH_LONG).show();
        }
        else
        {
            //throws error
            username.setText("");
            password.setText("");
            builder.setMessage("Wrong username and / or password");
            builder.setTitle("Notice");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            alert = builder.create();
            asyncDialog.dismiss();
            alert.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void makeRequest(String username, String password) throws IOException, ParserConfigurationException, SAXException
    {
        customCurl = new CustomCurl(getString(R.string.individual_id_from_login_password_url));
        String response = customCurl.getIDFromLogin(username,password);
        xmlParser = new XMLParser(response,null,null);
        xmlParser.writeToFile(getString(R.string.individual_id_from_login_password),this);
        id = Integer.parseInt(xmlParser.getXMLValues("id"));
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
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
