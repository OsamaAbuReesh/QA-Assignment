export class ProductStock {
  constructor(
    private stock: number,
    private reserved: number,
    private reorderThreshold: number,
    private maxCapacity: number
  ) {
    if (stock < 0 || reserved < 0 || reorderThreshold < 0 || maxCapacity <= 0) {
      throw new Error("Invalid constructor parameters");
    }
  }

  addStock(amount: number) {
    if (amount <= 0) throw new Error("Invalid amount");
    if (this.stock + amount > this.maxCapacity) throw new Error("Exceeds capacity");
    this.stock += amount;
  }

  reserve(amount: number) {
    if (amount <= 0 || amount > this.stock) throw new Error("Cannot reserve");
    this.stock -= amount;
    this.reserved += amount;
  }

  releaseReservation(amount: number) {
    if (amount <= 0 || amount > this.reserved) throw new Error("Invalid release");
    this.reserved -= amount;
    this.stock += amount;
  }

  shipReserved(amount: number) {
    if (amount <= 0 || amount > this.reserved) throw new Error("Cannot ship");
    this.reserved -= amount;
  }

  removeDamaged(amount: number) {
    if (amount <= 0 || amount > this.stock) throw new Error("Invalid removal");
    this.stock -= amount;
  }

  isReorderNeeded() {
    return this.stock <= this.reorderThreshold;
  }

  updateReorderThreshold(value: number) {
    if (value < 0) throw new Error("Invalid threshold");
    this.reorderThreshold = value;
  }

  updateMaxCapacity(value: number) {
    if (value <= 0) throw new Error("Invalid capacity");
    this.maxCapacity = value;
  }
}
