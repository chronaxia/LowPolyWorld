package com.chronaxia.lowpolyworld.model.local.greenDao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.chronaxia.lowpolyworld.model.entity.Token;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TOKEN".
*/
public class TokenDao extends AbstractDao<Token, Long> {

    public static final String TABLENAME = "TOKEN";

    /**
     * Properties of entity Token.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AppId = new Property(1, String.class, "appId", false, "APP_ID");
        public final static Property ApiKey = new Property(2, String.class, "apiKey", false, "API_KEY");
        public final static Property SecretKey = new Property(3, String.class, "secretKey", false, "SECRET_KEY");
        public final static Property Token = new Property(4, String.class, "token", false, "TOKEN");
        public final static Property Opdate = new Property(5, String.class, "opdate", false, "OPDATE");
    }


    public TokenDao(DaoConfig config) {
        super(config);
    }
    
    public TokenDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TOKEN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"APP_ID\" TEXT," + // 1: appId
                "\"API_KEY\" TEXT," + // 2: apiKey
                "\"SECRET_KEY\" TEXT," + // 3: secretKey
                "\"TOKEN\" TEXT," + // 4: token
                "\"OPDATE\" TEXT);"); // 5: opdate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TOKEN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Token entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String appId = entity.getAppId();
        if (appId != null) {
            stmt.bindString(2, appId);
        }
 
        String apiKey = entity.getApiKey();
        if (apiKey != null) {
            stmt.bindString(3, apiKey);
        }
 
        String secretKey = entity.getSecretKey();
        if (secretKey != null) {
            stmt.bindString(4, secretKey);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(5, token);
        }
 
        String opdate = entity.getOpdate();
        if (opdate != null) {
            stmt.bindString(6, opdate);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Token entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String appId = entity.getAppId();
        if (appId != null) {
            stmt.bindString(2, appId);
        }
 
        String apiKey = entity.getApiKey();
        if (apiKey != null) {
            stmt.bindString(3, apiKey);
        }
 
        String secretKey = entity.getSecretKey();
        if (secretKey != null) {
            stmt.bindString(4, secretKey);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(5, token);
        }
 
        String opdate = entity.getOpdate();
        if (opdate != null) {
            stmt.bindString(6, opdate);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Token readEntity(Cursor cursor, int offset) {
        Token entity = new Token( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // appId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // apiKey
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // secretKey
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // token
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // opdate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Token entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAppId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setApiKey(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSecretKey(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setToken(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setOpdate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Token entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Token entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Token entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
