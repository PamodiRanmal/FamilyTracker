package com.example.familytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karan.churi.PermissionManager.PermissionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText e1 , e2;
    FirebaseUser user;
    TextView resetpw;
    Button testgoogle;
    private static final int RC_SIGN_IN=1;
    public GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleBtn;
    PermissionManager manager;
    public static  GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListner;
    public boolean res;
    DatabaseReference reference, userreference;
    LoginButton loginButton;

  String name, email, password, date, isSharing, code;
  Uri imageUri;
  TextView t1;
  //FirebaseAuth auth;
 // FirebaseUser user;
  //DatabaseReference reference;
  ProgressDialog progressDialog;
  String userId;
  StorageReference storageReference;
  CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


      mAuthListner = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

          if(firebaseAuth.getCurrentUser()!=null){

            startActivity(new Intent(LoginActivity.this,UserLocationMainActivity.class));

          }
          else {
          // setContentView(R.layout.activity_login);
            manager = new PermissionManager() {};
            manager.checkAndRequestPermissions(LoginActivity.this);

          }

        }
      };

        resetpw=(TextView)findViewById(R.id.password_reset);
        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.password);
        //testgoogle=findViewById(R.id.googlesignin2);
        mGoogleBtn=findViewById(R.id.googlesignin);
        auth = FirebaseAuth.getInstance();
            //FacebookSdk.getApplicationContext();
       FacebookSdk.sdkInitialize(getApplicationContext());
         loginButton=(LoginButton) findViewById(R.id.fb_login_button);
      loginButton.setReadPermissions(Arrays.asList("email"));

         callbackManager=CallbackManager.Factory.create();

       ////




     // auth = FirebaseAuth.getInstance();

//      auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        if(user == null){
//         setContentView(R.layout.activity_login);
//          manager = new PermissionManager() {};
//          manager.checkAndRequestPermissions(this);
//        }else{
//          Intent intent = new Intent(LoginActivity.this,UserLocationMainActivity.class);
//          startActivity(intent);
//          finish();
//        }

      // Configure Google Sign In
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         mGoogleApiClient =new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
         @Override
         public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

          Toast.makeText(LoginActivity.this, "An error has occoured", Toast.LENGTH_LONG).show();


            }
            }).addApi(Auth.GOOGLE_SIGN_IN_API,gso ).build();


        //auth = FirebaseAuth.getInstance();

mGoogleBtn.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {

    googlesignIn();
  }
});

loginButton.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View v) {

    fbsignIn();
  }
});

//      testgoogle.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//          googlesignIn();
//        }
//      });
    }


    @Override
    protected  void onStart() {

      super.onStart();

      auth.addAuthStateListener(mAuthListner);

    }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    manager.checkResult(requestCode,permissions,grantResults);
    ArrayList<String> denied_permissions = manager.getStatus().get(0).denied;
    if(denied_permissions.isEmpty()){
      Toast.makeText(getApplicationContext(),"Permissions enabled",Toast.LENGTH_SHORT).show();        }
  }


    public void login(View v) {
      e1 = (EditText) findViewById(R.id.email);
      e2 = (EditText) findViewById(R.id.password);



      if ((e1.getText().toString().equals(null)) || (e2.getText().toString().equals(null)) || (e2.getText().length()<6) ) {

        Toast.makeText(getApplicationContext(), "Enter valid credentials", Toast.LENGTH_LONG).show();

      }


      else {
        auth.signInWithEmailAndPassword(e1.getText().toString(), e2.getText().toString())
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                //Toast.makeText(getApplicationContext(),"User Logged in Successfully",Toast.LENGTH_LONG).show();

                FirebaseUser user = auth.getCurrentUser();
                if (user.isEmailVerified()) {
                  Intent intent = new Intent(LoginActivity.this, UserLocationMainActivity.class);
                  startActivity(intent);
                  finish();

                } else {
                  Toast.makeText(getApplicationContext(), "Email is not verified yet.", Toast.LENGTH_SHORT).show();
                }
              } else {
                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
              }
            }
          });
      }
    }

  public void signUp(View v){
    Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
    startActivity(intent);
  }

  public void resetpw(View v){
    startActivity(new Intent(LoginActivity.this,password_reset.class));

  }


  public void fbsignIn(){


    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {


        handleFacebookToken(loginResult.getAccessToken());


      }

      @Override
      public void onCancel() {

        Toast.makeText(getApplicationContext(),"User cancelled authenication" , Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onError(FacebookException error) {
        Toast.makeText(getApplicationContext(),error.getMessage() , Toast.LENGTH_SHORT).show();

      }
    });

  }

  private void handleFacebookToken(AccessToken accessToken) {

      AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
     auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
      public void onComplete(@NonNull Task<AuthResult> task) {

      if(task.isSuccessful()) {
       // FirebaseUser myuserobj = auth.getCurrentUser();
        Intent intent = new Intent(LoginActivity.this, UserLocationMainActivity.class);
        startActivity(intent);
        //finish();

      }

      else {

          Toast.makeText(getApplicationContext(),"Could not authenicate" , Toast.LENGTH_SHORT).show();
      }

    }
  });


    }


  public void googlesignIn() {
    //mGoogleBtn=findViewById(R.id.googlesignin);

    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        //Log.w(TAG, "Google sign in failed", e);
        // ...
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

    //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    auth.signInWithCredential(credential)
      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            //Log.d(TAG, "signInWithCredential:success");
            FirebaseUser user = auth.getCurrentUser();
            Intent intent = new Intent(LoginActivity.this, UserLocationMainActivity.class);
            startActivity(intent);
            finish();

            // updateUI(user);
          } else {
            // If sign in fails, display a message to the user.
           // Log.w(TAG, "signInWithCredential:failure", task.getException());
            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            //updateUI(null);

            Toast.makeText(LoginActivity.this, "Authenication Failed", Toast.LENGTH_LONG).show();
          }

          // ...
        }
      });

  }

