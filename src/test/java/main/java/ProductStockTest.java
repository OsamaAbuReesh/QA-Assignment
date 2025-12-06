package main.java;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductStock Test Suite")
@Tag("regression")
public class ProductStockTest {

    static ProductStock sharedStock;
    ProductStock stock;

    @BeforeAll
    static void beforeAll() {
        sharedStock = new ProductStock("P-1", "WH-1-A1", 10, 5, 100);
    }

    @AfterAll
    static void afterAll() {
        sharedStock = null;
    }

    @BeforeEach
    void setup() {
        stock = new ProductStock("P-1", "WH-1-A1", 10, 5, 100);
    }

    @AfterEach
    void teardown() {
        stock = null;
    }

    @Nested
    @DisplayName("Constructor validations")
    class ConstructorTests {
        @Test
        @DisplayName("Valid constructor creates instance with correct fields")
        void validConstructor() {
            assertAll("created",
                    () -> assertEquals("P-1", stock.getProductId()),
                    () -> assertEquals("WH-1-A1", stock.getLocation()),
                    () -> assertEquals(10, stock.getOnHand()),
                    () -> assertEquals(0, stock.getReserved()),
                    () -> assertEquals(5, stock.getReorderThreshold()),
                    () -> assertEquals(100, stock.getMaxCapacity())
            );
        }

        @Test
        @DisplayName("Invalid constructor parameters throw IllegalArgumentException")
        void invalidConstructor() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock(null, "loc", 0, 0, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("", "loc", 0, 0, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", null, 0, 0, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", "", 0, 0, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", "L", -1, 0, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", "L", 0, -1, 1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", "L", 0, 0, 0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> new ProductStock("ID", "L", 10, 0, 5))
            );
        }
    }

    @Nested
    @DisplayName("Add stock")
    class AddStockTests {
        @Test
        @DisplayName("Add positive amount updates onHand")
        void addStockNormal() {
            stock.addStock(5);
            assertEquals(15, stock.getOnHand());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 20, 90})
        @DisplayName("Add stock parameterized valid amounts")
        void addStockParameterized(int amount) {
            ProductStock p = new ProductStock("X", "L", 0, 0, 100);
            p.addStock(amount);
            assertEquals(amount, p.getOnHand());
        }

