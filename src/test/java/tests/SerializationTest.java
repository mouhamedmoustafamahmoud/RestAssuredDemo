package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import serialization.Booking;
import serialization.BookingDates;
import serialization.ResponseBody;

public class SerializationTest extends BaseTest{

    @Test
    public void createBookingUsingPOJOsTest(){

        BookingDates bookingDates = new BookingDates("2022-05-25", "2022-06-12");
        Booking booking = new Booking("Nabil", "Ahmed", 245, true, bookingDates, "Breakfast");
        Response response = RestAssured.given(requestSpec).contentType(ContentType.JSON).body(booking).post("/booking");
        response.print();

        Assert.assertEquals(response.statusCode(), 200);
        ResponseBody responseBody = response.as(ResponseBody.class);
        Assert.assertEquals(responseBody.getBooking().toString(), booking.toString());

    }
}
