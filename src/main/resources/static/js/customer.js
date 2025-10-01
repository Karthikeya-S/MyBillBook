// customer.js
document.addEventListener("DOMContentLoaded", function () {

    const businessSelect = document.getElementById("businessSelect");
    const gstinContainer = document.getElementById("gstinContainer");
    const form = document.getElementById("createCustomerForm");
    const resultMsg = document.getElementById("resultMessage");
    let businesses = [];

    // Fetch all businesses and populate dropdown
    fetch("/api/businesses")
        .then(response => response.json())
        .then(data => {
            businesses = data;
            businesses.forEach(biz => {
                const option = document.createElement("option");
                option.value = biz.id;
                option.textContent = biz.businessName;
                businessSelect.appendChild(option);
            });

            // Automatically select the first business
            if (businesses.length > 0) {
                businessSelect.value = businesses[0].id;
            }

            // Update GSTIN field based on the selected business
            updateGSTField();
        });

    // Show/hide GSTIN field when business changes
    businessSelect.addEventListener("change", updateGSTField);

    function updateGSTField() {
        const selected = businesses.find(b => b.id === businessSelect.value);
        if (selected && selected.enableGst) {
            gstinContainer.style.display = "block";
        } else {
            gstinContainer.style.display = "none";
        }
    }

    // Handle form submission via AJAX
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        const formData = new URLSearchParams(new FormData(this));

        fetch("/create-customer", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: formData.toString()
        })
            .then(response => response.text())
            .then(data => {
                if (data.startsWith("success")) {
                    resultMsg.textContent = "✅ " + data.split(":")[1].trim();
                    resultMsg.className = "message success";
                    form.reset();

                    // Reset to first business and update GSTIN field
                    if (businesses.length > 0) {
                        businessSelect.value = businesses[0].id;
                        updateGSTField();
                    }

                    // Redirect to dashboard after 1.5s
                    setTimeout(() => {
                        window.location.href = "/dashboard";
                    }, 1500);
                } else {
                    resultMsg.textContent = "❌ " + data.split(":")[1].trim();
                    resultMsg.className = "message error";
                }
            })
            .catch(err => {
                resultMsg.textContent = "❌ Error creating customer";
                resultMsg.className = "message error";
            });
    });
});
