package com.journaldev.sqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class WebAppInterface {
    Context mContext;
    SharedPreferences sharedPreferences;
    private DBManager dbManager;
    // Instantiate the interface and set the context
    WebAppInterface(Context c) {
        mContext = c;
        sharedPreferences = mContext.getSharedPreferences("avdlogdata",MODE_PRIVATE);

        dbManager = new DBManager(mContext);
        dbManager.open();

    }

    // Show a toast from the web page
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();

    }

    @JavascriptInterface
    public String getCategories() {
        String[] catcols = new String[] {"cat_name"};
        Cursor cursor=dbManager.fetch(DbTables.TBL_CAT,catcols,null);
        JSONArray catres = new JSONArray();
        catres=dbManager.cursorToJson(cursor);
        return catres.toString();

    }
    @JavascriptInterface
    public String getBrands() {
        String[] brcols = new String[] {"br_name"};
        Cursor cursor=dbManager.fetch(DbTables.TBL_BRND,brcols,null);
        JSONArray brres = new JSONArray();
        brres=dbManager.cursorToJson(cursor);
        return brres.toString();

    }
    @JavascriptInterface
    public String getUom() {
        String[] uomcols = new String[] {"uom_name"};
        Cursor cursor=dbManager.fetch(DbTables.TBL_UOM,uomcols,null);
        JSONArray uomres = new JSONArray();
        uomres=dbManager.cursorToJson(cursor);
        return uomres.toString();

    }
    @JavascriptInterface
    public String validateBarcode(String pdt,String codes) {
        boolean duplicates=false;
        String[] bcodes = codes.split(",");
        if(bcodes.length>0){
            List<String> list = Arrays.asList(bcodes);
            // Then convert to Set
            Set<String> set = new HashSet<String>(list);
            // check String Arrays length and HashSet size
            int strArrayLength = bcodes.length;
            int setSize = set.size();

            if(strArrayLength > setSize) {
                duplicates=true;
            }

            if(duplicates) {
                return "Duplicate barcodes please correct it";
            }
            else{

                return dbManager.dupBarcode(pdt,bcodes);
            }

        }else{
return "";
        }


    }

    @JavascriptInterface
    public String updateProduct(String flds) {
        try {
            JSONObject jsnobj = new JSONObject(flds);
            JSONArray jbcodes;
            String pdtid,pdtname,pdtdesc,pdtcat,pdtuom,pdtbrnd,pdtcst,pdtprc,pdtrodlvl,pdtrodqty,pdtopqty,bcodes;
            String bcodexist="";
            long catid=0;long uomid=0;long brndid=0;
            Object jobj;
            pdtid=jsnobj.getString("txtPdtId");
            pdtname=jsnobj.getString("txtPdtName");
            pdtdesc=jsnobj.getString("txtPdtDesc");
            pdtcat=jsnobj.getString("txtpdtCat");
            pdtuom=jsnobj.getString("txtPdtUom");
            pdtbrnd=jsnobj.getString("txtPdtBrand");
            pdtcst=jsnobj.getString("txtPdtCost");
            pdtprc=jsnobj.getString("txtPdtPrice");
            pdtrodlvl=jsnobj.getString("txtPdtRlvl");
            pdtrodqty=jsnobj.getString("txtpdtRqty");
            pdtopqty=jsnobj.getString("txtPdtOqty");
            try {
                jobj=jsnobj.get("txtPdtBcode");
                bcodes="";
                jbcodes = (JSONArray)jobj;
                for (int i=0;i<jbcodes.length();i++) {
                    bcodes+=jbcodes.getString(i)+",";
                }
                bcodes=bcodes.substring(0,bcodes.length()-1);
            }catch (Exception exp){
                bcodes=jsnobj.getString("txtPdtBcode");

            }
            if (bcodes.length()>0)
            {
                bcodexist=validateBarcode(null,bcodes);
            }
            if(bcodexist==""){
                if(pdtcat.length()>0){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("cat_name", pdtcat);
                    catid=dbManager.getOrInsert("TBL_MASTER_CATEGORY","cat_name LIKE '"+pdtcat+"'","cat_id",map);
                }
                if(pdtuom.length()>0){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("uom_name", pdtuom);
                    uomid=dbManager.getOrInsert("TBL_MASTER_UOM","uom_name LIKE '"+pdtuom+"'","uom_id",map);
                }

                if(pdtbrnd.length()>0){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("br_name", pdtbrnd);
                    brndid=dbManager.getOrInsert("TBL_MASTER_BRAND","br_name LIKE '"+pdtbrnd+"'","br_id",map);
                }
                ContentValues pdtflds = new ContentValues();
                pdtflds.put("pdt_name", pdtname);
                pdtflds.put("pdt_desc", pdtdesc);
                pdtflds.put("cat_id",String.valueOf(catid));
                pdtflds.put("br_id", String.valueOf(brndid));
                pdtflds.put("uom_id", String.valueOf(uomid));
                pdtflds.put("pdt_cost", pdtcst);
                pdtflds.put("pdt_mrp", pdtprc);
                pdtflds.put("pdt_reord_lvl", pdtrodlvl);
                pdtflds.put("pdt_reord_qty", pdtrodqty);
                pdtflds.put("pdt_opqty", pdtopqty);
                long insid=dbManager.insertVal("TBL_MASTER_PRODUCT",pdtflds);
                if (bcodes.length()>0)
                {
                    dbManager.insertBcodes(insid,bcodes);
                }
                return "";
            }
            else{
                return bcodexist;
            }

        } catch (Exception e) {
            Toast.makeText(mContext, "error"+e.toString(), Toast.LENGTH_LONG).show();
            Log.e("error",e.toString());
            e.printStackTrace();
            return "Error Please try again";
        }


    }


