package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static String TAG = "JSONUtils";

    public static Sandwich parseSandwichJson(String json) {
        //Parse JSON to sandwich obj
        Log.i(TAG , json+"");

        //Model Obj
        Sandwich sandwichModel= new Sandwich() ;

        //check the json is not null
        if(json != null ){

            try {
                // root obj
                JSONObject baseObj = new JSONObject(json);

                //Name obj
                JSONObject nameObj = baseObj.getJSONObject("name");
                String name= nameObj.getString("mainName");
                JSONArray otherNames = nameObj.getJSONArray("alsoKnownAs");
                // fill the list with JSONArray
                List<String> alsoKnownAs = toStringList(otherNames);
                //place obj
                String placeOfOrigin = baseObj.getString("placeOfOrigin");
                //Desc obj
                String description = baseObj.getString("description");
                //image obj
                String imageURL= baseObj.getString("image");
                // ingredients obj
                JSONArray ingredients= baseObj.getJSONArray("ingredients");
                // fill the list with JSONArray
                List<String> ingredientsList = toStringList(ingredients);

                sandwichModel = new Sandwich(name, alsoKnownAs , placeOfOrigin, description , imageURL , ingredientsList);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  sandwichModel ;

        }else
            return null;





    }
    // Method to convert JsonArray to List of Strings
    private static List<String> toStringList(JSONArray array) throws JSONException {
        if(array==null)
            return null;

        List<String> paredList =new ArrayList<>();
        for(int i=0; i < array.length(); i++) {
            paredList.add(array.getString(i));
        }
        return paredList;
    }
}