        @Test
        @DisplayName("Adding zero or negative throws IllegalArgumentException")
        void addStockInvalidAmount() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.addStock(0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.addStock(-1))
            );
        }

        @Test
        @DisplayName("Adding beyond maxCapacity throws IllegalStateException")
        void addStockBeyondCapacity() {
            ProductStock p = new ProductStock("X", "L", 95, 0, 100);
            assertThrows(IllegalStateException.class, () -> p.addStock(6));
            p.addStock(5);
            assertEquals(100, p.getOnHand());
        }
    }

    @Nested
    @DisplayName("Reserve and release stock")
    class ReserveReleaseTests {
        @Test
        @DisplayName("Reserve valid amount reduces available")
        void reserveNormal() {
            stock.reserve(3);
            assertEquals(3, stock.getReserved());
            assertEquals(7, stock.getAvailable());
        }

        @Test
        @DisplayName("Reserving more than available throws")
        void reserveTooMuch() {
            assertThrows(IllegalStateException.class, () -> stock.reserve(11));
        }

        @Test
        @DisplayName("Release reserved amount works and errors when releasing too much")
        void releaseReservation() {
            stock.reserve(4);
            stock.releaseReservation(2);
            assertEquals(2, stock.getReserved());
            assertThrows(IllegalStateException.class, () -> stock.releaseReservation(3));
        }

        @Test
        @DisplayName("Reserve/release invalid amounts throw IllegalArgumentException")
        void reserveReleaseInvalidAmount() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.reserve(0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.releaseReservation(0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.reserve(-1)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.releaseReservation(-1))
            );
        }
    }

    @Nested
    @DisplayName("Shipping reserved stock")
    class ShipReservedTests {
        @Test
        @DisplayName("Ship reserved reduces both reserved and onHand")
        void shipReservedNormal() {
            stock.reserve(5);
            stock.shipReserved(3);
            assertEquals(2, stock.getReserved());
            assertEquals(7, stock.getOnHand());
        }

        @Test
        @DisplayName("Shipping more than reserved throws")
        void shipMoreThanReserved() {
            stock.reserve(2);
            assertThrows(IllegalStateException.class, () -> stock.shipReserved(3));
        }

        @Test
        @DisplayName("Shipping invalid amounts throw")
        void shipInvalidAmounts() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.shipReserved(0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.shipReserved(-1))
            );
        }
    }

    @Nested
    @DisplayName("Remove damaged stock")
    class RemoveDamagedTests {
        @Test
        @DisplayName("Remove damaged normal reduces onHand and respects reserved cap")
        void removeDamagedNormal() {
            stock.reserve(8); // reserved=8, onHand=10
            stock.removeDamaged(5); // onHand->5, reserved should be capped to 5
            assertEquals(5, stock.getOnHand());
            assertEquals(5, stock.getReserved());
        }

        @Test
        @DisplayName("Remove more than onHand throws")
        void removeTooMuch() {
            assertThrows(IllegalStateException.class, () -> stock.removeDamaged(11));
        }

        @Test
        @DisplayName("Remove invalid amount throws")
        void removeInvalidAmount() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.removeDamaged(0)),
                    () -> assertThrows(IllegalArgumentException.class, () -> stock.removeDamaged(-1))
            );
        }
    }

    @Nested
    @DisplayName("Reorder logic and thresholds")
    class ReorderTests {
        @Test
        @DisplayName("isReorderNeeded true when available < threshold")
        void reorderNeededTrue() {
            ProductStock p = new ProductStock("A", "L", 5, 6, 100);
            assertTrue(p.isReorderNeeded());
        }

        @Test
        @DisplayName("isReorderNeeded false when available >= threshold")
        void reorderNeededFalse() {
            ProductStock p = new ProductStock("A", "L", 10, 5, 100);
            assertFalse(p.isReorderNeeded());
        }

        @Test
        @DisplayName("updateReorderThreshold enforces bounds")
        void updateThreshold() {
            stock.updateReorderThreshold(20);
            assertEquals(20, stock.getReorderThreshold());
            assertThrows(IllegalArgumentException.class, () -> stock.updateReorderThreshold(-1));
            assertThrows(IllegalArgumentException.class, () -> stock.updateReorderThreshold(1000));
        }

        @Test
        @DisplayName("updateMaxCapacity adjusts and enforces invariants")
        void updateMaxCapacity() {
            stock.addStock(10); // onHand 20
            assertThrows(IllegalStateException.class, () -> stock.updateMaxCapacity(5));
            stock.updateMaxCapacity(200);
            assertEquals(200, stock.getMaxCapacity());
            stock.updateReorderThreshold(180);
            stock.updateMaxCapacity(150); // reorderThreshold should be capped to 150
            assertEquals(150, stock.getMaxCapacity());
            assertEquals(150, stock.getReorderThreshold());
        }

        @Test
        @DisplayName("updateMaxCapacity invalid non-positive throws")
        void updateMaxCapacityInvalidNonPositive() {
            assertThrows(IllegalArgumentException.class, () -> stock.updateMaxCapacity(0));
            assertThrows(IllegalArgumentException.class, () -> stock.updateMaxCapacity(-5));
        }
    }

    @Test
    @DisplayName("changeLocation rejects invalid and changes location")
    void changeLocationTest() {
        assertThrows(IllegalArgumentException.class, () -> stock.changeLocation(null));
        assertThrows(IllegalArgumentException.class, () -> stock.changeLocation("  "));
        stock.changeLocation("WH-2-B2");
        assertEquals("WH-2-B2", stock.getLocation());
    }

    @Test
    @DisplayName("toString contains useful info")
    void toStringContainsInfo() {
        String s = stock.toString();
        assertAll(() -> assertTrue(s.contains("productId='P-1'")),
                () -> assertTrue(s.contains("onHand=10")));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Timeout example - quick test")
    @Timeout(1)
    void quickTimeoutTest() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            Thread.sleep(10);
        });
    }

    @Disabled("Future feature: bulkReserve not implemented yet")
    @Test
    void futureFeatureDisabled() {
    }
}
