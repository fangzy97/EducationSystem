package com.lepetit.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.lepetit.greendaohelper.GradeInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GRADE_INFO".
*/
public class GradeInfoDao extends AbstractDao<GradeInfo, Long> {

    public static final String TABLENAME = "GRADE_INFO";

    /**
     * Properties of entity GradeInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Year = new Property(1, String.class, "year", false, "Year");
        public final static Property Course = new Property(2, String.class, "course", false, "Course");
        public final static Property Score = new Property(3, String.class, "score", false, "Score");
        public final static Property Credit = new Property(4, String.class, "credit", false, "Credit");
    }


    public GradeInfoDao(DaoConfig config) {
        super(config);
    }
    
    public GradeInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GRADE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"Year\" TEXT," + // 1: year
                "\"Course\" TEXT," + // 2: course
                "\"Score\" TEXT," + // 3: score
                "\"Credit\" TEXT);"); // 4: credit
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GRADE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GradeInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(2, year);
        }
 
        String course = entity.getCourse();
        if (course != null) {
            stmt.bindString(3, course);
        }
 
        String score = entity.getScore();
        if (score != null) {
            stmt.bindString(4, score);
        }
 
        String credit = entity.getCredit();
        if (credit != null) {
            stmt.bindString(5, credit);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GradeInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String year = entity.getYear();
        if (year != null) {
            stmt.bindString(2, year);
        }
 
        String course = entity.getCourse();
        if (course != null) {
            stmt.bindString(3, course);
        }
 
        String score = entity.getScore();
        if (score != null) {
            stmt.bindString(4, score);
        }
 
        String credit = entity.getCredit();
        if (credit != null) {
            stmt.bindString(5, credit);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GradeInfo readEntity(Cursor cursor, int offset) {
        GradeInfo entity = new GradeInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // year
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // course
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // score
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // credit
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GradeInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setYear(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCourse(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setScore(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCredit(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GradeInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GradeInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GradeInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
