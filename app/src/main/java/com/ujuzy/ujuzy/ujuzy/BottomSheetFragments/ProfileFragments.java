package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ujuzy.ujuzy.ujuzy.Activities.EditProfileActivity;
import com.ujuzy.ujuzy.ujuzy.Activities.MapsActivity;
import com.ujuzy.ujuzy.ujuzy.Activities.RequestServiceActivity;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmService;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmToken;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmTokenHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUserHelper;
import com.ujuzy.ujuzy.ujuzy.model.Constants;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragments extends Fragment {

    View v;
    private Button upgradeBtn;
    private EditText inputDate;
    private Spinner genderSpinner;
    private ImageView toolBack;
    Calendar myCalender;
    LinearLayout linDate;

    List<String> genderList;
    String selectedGender = "";

    public ProfileFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);


        init();
        initDateDialog();
        initGender();
        initBack();
        //initEditAcc();
        return v;
    }

    private void initBack()
    {
        toolBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void initGender()
    {
        genderSpinner = (Spinner) v.findViewById(R.id.spinner_gender);

        String[] service_category_string = new String[]{
                "Select Gender",
                "Male",
                "Female",
                "Others"
        };

        genderList = new ArrayList<>(Arrays.asList(service_category_string));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_filter_item, genderList) {
            @Override
            public boolean isEnabled(int position)
            {
                if (position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };


        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        genderSpinner.setAdapter(spinnerArrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init()
    {
        inputDate = (EditText) v.findViewById(R.id.inputDate);
        linDate = (LinearLayout) v.findViewById(R.id.linDate);
        toolBack = (ImageView) v.findViewById(R.id.backBtn);
    }

    private void initDateDialog()
    {
        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myCalender = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.YEAR, year);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }
                };

                new DatePickerDialog(getActivity(), date, myCalender
                        .get(Calendar.YEAR), myCalender
                        .get(Calendar.MONTH), myCalender
                        .get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    private void updateLabel()
    {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        inputDate.setText(sdf.format(myCalender.getTime()));
    }


//    private void initEditAcc()
//    {
//        editAcc = (TextView) v.findViewById(R.id.tvEdit);
//        editAcc.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), EditProfileActivity.class));
//            }
//        });
//    }
//
//    private void initUpgrade()
//    {
//        upgradeBtn = (Button) v.findViewById(R.id.upgradeBtn);
//        upgradeBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                initDialog();
//            }
//        });
//    }
//
//    private void initDialog() {
//        Context context = getActivity();
//
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.upgrade_dialog);
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.show();
//
//        TextView openProfessional = (TextView) dialog.findViewById(R.id.options_professional);
//        openProfessional.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    final AuthzModule authzModule = AuthorizationManager
//                            .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
//                            .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
//                            .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
//                            .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
//                            .setAccountId("account")
//                            .setClientId("account")
//                            .setRedirectURL("https://ujuzy.com")
//                            .setScopes(Arrays.asList("openid"))
//                            .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
//                            .asModule();
//
//                    authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>() {
//                        @Override
//                        public void onSuccess(final String data) {
//
//                            //SAVE TOKEN TO REALM DATABASE
//                            RealmToken token = new RealmToken();
//                            token.setToken(data);
//
//
//                            Map<String, String> params= new HashMap<String, String>();
//                            params.put("new_role","professional");
//
//                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
//                                    Constants.HTTP.UPGRADE_PROFILE_JSON_URL, new JSONObject(params),
//                                    new com.android.volley.Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//
//                                            initUserInfo();
//                                            //REFRESH ACTIVITY
//                                            Toast.makeText(getActivity(), "Your account has been upgraded successfully", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    }, new com.android.volley.Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                                }
//
//                            }) {
//
//                                /**
//                                 * Passing some request headers
//                                 **/
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//
//                                    HashMap<String, String> headers = new HashMap<String, String>();
//                                    headers.put("Authorization","Bearer "+ data);
//                                    headers.put("Content-Type", "application/json; charset=utf-8");
//                                    headers.put("Accept","application/json");
//                                    return headers;
//                                }
//
//                            };
//
//                            // Adding request to request queue
//                            requestQueue = Volley.newRequestQueue(getActivity());
//                            requestQueue.add(jsonObjReq);
//
//                            realm = Realm.getDefaultInstance();
//                            RealmTokenHelper helper = new RealmTokenHelper(realm);
//                            helper.save(token);
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                            //authzModule.deleteAccount();
//                        }
//                    });
//
//
//                } catch (MalformedURLException e) {
//                    // e.printStackTrace();
//                }
//                dialog.dismiss();
//            }
//        });
//
//        TextView openCompany = (TextView) dialog.findViewById(R.id.options_company);
//        openCompany.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    final AuthzModule authzModule = AuthorizationManager
//                            .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
//                            .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
//                            .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
//                            .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
//                            .setAccountId("account")
//                            .setClientId("account")
//                            .setRedirectURL("https://ujuzy.com")
//                            .setScopes(Arrays.asList("openid"))
//                            .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
//                            .asModule();
//
//                    authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>() {
//                        @Override
//                        public void onSuccess(final String data) {
//
//                            //SAVE TOKEN TO REALM DATABASE
//                            RealmToken token = new RealmToken();
//                            token.setToken(data);
//
//                            Map<String, String> params= new HashMap<String, String>();
//                            params.put("new_role","company");
//
//                            JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
//                                    Constants.HTTP.UPGRADE_PROFILE_JSON_URL, new JSONObject(params),
//                                    new com.android.volley.Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//
//                                            initUserInfo();
//                                            //REFRESH ACTIVITY
//                                            Toast.makeText(getActivity(), "Your account has been upgraded successfully", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    }, new com.android.volley.Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
//                                }
//
//                            }) {
//
//                                /**
//                                 * Passing some request headers
//                                 * */
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//
//                                    HashMap<String, String> headers = new HashMap<String, String>();
//                                    headers.put("Authorization","Bearer "+ data);
//                                    headers.put("Content-Type", "application/json; charset=utf-8");
//                                    headers.put("Accept","application/json");
//                                    return headers;
//                                }
//
//                            };
//
//                            // Adding request to request queue
//                            requestQueue = Volley.newRequestQueue(getActivity());
//                            requestQueue.add(jsonObjReq);
//
//                            realm = Realm.getDefaultInstance();
//                            RealmTokenHelper helper = new RealmTokenHelper(realm);
//                            helper.save(token);
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                            //authzModule.deleteAccount();
//                        }
//                    });
//
//
//                } catch (MalformedURLException e) {
//                    // e.printStackTrace();
//                }
//                dialog.dismiss();
//            }
//        });
//
//    }
//
//    private void initCancelFragment()
//    {
//        cancelIv = (ImageView) v.findViewById(R.id.backBtn);
//        cancelIv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                /*ProfileFragments profileFragments = new ProfileFragments();
//                getActivity().getSupportFragmentManager();
//                profileFragments.dismiss(getActivity().getSupportFragmentManager(), getActivity().);*/
//            }
//        });
//    }
//
//    private void initAeroGearSignIn() {
//        try {
//
//            final AuthzModule authzModule = AuthorizationManager
//                    .config("KeyCloakAuthz", OAuth2AuthorizationConfiguration.class)
//                    .setBaseURL(new URL(Constants.HTTP.AUTH_BASE_URL))
//                    .setAuthzEndpoint("/auth/realms/ujuzy/protocol/openid-connect/auth")
//                    .setAccessTokenEndpoint("/auth/realms/ujuzy/protocol/openid-connect/token")
//                    .setAccountId("account")
//                    .setClientId("account")
//                    .setRedirectURL("https://ujuzy.com")
//                    .setScopes(Arrays.asList("openid"))
//                    .addAdditionalAuthorizationParam((Pair.create("grant_type", "password")))
//                    .asModule();
//
//            authzModule.requestAccess(getActivity(), new org.jboss.aerogear.android.core.Callback<String>() {
//                @Override
//                public void onSuccess(final String data) {
//
//                    //SAVE TOKEN TO REALM DATABASE
//                    RealmToken token = new RealmToken();
//                    token.setToken(data);
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.HTTP.USER_PROFILE_JSON_URL,
//                            new com.android.volley.Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//
//                                    try {
//
//                                        JSONObject jsonObject = new JSONObject(response);
//
//                                        RealmUser realmService = new RealmUser();
//                                        realmService.setId(jsonObject.getString("id"));
//                                        realmService.setFirstname(jsonObject.getString("firstname"));
//                                        realmService.setLastname(jsonObject.getString("lastname"));
//                                        realmService.setGender(jsonObject.getString("gender"));
//                                        realmService.setEmail(jsonObject.getString("email"));
//                                        realmService.setCreated_at(jsonObject.getString("created_at"));
//                                        realmService.setPhone(jsonObject.getString("phone_number"));
//                                        //realmService.setVerified(jsonObject.getString("phone_number"));
//
//                                        JSONObject jsonUserRole = new JSONObject(response).getJSONObject("user_role");
//                                        realmService.setUserRole(jsonUserRole.getString("role_name"));
//                                        //JSONObject jsonUserProfilePic = new JSONObject(response).getJSONObject("profile_pic");
//                                        //realmService.setProfilePic(jsonUserProfilePic.getString("thumb"));
//
//                                        //SAVE
//                                        realm = Realm.getDefaultInstance();
//                                        RealmUserHelper helper = new RealmUserHelper(realm);
//                                        helper.save(realmService);
//
//                                        initUserInfo();
//
//                                    } catch (JSONException e) {
//                                        //e.printStackTrace();
//                                    }
//
//                                }
//                            }, new com.android.volley.Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            authzModule.deleteAccount();
//
//                        }
//                    }) {
//
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Authorization", "Bearer " + data);
//                            params.put("Content-Type", "application/json");
//                            params.put("Accept", "application/json");
//                            return params;
//                        }
//                    };
//
//                    requestQueue = Volley.newRequestQueue(getActivity());
//                    requestQueue.add(stringRequest);
//
//                    realm = Realm.getDefaultInstance();
//                    RealmTokenHelper helper = new RealmTokenHelper(realm);
//                    helper.save(token);
//
//
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                    authzModule.deleteAccount();
//                }
//            });
//
//
//        } catch (MalformedURLException e) {
//            // e.printStackTrace();
//        }
//        // startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
//
//    }
//
//    private void initUserInfo()
//    {
//
//        userName = (TextView) v.findViewById(R.id.tv_user_name);
//        userEmail = (TextView) v.findViewById(R.id.tv_user_email);
//        userPhone = (TextView) v.findViewById(R.id.tv_user_phone);
//        createdAt = (TextView) v.findViewById(R.id.tv_created_at);
//        userRole = (TextView) v.findViewById(R.id.tv_user_role);
//        userFirstName = (TextView) v.findViewById(R.id.tv_user_firstname);
//        userLastName = (TextView) v.findViewById(R.id.tv_user_lastname);
//
//        realm = Realm.getDefaultInstance();
//        RealmUser realmUser = realm.where(RealmUser.class).findFirst();
//        if (realmUser != null) {
//            if (realmUser.getFirstname() != null)
//                userName.setText(realmUser.getFirstname() + " " + realmUser.getLastname());
//            if (realmUser.getEmail() != null)
//                userEmail.setText(realmUser.getEmail());
//            if (realmUser.getPhone() != null)
//                userPhone.setText(realmUser.getPhone());
//            if (realmUser.getCreated_at() != null)
//                createdAt.setText(realmUser.getCreated_at());
//            if (realmUser.getUserRole() != null)
//                userRole.setText(realmUser.getUserRole());
//            if (realmUser.getFirstname() != null)
//                userFirstName.setText(realmUser.getFirstname());
//            if (realmUser.getLastname() != null)
//                userLastName.setText(realmUser.getLastname());
//
//        }
//
//    }
//
//    @Override
//    public void onDestroy()
//    {
//        super.onDestroy();
//        realm = Realm.getDefaultInstance();
//        if (realmChangeListener != null)
//            realm.removeChangeListener(realmChangeListener);
//    }

}
