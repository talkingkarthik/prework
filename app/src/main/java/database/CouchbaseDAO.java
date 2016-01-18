package database;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import models.ToDoItem;

/**
 * Created by kartikkulkarni on 1/17/16.
 */
public class CouchbaseDAO {

    final static String TAG = "CouchbaseEvents";
    final static String DB_NAME = "todopersist1";
    private Manager manager = null;
    private Database database = null;
    private static CouchbaseDAO instance = null;

    private void CouchbaseDAO ()
    {
        //Singleton!
    }
    public static CouchbaseDAO getInstance (Context ctx)
    {
        if (instance == null)
        {
            instance = new CouchbaseDAO();
            try {
                instance.manager = new Manager(new AndroidContext(ctx), Manager.DEFAULT_OPTIONS);
                instance.database = instance.manager.getDatabase(DB_NAME);
                return instance;
            } catch (Exception e) {
                Log.e(TAG, "Error getting database", e);
            }
        }
        return instance;
    }
    public String createDocument( String title, String text, Date time, boolean pri) {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", title);
        map.put("text", text);
        map.put("highpri", pri);
        map.put("time", time);
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    public void deleteDocument (String id) {
        try {
            database.getDocument(id).delete();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error deleting", e);
        }
    }

    private String updateDocument( String docid, String title, String text, Date time, boolean pri)
    {
        Document document = database.getDocument(docid);
        String newdocid = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", title);
        map.put("text", text);
        map.put("highpri", pri);
        map.put("time", time);
        try {
            document.delete();
            document = database.createDocument();
            newdocid = document.getId();
            // Save the properties to the document
            document.putProperties(map);
            Log.d(TAG, "updated doc");

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return newdocid;
    }

    public void populateList(ArrayList<ToDoItem> list) {
        QueryEnumerator result = null;
        Query query = database.createAllDocumentsQuery();
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error populating", e);
        }
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            list.add(new ToDoItem((String) row.getDocument().getProperties().get("title"),
                    (String) row.getDocument().getProperties().get("text"),
                    new Date(),
                    (boolean) row.getDocument().getProperties().get("highpri"),
                    false,
                    row.getDocumentId()
            ));

        }
    }
    public void persistList( ArrayList<ToDoItem> list) {

        Iterator<ToDoItem> it = list.iterator();
        while (it.hasNext()) {

            ToDoItem item = it.next();
            if (item.isDirty()) {
                if (item.isNewly()) {
                    item.setDocid(createDocument(item.getTitle(), item.getText(),
                                                           item.getTimestamp(), item.isHighpri()));
                    item.setNewly(false);
                } else {
                    String newdocid = updateDocument(item.getDocid(), item.getTitle(),
                                            item.getText(), item.getTimestamp(), item.isHighpri());
                    item.setDocid(newdocid);
                }
                item.setDirty(false);
            }

        }
    }

}
