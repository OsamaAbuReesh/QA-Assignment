import { ProductStock } from "../src/ProductStock";

describe("ProductStock Test Suite", () => {

  let stock: ProductStock;

  // === Lifecycle ===
  beforeAll(() => {
    console.log("Starting ProductStock tests");
  });

  beforeEach(() => {
    stock = new ProductStock(50, 0, 10, 100);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  afterAll(() => {
    console.log("Finished ProductStock tests");
  });

  // === Constructor Tests ===
  describe("Constructor validation", () => {

    test("Valid construction", () => {
      expect(() => new ProductStock(10, 0, 5, 50)).not.toThrow();
    });

    test("Invalid constructor parameters", () => {
      expect(() => new ProductStock(-1, 0, 5, 50)).toThrow();
    });

  });

  // === addStock ===
  describe("addStock behavior", () => {

    test.each([1, 10, 40])(
      "Adding valid stock amount: %i",
      (amount) => {
        stock.addStock(amount);
        expect(stock.isReorderNeeded()).toBe(false);
      }
    );

    test("Adding stock beyond capacity throws error", () => {
      expect(() => stock.addStock(100)).toThrow();
    });

    test("Adding zero or negative stock throws error", () => {
      expect(() => stock.addStock(0)).toThrow();
      expect(() => stock.addStock(-5)).toThrow();
    });

  });

  // === Reserve & Release ===
  describe("Reservation logic", () => {

    test("Reserve and release stock correctly", () => {
      stock.reserve(10);
      stock.releaseReservation(5);
      expect(stock.isReorderNeeded()).toBe(false);
    });

    test("Reserving more than available throws error", () => {
      expect(() => stock.reserve(100)).toThrow();
    });

  });

  // === Shipping ===
  describe("Shipping reserved stock", () => {

    test("Ship reserved stock successfully", () => {
      stock.reserve(10);
      stock.shipReserved(5);
      expect(stock.isReorderNeeded()).toBe(false);
    });

  });

  // === Damaged Stock ===
  describe("Removing damaged stock", () => {

    test("Remove damaged stock", () => {
      stock.removeDamaged(5);
      expect(stock.isReorderNeeded()).toBe(false);
    });

    test("Removing more than available throws error", () => {
      expect(() => stock.removeDamaged(100)).toThrow();
    });

  });

  // === Reorder Logic ===
  describe("Reorder logic", () => {

    test("Reorder needed when below threshold", () => {
      stock.removeDamaged(45);
      expect(stock.isReorderNeeded()).toBe(true);
    });

  });

  // === Updates ===
  describe("Updating thresholds and capacity", () => {

    test("Update reorder threshold", () => {
      stock.updateReorderThreshold(20);
      expect(stock.isReorderNeeded()).toBe(false);
    });

    test("Update max capacity", () => {
      stock.updateMaxCapacity(200);
      expect(() => stock.addStock(100)).not.toThrow();
    });

  });

  // === Disabled Test ===
  test.skip("Future feature: auto-reorder integration", () => {
    // To be implemented later
  });

});
