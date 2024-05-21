package com.example.feeds.services.api_calls

import android.content.Context
import android.widget.Toast
import com.example.feeds.constants.Constants
import com.example.feeds.interfaces.ApiInterface
import com.example.feeds.models.DataModel
import com.example.feeds.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCalls {
    private val client: ApiClient = ApiClient()
    private val constants: Constants = Constants()

    fun getJokes(context: Context, callback: (DataModel) -> Unit) {
        val apiInterface = client.createClient(constants.getBaseUrl()).create(ApiInterface::class.java)
        val call: Call<DataModel> = apiInterface.getJokes()
        call.enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.isSuccessful) {
                    val jokes: DataModel = response.body() as DataModel
                    callback(jokes)
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                Toast.makeText(context, "Request failed", Toast.LENGTH_LONG).show()
            }
        })
    }
}