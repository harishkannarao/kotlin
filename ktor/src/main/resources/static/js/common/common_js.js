function deepCopy(input) {
    return JSON.parse(JSON.stringify(input));
}

function toIsoUtcTimeStamp(epochMillis) {
    return (new Date(Number(epochMillis))).toISOString();
}