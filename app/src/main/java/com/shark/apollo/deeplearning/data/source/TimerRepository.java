package com.shark.apollo.deeplearning.data.source;

import com.shark.apollo.deeplearning.data.Note;

import java.util.List;

public class TimerRepository implements TimerDataSource{

    private static TimerRepository INSTANCE = null;

    private TimerDataSource mLocalDataSource;

    private TimerRepository(TimerDataSource notesLocalDataSource) {
        mLocalDataSource = notesLocalDataSource;
    }

    public static TimerRepository getInstance(TimerDataSource notesLocalDataSource) {
        if(INSTANCE == null) {
            INSTANCE = new TimerRepository(notesLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTodayNotesData(LoadNotesCallback callback) {
        mLocalDataSource.getTodayNotesData(callback);
    }

    @Override
    public void getDailyNotesData(QueryNotesCallback callback) {
        mLocalDataSource.getDailyNotesData(callback);
    }

    @Override
    public void getWeeklyNotesData(QueryNotesCallback callback) {
        mLocalDataSource.getWeeklyNotesData(callback);
    }

    @Override
    public void getMonthlyNotesData(QueryNotesCallback callback) {
        mLocalDataSource.getMonthlyNotesData(callback);
    }

    @Override
    public void getNotes(LoadNotesCallback callback) {
        mLocalDataSource.getNotes(new LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                callback.onNotesLoaded(notes);
            }

            @Override
            public void onDataNotAvailable() {
                //TODO
            }
        });
    }

    @Override
    public void getNote(String noteId, GetNotesCallback callback) {

    }

    @Override
    public void saveNote(Note note) {
        mLocalDataSource.saveNote(note);
    }

    @Override
    public void removeNote(Note note) {
        mLocalDataSource.removeNote(note);
    }
}
