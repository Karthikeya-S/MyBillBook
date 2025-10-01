package com.application.mybillbook.controller;

import com.application.mybillbook.config.CounterConfig;
import com.application.mybillbook.model.*;
import com.application.mybillbook.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CounterConfigRepository counterConfigRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseBillRepository purchaseBillRepository;

    /* ------------------------ USER REGISTRATION ------------------------ */
    @GetMapping("/register-form")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String email,
                               @RequestParam String userName,
                               @RequestParam String password) {

        if (userRepository.findByUserName(userName) != null) {
            return "Error: Username '" + userName + "' is already taken.";
        }

        try {
            Users user = new Users(firstName, lastName, email, userName, passwordEncoder.encode(password));
            userRepository.save(user);
            return "success: User registered successfully";
        } catch (DuplicateKeyException e) {
            return "error: Username already exists (duplicate key)";
        }
    }

    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        return userRepository.findByEmail(email) != null ? "taken" : "available";
    }

    @GetMapping("/check-username")
    @ResponseBody
    public String checkUsername(@RequestParam String userName) {
        return userRepository.findByUserName(userName) != null ? "taken" : "available";
    }

    @GetMapping("/validate-password")
    @ResponseBody
    public String validatePassword(@RequestParam String password) {
        String pattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={}:;\"'<>,.?/]).{8,}$";
        return password.matches(pattern) ? "valid" : "Password must be at least 8 characters long, include at least one number and one special character.";
    }

    /* ------------------------ LOGIN ------------------------ */
    @GetMapping("/login")
    public String showLoginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    /* ------------------------ DASHBOARD ------------------------ */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || (authentication.getPrincipal() instanceof String
                && authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users user = userRepository.findByUserName(userDetails.getUsername());

        // Get businesses only for this user
        List<Business> businesses = businessRepository.findByOwnerName(Collections.singletonList(user.getUserName()));

        model.addAttribute("businesses", businesses);
        model.addAttribute("username", user.getUserName());
        return "dashboard";
    }

    /* ------------------------ LOGOUT ------------------------ */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }

    /* ------------------------ BUSINESS ------------------------ */
    @GetMapping("/create-business-form")
    public String showCreateBusinessForm() {
        return "create-business";
    }

    @PostMapping("/create-business")
    @ResponseBody
    public String createBusiness(@RequestParam String businessName,
                                 @RequestParam String doorNo,
                                 @RequestParam String street,
                                 @RequestParam String area,
                                 @RequestParam String city,
                                 @RequestParam String state,
                                 @RequestParam String country,
                                 @RequestParam String pinCode,
                                 @RequestParam String mobile,
                                 @RequestParam String email,
                                 @RequestParam boolean enableGst,
                                 @RequestParam(required = false) String gstinNo) {
        try {
            if (!enableGst) gstinNo = null;

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            Users loggedInUser = userRepository.findByUserName(userDetails.getUsername());

            Business business = new Business();
            business.setBusinessName(businessName);
            business.setDoorNo(doorNo);
            business.setStreet(street);
            business.setArea(area);
            business.setCity(city);
            business.setState(state);
            business.setCountry(country);
            business.setPinCode(pinCode);
            business.setMobile(mobile);
            business.setEmail(email);
            business.setEnableGst(enableGst);
            business.setGstinNo(gstinNo);

            // Map to logged-in user
            business.setOwnerName(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
            business.setOwnerId(loggedInUser.getUserName());

            businessRepository.save(business);
            return "success: Business created successfully";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/api/businesses")
    @ResponseBody
    public List<Business> getAllBusinesses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return businessRepository.findByOwnerId(userDetails.getUsername());
    }


    /* ------------------------ CUSTOMER ------------------------ */
    @GetMapping("/create-customer-form")
    public String showCreateCustomerForm() {
        return "create-customer";
    }

    @PostMapping("/create-customer")
    @ResponseBody
    public String createCustomer(@RequestParam String customerName,
                                 @RequestParam(required = false) String gstinNo,
                                 @RequestParam String mobile,
                                 @RequestParam String email,
                                 @RequestParam String doorNo,
                                 @RequestParam String street,
                                 @RequestParam String area,
                                 @RequestParam String city,
                                 @RequestParam String state,
                                 @RequestParam String country,
                                 @RequestParam String pinCode,
                                 @RequestParam String businessId) {
        try {
            Business business = businessRepository.findById(businessId).orElse(null);
            if (business == null) return "error: Business not found";

            if (!business.isEnableGst()) gstinNo = null;

            String counterId = "customer_" + businessId;
            CounterConfig counter = counterConfigRepository.findById(counterId).orElse(new CounterConfig(counterId, 0));
            long newId = counter.getSeq() + 1;
            counter.setSeq(newId);
            counterConfigRepository.save(counter);

            Customer customer = new Customer();
            customer.setCustomerId(String.valueOf(newId));
            customer.setCustomerName(customerName);
            customer.setGstinNo(gstinNo);
            customer.setMobile(mobile);
            customer.setEmail(email);
            customer.setDoorNo(doorNo);
            customer.setStreet(street);
            customer.setArea(area);
            customer.setCity(city);
            customer.setState(state);
            customer.setCountry(country);
            customer.setPinCode(pinCode);
            customer.setBusinessId(businessId);

            customerRepository.save(customer);

            return "success: Customer created successfully";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        List<Customer> customers = customerRepository.findAll();

        Map<String, String> businessMap = businessRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Business::getId, Business::getBusinessName));

        model.addAttribute("customers", customers);
        model.addAttribute("businessMap", businessMap);

        return "customers";
    }

    @GetMapping("/api/customers")
    @ResponseBody
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /* ---------------- CREATE VENDOR FORM ----------------- */
    @GetMapping("/create-vendor-form")
    public String showCreateVendorForm(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Fetch only businesses of logged-in user
        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        model.addAttribute("businesses", businesses);

        return "create-vendor";
    }

    /* ---------------- CREATE VENDOR POST ----------------- */
    @PostMapping("/create-vendor")
    @ResponseBody
    public String createVendor(@RequestParam String vendorName,
                               @RequestParam String vendorBusinessName,
                               @RequestParam(required = false) String gstinNo,
                               @RequestParam String mobile,
                               @RequestParam String email,
                               @RequestParam String doorNo,
                               @RequestParam String street,
                               @RequestParam String area,
                               @RequestParam String city,
                               @RequestParam String state,
                               @RequestParam String country,
                               @RequestParam String pinCode,
                               @RequestParam String businessId) {
        try {
            var business = businessRepository.findById(businessId).orElse(null);
            if (business == null) return "error: Business not found";

            if (!business.isEnableGst()) gstinNo = null;

            // Auto-increment vendorId per business
            String counterId = "vendor_" + businessId;
            CounterConfig counter = counterConfigRepository.findById(counterId)
                    .orElse(new CounterConfig(counterId, 0));
            long newId = counter.getSeq() + 1;
            counter.setSeq(newId);
            counterConfigRepository.save(counter);

            Vendors vendor = new Vendors();
            vendor.setVendorId(String.valueOf(newId));
            vendor.setVendorName(vendorName);
            vendor.setVendorBusinessName(vendorBusinessName);
            vendor.setGstinNo(gstinNo);
            vendor.setMobile(mobile);
            vendor.setEmail(email);
            vendor.setDoorNo(doorNo);
            vendor.setStreet(street);
            vendor.setArea(area);
            vendor.setCity(city);
            vendor.setState(state);
            vendor.setCountry(country);
            vendor.setPinCode(pinCode);
            vendor.setBusinessId(businessId);

            vendorRepository.save(vendor);

            return "success: Vendor created successfully";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    /* ---------------- VIEW VENDORS ----------------- */
    @GetMapping("/vendors")
    public String listVendors(Model model, Authentication authentication) {
        // Get logged-in user's businesses
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        List<String> businessIds = businesses.stream().map(Business::getId).toList();

        // Get vendors only for those businesses
        List<Vendors> vendors = vendorRepository.findByBusinessIdIn(businessIds);

        // Pass vendors as-is; do NOT map to parent business
        model.addAttribute("vendors", vendors);
        return "vendors";
    }

    @GetMapping("/api/vendors")
    @ResponseBody
    public List<Vendors> getAllVendors(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        List<String> businessIds = businesses.stream().map(Business::getId).toList();

        // Return only vendors linked to user's businesses
        return vendorRepository.findByBusinessIdIn(businessIds);
    }

    /* ------------------ PRODUCT ------------------ */
    @GetMapping("/create-product-form")
    public String showCreateProductForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Fetch only businesses for this user
        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        model.addAttribute("businesses", businesses);
        return "create-product";
    }

    @PostMapping("/create-product")
    @ResponseBody
    public String createProduct(@RequestParam String productName,
                                @RequestParam BigDecimal remainingQuantity,
                                @RequestParam String businessId) {
        try {
            Product product = new Product();
            product.setProductName(productName);
            product.setRemainingQuantity(remainingQuantity);
            product.setBusinessId(businessId);

            productRepository.save(product);
            return "success: Product created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }


    @GetMapping("/api/products")
    @ResponseBody
    public List<Product> getProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        List<String> businessIds = businesses.stream().map(Business::getId).toList();

        return productRepository.findAll().stream()
                .filter(p -> businessIds.contains(p.getBusinessId()))
                .toList();
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        List<Business> businesses = businessRepository.findByOwnerId(userDetails.getUsername());
        List<String> businessIds = businesses.stream().map(Business::getId).toList();

        List<Product> products = productRepository.findAll().stream()
                .filter(p -> businessIds.contains(p.getBusinessId()))
                .toList();

        model.addAttribute("products", products);
        return "product-list";
    }


    // Open Purchase Bill Form
    @GetMapping("/create-purchase-bill")
    public String createPurchaseBillForm(@RequestParam String vendorId, Model model) {
        Vendors vendor = vendorRepository.findById(vendorId).orElse(null);
        if (vendor == null) {
            model.addAttribute("error", "Vendor not found");
            return "error";
        }

        model.addAttribute("vendor", vendor);

        // Get business for GSTIN check
        Business business = businessRepository.findById(vendor.getBusinessId()).orElse(null);
        model.addAttribute("business", business);

        // Get products for this vendor's business
        List<Product> products = productRepository.findByBusinessIdIn(List.of(vendor.getBusinessId()));
        model.addAttribute("products", products);

        return "create-purchase-bill";
    }

    @PostMapping("/save-purchase-bill")
    @ResponseBody
    public String savePurchaseBill(
            @RequestParam String vendorId,
            @RequestParam BigDecimal paid,
            @RequestParam List<String> productIds,
            @RequestParam List<BigDecimal> rates,
            @RequestParam List<BigDecimal> quantities,
            @RequestParam(required = false) List<BigDecimal> gstRates) {

        try {
            // 1️⃣ Fetch vendor
            Vendors vendor = vendorRepository.findById(vendorId).orElse(null);
            if (vendor == null) return "error: Vendor not found";

            // 2️⃣ Fetch business
            Business business = businessRepository.findById(vendor.getBusinessId()).orElse(null);
            if (business == null) return "error: Business not found";

            // 3️⃣ Generate auto-increment billNo
            String counterId = "bill_" + business.getId();
            CounterConfig counter = counterConfigRepository.findById(counterId)
                    .orElse(new CounterConfig(counterId, 0));
            long billNo = counter.getSeq() + 1;
            counter.setSeq(billNo);
            counterConfigRepository.save(counter);

            // 4️⃣ Create purchase bill
            PurchaseBill bill = new PurchaseBill();
            bill.setBillNo(billNo);
            bill.setVendorId(vendor.getId());
            bill.setVendorBusinessName(vendor.getVendorBusinessName());
            bill.setBillDate(new Date());

            List<PurchaseBillItem> items = new ArrayList<>();
            BigDecimal finalSum = BigDecimal.ZERO;

            // 5️⃣ Process each product
            for (int i = 0; i < productIds.size(); i++) {
                Product product = productRepository.findById(productIds.get(i)).orElse(null);
                if (product == null) continue;

                BigDecimal gstRate = BigDecimal.ZERO;
                if (gstRates != null && gstRates.size() > i) gstRate = gstRates.get(i);

                BigDecimal total = rates.get(i).multiply(quantities.get(i));
                BigDecimal gstTotal = BigDecimal.ZERO;

                if (business.isEnableGst() && gstRate.compareTo(BigDecimal.ZERO) > 0) {
                    gstTotal = total.multiply(gstRate).divide(BigDecimal.valueOf(100));
                    total = total.add(gstTotal);
                }

                PurchaseBillItem item = new PurchaseBillItem();
                item.setProductId(product.getId());
                item.setProductName(product.getProductName());
                item.setRate(rates.get(i));
                item.setQuantity(quantities.get(i));
                item.setGstRate(gstRate);
                item.setGstTotal(gstTotal);
                item.setTotal(total);

                items.add(item);
                finalSum = finalSum.add(total);

                // ✅ Update product remaining quantity and availability
                BigDecimal newQuantity = product.getRemainingQuantity().add(quantities.get(i));
                product.setRemainingQuantity(newQuantity);
                product.setAvailable(newQuantity.compareTo(BigDecimal.ZERO) > 0);
                product.setAvailable(product.getRemainingQuantity().compareTo(BigDecimal.ZERO) > 0);
                productRepository.save(product);
            }

            bill.setItems(items);
            bill.setFinalSum(finalSum);
            bill.setPaid(paid);

            // 6️⃣ Update vendor balance and business debt
            BigDecimal remaining = finalSum.subtract(paid);
            vendor.setTotalBalance(vendor.getTotalBalance().add(remaining));
            vendorRepository.save(vendor);

            business.setDebtLiabilities(business.getDebtLiabilities() + remaining.doubleValue());
            businessRepository.save(business);

            // 7️⃣ Save purchase bill
            purchaseBillRepository.save(bill);

            return "success: Purchase Bill saved successfully";

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // Get Purchase Bills by Vendor ID
    @GetMapping("/api/purchase-bills")
    @ResponseBody
    public List<PurchaseBill> getPurchaseBills(@RequestParam String vendorId) {
        try {
            return purchaseBillRepository.findByVendorId(vendorId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/vendor/{vendorId}/bills")
    @ResponseBody
    public List<PurchaseBill> getVendorBills(@PathVariable String vendorId) {
        return purchaseBillRepository.findByVendorId(vendorId);
    }

    @DeleteMapping("/delete-purchase-bill/{billId}")
    @ResponseBody
    public String deletePurchaseBill(@PathVariable String billId) {
        try {
            PurchaseBill bill = purchaseBillRepository.findById(billId).orElse(null);
            if (bill == null) return "error: Bill not found";

            Vendors vendor = vendorRepository.findById(bill.getVendorId()).orElse(null);
            Business business = businessRepository.findById(vendor.getBusinessId()).orElse(null);

            if (vendor != null) {
                BigDecimal remaining = bill.getFinalSum().subtract(bill.getPaid());
                vendor.setTotalBalance(vendor.getTotalBalance().subtract(remaining));
                vendorRepository.save(vendor);

                if (business != null) {
                    business.setDebtLiabilities(business.getDebtLiabilities() - remaining.doubleValue());
                    businessRepository.save(business);
                }
            }

            // Restore product quantities
            for (PurchaseBillItem item : bill.getItems()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                if (product != null) {
                    BigDecimal newQty = product.getRemainingQuantity().subtract(item.getQuantity());

// Prevent negative stock
                    if (newQty.compareTo(BigDecimal.ZERO) < 0) {
                        newQty = BigDecimal.ZERO;
                    }

                    product.setRemainingQuantity(newQty);
                    product.setAvailable(newQty.compareTo(BigDecimal.ZERO) > 0);

                    productRepository.save(product);
                }
            }

            purchaseBillRepository.delete(bill);
            return "success: Bill deleted successfully";

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
