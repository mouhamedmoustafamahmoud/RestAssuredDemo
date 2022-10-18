package serialization;

import lombok.Data;

@Data
public class ResponseBody {

    private int bookingid;
    private Booking booking;

    public ResponseBody(int bookingid, Booking booking) {
        this.bookingid = bookingid;
        this.booking = booking;
    }

    public ResponseBody() {
    }
}
