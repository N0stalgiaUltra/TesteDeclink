package com.n0stalgiaultra.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

fun exportDatabase(context: Context): Pair<Boolean, String> {
    val currentDBPath = context.getDatabasePath("declink-test-app-db")

    if(!currentDBPath.isFile){
        return Pair(false, "Banco de dados não encontrado")
    }
    val backupDBPath = File(context.getExternalFilesDir(
        Environment.DIRECTORY_DOCUMENTS
    ), "backup-declink")
    backupDBPath.mkdirs()

    val backupFile = File(backupDBPath, "bd-declink.json")
    return try {
        val db = SQLiteDatabase.openDatabase(
            currentDBPath.absolutePath, null, SQLiteDatabase.OPEN_READWRITE)

        val jsonString = convertDBtoJson(db)
        val outputStream = FileOutputStream(backupFile)
        outputStream.use {
            it.write(jsonString.toByteArray())
        }
        Pair(true, "Banco de dados exportado com sucesso para: ${backupFile.absolutePath}")
    } catch (e: IOException) {
        Pair(false, "Erro ao exportar banco de dados: ${e.message}")
    }
}

fun convertDBtoJson(db: SQLiteDatabase): String{
    val jsonArray = JSONArray()
    val cursor = db.rawQuery("SELECT * FROM PhotoEntity", null)
    cursor.use {
        while (it.moveToNext()){
            val jsonObj = JSONObject()
            for (i in 0 until it.columnCount){
                val columnName = it.getColumnName(i)
                val columnVal = it.getString(i)
                jsonObj.put(columnName, columnVal)
            }
            jsonArray.put(jsonObj)
        }
    }

    cursor.close()
    return jsonArray.toString()
}
