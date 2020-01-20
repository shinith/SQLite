package com.journaldev.sqlite;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbTables extends SQLiteOpenHelper {

    // Table Name
    public static final String TBL_PDT = "TBL_MASTER_PRODUCT";
    public static final String TBL_CAT = "TBL_MASTER_CATEGORY";
    public static final String TBL_BRND = "TBL_MASTER_BRAND";
    public static final String TBL_UOM = "TBL_MASTER_UOM";
    public static final String TBL_BCODE = "TBL_MASTER_PRODUCTBARCODE";
    public static final String TBL_LDG = "TBL_MASTER_LEDGER";
    public static  final String[] pdtflds={"pdt_id","pdt_name","pdt_desc","cat_id","uom_id","br_id","pdt_cost",};
    // Table columns
    /*public static final String _ID = "pdt_id";
    public static final String PNAME = "pdt_name";
    public static final String CODE = "pdt_code";
    public static final String DESC = "pdt_desc";
    public static final String CATID = "cat_id";
    public static final String UOMID = "uom_id";
    public static final String BRID = "br_id";
    public static final String UCOST = "pdt_cost";
    public static final String AVGCOST = "pdt_avg_cost";
    public static final String MRP = "pdt_mrp";
    public static final String UPRICE1 = "pdt_uprice1";
    public static final String UPRICE2 = "pdt_uprice2";
    public static final String UPRICE3 = "pdt_uprice3";
    public static final String ISBATCH = "pdt_need_batch";
    public static final String ISCOLOR = "pdt_need_color";
    public static final String ISSIZE = "pdt_need_size";
    public static final String ISSINO = "pdt_need_sino";
    public static final String RORDLVL = "pdt_reord_lvl";
    public static final String RORDQTY = "pdt_reord_qty";
    public static final String OPQTY = "pdt_opqty";
    public static final String LOGID = "log_id";*/

    // Database Information
    static final String DB_NAME = "RETAILER.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_PDT = "create table " + TBL_PDT + "(pdt_id  INTEGER PRIMARY KEY AUTOINCREMENT ,pdt_name VARCHAR(100) DEFAULT NULL,pdt_code VARCHAR(50) DEFAULT NULL ,pdt_desc VARCHAR(100) DEFAULT NULL,br_id INT(11)  NOT NULL DEFAULT '0',cat_id INT(11) NOT NULL NOT NULL DEFAULT '0',uom_id INT(11) NOT NULL NOT NULL DEFAULT '0',pdt_cost FLOAT NOT NULL NOT NULL DEFAULT '0',pdt_avg_cost FLOAT NOT NULL DEFAULT '0',pdt_mrp FLOAT NOT NULL DEFAULT '0',pdt_uprice1 FLOAT NOT NULL DEFAULT '0',pdt_uprice2 FLOAT NOT NULL DEFAULT '0',pdt_uprice3 FLOAT NOT NULL DEFAULT '0' ,pdt_need_batch BIT(1) DEFAULT '0',pdt_need_color BIT(1) NOT NULL DEFAULT '0',pdt_need_size BIT(1) NOT NULL DEFAULT '0',pdt_need_sino BIT(1) NOT NULL DEFAULT '0',pdt_reord_lvl FLOAT NOT NULL DEFAULT '0',pdt_reord_qty FLOAT NOT NULL DEFAULT '0',pdt_opqty FLOAT NOT NULL DEFAULT '0',log_id INT(11) NOT NULL DEFAULT '0')";
    private static final String CREATE_TABLE_BCODE = "create table " + TBL_BCODE + "(bc_id INTEGER PRIMARY KEY AUTOINCREMENT, pdt_id INTEGER DEFAULT NULL, bc_code VARCHAR(50) DEFAULT NULL, bt_id INTEGER DEFAULT NULL, cl_id INTEGER DEFAULT NULL, sz_id INTEGER DEFAULT NULL, login_id INTEGER DEFAULT NULL,UNIQUE(pdt_id,bc_code))";
    private static final String CREATE_TABLE_CAT = "create table " + TBL_CAT + "( cat_id INTEGER PRIMARY KEY AUTOINCREMENT ,cat_name VARCHAR(50) , login_id INTEGER)";
    private static final String CREATE_TABLE_UOM = "create table " + TBL_UOM + "(uom_id INTEGER PRIMARY KEY AUTOINCREMENT , uom_name VARCHAR(50) , login_id INTTGER)";
    private static final String CREATE_TABLE_BRND = "create table " + TBL_BRND + "( br_id INTEGER PRIMARY KEY AUTOINCREMENT , br_name VARCHAR(50) NOT NULL , login_id INTEGER)";
    private static final String CREATE_TABLE_LDG = "create table " + TBL_LDG + "(ldg_id INTEGER PRIMARY KEY AUTOINCREMENT,grp_id INTEGER NOT NULL,ldg_code VARCHAR(50) NOT NULL,ldg_name VARCHAR(50) NOT NULL,area_id INTEGER NOT NULL,ldg_cno VARCHAR(50) NOT NULL,ldg_email VARCHAR(100) NOT NULL,lgd_opbal float NOT NULL,ldg_debcrd bit(1) NOT NULL DEFAULT '0',ldg_editable bit(1) NOT NULL DEFAULT '0',ldg_iscrdit bit(1) NOT NULL DEFAULT '0',ldg_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,login_id INTEGER NOT NULL)";

    public DbTables(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PDT);
        db.execSQL(CREATE_TABLE_BCODE);
        db.execSQL(CREATE_TABLE_CAT);
        db.execSQL(CREATE_TABLE_UOM);
        db.execSQL(CREATE_TABLE_BRND);
        db.execSQL(CREATE_TABLE_LDG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PDT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BCODE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_UOM);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BRND);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_LDG);
        onCreate(db);
    }
}
