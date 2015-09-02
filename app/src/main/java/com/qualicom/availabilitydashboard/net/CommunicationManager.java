package com.qualicom.availabilitydashboard.net;

import android.database.sqlite.SQLiteException;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qualicom.availabilitydashboard.AvailabilityDashboardApplication;
import com.qualicom.availabilitydashboard.db.PersistenceManager;
import com.qualicom.availabilitydashboard.vo.Settings;
import com.qualicom.availabilitydashboard.vo.Status;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by kangelov on 2015-09-02.
 */
public class CommunicationManager {

    private static final String TAG = "CommunicationManager";

    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    final JsonObject jsonObject = json.getAsJsonObject();
                    long msSince1970 = jsonObject.getAsLong();
                    return new Date(msSince1970);
                }
            }).
            registerTypeAdapter(Status.class, new JsonDeserializer<Status>() {

                @Override
                public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    final JsonObject jsonObject = json.getAsJsonObject();
                    String statusStr = jsonObject.getAsString();
                    for (Status status : Status.values()) {
                        if (status.toString().equalsIgnoreCase(statusStr)) {
                            return status;
                        }
                    }
                    return Status.UNKNOWN;
                }
            }).
            create();

    private final Settings settings;
    private final CommunicationHandler handler;
    private final PersistenceManager pm;

    public CommunicationManager(CommunicationHandler handler, PersistenceManager pm) {
        this.pm = pm;
        this.handler = handler;
        this.settings = pm.getSettings();
    }

    public void refreshAvailability() {
        String url = settings.getUri();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                final AvailabilityResponse availabilityResponse = gson.fromJson(jsonObject, AvailabilityResponse.class);
                try {
                    pm.setAllEnvironments(availabilityResponse.getEnvironments());
                    handler.handleResponse(availabilityResponse.getEnvironments(), availabilityResponse.getUpdateTime());
                } catch (SQLiteException e) {
                    handler.handleError(e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handler.handleError(volleyError.getLocalizedMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        AvailabilityDashboardApplication.getInstance().addToRequestQueue(request, TAG);

    }
}
