package in.mycitycart.com.mycitycart.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.activties.Home;
import in.mycitycart.com.mycitycart.networks.VollyConnection;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.*;
import static android.app.Activity.RESULT_OK;
import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.*;
public class Fragment_LogInForm extends Fragment implements View.OnClickListener  {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private EditText uemail;
    private EditText upassword;
    private Button loginfb,loginGoogle;
    private Button login;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private String getemail;
    private String getpassword;
    private String getname;
    private String getProfilePic;
    private String uid=null;
    private ProgressDialog pd=null;
    private static int RC_SIGH_IN=0;
    private static String TAG="Main Activity";
    private GoogleApiClient mGoogleAppClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListnear;


    private String mParam2;
    public Fragment_LogInForm() {
        // Required empty public constructor
    }
    public static Fragment_LogInForm newInstance(String param1, String param2) {
        Fragment_LogInForm fragment = new Fragment_LogInForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login_form, viewGroup, false);
            uemail=(EditText)view.findViewById(R.id.et_login_email);
            upassword=(EditText)view.findViewById(R.id.et_login_password);
            login =(Button)view.findViewById(R.id.b_button_login);
        loginGoogle=(Button)view.findViewById(R.id.sign_in_button);
            loginfb=(Button)view.findViewById(R.id.b_log_in_buttonfb);
            vollyConnection= VollyConnection.getsInstance();
            requestQueue=VollyConnection.getsInstance().getRequestQueue();
            mAuth=FirebaseAuth.getInstance();
            //googleLoginSetup();
            pd=new ProgressDialog(getActivity());
            view.findViewById(R.id.sign_in_button).setOnClickListener(this);
            login.setOnClickListener(this);
        loginGoogle.setOnClickListener(this);
            loginfb.setOnClickListener(this);
            return view;
        }
    public void VollyRequest(final String email,final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("JSONOBJECTLOGIN", obj.toString());

                        } catch (Throwable t) {
                            Log.e("JSONBOJECTLOGIN", "Could not parse malformed JSON: \"" + response + "\"");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();

                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    public void VollyRequestForGmailLogin(final String email,final String type,final String photo,final String name,final String uid){
        Toast.makeText(getActivity(),"check for db",Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_GLOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponceGmailLogin(obj,email,type, photo,uid,name);
                            Log.d("JSONOBJECTGmailLOGIN", obj.toString());

                        } catch (Throwable t) {
                            Log.d("JSONBOJECTGmailLOGIN", "Could not parse malformed JSON: \"" + response + "\"");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JSONOBJECTGmailLOGIN", error.toString());
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("type",type);
                params.put("name",name);
                params.put("photo",photo);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    private void parseJSONResponce(JSONObject response) {

        if(response==null || response.length()==0){
            return;
        }
        String result=null;
        String name=null,email=null,password=null,uid=null,photo=null,type=null;
        JSONObject data;
        try{

            Log.d("JSONarray",response.toString());
            for(int i=0;i<=0;i++){
                result=response.getString("success").toString();
                data=response.getJSONObject("data");
                uid=data.getString("id");
                email=data.getString("email");
                name=data.getString("name");
                password=getpassword;
                photo=data.getString("photo");
                type=data.getString("type");
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                Log.d("jsonData",result+uid);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(result.equals("true")){
            setdate(email,name,uid,photo,"true",type,password);
            pd.dismiss();

        }else {
            setdate(email,name,uid,photo,"false",type,password);
            pd.dismiss();
            Toast.makeText(getActivity(),"wrong email or password",Toast.LENGTH_LONG).show();
        }
    }
    private void parseJSONResponceGmailLogin(JSONObject response,final String email,final String type,final String photo,String uid,String name) {
        if(response==null || response.length()==0){
            return;
        }
        String result=null;
        JSONObject data;
        try{

            //Log.d("JSONarray",response.toString());
                result=response.getString("success");
                data=response.getJSONObject("data");
                uid=data.getString("id");
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                Log.d("jsonData",result);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(result.equals("true")){
            setdate(email,name,uid,photo,"true",type,null);
        }else {
            setdate(email,name,uid,photo,"false",type,null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGH_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(resultCode==RESULT_OK){
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Log.d("AUTH","userLogout in"+user.getEmail());
                    Log.d("AUTH","userLogout in"+user.getDisplayName());
                    Log.d("AUTH","userLogout in"+user.getPhotoUrl());
                    Log.d("AUTH","userLogout in"+user.getUid());
                    getemail=user.getEmail();
                    uid=user.getUid();
                    getname=user.getDisplayName();
                    Uri uri=user.getPhotoUrl();
                    getProfilePic=uri.toString();
                    Log.d("CHECK LOGIN TYPE",user.getProviders().toString());
                    if(user.getProviders().toString().equals("[google.com]")){
                        VollyRequestForGmailLogin(getemail,"GOOGLE",getProfilePic,getname,uid);
                    }
                    else if(user.getProviders().toString().equals("[facebook.com]")){
                        VollyRequestForGmailLogin(getemail,"FB",getProfilePic,getname,uid);

                    }
                }
            }else
            Toast.makeText(getActivity(),"can't loged in try again",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_button_login:
                getemail=uemail.getText().toString();
                getpassword=upassword.getText().toString();
                if(getemail!=null && getpassword!=null ){
                pd = ProgressDialog.show(getActivity(), "Login with Ushopper.in", "wait...");
                    pd.setCancelable(true);
                VollyRequest(getemail,getpassword);
                }else
                Toast.makeText(getActivity(),"email and password not be empty",Toast.LENGTH_LONG).show();
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.b_log_in_buttonfb:
                signIn();
                break;
        }
    }
    public void signIn(){
        if(mAuth.getCurrentUser()!=null){
            FirebaseUser user=mAuth.getCurrentUser();
            if(user!=null){
                Log.d("AUTH","userLogout in "+user.getEmail());
                Log.d("AUTH","userLogout in "+user.getDisplayName());
                Log.d("AUTH","userLogout in "+user.getPhotoUrl());
                Log.d("AUTH","userLogout in "+user.getUid());
                Log.d("AUTH","userLogout in "+user.getProviderId());
                getemail=user.getEmail();
                uid=user.getUid();
                getname=user.getDisplayName();
                Uri uri=user.getPhotoUrl();
                getProfilePic=uri.toString();
                if(user.getProviders().toString().equals("[google.com]")){
                    VollyRequestForGmailLogin(getemail,"GOOGLE",getProfilePic,getname,uid);
                }
                else if(user.getProviders().toString().equals("[facebook.com]")){
                    VollyRequestForGmailLogin(getemail,"FB",getProfilePic,getname,uid);
                }
            }
        }
        else {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGH_IN);
        }
    }
    public void setdate(String email,String name ,String uid,String profilepic,String login,String type,String password){
        if(login=="true"){
        saveToPref(getActivity(),KEY_EMAIL,email);
        saveToPref(getActivity(),KEY_UID,uid);
        saveToPref(getActivity(),KEY_PROFILE_PIC,profilepic);
        saveToPref(getActivity(),KEY_LOGIN,login);
        saveToPref(getActivity(),KEY_LOGIN_TYPE,type);
        saveToPref(getActivity(),KEY_NAME,name);
            getActivity().finish();
            Toast.makeText(getActivity(),"you loged in",Toast.LENGTH_LONG).show();
            Intent go=new Intent(getActivity(),Home.class);
            startActivity(go);
        }else{
            saveToPref(getActivity(),KEY_EMAIL,"not giving");
            saveToPref(getActivity(),KEY_UID,"");
            saveToPref(getActivity(),KEY_PROFILE_PIC,null);
            saveToPref(getActivity(),KEY_LOGIN,login);
            saveToPref(getActivity(),KEY_LOGIN_TYPE,"");
            saveToPref(getActivity(),KEY_NAME,"not giving you need to login");
            Toast.makeText(getActivity(),"cant log you in",Toast.LENGTH_LONG).show();
        }
    }
}