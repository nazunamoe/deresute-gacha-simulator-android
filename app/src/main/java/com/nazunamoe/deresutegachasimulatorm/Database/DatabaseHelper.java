package com.nazunamoe.deresutegachasimulatorm.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nazunamoe.deresutegachasimulatorm.Card.Card;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "10056500.sqlite";
    private static String DB_PATH = "10056500.sqlite";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    Random random = new Random();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (dbFile.exists())
            dbFile.delete();

        copyDataBase();

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    // id를 넣어서 카드를 반환받는 메소드
    public Card getResult(int id){
        // 결과 카드 선언
        Card result = null;
        // SQL문 실행
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM card_info WHERE card_info.id="+id,null);
        cursor.moveToFirst();
        /*
            public Card(int no, String cardName, String charaName, String rarity, int hp_Min, int vocal_Min, int dance_Min, int visual_Min, int hp_Max, int vocal_Max, int dance_Max, int visual_Max, String skillName,
                String skillExplain, String centerSkillName, String centerSkillExplain, String eventName, Boolean limited, Boolean fes){
         */
        int cardno = cursor.getInt(0);
        String cardname = cursor.getString(1);
        String charaname = cursor.getString(2);
        String rarity = cursor.getString(3);

        int hp_min = cursor.getInt(5);
        int vo_min = cursor.getInt(6);
        int da_min = cursor.getInt(7);
        int vi_min = cursor.getInt(8);

        int hp_max = cursor.getInt(10);
        int vo_max = cursor.getInt(11);
        int da_max = cursor.getInt(12);
        int vi_max = cursor.getInt(13);

        String skillname = cursor.getString(15);
        String skillexplain = cursor.getString(16);
        String centerskillname = cursor.getString(17);
        String centerskillexplain = cursor.getString(18);

        String eventname = cursor.getString(19);

        int limitedint = cursor.getInt(cursor.getColumnIndex("limited"));
        int fesint = cursor.getInt(cursor.getColumnIndex("fes"));

        boolean limited = false;
        if(limitedint == 1){
            limited = true;
        }else{
            limited = false;
        }

        boolean fes = false;
        if(fesint == 1){
            fes = true;
        }else{
            fes = false;
        }

        // 필요한 정보를 모두 변수에 연결.
        result = new Card(cardno, cardname, charaname, rarity
                    , hp_min, vo_min, da_min, vi_min, hp_max, vo_max, da_max, vi_max
                    , skillname, skillexplain, centerskillname, centerskillexplain
                    , eventname, limited, fes);
        return result;
        // DB에서 받아온 정보를 이용해 새 카드 클래스를 생성 후 반환
    }

    // 특정 레어도의 카드를 랜덤으로 가져오는 메소드
    public Card getRarityCard(String rarity){
        Card result = null;
        // 결과 카드 선언
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM card_info WHERE rarity="+rarity,null);
        // 쿼리문 실행
        cursor.moveToFirst();
        int pos = random.nextInt(cursor.getCount());
        // 쿼리문 결과 크기를 이용해서 랜덤 상수 획득
        cursor.moveToPosition(pos);
        result = getResult(cursor.getInt(0));
        return result;
        // 해당하는 위치로 이동 후 getResult 메소드를 이용해 해당하는 카드 변수 반환
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}