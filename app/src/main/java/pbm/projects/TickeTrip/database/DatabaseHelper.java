package pbm.projects.TickeTrip.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_ticketrip";
    public static final String TABLE_USER = "tb_user";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_NAME = "name";
//    table book
    public static final String TABLE_BOOK = "tb_book";
    public static final String COL_ID_BOOK = "id_book";
    public static final String COL_ASAL = "asal";
    public static final String COL_TUJUAN = "tujuan";
    public static final String COL_TANGGAL = "tanggal";
    public static final String COL_DEWASA = "dewasa";
    public static final String COL_ANAK = "anak";
    public static final String COL_TYPE = "tipe";
//    table harga
    public static final String TABLE_HARGA = "tb_harga";
    public static final String COL_HARGA_DEWASA = "harga_dewasa";
    public static final String COL_HARGA_ANAK = "harga_anak";
    public static final String COL_HARGA_TOTAL = "harga_total";
//    table hotel
    public static final String TABLE_HOTEL = "tb_hotel";
    public static final String COL_ID_HOTEL = "id_hotel";
    public static final String COL_NAMA_HOTEL = "hotel_nama";
    public static final String COL_LOKASI = "hotel_lokasi";
    public static final String COL_ALAMAT = "hotel_alamat";
    public static final String COL_HARGA_KAMAR = "harga_kamar";
    public static final String COl_PATH = "hotel_path";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys=ON");

        db.execSQL("create table " + TABLE_USER + " (" + COL_USERNAME + " TEXT PRIMARY KEY, " + COL_PASSWORD +
                " TEXT, " + COL_NAME + " TEXT)");

        db.execSQL("create table " + TABLE_BOOK + " (" + COL_ID_BOOK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ASAL + " TEXT, " + COL_TUJUAN + " TEXT, " + COL_ID_HOTEL + " INTEGER," + COL_TANGGAL + " TEXT, " + COL_DEWASA + " TEXT, "
                + COL_ANAK + " TEXT" + ", FOREIGN KEY(" + COL_ID_HOTEL + ") REFERENCES " + TABLE_HOTEL + ")");

        db.execSQL("create table " + TABLE_HARGA + " (" + COL_USERNAME + " TEXT, " + COL_ID_BOOK + " INTEGER, " +
                COL_HARGA_DEWASA + " TEXT, " + COL_HARGA_ANAK + " TEXT, " + COL_HARGA_TOTAL +
                " TEXT, FOREIGN KEY(" + COL_USERNAME + ") REFERENCES " + TABLE_USER
                + ", FOREIGN KEY(" + COL_ID_BOOK + ") REFERENCES " + TABLE_BOOK + ")");

        db.execSQL("create table " + TABLE_HOTEL + " (" + COL_ID_HOTEL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAMA_HOTEL + " TEXT, " + COL_LOKASI + " TEXT" + ", " + COL_HARGA_KAMAR + " TEXT," + COl_PATH + " TEXT," + COL_ALAMAT + " TEXT)");

        insertHotel(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void open() throws SQLException {
        db = this.getWritableDatabase();
    }

    public boolean Register(String username, String password, String name) throws SQLException {

        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("INSERT INTO " + TABLE_USER + "(" + COL_USERNAME + ", " + COL_PASSWORD + ", " + COL_NAME + ") VALUES (?,?,?)", new String[]{username, password, name});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean Login(String username, String password) throws SQLException {
        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean insertData(SQLiteDatabase db, String nama, String lokasi, String harga, String alamat, String path) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAMA_HOTEL,nama);

        contentValues.put(COL_LOKASI,lokasi);

        contentValues.put(COL_HARGA_KAMAR,harga);

        contentValues.put(COl_PATH,path);

        contentValues.put(COL_ALAMAT,alamat);


        long result = db.insert(TABLE_HOTEL, null, contentValues);

        if(result == -1) {

            return false;
        }

        else {

            return true;
        }

    }

    public void insertHotel(SQLiteDatabase db) {

        insertData(db,"The Sultan Hotel","Jakarta","1500000","Jl. Jend. Gatot Subroto", "hotel1");
        insertData(db,"Batavia Hotel","Jakarta","1750000","Jl. Kalibesar Barat No. 44 – 46", "hotel2");
        insertData(db,"Le Grandeur","Jakarta","2000000","Jl. Mangga Dua Raya", "hotel3");

        insertData(db,"The Trans Luxury Hotel","Bandung","850000","Jalan Gatot Subroto 289", "hotel1");
        insertData(db,"GH Universal Hotel","Bandung","1800000","Jalan Setiabudi 376, Setiabudi, Bandung", "hotel2");
        insertData(db,"Hyatt Regency Bandung","Bandung","1500000","Jalan Sumatera Nomor 51", "hotel3");

        insertData(db,"Aston Denpasar","Bali","1500000","Denpasar", "hotel1");
        insertData(db,"Bali Mulia Villas","Bali","700000","kuta", "hotel2");
        insertData(db,"Villa De Daun","Bali","1800000","kuta", "hotel3");

        insertData(db,"Hotel Hermes","Banda Aceh","850000","Jl. T. Panglima Nyak Makam, Lambhuk, Kec. Ulee Kareng, Kota Banda Aceh", "hotel3");
        insertData(db,"Hotel Kyriad Muraya","Banda Aceh","1200000","Jl. Teuku Moh. Daud Beureueh No.5, Laksana, Kec. Kuta Alam, Kota Banda Aceh", "hotel2");
        insertData(db,"Hotel Grand Arabia","Banda Aceh","1550000","Jl. Prof. A. Majid Ibrahim II No.3, Kp. Baru, Kec. Baiturrahman, Kota Banda Aceh", "hotel1");
    }
}