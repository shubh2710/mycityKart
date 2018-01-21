package in.mycitycart.com.mycitycart.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.activties.Home;
import in.mycitycart.com.mycitycart.networks.VollyConnection;
import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.*;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.*;

public class Fragment_RegistrationForm extends Fragment {
    private EditText UserEmail;
    private EditText UserReEneterEmail;
    private EditText UserPassword;
    private EditText UserReEnterPassword;
    private Button SignInButton;
    private String Email;
    private String ReEmail;
    private String Password;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private String RePassword;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ProgressDialog pd=null;
    public Fragment_RegistrationForm() {
        // Required empty public constructor
    }
    public static Fragment_RegistrationForm newInstance(String param1, String param2) {
        Fragment_RegistrationForm fragment = new Fragment_RegistrationForm();
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
            View view = inflater.inflate(R.layout.fragment_registration_form, viewGroup, false);
            UserEmail =(EditText)view.findViewById(R.id.et_signIn_user_email);
            UserReEneterEmail=(EditText)view.findViewById(R.id.et_signIn_user_reEnteremail);
            UserPassword=(EditText)view.findViewById(R.id.et_signIn_user_password);
            UserReEnterPassword=(EditText)view.findViewById(R.id.et_signIn_user_Reenter_password);
            SignInButton=(Button)view.findViewById(R.id.bSignIn_user_SignInbutton);
            pd=new ProgressDialog(getActivity());
            vollyConnection= VollyConnection.getsInstance();
            requestQueue=VollyConnection.getsInstance().getRequestQueue();
            SignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Email=UserEmail.getText().toString().trim();
                    ReEmail=UserReEneterEmail.getText().toString().trim();
                    Password=UserPassword.getText().toString().trim();
                    RePassword=UserReEnterPassword.getText().toString().trim();
                    if(Email.equals(ReEmail)){
                        if(Password.equals(RePassword)){
                            pd = ProgressDialog.show(getActivity(), "Sign in with Ushopper.in", "wait...");
                            VollyRequest(Email,Password,RePassword);
                        }else {
                            Toast.makeText(getActivity(),"password is Not Matching",Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(getActivity(),"Email is Not Matching",Toast.LENGTH_LONG).show();
                    }
                }
            });
            return view;
        }
    public void VollyRequest(final String email,final String password, final String re_password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_RESGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj);
                            Log.d("JSONOBJECT", obj.toString());

                        } catch (Throwable t) {
                            Log.e("JSONBOJECT", "Could not parse malformed JSON: \"" + response + "\"");
                            pd.dismiss();
                        }
                        Log.d("USERERROR",response);
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
                params.put("re_password", re_password);
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
        String uid=null;
        try{

            Log.d("JSONarray",response.toString());
            for(int i=0;i<response.length();i++){
                result=response.getString("success").toString();
                uid=response.getString("uid").toString();
                Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                Log.d("jsonData",result+uid);
            }
        }catch (JSONException e){
            e.printStackTrace();
            pd.dismiss();
        }
        if(result.equals("true")){
            setdate(Email,"",uid,"","true","SELF",Password);
            pd.dismiss();
        }else {
            setdate(Email,"",uid,"","false","SELF",Password);
            pd.dismiss();
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