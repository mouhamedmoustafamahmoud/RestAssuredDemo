package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected RequestSpecification requestSpec;

    @BeforeMethod
    public void startUp(){
        requestSpec = new RequestSpecBuilder().setBaseUri("https://restful-booker.herokuapp.com").build();
    }


    protected Response createBooking() {

        JSONObject body = new JSONObject();
        body.put("firstname", "Mohamed");
        body.put("lastname", "Mahmoud");
        body.put("totalprice", 215);
        body.put("depositpaid", true);
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2022-10-16");
        bookingDates.put("checkout", "2022-11-25");
        body.put("bookingdates", bookingDates);
        body.put("additionalneeds", "Launch");

        return RestAssured.given().contentType(ContentType.JSON).body(body.toString())
                .post("https://restful-booker.herokuapp.com/booking");

    }

    protected Response updateBooking(int bookingId, String firstName, String lastName,
                                     int totalPrice, boolean depositPaid, String checkIn, String checkOut, String additionalNeeds) {
        JSONObject body = new JSONObject();
        body.put("firstname", firstName);
        body.put("lastname", lastName);
        body.put("totalprice", totalPrice);
        body.put("depositpaid", depositPaid);
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", checkIn);
        bookingDates.put("checkout", checkOut);
        body.put("bookingdates", bookingDates);
        body.put("additionalneeds", additionalNeeds);

        return RestAssured.given().auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString())
                .put("https://restful-booker.herokuapp.com/booking/" + bookingId);
    }

    protected Response partialUpdateBooking(int bookingId, String firstName, String lastName) {
        JSONObject body = new JSONObject();
        body.put("firstname", firstName);
        body.put("lastname", lastName);

        return RestAssured.given().auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).body(body.toString())
                .patch("https://restful-booker.herokuapp.com/booking/" + bookingId);
    }
}
