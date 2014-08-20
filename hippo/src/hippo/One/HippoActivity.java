package hippo.One;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HippoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Create Buttons
        Button buttonCreate = (Button) findViewById(R.id.createHip);
        Button login =(Button) findViewById(R.id.login);
        Button profile = (Button) findViewById(R.id.profile);
        
        
      //Get scenario and questions
        Post npost = new Post();
        //Post mpost = new Post();
        // Mess with the array lists returned based on previous selections
        //
        //sends query to Post Class
        npost.query = "select * from sampleScen";
        //mpost.query = "select *"
        
        //ArrayList<ArrayList> temp = postit("SELECT id from sampleScenCav where id >1");
        //Log.d("temp is", temp.toString());
        
        //Starts Post  calls start in Post clss which gets the postit thread running
        npost.start();
        
        //keeps the post thread running
        while (npost.results == null);
        
        
        //log results
        Log.d("Made Post Call to DB", npost.results.toString());
        
        
        //Populate Scenario with info from sampleScen
        //Scenario.scenId = (Integer) Post.results.get(0).get(0);
        //Scenario.name = (String) Post.results.get(0).get(1).toString();
        Scenario.question1 = (String) npost.results.get(0).get(2);
        Scenario.question2 = (String) npost.results.get(0).get(3);
        //Scenario.category = (String) Post.results.get(0).get(4);
        
        Log.d("Successfully instantiated Question1 and Question2", Scenario.question1.toString());
        
        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),GameBoard.class);
                startActivity(intent);
            }
        });
        
        
      //Create Hippo Button, Lets User Add A Caveat to the Scenario
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // Making Alert Dialog Box and sticking the spinner xml stuff in it.  Didn't realize this but
                // Alert Dialog will let you put views in it then build the cancel and ok buttons below.
                AlertDialog.Builder alert = new AlertDialog.Builder(HippoActivity.this);
                View view = getLayoutInflater().inflate(R.layout.spinner2,null);
                alert.setView(view);
                
                //Prompt 
                //alert.setTitle("Add ");
                // Set an EditText view to get user input 
                final EditText choice1 = (EditText) findViewById(R.id.choice1);//new EditText(GameBoard.this);


                //Setting the Submit Button
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  String value = choice1.getText().toString();
                  Post npost = new Post();
                  // Mess with the array lists returned based on previous selections
                  //
                  //sends query to Post Class
                  npost.query = value; //("+ Scenario.scenId + ",1," + input +",0) Values(1,1, /value, 0);" ;
                  
                  //Starts Post  calls start in Post clss which gets the postit thread running
                  npost.start();
                  
                  //keeps the post thread running
                  while (npost.results == null);
                  
                  //log results
                  Log.d("caveate data base", npost.results.toString());
                  }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                  }
                });

                alert.show();
            }
        });
        
        
        //Login/signup
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                // Making Alert Dialog Box and sticking the spinner xml stuff in it.  Didn't realize this but
                // Alert Dialog will let you put views in it then build the cancel and ok buttons below.
                AlertDialog.Builder alert = new AlertDialog.Builder(HippoActivity.this);
                View view = getLayoutInflater().inflate(R.layout.login,null);
                alert.setView(view);
                
                //Prompt 
                //alert.setTitle("Add ");
                // Set an EditText view to get user input 
                final EditText usName = (EditText) findViewById(R.id.userName);//new EditText(GameBoard.this);
                final EditText pass = (EditText) findViewById(R.id.passWord);//new EditText(GameBoard.this);
               
                

                //Setting the Submit Button
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  
                  final String passPrl = pass.getText().toString();
                  final String userNamePrl = usName.getText().toString();
                  Post npost = new Post();
                  // Mess with the array lists returned based on previous selections
                  //
                  //sends query to Post Class
                  //npost.query = "Select * from user where userName="+userNamePrl "and password = "+passPrl;//+usName"k)"; //("+ Scenario.scenId + ",1," + input +",0) Values(1,1, /value, 0);" ;
                  
                  //Starts Post  calls start in Post clss which gets the postit thread running
                  npost.start();
                  
                  //keeps the post thread running
                  while (npost.results == null);
                  
                  //log results
                  Log.d("caveate data base", npost.results.toString());
                  }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                  }
                });

                alert.show();
            }
        });
        
        
       
        
        
        
    }
}