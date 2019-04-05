package com.shark.apollo.deeplearning.data.source.local;

import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.source.TimerDataSource;
import com.shark.apollo.deeplearning.util.AppExecutors;

import java.util.List;

public class NotesLocalDataSource implements TimerDataSource{

    private static NotesLocalDataSource INSTANCE = null;

    private NoteDao mNoteDao;

    private AppExecutors mAppExecutors;

    private NotesLocalDataSource(AppExecutors appExecutors, NoteDao noteDao) {
        mAppExecutors = appExecutors;
        mNoteDao = noteDao;
    }

    public static NotesLocalDataSource getInstance(AppExecutors appExecutors, NoteDao noteDao) {
        if(INSTANCE == null) {
            synchronized (NotesLocalDataSource.class) {
                if(INSTANCE == null) {
                    INSTANCE = new NotesLocalDataSource(appExecutors, noteDao);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getTodayNotesData(LoadNotesCallback callback) {
        Runnable runnable = () -> {
            List<Note> datas = mNoteDao.getTodayNotesData();
            mAppExecutors.mainThread().execute(() -> callback.onNotesLoaded(datas));
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getDailyNotesData(QueryNotesCallback callback) {
        Runnable runnable = () -> {
            final int[] datas = mNoteDao.getRecentlyWeekNotesData();
            mAppExecutors.mainThread().execute(() -> callback.onNotesQueried(datas));
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getWeeklyNotesData(QueryNotesCallback callback) {
        Runnable runnable = () -> {
            final int[] datas = mNoteDao.getWeeklyNotesData();
            mAppExecutors.mainThread().execute(() -> callback.onNotesQueried(datas));
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getMonthlyNotesData(QueryNotesCallback callback) {
        Runnable runnable = () -> {
            final int[] datas = mNoteDao.getMonthlyNotesData();
            mAppExecutors.mainThread().execute(() -> callback.onNotesQueried(datas));
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getNotes(LoadNotesCallback callback) {
        Runnable runnable = () -> {
            final List<Note> notes = mNoteDao.getAllNotes();
            mAppExecutors.mainThread().execute(() -> {
                if(notes.isEmpty()) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onNotesLoaded(notes);
                }
            });
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getNote(String noteId, GetNotesCallback callback) {

    }

    @Override
    public void saveNote(final Note note) {
        Runnable saveRunnable = () -> mNoteDao.addNote(note);
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void removeNote(Note note) {
        Runnable removeRunnable = () -> mNoteDao.removeNote(note);
        mAppExecutors.diskIO().execute(removeRunnable);
    }
}
