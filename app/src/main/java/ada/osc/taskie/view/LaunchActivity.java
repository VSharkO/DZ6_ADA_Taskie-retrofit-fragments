package ada.osc.taskie.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ada.osc.taskie.R;

public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //if user enters to TasksActivity, that means that hes registered, and we save that inf to
        //shared prefs(in onCreate of TaskActivity), then here we check if that is true and based
        //on that, we know witch activity to start.
        SharedPreferences prefs = this.getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);

        if(prefs.getBoolean(getString(R.string.isRegisteredKey),false)){
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);

        }else{
            Intent intent = new Intent(this, RegisterActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
