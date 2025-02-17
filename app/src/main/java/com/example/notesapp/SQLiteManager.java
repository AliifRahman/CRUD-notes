package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "NoteDBMahasiswa1";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Note";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String NIM_FIELD = "nim";
    private static final String NAMA_FIELD = "nama";
    private static final String TANGGALLAHIR_FIELD = "tanggal_lahir";
    private static final String GENDER_FIELD = "gender";
    private static final String ALAMAT_FIELD = "alamat";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(NIM_FIELD)
                .append(" TEXT, ")
                .append(NAMA_FIELD)
                .append(" TEXT, ")
                .append(TANGGALLAHIR_FIELD)
                .append(" TEXT, ")
                .append(GENDER_FIELD)
                .append(" TEXT, ")
                .append(ALAMAT_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    public void addNoteToDatabase(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(NIM_FIELD, note.getNim());
        contentValues.put(NAMA_FIELD, note.getNama());
        contentValues.put(TANGGALLAHIR_FIELD, getStringFromDate(note.getTanggalLahir()));
        contentValues.put(GENDER_FIELD, note.getGender());
        contentValues.put(ALAMAT_FIELD, note.getAlamat());
        contentValues.put(DELETED_FIELD, getStringFromDate(note.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateNoteListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    String nim = result.getString(2);
                    String nama = result.getString(3);
                    String stringTanggalLahir = result.getString(4);
                    Date tanggalLahir = getDateFromString(stringTanggalLahir);
                    String gender = result.getString(5);
                    String alamat = result.getString(6);
                    String stringDeleted = result.getString(7);
                    Date deleted = getDateFromString(stringDeleted);
                    Note note = new Note(id, nim, nama, tanggalLahir, gender, alamat, deleted);
                    Note.noteArrayList.add(note);
                }
            }
        }
    }

    public void updateNoteInDB(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, note.getId());
        contentValues.put(NIM_FIELD, note.getNim());
        contentValues.put(NAMA_FIELD, note.getNama());
        contentValues.put(TANGGALLAHIR_FIELD, getStringFromDate(note.getTanggalLahir()));
        contentValues.put(GENDER_FIELD, note.getGender());
        contentValues.put(ALAMAT_FIELD, note.getAlamat());
        contentValues.put(DELETED_FIELD, getStringFromDate(note.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(note.getId())});
    }

    private String getStringFromDate(Date date) {
        if (date == null)
            return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
