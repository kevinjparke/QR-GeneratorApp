package com.example.myapplication.data

import android.content.Context
import android.graphics.BitmapFactory
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.model.QRItem
import org.json.JSONObject

object DataSource {
    var qrCodes = mutableListOf<QRItem>()
    private var endpoint = "https://ka25jneepi.execute-api.us-east-1.amazonaws.com/alpha"

    //Call from onCreate in Main Activity
    fun loadQrCodes(context: Context){
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val volleyQueue = Volley.newRequestQueue(context)
        val url = endpoint

        val qrList = mutableListOf<QRItem>()

        val jsonObject = JSONObject()
        jsonObject.put("input","{\"type\": \"RETRIEVE\", \"deviceId\": \"$androidId\"}")
        jsonObject.put("stateMachineArn", "arn:aws:states:us-east-1:638997125533:stateMachine:project_state_machine_express_fn")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonObject,
            { response ->
                // get the images array from the response body
                val bodyString = response.getString("response")
                val bodyJson = JSONObject(bodyString)
                val imagesJsonArray = bodyJson.getJSONArray("images")

                for (i in 0 until imagesJsonArray.length()) {
                    val imageJson = imagesJsonArray.getJSONObject(i)
                    val imageString = imageJson.getString("image")
                    val filenameString = imageJson.getString("filename").substringAfter("/").substringBefore(".png")

                    // decode the base64-encoded image string
                    val decodedBytes = Base64.decode(imageString, Base64.DEFAULT)
                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                    val newQRCode = QRItem(decodedBitmap, filenameString)
                    qrCodes.add(newQRCode)
                }
            },
            { error ->
                Toast.makeText(context, "Some error occurred! Cannot fetch images", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "loadImages error: ${error.localizedMessage}")
            }
        )

        volleyQueue.add(jsonObjectRequest)
    }

    fun addQRCode(context: Context, stringToGenerate: String, filename: String, detail: Int) {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val volleyQueue = Volley.newRequestQueue(context)
        val url = endpoint

        val jsonObject = JSONObject()
        jsonObject.put("input", "{\"type\": \"GENERATE\", \"deviceId\": \"$androidId\", \"string_to_generate\": \"$stringToGenerate\", \"filename\": \"$filename\", \"detail\": $detail}")
        jsonObject.put("stateMachineArn", "arn:aws:states:us-east-1:638997125533:stateMachine:project_state_machine_express_fn")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            { response ->
                val bodyString = response.getString("body")
                val bodyJson = JSONObject(bodyString)
                val imageString = bodyJson.getString("image")

                print(imageString)
                val decodedBytes = Base64.decode(imageString, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                val newQrItem = QRItem(decodedBitmap, filename)
                DataSource.qrCodes.add(newQrItem)
            },
            { error ->
                Toast.makeText(context, "Something went wrong while generating your QRCode", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Generate error: ${error.localizedMessage}")
            }
        )
        volleyQueue.add(jsonObjectRequest)
    }

    fun deleteQRCode(context: Context, filename: String, position: Int){
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val volleyQueue = Volley.newRequestQueue(context)
        val url = endpoint

        val jsonObject = JSONObject()
        jsonObject.put("input", "{\"type\": \"DELETE\", \"deviceId\": \"$androidId\", \"filename\": \"$filename\"}")
        jsonObject.put("stateMachineArn", "arn:aws:states:us-east-1:638997125533:stateMachine:project_state_machine_express_fn")


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonObject,

            { response ->
                val bodyString = response.getString("body")
                Toast.makeText(context, bodyString, Toast.LENGTH_SHORT).show()
                qrCodes.removeAt(position)
            },
            { error ->
                Toast.makeText(context, "There was an error deleting your image", Toast.LENGTH_LONG).show()
                Log.e("Delete Activity", "Error: ${error.localizedMessage}")
            }
        )
        volleyQueue.add(jsonObjectRequest)
    }
}