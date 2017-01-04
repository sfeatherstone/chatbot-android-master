package com.schibsted.android.chatbot.data;

import com.google.gson.JsonDeserializer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by simonfea on 28/10/2016.
 */

public class ChatAPI {
    final static String baseUrl = "http://samplesofworkserver20170103074058.azurewebsites.net/api/";

    static public class RetrofitFactory{

        static public Retrofit createRetrofitInstance(HttpLoggingInterceptor.Level loggingLevel) {

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create());

            if (loggingLevel != HttpLoggingInterceptor.Level.NONE) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(loggingLevel);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                builder.client(client);
            }

            return builder.build();
        }
    }
/*
    private class FooDeserializerFromJsonWithDifferentFields implements JsonDeserializer<Chat> {

        @Override
        public Chat deserialize(JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jObject = jElement.getAsJsonObject();
            int intValue = jObject.get("valueInt").getAsInt();
            String stringValue = jObject.get("valueString").getAsString();
            return new Foo(intValue, stringValue);
        }
    }
*/

    public interface API {
        @GET("chat")
        Call<Chats> getChats();
    }

}
