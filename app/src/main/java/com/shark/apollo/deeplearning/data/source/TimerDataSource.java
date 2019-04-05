package com.shark.apollo.deeplearning.data.source;

import com.shark.apollo.deeplearning.data.Note;

import java.util.List;

public interface TimerDataSource {

    interface QueryNotesCallback {
        void onNotesQueried(int[] datas);
    }

    interface LoadNotesCallback {

        void onNotesLoaded(List<Note> notes);

        void onDataNotAvailable();
    }

    interface GetNotesCallback {

        void onNotesLoaded(Note note);

        void onDataNotAvailable();
    }

    void getTodayNotesData(LoadNotesCallback callback);

    void getDailyNotesData(QueryNotesCallback callback);

    void getWeeklyNotesData(QueryNotesCallback callback);

    void getMonthlyNotesData(QueryNotesCallback callback);

    void getNotes(LoadNotesCallback callback);

    void getNote(String noteId, GetNotesCallback callback);

    void saveNote(Note note);

    void removeNote(Note note);
}
