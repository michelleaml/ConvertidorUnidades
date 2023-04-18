package com.example.convertidorunidades.database;

import static java.util.Collections.shuffle;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vicentearmenta.androidtriviatesting.models.Answer;
import com.vicentearmenta.androidtriviatesting.models.Question;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    private SQLiteDatabase mDatabase;

    private final DatabaseHelper mHelper;

    public DatabaseOperations(Context context){
        mHelper = new DatabaseHelper(context);
        this.open();
    }

    public void open() throws SQLException{
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close(){
        if (mDatabase != null && mDatabase.isOpen()){
            mDatabase.close();
        }
    }

    public String insertUsername(String username){
        if (!mDatabase.isOpen()){
            this.open();
        } // Si la instancia de la base de datos este cerrada la abierta

        mDatabase.beginTransaction(); // Base de datos transaccionales -> No se hace el comit hasta
        // que nosotros lo terminemos

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RS_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_RS_SCORE, 0);
        long lastRowID = mDatabase.insert(DatabaseHelper.TABLE_RESULT, null, values);

        mDatabase.setTransactionSuccessful();
        // Transaction para hacer rollback
        // No se hacen commit a la base de datos hasta que setTransactionSuccesful
        // Util para cuando tienes datos importantes

        mDatabase.endTransaction();

        this.close();

        return Long.toString(lastRowID);
    }

    public int updateScore(String userID){
        if (!mDatabase.isOpen()){
            this.open();
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RS_SCORE, DatabaseHelper.COLUMN_RS_SCORE + " + 1");
        int rowsUpdated = mDatabase.update(
                DatabaseHelper.TABLE_RESULT,
                values,
                DatabaseHelper.COLUMN_RS_ID + " = ?",
                // ? Es un placeholder, y los valores van en String array con un chorro de valores
                // where donde se puede anidar las diferentes tablas como en where SQLite
                new String[]{ userID });

        // Rows updated va a regresar el # de columnas que se actualizan esta en teoria tiene que ser mayor a 1
        // Si no se actualiza la columna esta regresará 0

        this.close();

        return rowsUpdated;
    }

    public Question getNextQuestion(String questionAlreadyAsked){
        if (!mDatabase.isOpen()){
            // is te regresa un booleano normalemente
            // ! para negación
            this.open();
        }

        String questionId = null;
        String questionText = null;
        String questionAnswer = null;

        // Variables almacenadas en memoria

        // Cursor para sacar info de la base de datos
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_QUESTION, // Que tabla es la info a consultar
                new String[]{ // Columnas o campos que ocupamos
                        DatabaseHelper.COLUMN_QT_ID,
                        DatabaseHelper.COLUMN_QT_TEXT,
                        DatabaseHelper.COLUMN_QT_ANSWER },
                DatabaseHelper.COLUMN_QT_ID + " NOT IN ( ? )", // Filtro o Query (Where)
                new String[]{ questionAlreadyAsked },
                null,
                null,
                "RANDOM()",
                "1"
        );

        while(cursor.moveToNext()){ // Se trata como si fuera un conjunto de datos
            // Automaticamente se mueve al siguiente registro y regresa un bool
            questionId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_ID));
            // getColumnIndexOrThrow = Regresa el numero del orden de la columna que se están buscanod
            questionText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_TEXT));
            questionAnswer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QT_ANSWER));
        }

        cursor.close(); // Siempre cerrar los cursores

        List<Answer> options = new ArrayList<>();

        // Llevar la opción A-D que es la opción correcta
        cursor = mDatabase.query(
                DatabaseHelper.TABLE_ANSWER,
                new String[]{
                        DatabaseHelper.COLUMN_AW_ID,
                        DatabaseHelper.COLUMN_AW_TEXT },
                DatabaseHelper.COLUMN_AW_ID + " = ? ",
                new String[]{ questionAnswer },
                null,
                null,
                null
        );

        // No es necesario el limit (en este caso es 1)

        // SELECT * FROM tabla WHERE column1 = ? AND column2 = ?
        // Se puede

        while(cursor.moveToNext()){
            Answer option = new Answer();
            option.setAnswerId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_ID)));
            option.setAnswerText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_TEXT)));

            options.add(option);
        }

        cursor.close();

        // opciones restantes

        cursor = mDatabase.query(
                DatabaseHelper.TABLE_ANSWER,
                new String[]{
                        DatabaseHelper.COLUMN_AW_ID,
                        DatabaseHelper.COLUMN_AW_TEXT },
                DatabaseHelper.COLUMN_AW_ID + " NOT IN ( ? )",
                new String[]{ questionAnswer },
                null,
                null,
                "RANDOM()",
                "3"
        );

        while(cursor.moveToNext()){
            Answer option = new Answer();
            option.setAnswerId(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_ID)));
            option.setAnswerText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AW_TEXT)));

            options.add(option);
        }

        cursor.close();

        shuffle(options);

        Question nextQuestion = new Question(
                questionId,
                questionText,
                questionAnswer,
                options.get(0),
                options.get(1),
                options.get(2),
                options.get(3)
        );

        return nextQuestion;

    }
    @SuppressLint("Range")
    public List<List<String>> getTopUsers() {
        // saves the users from every game
        List<List<String>> users = new ArrayList<>();

        // the purpose of a cursor is to point to a single row of the result fetched by the query
        Cursor cursor = mDatabase.rawQuery("SELECT RSUserName, RSScore " +
                "FROM result " +
                "ORDER BY RSScore " +
                "DESC LIMIT 10", null); //Descendent Order

        // adds the user / points gathered
        while (cursor.moveToNext()) {
            List<String> user = new ArrayList<>();
            user.add(cursor.getString(cursor.getColumnIndex("RSUserName")));
            user.add(cursor.getString(cursor.getColumnIndex("RSScore")));
            users.add(user);
        }

        cursor.close();

        return users;
    }
}
