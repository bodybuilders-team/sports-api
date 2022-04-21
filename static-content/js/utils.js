export function range(startNumber, endNumber) {
    return Array(endNumber - startNumber + 1)
        .fill(0)
        .map((_, index) => startNumber + index);
}
