function reduce(arr, reduceFN, initialValue, ...extraArgs) {
    let startIndex = 0;
    let accumulator;

    if (initialValue === undefined) {
        accumulator = arr[0];
        startIndex = 1;
    } else {
        accumulator = initialValue;
    }

    for (let i = startIndex; i < arr.length; i++) {
        accumulator = reduceFN(accumulator, arr[i], ...extraArgs);
    }

    return accumulator;
}

arr=[1,2,3,4]
console.log(reduce(arr, (a,b,c)=> a+b+c, 0,0))