public boolean checkuser(String email){

      res=false;
//  auth.fetchProvidersForEmail(email)
//    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
//      @Override
//      public void onComplete(@NonNull Task<ProviderQueryResult> task) {
//        if(task.isSuccessful())
//        {
////          dialog.dismiss();
//          boolean check = !task.getResult().getProviders().isEmpty();
//
//          if(!check)
//          {
//            res=true;
//          }
//          else
//          {
//            res=false;
//          }
//        }
//      }
//    });

//
//  userreference = FirebaseDatabase.getInstance().getReference().child("users");
//  reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("CircleMembers");
//
//  reference.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//      //nameList.clear();
//
//      if(dataSnapshot.exists()){
//        for(DataSnapshot dss : dataSnapshot.getChildren()){
//          email = dss.child("email").getValue(String.class);
//          userreference.child(circleMemberId)
//            .addListenerForSingleValueEvent(new ValueEventListener() {
//              @Override
//              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                createUser = dataSnapshot.getValue(CreateUser.class);
//              //  nameList.add(createUser);
//                adapter.notifyDataSetChanged();
//              }
//
//              @Override
//              public void onCancelled(@NonNull DatabaseError databaseError) {
//
//              }
//            });
//
//        }
//      }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }
//  });


  return res;

  }



  public void registerNewUser(View v) {


    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

    if (acct != null) {
      name = acct.getGivenName()+" "+acct.getFamilyName();;
      email = acct.getEmail();
      password="gl";
      date = acct.getId();
      Uri imageUri= acct.getPhotoUrl();
    }


    Date myDate = new Date();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
    date = format1.format(myDate);

    Random r = new Random();
    int n = 100000 + r.nextInt(900000);
    code = String.valueOf(n);

    progressDialog.setMessage("Please wait while we creating an account for you.");
    progressDialog.show();
    auth.createUserWithEmailAndPassword(email,password)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if(task.isSuccessful()){
            user = auth.getCurrentUser();
            CreateUser createUser = new CreateUser(name,email,password, date,"false",code,imageUri.toString(),null,null,user.getUid());
            user = auth.getCurrentUser();
            userId = user.getUid();

            reference.child(userId).setValue(createUser)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()){
                    StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                    sr.putFile(imageUri)
                      .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                          if(task.isSuccessful()){
                            String download_image_path = task.getResult().getUploadSessionUri().toString();
                            reference.child(user.getUid()).child("imageUri").setValue(download_image_path)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    //Toast.makeText(getApplicationContext(),"Email sent for varification",Toast.LENGTH_SHORT).show();
                                    //finish();
                                    //sendVarificationEmail();
                                    //Intent intent = new Intent(InviteCodeActivity.this,MainActivity.class);
                                    //startActivity(intent);
                                  }else {
                                    Toast.makeText(getApplicationContext(),"An error occured while creating an account",Toast.LENGTH_SHORT).show();
                                  }
                                }
                              });
                          }
                        }
                      });

                  }
                  else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Could not insert values in a database",Toast.LENGTH_SHORT).show();
                  }
                }
              });
          }
        }
      });

  }

  private void signOut() {
    mGoogleSignInClient.signOut()
      .addOnCompleteListener(this, new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          // ...
        }
      });
  }
}
