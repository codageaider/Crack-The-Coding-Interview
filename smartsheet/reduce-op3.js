function reduce(iterable, reduceFN, initialValue, ...extraArgs) {
    // Check if iterable is not null or undefined
    if (iterable == null) {
        throw new TypeError("First argument must be an iterable, got null or undefined");
    }

    // Check if the second argument is a function
    if (typeof reduceFN !== 'function') {
        throw new TypeError("Second argument must be a function");
    }

    // Check if iterable is an actual iterable
    if (typeof iterable[Symbol.iterator] !== 'function') {
        throw new TypeError("First argument must be an iterable");
    }

    const iterator = iterable[Symbol.iterator]();
    let accumulator;
    let startIndex = 0;
    
    if (initialValue === undefined) {
        const first = iterator.next();
        if (first.done) {
            throw new TypeError("Reduce of empty iterable with no initial value");
        }
        accumulator = first.value;
    } else {
        accumulator = initialValue;
    }

    for (let next = iterator.next(); !next.done; next = iterator.next()) {
        accumulator = reduceFN(accumulator, next.value, ...extraArgs);
    }

    return accumulator;
}

// Example usage:

// A reduce function that takes three arguments: accumulator, current value, and an extra argument
function sumWithExtra(acc, curr, extra) {
    return acc + curr + extra;
}

try {
    const numbers = [1, 2, 3, 4];
    const result = reduce(numbers, sumWithExtra, 0, 1); // The extra argument is 1
    console.log(result); // Output: 14 (1+1 + 2+1 + 3+1 + 4+1)

    const strIterable = 'abcd';
    const concatResult = reduce(strIterable, (acc, curr) => acc + curr, '');
    console.log(concatResult); // Output: "abcd"

    const setIterable = new Set([1, 2, 3]);
    const setResult = reduce(setIterable, (acc, curr) => acc + curr, 0);
    console.log(setResult); // Output: 6
} catch (error) {
    console.error(error.message);
}
