package com.android.example.syncadapterdemo.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bodhidipta on 01/06/17.
 */

public class AccountUtils {
    public static final String ACCOUNT_TYPE = "example.android.com";
    public static final String AUTH_TOKEN_TYPE = "example.android.com.users";

    public static Account getAccount(Context context, String accountName) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }

    public static void signIn(final String userName, final String password, final onGenerateToken call) {
        new AsyncTask<Void,Void,Void>(){
            String token = "";
            String urlPost = "https://www.uaveditor.com/api/secret/profile";
            String passwordObject = "{\n" +
                    "    \"query\": {\n" +
                    "        \"username\": \"" + userName + "\",\n" +
                    "        \"email\":\"\",\n" +
                    "        \"password\": \"" + password + "\"\n" +
                    "    }\n" +
                    "}";
            @Override
            protected Void doInBackground(Void... params) {
                try{
                    MediaType text = MediaType.parse("text");
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(text, passwordObject);

                    Request request = new Request.Builder()
                            .url(urlPost)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    String msg_res = response.body().string();
                    Log.i("ON_SYNC_DEMO", "message " + msg_res);
                    JSONObject responseObj = new JSONObject(msg_res);
                    if (responseObj.getString("status").equalsIgnoreCase("1")) {

                        JSONObject responseSuccess = responseObj.getJSONObject("response");
                        token = "" + responseSuccess.getString("app").toString() + "/" + responseSuccess.getString("secret").toString();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                call.onTokenGen(token);
            }
        }.execute();

    }

    public interface onGenerateToken{
        void onTokenGen(String tok)
    ;}
}