/*
    @JavascriptInterface
    public void sendLogout (String Logout) {

        Toast.makeText(mContext, Logout, Toast.LENGTH_SHORT).show();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Location", "");// Storing string
        editor.putString("loguser", "");// Storing string
        editor.putString("logpwd", "");// Storing string
        editor.putString("LocationName", "");// Storing string

        editor.commit();

        Intent i = new Intent(mContext, loginActivity.class);
        mContext.startActivity(i);
        ((Activity)mContext).finish();


    }
*/





    @JavascriptInterface
    public void sendLoc(String toast) {
        String[] separated = toast.split(":");

        Toast.makeText(mContext, "Selected Location: "+separated[1], Toast.LENGTH_SHORT).show();


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Location",  separated[0]);// Storing string
        editor.putString("LocationName",  separated[1]);// Storing string
        editor.commit();
        String Lctn;

        Lctn=sharedPreferences.getString("Location","");
        Intent i = new Intent(mContext, MainActivity.class).putExtra("parseLoc", "done");
        mContext.startActivity(i);
        ((Activity)mContext).finish();
        // Rstart();

        //   Toast.makeText(mContext, "sp: "+Lctn, Toast.LENGTH_SHORT).show();


    }


    @JavascriptInterface
    public int getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @JavascriptInterface
    public void PaymentCloseBtn() {
        //Toast.makeText(mContext, feedback, Toast.LENGTH_SHORT).show();
        ((MainActivity) mContext).webview.goBack();
        /*if ( ((MainActivity)mContext).webview.canGoBack()) {
        }
        else {
            ((MainActivity) mContext).webview.
        }*/


    }



    @JavascriptInterface
    public String getUserId() {
        return sharedPreferences.getString("loguid","");
    }
    @JavascriptInterface
    public String getUserFname() {
        return sharedPreferences.getString("FirstName","");
    }
    @JavascriptInterface
    public String getUserEmail() {
        return sharedPreferences.getString("EmailId","");
    }
    @JavascriptInterface
    public String getUserMobile() {
        return sharedPreferences.getString("MobileNo","");
    }
    @JavascriptInterface
    public String getUserLocation() {
        return sharedPreferences.getString("Location","");
    }
    @JavascriptInterface
    public String getUserLocationName() {
        return sharedPreferences.getString("LocationName","");
    }
    @JavascriptInterface
    public void showAndroidVersion(String versionName) {
        Toast.makeText(mContext, versionName, Toast.LENGTH_SHORT).show();
    }


}