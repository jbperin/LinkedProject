package biz.perin.jibe.linkedproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import biz.perin.jibe.linkedproject.model.*;

/**
 * Created by Famille PERIN on 25/10/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements LetsObserver{

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    public static final String CREATE_ANNOUNCES = "CREATE TABLE IF NOT EXISTS Announces("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
            +", ID INTEGER"
            +", ownerPseudo VARCHAR"
            +", description VARCHAR"
            +", direction VARCHAR"
            +", category VARCHAR"
            +");";

    public static final String CREATE_PERSONS = "CREATE TABLE IF NOT EXISTS Persons("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
            +", pseudo VARCHAR"
            +", name VARCHAR"
            +", address VARCHAR"
            +", phone1 VARCHAR"
            +", phone2 VARCHAR"
            +", solde INTEGER"
            +", numberOfExchange INTEGER"
            +", lastPublish VARCHAR"
            +");";

    public static final String CREATE_POST = "CREATE TABLE IF NOT EXISTS Posts("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
            +", pseudo VARCHAR"
            +", discussionId INTEGER"
            +", text VARCHAR"
            +", date VARCHAR"
            +", category VARCHAR"
            +");";

    public static final String CREATE_TRANSACTION = "CREATE TABLE IF NOT EXISTS Transactions("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
            +", pseudoOfferer VARCHAR"
            +", pseudoDemander VARCHAR"
            +", offerDescription VARCHAR"
            +", demandDescription VARCHAR"
            +", amount INTEGER"
            +");";


    private static DatabaseHelper mInstance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SelDatabase.db";

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }
    public static DatabaseHelper getInstance(){
        return mInstance;
    }
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void deleteAll(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS Announces");
        db.execSQL("DROP TABLE IF EXISTS Persons");
        db.execSQL("DROP TABLE IF EXISTS Posts");
        db.execSQL("DROP TABLE IF EXISTS Transactions");
    }

    public void deleteAll() {
        deleteAll(this.getWritableDatabase());
    }

    public void createAll(SQLiteDatabase db) {
        db.execSQL(CREATE_ANNOUNCES);
        db.execSQL(CREATE_PERSONS);
        db.execSQL(CREATE_POST);
        db.execSQL(CREATE_TRANSACTION);
    }

    public void createAll() {
        createAll(this.getWritableDatabase());
    }


//    public void pushInDatabase() {
//        List<Announce> listeDesAnnonces;
//        SQLiteDatabase mydatabase = this.getWritableDatabase();
//        mydatabase.execSQL("DROP TABLE IF EXISTS Announces" );
//        mydatabase.execSQL(CREATE_ANNOUNCES);
//        for (Announce ann: listeDesAnnonces){
//            if (ann != null) {
//                int Id = ann.getId();
//                String ownerPseudo = (ann.getOwnerPseudo()==null)? "null":"'"+ann.getOwnerPseudo()+"'";
//                String description= (ann.getDescription()==null)? "null":"'"+ann.getDescription().replaceAll("'","''")+"'";
//                String direction = (ann.getDirection().name()==null)? "null":"'"+ann.getDirection()+"'";
//                String category = (ann.getCategory()==null)? "null":"'"+ann.getCategory()+"'";
//                String SqlRequest = String.format("INSERT INTO Announces VALUES(%d,%s, %s, %s, %s);", Id, ownerPseudo, description, direction, category);
//                System.out.println (SqlRequest);
//                mydatabase.execSQL(SqlRequest);
//            }
//        }
//
//    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createAll(db);

        Log.i(TAG, "onCreate() finished");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteAll(db);
        onCreate(db);
        Log.i(TAG, "onUpgrade() finished");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    @Override
    public void onNewAnnounce(Announce ann) {

        if (ann == null) return;

        SQLiteDatabase mydatabase = this.getWritableDatabase();

        int Id = ann.getId();
        String ownerPseudo = (ann.getOwnerPseudo()==null)? "null":"'"+ann.getOwnerPseudo()+"'";
        String description= (ann.getDescription()==null)? "null":"'"+ann.getDescription().replaceAll("'","''")+"'";
        String direction = (ann.getDirection().name()==null)? "null":"'"+ann.getDirection()+"'";
        String category = (ann.getCategory()==null)? "null":"'"+ann.getCategory()+"'";
//        String SqlRequest = String.format("INSERT INTO Announces VALUES(%d,%s, %s, %s, %s);", Id, ownerPseudo, description, direction, category);

        ContentValues values = new ContentValues();
        values.put("ID", Id);
        values.put("ownerPseudo",ownerPseudo);
        values.put("description", description);
        values.put("direction", direction);
        values.put("category", category);
        int returnCode = (int) mydatabase.insert("Announces", null, values);

        //System.out.println (SqlRequest);
//        mydatabase.execSQL(SqlRequest);
    }

    @Override
    public void onNewPerson(Person pers) {

        if (pers == null) return;

        SQLiteDatabase mydatabase = this.getWritableDatabase();


        String pseudo = "'" + pers.getPseudo()+"'";
        Integer solde = pers.getSolde();
        String address = "'" + pers.getAddress()+"'";
        String name = "'" + pers.getName()+"'";
        String phone1 = "'" + pers.getPhone1()+"'";
        String phone2 = "'" + pers.getPhone2()+"'";
        Integer numberOfExchange = pers.getNumberOfExchange();
        String lastPublish = "'" + pers.getLastPublish()+"'";

        ContentValues values = new ContentValues();
        //values.put("ID", Id);
        values.put("pseudo",pseudo);
        values.put("name", name);
        values.put("address", address);
        values.put("phone1", phone1);
        values.put("phone2", phone2);
        values.put("solde", solde);
        values.put("numberOfExchange", numberOfExchange);
        values.put("lastPublish", lastPublish);


        //String SqlRequest = String.format("INSERT INTO Persons VALUES(%s, %d, %s, %s, %s, %s, %d, %s);",pseudo, solde, address, name, phone1, phone2, numberOfExchange,lastPublish);
        // mydatabase.execSQL(SqlRequest);
        int returnCode = (int) mydatabase.insert("Persons", null, values);

    }

    @Override
    public void onNewPost(ForumMessage mess) {

        if (mess == null) return;


        Integer discussionId = mess.getDiscussionId();
        String pseudo = "'" + mess.getPseudo()+ "'";
        String text = "'" + mess.getText()+ "'";
        String date = "'" + mess.getDate()+ "'";
        String category = "'" + mess.getCategory().toString() +"'";


        SQLiteDatabase mydatabase = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        //values.put("ID", Id);
        values.put("pseudo",pseudo);
        values.put("discussionId", discussionId);
        values.put("text", text);
        values.put("date", date);
        values.put("category", category);

//        String SqlRequest = String.format("INSERT INTO Posts VALUES(%s, %d, %s, %s, %s);",pseudo, discussionId, text, date, category);
//
//        mydatabase.execSQL(SqlRequest);


        int returnCode = (int) mydatabase.insert("Posts", null, values);
    }




    @Override
    public void onNewTransaction(Transaction trans) {

        if (trans == null) return;

        String pseudoOfferer = "'" + trans.getPseudoOfferer() + "'";
        String pseudoDemander = "'" + trans.getPseudoDemander() + "'";
        String offerDescription = "'" + trans.getOfferDescription()+"'";
        String demandDescription = "'" + trans.getDemandDescription()+ "'";
        Integer amount = trans.getAmount();

        SQLiteDatabase mydatabase = this.getWritableDatabase();

//        String SqlRequest = String.format("INSERT INTO Transactions VALUES(%s, %s, %s, %s, %d);",pseudoOfferer, pseudoDemander, offerDescription, demandDescription, amount);
//
//        mydatabase.execSQL(SqlRequest);


        ContentValues values = new ContentValues();
        //values.put("ID", Id);
        values.put("pseudoOfferer",pseudoOfferer);
        values.put("pseudoDemander", pseudoDemander);
        values.put("offerDescription", offerDescription);
        values.put("demandDescription", demandDescription);
        values.put("amount", amount);


        int returnCode = (int) mydatabase.insert("Transactions", null, values);
    }
}

