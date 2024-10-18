package mizdooni.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.utils.SampleUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TableTest {
    private Table table;

    @BeforeEach
    public void setup() {
        table = create_sample_table();
    }

    @Test
    public void add_new_reservation_added_to_the_reservations() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        assertEquals(1, table.getReservations().size());
        assertEquals(reservation, table.getReservations().getFirst());
    }

    @Test
    public void check_reserved_for_a_non_reserved_datetime_returns_false() {
        assertFalse(table.isReserved(LocalDateTime.now()));
    }

    @Test
    public void check_reserved_for_a_reserved_datetime_returns_true() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        assertTrue(table.isReserved(reservation.getDateTime()));
    }

    @Test
    public void check_reserved_for_a_cancelled_datetime_returns_false() {
        Reservation reservation = create_sample_reservation();
        table.addReservation(reservation);
        reservation.cancel();
        assertFalse(table.isReserved(reservation.getDateTime()));
    }

}
