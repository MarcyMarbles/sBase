function validatePasswords() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorMessage = document.getElementById('error-message');

    if (password !== confirmPassword) {
        errorMessage.innerHTML = 'Passwords do not match';
        errorMessage.style.display = 'block';
        return false;
    }

    return true;
}

function checkIfAdmin() {
    const admin = 'admin';
    const login = document.getElementById('username').value;
    const errorMessage = document.getElementById('error-message');

    if (login === admin) {
        errorMessage.innerHTML = "а ты смешной)";
        errorMessage.style.display = 'block';
        return false;
    }

    return true;
}

function validateForm() {
    const passwordsValid = validatePasswords();
    const adminCheckValid = checkIfAdmin();

    if (!passwordsValid || !adminCheckValid) {
        return false;
    }

    return true;
}
