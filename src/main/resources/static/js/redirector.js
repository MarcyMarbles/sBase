/**
 * Redirect to a URL after a given delay.
 * @param {string} url - The destination URL.
 * @param {number} delay - Delay in milliseconds (default: 1000 ms).
 */
function redirectTo(url, delay = 1000) {
    setTimeout(function () {
        window.location.href = url;
    }, delay);
}