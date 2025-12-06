# ProductStock Test Case Design

| ID | Method / Feature | Preconditions | Inputs | Expected Result | Type |
|----|------------------|---------------|--------|-----------------|------|
| TC-01 | Constructor valid | none | ("P-1","L",0,0,1) | instance created with fields set | normal |
| TC-02 | Constructor invalid productId | none | (null, ... ) or ("",...) | IllegalArgumentException | error |
| TC-03 | Constructor invalid location | none | (.., null) or (.., "") | IllegalArgumentException | error |
| TC-04 | Constructor bad quantities | none | negative initialOnHand, negative threshold, non-positive maxCapacity, initialOnHand>maxCapacity | IllegalArgumentException | error |
| TC-05 | addStock normal | onHand=10, maxCapacity=100 | add 5 | onHand=15 | normal |
| TC-06 | addStock boundary | onHand=95, maxCapacity=100 | add 5 | onHand=100 | boundary |
| TC-07 | addStock exceed capacity | onHand=95 | add 6 | IllegalStateException | error |
| TC-08 | addStock invalid amount | any | add 0 or negative | IllegalArgumentException | error |
| TC-09 | reserve normal | onHand=10, reserved=0 | reserve 3 | reserved=3, available=7 | normal |
| TC-10 | reserve too much | available < amount | reserve more than available | IllegalStateException | error |
| TC-11 | reserve invalid amount | any | reserve 0 or negative | IllegalArgumentException | error |
| TC-12 | releaseReservation normal | reserved=5 | release 2 | reserved=3 | normal |
| TC-13 | releaseReservation too much | reserved=2 | release 3 | IllegalStateException | error |
| TC-14 | release invalid amount | any | release 0 or negative | IllegalArgumentException | error |
| TC-15 | shipReserved normal | reserved>=amt, onHand>=amt | ship amt | reserved & onHand reduced by amt | normal |
| TC-16 | shipReserved more than reserved | reserved < amt | ship | IllegalStateException | error |
| TC-17 | shipReserved invalid | any | ship 0 or negative | IllegalArgumentException | error |
| TC-18 | removeDamaged normal | onHand>=amt | remove amt | onHand decreased; if reserved>onHand then reserved set to onHand | normal |
| TC-19 | removeDamaged too much | amount>onHand | remove | IllegalStateException | error |
| TC-20 | removeDamaged invalid | any | remove 0 or negative | IllegalArgumentException | error |
| TC-21 | isReorderNeeded true | available < threshold | any | returns true | normal |
| TC-22 | isReorderNeeded false | available >= threshold | any | returns false | normal |
| TC-23 | updateReorderThreshold valid | maxCapacity known | newThreshold within [0,maxCapacity] | updates value | normal |
| TC-24 | updateReorderThreshold invalid | any | newThreshold <0 or >maxCapacity | IllegalArgumentException | error |
| TC-25 | updateMaxCapacity increase | onHand <= newMax | newMax>0 | maxCapacity updated | normal |
| TC-26 | updateMaxCapacity decrease below onHand | newMax < onHand | newMax | IllegalStateException | error |
| TC-27 | updateMaxCapacity triggers reorderThreshold cap | reorderThreshold > newMax | newMax>=onHand but < oldThreshold | reorderThreshold set to newMax | boundary |
| TC-28 | changeLocation valid | any | non-blank location | location updated | normal |
| TC-29 | changeLocation invalid | any | blank/null | IllegalArgumentException | error |

This table covers normal, boundary, and error cases for the core public API of `ProductStock`.
