package hippo.One;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;


public class GameBoard extends Activity {
    /** Called when the activity is first created. 
     * @return */
    Random rr = new Random();
    Scenario scenario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scenario);
        
        
      /////////////////POST FOR CAVEATS////////////////
        Post npost = new Post();
        // Mess with the array lists returned based on previous selections
        //
        //sends query to Post Class
        npost.query = "select * from sampleScenCav";
        
        //ArrayList<ArrayList> temp = postit("SELECT id from sampleScenCav where id >1");
        //Log.d("temp is", temp.toString());
        
        //Starts Post  calls start in Post clss which gets the postit thread running
        npost.start();
        
        //keeps the post thread running
        while (npost.results == null);
        
        
        //log results
        Log.d("caveate data base", npost.results.toString());
        

        
    
        
                                       ///////// Buttons and TextViews ////////////
        //Back Button to go back to home screen 
        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        
        //Button to choose Prompt 1
        final Button button1 = (Button) findViewById(R.id.button1);
        //Scenario.question1 =  (String) Post.results.get(0).get(2);
        button1.setText(Scenario.question1);
        
        
        //Button to choose Prompt 2
        final Button button2 = (Button) findViewById(R.id.button2);
        //Scenario.question2 =  (String) Post.results.get(0).get(3);
        button2.setText(Scenario.question2);
        
        //Placeholder for Caveates
        final TextView prompt1 = (TextView) findViewById(R.id.prompt1);
        //final TextView whatif = (TextView) findViewById(R.id.whatif);
        
        //Button to add own Caveat.  Inflates an Alert Dialog Box with a Text Editor
        final Button buttonAdd = (Button) findViewById(R.id.addCaveat);

        //Button to see prev Decisions
        final Button myChoice = (Button) findViewById(R.id.choices);
        
        
        
        //////////Getting info from Post into a usable format/////////////
        //get array row length for for loop
        int arrayLength = (int) npost.results.size();
        Log.d("made it past array length", Scenario.question1);
        //populate lists goodA, badA, etc
        for(int i =0; i < arrayLength; i++){
                ArrayList t = npost.results.get(i);
                Long res = (Long)  t.get(2) ;//COLUMN 2 is type ie goodA
                int ress = (int) (res*1);
                switch(ress){
                case 1: Scenario.goodA.add(npost.results.get(i).get(4).toString());
                        break;
                case 2: Scenario.badA.add(npost.results.get(i).get(4).toString());
                        break;
                case 3: Scenario.goodB.add(npost.results.get(i).get(4).toString());
                        break;
                case 4: Scenario.badB.add(npost.results.get(i).get(4).toString());
                        break;
                case 5: Scenario.gen.add(npost.results.get(i).get(4).toString());
                        break;
                } 
   }         
        //Log.d("made it past switch", Scenario.question1);
    
            
        
        
        ///////////////Wiring Buttons/Defining GamePlay////////////////////
    //Goes back to main Activity
    buttonBack.setOnClickListener(new View.OnClickListener(){
       public void onClick(View view) {
           Intent intent = new Intent();
           setResult(RESULT_OK,intent);
           finish();
       }
    });
    
    //Goes to MyPlay
    myChoice.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)
        {
            Intent intent = new Intent(getApplicationContext(),MyPlay.class);
            //putExtra("mylist", listname);
            startActivity(intent);
        }
    });
    
    
    
    
    ////////////////////////////////  GAME PLAY   ////////////////////////////////////
    
    //Chooses Prompt 1, Animates Button1 and Button 2 to bottom of parent View, and brings up Caveat
    button1.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)
        {   
            
            //Checks to make sure there are caveats left
            if(Scenario.badA.isEmpty() && Scenario.goodB.isEmpty() && Scenario.gen.isEmpty() == true){
                ////Get new Scenario
            }
          
            //Picks a random element from the Union of badA, gen, and goodA if all non-empty
            else if((Scenario.badA.isEmpty() == false) && 
                     Scenario.goodB.isEmpty() == false && (Scenario.gen.isEmpty()==false)){
                //Choose between 1 2 and 3 to pick list, badA for 1, goodB for 2, gen for 3
                 int wlA = rr.nextInt(4-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.badA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.badA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.badA.get(lst).toString());
                        ////////////////////Trying to get the what if into a list
                      //Remove the caveat from the badA list
                        Scenario.badA.remove(lst);
                break;
                case 2: int lstb = rr.nextInt(Scenario.goodB.size()-0)+0;
                        prompt1.setText(Scenario.goodB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.goodB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        //MyPlay.mylist.add(Scenario.goodB.get(lstb).toString());
                        Scenario.goodB.remove(lstb);
                break;
                case 3: int lstg = rr.nextInt(Scenario.gen.size()-0)-0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        //MyPlay.mylist.add(Scenario.gen.get(lstg).toString());
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            ////////Switch when gen is empty but goodB and badA have elements
            else if(Scenario.badA.isEmpty() == false && Scenario.goodB.isEmpty()==false){
                //Choose between 1 2  to pick list, badA for 1, goodB for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.badA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.badA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.badA.get(lst).toString());
                        //Remove the caveat from the badA list
                        Scenario.badA.remove(lst);
                break;
                case 2: int lstb = rr.nextInt(Scenario.goodB.size()-0)+0;
                        prompt1.setText(Scenario.goodB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.goodB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        Scenario.goodB.remove(lstb);
                break;
                }
            }
           ///////////Switch when badA and gen have elements, but goodB doesn't
            else if(Scenario.badA.isEmpty() ==false && Scenario.gen.isEmpty()==false){
                //Choose between 1 2 to pick list, badA for 1, gen for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.badA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.badA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.badA.get(lst).toString());
                        //Remove the caveat from the badA list
                        Scenario.badA.remove(lst);
                break;
                case 2: int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            /////If good B and gen are non empty
            else if(Scenario.goodB.isEmpty() ==false && Scenario.gen.isEmpty()==false){
                //Choose between 1 2 to pick list, badA for 1, gen for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: int lstb = rr.nextInt(Scenario.goodB.size()-0)+0;
                        prompt1.setText(Scenario.goodB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.goodB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        Scenario.goodB.remove(lstb);
                break;
                case 2: int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            //if badA, gen has no elements, pull from goodB if goodB has elements
            else if(Scenario.badA.isEmpty()==true && Scenario.gen.isEmpty()==true){
                int lstb = rr.nextInt(Scenario.goodB.size()-0)+0;
                prompt1.setText(Scenario.goodB.get(lstb).toString());
                //Add caveat to the used list
                Scenario.used.add(Scenario.goodB.get(lstb).toString());
                //Remove the caveat from the goodB list
                Scenario.goodB.remove(lstb);
            }
            //if goodB and gen has no elements, pull from badA
            else if(Scenario.goodB.isEmpty()== true && Scenario.gen.isEmpty()== true){
                //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                int lst = rr.nextInt(Scenario.badA.size()-0)+0;
                //display the random caveat
                prompt1.setText(Scenario.badA.get(lst).toString());
                //Add the caveat to the used list
                Scenario.used.add(Scenario.badA.get(lst).toString());
                //Remove the caveat from the badA list
                Scenario.badA.remove(lst);
            }
            //Pull from gen
            else if(Scenario.badA.isEmpty()==true && Scenario.goodB.isEmpty()==true){
                int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                prompt1.setText(Scenario.gen.get(lstg).toString());
                //Add caveat to the used list
                Scenario.used.add(Scenario.gen.get(lstg).toString());
                //Remove the caveat from the gen list
                Scenario.gen.remove(lstg);
            }
            
            //whatif.setText("What if...");
        }       
        
    });
    
    
   
    
  //Chooses Prompt 2,  and brings up Caveat
    button2.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)
        {
          //Checks to make sure there are caveats left
            if(Scenario.goodA.isEmpty() && Scenario.badB.isEmpty() && Scenario.gen.isEmpty() == true){
                ////Get new Scenario
            }
            //Picks a random element from the Union of badA, gen, and goodA if all non-empty
            else if((Scenario.goodA.isEmpty() == false) && 
                     Scenario.badB.isEmpty() == false && (Scenario.gen.isEmpty()==false)){
                //Choose between 1 2 and 3 to pick list, badA for 1, goodB for 2, gen for 3
                 int wlA = rr.nextInt(4-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.goodA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.goodA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.goodA.get(lst).toString());
                        //Remove the caveat from the badA list
                        Scenario.goodA.remove(lst);
                break;
                case 2: int lstb = rr.nextInt(Scenario.badB.size()-0)+0;
                        prompt1.setText(Scenario.badB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.badB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        Scenario.badB.remove(lstb);
                break;
                case 3: int lstg = rr.nextInt(Scenario.gen.size()-0)-0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            ////////Switch when gen is empty but goodB and badA have elements
            else if(Scenario.goodA.isEmpty() == false && Scenario.badB.isEmpty()==false){
                //Choose between 1 2  to pick list, badA for 1, goodB for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.goodA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.goodA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.goodA.get(lst).toString());
                        //Remove the caveat from the badA list
                        Scenario.goodA.remove(lst);
                break;
                case 2: int lstb = rr.nextInt(Scenario.badB.size()-0)+0;
                        prompt1.setText(Scenario.badB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.badB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        Scenario.badB.remove(lstb);
                break;
                }
            }
           ///////////Switch when badA and gen have elements, but goodB doesn't
            else if(Scenario.goodA.isEmpty() ==false && Scenario.gen.isEmpty()==false){
                //Choose between 1 2 to pick list, badA for 1, gen for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                        int lst = rr.nextInt(Scenario.goodA.size()-0)+0;
                        //display the random caveat
                        prompt1.setText(Scenario.goodA.get(lst).toString());
                        //Add the caveat to the used list
                        Scenario.used.add(Scenario.goodA.get(lst).toString());
                        //Remove the caveat from the badA list
                        Scenario.goodA.remove(lst);
                break;
                case 2: int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            /////If good B and gen are non empty
            else if(Scenario.badB.isEmpty() ==false && Scenario.gen.isEmpty()==false){
                //Choose between 1 2 to pick list, badA for 1, gen for 2
                int wlA = rr.nextInt(3-1)+1;
                ///////Switch to get element from specified list when all lists non empty, then do stuff
                switch(wlA){
                case 1: int lstb = rr.nextInt(Scenario.badB.size()-0)+0;
                        prompt1.setText(Scenario.badB.get(lstb).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.badB.get(lstb).toString());
                        //Remove the caveat from the goodB list
                        Scenario.badB.remove(lstb);
                break;
                case 2: int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                        prompt1.setText(Scenario.gen.get(lstg).toString());
                        //Add caveat to the used list
                        Scenario.used.add(Scenario.gen.get(lstg).toString());
                        //Remove the caveat from the goodB list
                        Scenario.gen.remove(lstg);
                break;
                }
            }
            //if badA, gen has no elements, pull from goodB if goodB has elements
            else if(Scenario.goodA.isEmpty()==true && Scenario.gen.isEmpty()==true){
                int lstb = rr.nextInt(Scenario.badB.size()-0)+0;
                prompt1.setText(Scenario.badB.get(lstb).toString());
                //Add caveat to the used list
                Scenario.used.add(Scenario.badB.get(lstb).toString());
                //Remove the caveat from the goodB list
                Scenario.badB.remove(lstb);
            }
            //if goodB and gen has no elements, pull from badA
            else if(Scenario.badB.isEmpty()== true && Scenario.gen.isEmpty()== true){
                //gets a random number with the size as the max exclusive (size is 1 bigger than last element number so good) and 0 inclusive
                int lst = rr.nextInt(Scenario.goodA.size()-0)+0;
                //display the random caveat
                prompt1.setText(Scenario.goodA.get(lst).toString());
                //Add the caveat to the used list
                Scenario.used.add(Scenario.goodA.get(lst).toString());
                //Remove the caveat from the badA list
                Scenario.goodA.remove(lst);
            }
            //Pull from gen
            else if(Scenario.goodA.isEmpty()==true && Scenario.goodB.isEmpty()==true){
                int lstg = rr.nextInt(Scenario.gen.size()-0)+0;
                prompt1.setText(Scenario.gen.get(lstg).toString());
                //Add caveat to the used list
                Scenario.used.add(Scenario.gen.get(lstg).toString());
                //Remove the caveat from the gen list
                Scenario.gen.remove(lstg);
            }
            //whatif.setText("What if...");
        }
    });
    
    
    
    //Add Caveat Button, Lets User Add A Caveat to the Scenario
    buttonAdd.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v)
        {
            // Making Alert Dialog Box and sticking the spinner xml stuff in it.  Didn't realize this but
            // Alert Dialog will let you put views in it then build the cancel and ok buttons below.
            AlertDialog.Builder alert = new AlertDialog.Builder(GameBoard.this);
            View view = getLayoutInflater().inflate(R.layout.spinner1,null);
            alert.setView(view);
            
            //Prompt 
            //alert.setTitle("Add ");
            // Set an EditText view to get user input 
            final EditText input = (EditText) findViewById(R.id.input);//new EditText(GameBoard.this);


            //Setting the Submit Button
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              String value = input.getText().toString();
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
    
    
    
   
    
    }
   
}  
