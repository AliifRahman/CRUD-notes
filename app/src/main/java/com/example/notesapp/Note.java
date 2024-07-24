package com.example.notesapp;

import java.util.ArrayList;
import java.util.Date;

public class Note {
    public static ArrayList<Note> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA =  "noteEdit";

    private int id;
    private String nim;
    private String nama;
    private Date tanggalLahir;
    private String gender;
    private String alamat;
    private Date deleted;

    public Note(int id, String nim, String nama, Date tanggalLahir, String gender, String alamat, Date deleted) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.gender = gender;
        this.alamat = alamat;
        this.deleted = deleted;
    }

    public Note(int id, String nim, String nama, Date tanggalLahir, String gender, String alamat) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.gender = gender;
        this.alamat = alamat;
        this.deleted = null;
    }

    public static Note getNoteForID(int passedNoteID) {
        for (Note note : noteArrayList) {
            if(note.getId() == passedNoteID)
                return note;
        }
        return null;
    }

    public static ArrayList<Note> nonDeletedNotes() {
        ArrayList<Note> nonDeleted = new ArrayList<>();
        for(Note note : noteArrayList) {
            if(note.getDeleted() == null)
                nonDeleted.add(note);
        }
        return nonDeleted;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public Date getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public Date getDeleted() { return deleted; }
    public void setDeleted(Date deleted) { this.deleted = deleted; }
}
