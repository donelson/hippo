package hippo.One;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyPlay extends Activity {
    public String q1;
    public String q2;
    public List<String> mylist = new ArrayList<String>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myplay);
       
        // Sets the questions for MyPlay
        TextView left = (TextView) findViewById(R.id.q1);
        TextView right = (TextView) findViewById(R.id.q2);
        
        //Set Text for questions
        left.setText(Scenario.question1);
        right.setText(Scenario.question2);
        
        //Get List
        List listPlay = (List) findViewById(R.id.listPlay);
        //
        
        
 
        Button buttonBack = (Button) findViewById(R.id.backToGame);
    
    //Goes back to main Activity
    buttonBack.setOnClickListener(new View.OnClickListener(){
       public void onClick(View v) {
           Intent intent = new Intent();
           setResult(RESULT_OK,intent);
           finish();
       }
    });
    
    
    
    
    }
}