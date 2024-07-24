package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText nimEditText, namaEditText, alamatEditText;
    private Button tanggalLahirButton, saveNoteButton, deleteNoteButton;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private DatePickerDialog datePickerDialog;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        initWidgets();
        checkForEditNote();

        tanggalLahirButton.setOnClickListener(view -> openDatePicker());
    }

    private void initWidgets() {
        nimEditText = findViewById(R.id.nimEditText);
        namaEditText = findViewById(R.id.namaEditText);
        alamatEditText = findViewById(R.id.alamatEditText);
        tanggalLahirButton = findViewById(R.id.tanggalLahirButton);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        deleteNoteButton = findViewById(R.id.deleteNoteButton);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);

        initDatePicker();
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null) {
            nimEditText.setText(selectedNote.getNim());
            namaEditText.setText(selectedNote.getNama());
            alamatEditText.setText(selectedNote.getAlamat());
            tanggalLahirButton.setText(getDateString(selectedNote.getTanggalLahir()));
            if (selectedNote.getGender().equals("Male")) {
                maleRadioButton.setChecked(true);
            } else if (selectedNote.getGender().equals("Female")) {
                femaleRadioButton.setChecked(true);
            }
        } else {
            deleteNoteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void saveNote(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        String nim = nimEditText.getText().toString();
        String nama = namaEditText.getText().toString();
        String alamat = alamatEditText.getText().toString();

        // Pengecekan RadioButton terpilih
        RadioButton checkedRadioButton = findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String gender = checkedRadioButton != null ? checkedRadioButton.getText().toString() : "";

        Date tanggalLahir = getDateFromButton(tanggalLahirButton);

        if (nim.isEmpty() || nama.isEmpty() || alamat.isEmpty() || gender.isEmpty() || tanggalLahir == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedNote == null) {
            int id = Note.noteArrayList.size();
            Note newNote = new Note(id, nim, nama, tanggalLahir, gender, alamat);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteToDatabase(newNote);
        } else {
            selectedNote.setNim(nim);
            selectedNote.setNama(nama);
            selectedNote.setAlamat(alamat);
            selectedNote.setGender(gender);
            selectedNote.setTanggalLahir(tanggalLahir);
            sqLiteManager.updateNoteInDB(selectedNote);
        }

        finish();
    }

    public void deleteNote(View view) {
        if (selectedNote != null) {
            selectedNote.setDeleted(new Date());
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.updateNoteInDB(selectedNote);
        }
        finish();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            tanggalLahirButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return month + "-" + day + "-" + year;
    }

    private Date getDateFromButton(Button button) {
        String date = button.getText().toString();
        String[] dateParts = date.split("-");
        int month = Integer.parseInt(dateParts[0]);
        int day = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    private String getDateString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return month + "-" + day + "-" + year;
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }
}
