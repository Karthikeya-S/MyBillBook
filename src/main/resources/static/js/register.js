// register.js
document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("registerForm");
    const resultMsg = document.getElementById("resultMessage");

    const firstNameField = document.getElementById("firstName");
    const lastNameField = document.getElementById("lastName");
    const emailField = document.getElementById("email");
    const userNameField = document.getElementById("userName");
    const passwordField = document.getElementById("password");

    const emailStatus = document.getElementById("emailStatus");
    const usernameStatus = document.getElementById("usernameStatus");
    const passwordHint = document.getElementById("passwordHint");
    const registerBtn = document.getElementById("registerBtn");

    let emailAvailable = false;
    let usernameAvailable = false;

    // Password validation
    function validatePassword(password) {
        const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;
        return regex.test(password);
    }

    function updateRegisterButton() {
        registerBtn.disabled = !(validatePassword(passwordField.value) && emailAvailable && usernameAvailable);
    }

    passwordField.addEventListener("input", () => {
        passwordHint.style.display = validatePassword(passwordField.value) ? "none" : "block";
        updateRegisterButton();
    });

    // Check email availability
    emailField.addEventListener("blur", () => {
        const email = emailField.value.trim();
        if (!email) return;

        fetch(`/check-email?email=${encodeURIComponent(email)}`)
            .then(res => res.text())
            .then(status => {
                if (status === "taken") {
                    emailStatus.textContent = "❌ Email already taken";
                    emailStatus.className = "hint taken";
                    emailAvailable = false;
                } else {
                    emailStatus.textContent = "✅ Email available";
                    emailStatus.className = "hint available";
                    emailAvailable = true;
                }
                updateRegisterButton();
            });
    });

    // Check username availability
    userNameField.addEventListener("blur", () => {
        const userName = userNameField.value.trim();
        if (!userName) return;

        fetch(`/check-username?userName=${encodeURIComponent(userName)}`)
            .then(res => res.text())
            .then(status => {
                if (status === "taken") {
                    usernameStatus.textContent = "❌ Username already taken";
                    usernameStatus.className = "hint taken";
                    usernameAvailable = false;
                } else {
                    usernameStatus.textContent = "✅ Username available";
                    usernameStatus.className = "hint available";
                    usernameAvailable = true;
                }
                updateRegisterButton();
            });
    });

    // Handle form submission
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        const formData = new URLSearchParams(new FormData(this));

        fetch("/register", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: formData.toString()
        })
            .then(res => res.text())
            .then(data => {
                if (data.toLowerCase().includes("success")) {
                    resultMsg.textContent = "✅ Registration successful! Redirecting to login...";
                    resultMsg.className = "message available";

                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 1500);
                } else {
                    // Show error message
                    const msg = data.includes(":") ? data.split(":")[1].trim() : data;
                    resultMsg.textContent = "❌ " + msg;
                    resultMsg.className = "message taken";
                }
            })
            .catch(err => {
                resultMsg.textContent = "❌ Error registering user";
                resultMsg.className = "message taken";
            });
    });
});