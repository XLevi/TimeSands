package com.shark.apollo.deeplearning.data.source.local;

import com.shark.apollo.deeplearning.App;
import com.shark.apollo.deeplearning.data.Note;
import com.shark.apollo.deeplearning.data.Note_;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

public class NoteDao {

    private static NoteDao noteDao = null;
    private Box<Note> noteBox;

    private NoteDao() {
        BoxStore boxStore = App.getApplication().getBoxStore();
        noteBox = boxStore.boxFor(Note.class);
    }

    public static NoteDao getInstance() {
        if(noteDao == null) {
            synchronized (NoteDao.class) {
                if(noteDao == null) {
                    noteDao = new NoteDao();
                }
            }
        }
        return noteDao;
    }

    public void addNote(Note note) {
        noteBox.put(note);
    }

    public void removeNote(Note note) {
        noteBox.remove(note);
    }

    public List<Note> getAllNotes() {
        Query<Note> queryAll = noteBox.query().order(Note_.id, QueryBuilder.DESCENDING).build();
        return queryAll.find();
    }

    public List<Note> getTodayNotesData() {
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();

        return getNotesByDates(start, end);
    }

    public int[] getRecentlyWeekNotesData() {
        int [] datas = new int[7];
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        datas[6] = getNotesCountByDates(start, end);

        end = start;
        for(int i = 5; i >= 0; i--) {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
            start = calendar.getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            datas[i] = getNotesCountByDates(start, end);
            end = start;
        }
        return datas;
    }

    public int[] getWeeklyNotesData() {
        int datas[] = new int[7];
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        datas[6] = getNotesCountByDates(start, end);

        end = start;
        for (int i = 5; i >= 0; i--) {
            calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - 7);
            start = calendar.getTime();
            datas[i] = getNotesCountByDates(start, end);
            end = start;
        }
        return datas;
    }

    public int[] getMonthlyNotesData() {
        int datas[] = new int[7];
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        datas[6] = getNotesCountByDates(start, end);

        end = start;
        for (int i = 5; i >= 0; i--) {
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) - 1);
            start = calendar.getTime();
            datas[i] = getNotesCountByDates(start, end);
            end = start;
        }
        return datas;
    }

    public int getNotesCountByDates(Date start, Date end) {
        QueryBuilder<Note> queryBuilder = noteBox.query();
        queryBuilder.between(Note_.date, start, end);
        return queryBuilder.build().find().size();
    }

    public List<Note> getNotesByDates(Date s, Date e) {
        Query<Note> query = noteBox.query().between(Note_.date, s, e).build();
        return query.find();
    }

}
