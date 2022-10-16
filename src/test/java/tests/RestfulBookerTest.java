package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class RestfulBookerTest extends BaseTest{


    @Test(description = "Check the health of the API")
    public void checkApiHealthTest(){
        RestAssured.given()
                       .spec(requestSpec)
                   .when()
                       .get("/ping")
                   .then()
                       .assertThat()
                       .statusCode(201);
    }

    @Test(description = "Verify that there is more than one booking in the API (Without Filter)")
    public void getBookingIdsWithoutFilterTest(){
        Response response = RestAssured.given(requestSpec).get("/booking");
        response.print();
        Assert.assertEquals(response.statusCode(), 200);
        List<Integer> bookingIds  = response.jsonPath().getList("bookingid");
        System.out.println(bookingIds.size());
        Assert.assertFalse(bookingIds.isEmpty());
    }

    @Test(description = "Verify that there is more than one booking in the API (With Filter)")
    public void getBookingIdsWithFilterTest(){
        requestSpec.queryParam("firstname", "James");
        Response response = RestAssured.given(requestSpec).get("/booking");
        response.print();
        Assert.assertEquals(response.statusCode(), 200);
        List<Integer> bookingIds  = response.jsonPath().getList("bookingid");
        System.out.println(bookingIds.size());
        Assert.assertFalse(bookingIds.isEmpty());
    }

    @Test(description = "Verify the booking with id equal to 720")
    public void getBookingIdWithoutFilterTest(){
        Response createResponse = createBooking();
        requestSpec.pathParam("bookingId", createResponse.jsonPath().getInt("bookingid"));
        Response response = RestAssured.given(requestSpec).get("/booking/{bookingId}");
        response.print();
        Assert.assertEquals(response.statusCode(), 200);

        String firstName = response.jsonPath().getString("firstname");
        String lastName = response.jsonPath().getString("lastname");
        int totalPrice = response.jsonPath().getInt("totalprice");
        boolean depositPaid = response.jsonPath().getBoolean("depositpaid");
        String checkIn = response.jsonPath().getString("bookingdates.checkin");
        String checkOut = response.jsonPath().getString("bookingdates.checkout");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(firstName, "Mohamed");
        softAssert.assertEquals(lastName, "Mahmoud");
        softAssert.assertEquals(totalPrice, 215);
        softAssert.assertTrue(depositPaid);
        softAssert.assertEquals(checkIn, "2022-10-16");
        softAssert.assertEquals(checkOut, "2022-11-25");
        softAssert.assertAll();

    }

    @Test(description = "Verify that user can create booking")
    public void createBookingTest(){
        Response response = createBooking();
        response.print();

        Assert.assertEquals(response.statusCode(), 200);

        String firstName = response.jsonPath().getString("booking.firstname");
        String lastName = response.jsonPath().getString("booking.lastname");
        int totalPrice = response.jsonPath().getInt("booking.totalprice");
        boolean depositPaid = response.jsonPath().getBoolean("booking.depositpaid");
        String checkIn = response.jsonPath().getString("booking.bookingdates.checkin");
        String checkOut = response.jsonPath().getString("booking.bookingdates.checkout");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(firstName, "Mohamed");
        softAssert.assertEquals(lastName, "Mahmoud");
        softAssert.assertEquals(totalPrice, 215);
        softAssert.assertTrue(depositPaid);
        softAssert.assertEquals(checkIn, "2022-10-16");
        softAssert.assertEquals(checkOut, "2022-11-25");
        softAssert.assertAll();

    }

    @Test(description = "Verify that user can update a booking")
    public void updateBookingTest(){
        Response createResponse = createBooking();
        createResponse.print();
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        Response updateResponse = updateBooking(bookingId, "Mostafa", "Mahmoud",
                150, false, "2022-10-20", "2022-11-20", "Breakfast");
        updateResponse.print();

        Assert.assertEquals(updateResponse.statusCode(), 200);

        String firstName = updateResponse.jsonPath().getString("firstname");
        String lastName = updateResponse.jsonPath().getString("lastname");
        int totalPrice = updateResponse.jsonPath().getInt("totalprice");
        boolean depositPaid = updateResponse.jsonPath().getBoolean("depositpaid");
        String checkIn = updateResponse.jsonPath().getString("bookingdates.checkin");
        String checkOut = updateResponse.jsonPath().getString("bookingdates.checkout");
        String additionalNeeds = updateResponse.jsonPath().getString("additionalneeds");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(firstName, "Mostafa");
        softAssert.assertEquals(lastName, "Mahmoud");
        softAssert.assertEquals(totalPrice, 150);
        softAssert.assertFalse(depositPaid);
        softAssert.assertEquals(checkIn, "2022-10-20");
        softAssert.assertEquals(checkOut, "2022-11-20");
        softAssert.assertEquals(additionalNeeds, "Breakfast");
        softAssert.assertAll();

    }

    @Test(description = "Verify that user can partially update a booking")
    public void partialUpdateBookingTest(){
        Response createResponse = createBooking();
        createResponse.print();
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        Response updateResponse = partialUpdateBooking(bookingId, "Karem", "Ahmed");
        updateResponse.print();

        Assert.assertEquals(updateResponse.statusCode(), 200);

        String firstName = updateResponse.jsonPath().getString("firstname");
        String lastName = updateResponse.jsonPath().getString("lastname");
        int totalPrice = updateResponse.jsonPath().getInt("totalprice");
        boolean depositPaid = updateResponse.jsonPath().getBoolean("depositpaid");
        String checkIn = updateResponse.jsonPath().getString("bookingdates.checkin");
        String checkOut = updateResponse.jsonPath().getString("bookingdates.checkout");
        String additionalNeeds = updateResponse.jsonPath().getString("additionalneeds");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(firstName, "Karem");
        softAssert.assertEquals(lastName, "Ahmed");
        softAssert.assertEquals(totalPrice, 215);
        softAssert.assertTrue(depositPaid);
        softAssert.assertEquals(checkIn, "2022-10-16");
        softAssert.assertEquals(checkOut, "2022-11-25");
        softAssert.assertEquals(additionalNeeds, "Launch");
        softAssert.assertAll();

    }

    @Test(description = "Verify that user can delete a booking")
    public void deleteBookingTest(){

        Response response = createBooking();
        response.print();
        int bookingId = response.jsonPath().getInt("bookingid");
        Response deleteResponse = RestAssured.given().auth().preemptive().basic("admin", "password123")
                .get("https://restful-booker.herokuapp.com/booking/" + bookingId);
        Assert.assertEquals(deleteResponse.statusCode(), 200);

    }
}
