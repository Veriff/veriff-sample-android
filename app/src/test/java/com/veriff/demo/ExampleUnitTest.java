package com.veriff.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import com.veriff.demo.data.TokenPayload;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void gsonConversionTest() {
        String pay = "{\"verification\":{\"document\":{\"number\":\"B01234567\",\"type\":\"ID_CARD\",\"country\":\"EE\"},\"additionalData\":{\"placeOfResidence\":\"Tartu\",\"citizenship\":\"EE\"},\"timestamp\":\"2018-12-12T11:02:05.261Z\",\"lang\":\"et\",\"features\":[\"selfid\",\"video_call\"],\"person\":{\"firstName\":\"Tundmatu\",\"idNumber\":\"38508260269\",\"lastName\":\"Toomas\"}}}";
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
        TokenPayload payload = gson.fromJson(pay, TokenPayload.class);
        String newPay = gson.toJson(payload);
        Assert.assertEquals(pay, newPay);
    }
}