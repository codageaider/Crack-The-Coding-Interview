
function reduce(arr, reduceFN, initialValue) {
    let startIndex = 0;
    let accumulator;

    if (initialValue === undefined) {
        accumulator = arr[0];
        startIndex = 1;
    } else {
        accumulator = initialValue;
    }

    for (let i = startIndex; i < arr.length; i++) {
        accumulator = reduceFN(accumulator, arr[i]);
    }

    return accumulator;
}

arr=[1,2,3,4]
console.log(reduce(arr, (a,b)=> a+b))
console.log(reduce(arr, (a,b)=> a*b))
console.log(reduce(arr, (a,b)=> a*b,2))