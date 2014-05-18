package my.app.free.musicloader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity {

    EditText editId;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        editId = (EditText) findViewById(R.id.login_edittext_id);
        editPassword = (EditText) findViewById(R.id.login_edittext_password);

        editId.setText("junghong456@gmail.com");
        editPassword.setText("jung190");

        Button loginBtn = (Button) findViewById(R.id.login_button_signin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = editId.getText().toString();
                final String password = editPassword.getText().toString();

                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Bot4Shared bot = new Bot4Shared(id, password);
                        if (bot.SignIn()) {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        return null;
                    }
                };
                asyncTask.execute();
            }
        });
    }
}



