package com.hnsh.dialogue.sql.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.hnsh.dialogue.bean.db.QACategoryBean;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "QACategory".
*/
public class QACategoryBeanDao extends AbstractDao<QACategoryBean, Long> {

    public static final String TABLENAME = "QACategory";

    /**
     * Properties of entity QACategoryBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TypeId = new Property(1, int.class, "typeId", false, "TYPE_ID");
        public final static Property TypeName = new Property(2, String.class, "typeName", false, "TYPE_NAME");
        public final static Property HaveNextlevel = new Property(3, int.class, "haveNextlevel", false, "HAVE_NEXTLEVEL");
        public final static Property Sort = new Property(4, int.class, "sort", false, "SORT");
    }


    public QACategoryBeanDao(DaoConfig config) {
        super(config);
    }
    
    public QACategoryBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"QACategory\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TYPE_ID\" INTEGER NOT NULL UNIQUE ," + // 1: typeId
                "\"TYPE_NAME\" TEXT," + // 2: typeName
                "\"HAVE_NEXTLEVEL\" INTEGER NOT NULL ," + // 3: haveNextlevel
                "\"SORT\" INTEGER NOT NULL );"); // 4: sort
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"QACategory\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, QACategoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTypeId());
 
        String typeName = entity.getTypeName();
        if (typeName != null) {
            stmt.bindString(3, typeName);
        }
        stmt.bindLong(4, entity.getHaveNextlevel());
        stmt.bindLong(5, entity.getSort());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, QACategoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTypeId());
 
        String typeName = entity.getTypeName();
        if (typeName != null) {
            stmt.bindString(3, typeName);
        }
        stmt.bindLong(4, entity.getHaveNextlevel());
        stmt.bindLong(5, entity.getSort());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public QACategoryBean readEntity(Cursor cursor, int offset) {
        QACategoryBean entity = new QACategoryBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // typeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // typeName
            cursor.getInt(offset + 3), // haveNextlevel
            cursor.getInt(offset + 4) // sort
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, QACategoryBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTypeId(cursor.getInt(offset + 1));
        entity.setTypeName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHaveNextlevel(cursor.getInt(offset + 3));
        entity.setSort(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(QACategoryBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(QACategoryBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(QACategoryBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
