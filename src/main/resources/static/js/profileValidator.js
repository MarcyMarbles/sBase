
$(document).ready(function () {
    const $newPassword = $('#newPassword');
    const $confirmPassword = $('#confirmPassword');
    const $passwordMismatchError = $('#passwordMismatchError');

    // Password Confirmation Validation
    function validatePassword() {
        if ($newPassword.val() !== $confirmPassword.val()) {
            $passwordMismatchError.text('Passwords do not match');
            $confirmPassword.get(0).setCustomValidity('Passwords do not match');
        } else {
            $passwordMismatchError.text('');
            $confirmPassword.get(0).setCustomValidity('');
        }
    }

    $newPassword.on('input', validatePassword);
    $confirmPassword.on('input', validatePassword);

    $('#email').inputmask({
        alias: "email"
    });

    $('#phoneNumber').inputmask({
        mask: "+7 (999) 999-9999",
        placeholder: "",
        clearMaskOnLostFocus: true,
        greedy: false
    });
});
