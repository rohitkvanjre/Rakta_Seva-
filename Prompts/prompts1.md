Grand Prompt 1 — Authentication, Registration & Core Architecture
You are an expert Android Java developer. Build the complete Android project for **Rakta-Seva Connect** — a blood donor emergency alert app. This prompt covers: project setup, architecture, all data models, dummy auth flow, and donor registration.

---

## PROJECT OVERVIEW
- App Name: Rakta-Seva Connect
- Language: Java (Android)
- Min SDK: 24, Target SDK: 34
- Architecture: MVVM + Repository pattern
- Local storage: SharedPreferences (for session) + Room Database (for donors/requests)
- NO real backend — all data is local/mock
- UI Theme: Red (#C0392B primary, #E74C3C accent), White cards, clean Material-style

---

## SCREEN 1 — SPLASH SCREEN (SplashActivity.java)
- Full screen, red background
- Center: App logo (blood drop drawable — red drop on white circle) + "RAKTA-SEVA" bold text + "CONNECT" smaller below + tagline "Connecting the right donor at the right time"
- After 2.5 seconds, navigate to:
  - MainActivity (Home) if user session exists in SharedPreferences (key: "is_logged_in" = true)
  - LandingActivity otherwise

---

## SCREEN 2 — LANDING SCREEN (LandingActivity.java)
- White background, app logo centered upper half
- App name "Rakta-Seva Connect" large bold
- Tagline below in gray
- Two buttons bottom half:
  - "Login" → filled red button → LoginActivity
  - "Register" → outlined red button → RegisterActivity (Step 1)

---

## SCREEN 3 — LOGIN SCREEN (LoginActivity.java)
- Back arrow top left
- Title "Login", subtitle "Enter your mobile number to continue"
- Mobile number field: prefix "+91", hint "XXXXX XXXXX", input type phone
- Red filled button "Send OTP" → if number == "9876543210", navigate to OtpActivity passing the number; else show toast "User not found. Please register."
- Below: "Don't have an account?" text + outlined button "Register as New User" → RegisterActivity

---

## SCREEN 4 — OTP VERIFICATION (OtpActivity.java)
- Back arrow, title "Verify OTP"
- Subtitle showing the phone number passed via Intent
- 4 individual EditText boxes for OTP digits (auto-focus next on input)
- "Verify" red button → if combined OTP == "1234":
  - Save to SharedPreferences: is_logged_in=true, user_phone="9876543210", user_name="Rajesh Kumar", user_blood_group="O-", user_age=28, user_gender="Male", user_email="rajesh.kumar@email.com", user_location="Sector 12, New Delhi", last_donation="March 15, 2026", is_verified=true, is_available=true
  - Navigate to MainActivity
  - Else: show toast "Invalid OTP. Try 1234"
- Resend OTP text with 30s countdown timer

---

## SCREEN 5 & 6 — DONOR REGISTRATION (Multi-step)

### RegisterActivity.java — Step 1
- Back arrow, title "Register as Donor"
- Fields: Full Name (EditText), Age (EditText, numeric), Gender (Spinner: Male/Female/Other), Blood Group (Spinner: A+/A-/B+/B-/AB+/AB-/O+/O-)
- Radio group "I want to": Donate Blood Only / Request Blood Only / Both Donate & Request
- "Next" red button → validate all fields → pass data to RegisterStep2Activity via Intent

### RegisterStep2Activity.java — Step 2
- Radio group pre-selected from Step 1 (shown at top, non-editable display)
- Fields: Email (EditText), Mobile Number (EditText, 10 digits), Location (EditText, hint "Enter your city/area"), Last Donation Date (DatePicker dialog on click, format mm/dd/yyyy, optional)
- Toggle "Available for Donation" (default ON)
- "Continue" red button → validate → save all registration data to SharedPreferences + seed a DonorEntity into Room DB with this user's data → navigate to OtpActivity with entered phone number

---

## ROOM DATABASE SETUP

### AppDatabase.java
- Room database with 2 entities: DonorEntity, RequestEntity

### DonorEntity.java
```java
@Entity(tableName = "donors")
public class DonorEntity {
    @PrimaryKey(autoGenerate = true) public int id;
    public String name;
    public String bloodGroup;
    public int age;
    public String gender;
    public String phone;
    public String email;
    public String location;
    public double latitude;
    public double longitude;
    public String lastDonationDate;
    public boolean isAvailable;
    public boolean isVerified;
    public String userType; // "donor", "requester", "both"
}
```

### RequestEntity.java
```java
@Entity(tableName = "requests")
public class RequestEntity {
    @PrimaryKey(autoGenerate = true) public int id;
    public String patientName;
    public String bloodGroup;
    public String urgency; // "Critical", "Urgent", "High Priority"
    public String hospitalName;
    public String location;
    public double latitude;
    public double longitude;
    public double distanceKm;
    public int unitsRequired;
    public String timeAgo;
    public long timestamp;
    public String status; // "pending", "accepted", "rejected"
    public String acceptedByPhone;
}
```

### DonorDao.java — methods: insertDonor, getAllDonors, getDonorByPhone, updateAvailability

### RequestDao.java — methods: insertRequest, getAllRequests, getRequestById, updateStatus, getPendingRequests

---

## SEED DATA (DatabaseSeeder.java)
On first launch (SharedPreferences flag "db_seeded"), insert the following into Room:

**Donors (10 records):**

Aarav Singh, O-, 25, Male, 9811111111, aarav@email.com, Sector 10 New Delhi, lat:28.630, lng:77.209, available:true, verified:true
Priya Sharma, B+, 30, Female, 9822222222, priya@email.com, Connaught Place Delhi, lat:28.632, lng:77.219, available:true, verified:true
Rahul Verma, A+, 28, Male, 9833333333, rahul@email.com, Karol Bagh Delhi, lat:28.645, lng:77.190, available:false, verified:true
Sneha Patel, AB+, 22, Female, 9844444444, sneha@email.com, Lajpat Nagar Delhi, lat:28.565, lng:77.243, available:true, verified:false
Kiran Rao, O+, 35, Male, 9855555555, kiran@email.com, Dwarka Delhi, lat:28.590, lng:77.046, available:true, verified:true
Meena Iyer, B-, 27, Female, 9866666666, meena@email.com, Rohini Delhi, lat:28.730, lng:77.118, available:true, verified:true
Deepak Nair, A-, 31, Male, 9877777777, deepak@email.com, Vasant Kunj Delhi, lat:28.520, lng:77.160, available:false, verified:true
Anita Gupta, O-, 24, Female, 9888888888, anita@email.com, Saket Delhi, lat:28.527, lng:77.210, available:true, verified:true
Vikram Joshi, AB-, 29, Male, 9899999999, vikram@email.com, Greater Kailash Delhi, lat:28.538, lng:77.232, available:true, verified:false
Pooja Menon, B+, 26, Female, 9810101010, pooja@email.com, Mayur Vihar Delhi, lat:28.608, lng:77.293, available:true, verified:true


**Emergency Requests (6 records):**

Emergency Case, O-, Critical, Apollo Hospital Sector 12, lat:28.628, lng:77.215, dist:2.5km, units:2, timeAgo:"10 mins ago", status:pending
Accident Victim, B+, Urgent, City Hospital Main Road, lat:28.635, lng:77.225, dist:4.2km, units:1, timeAgo:"25 mins ago", status:pending
Surgery Patient, A+, High Priority, Medical Center Park Street, lat:28.620, lng:77.240, dist:6.8km, units:3, timeAgo:"1 hour ago", status:pending
Trauma Case, AB+, Critical, AIIMS Delhi, lat:28.568, lng:77.210, dist:8.1km, units:2, timeAgo:"45 mins ago", status:pending
Cancer Patient, O+, Urgent, Safdarjung Hospital, lat:28.573, lng:77.202, dist:5.3km, units:4, timeAgo:"2 hours ago", status:pending
Newborn Emergency, B-, High Priority, RML Hospital, lat:28.638, lng:77.197, dist:3.7km, units:1, timeAgo:"30 mins ago", status:pending


---

## SESSION MANAGER (SessionManager.java)
Utility class wrapping SharedPreferences with methods:
- isLoggedIn(), setLoggedIn(boolean)
- getUserName(), getUserPhone(), getUserBloodGroup()
- getUserLocation(), getLastDonation()
- isAvailable(), setAvailable(boolean)
- clearSession()
All keys as public static final String constants.

---

## build.gradle (app) dependencies to include:
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.cardview:cardview:1.0.0'

---

## STYLES & COLORS (res/values/)

### colors.xml
```xml
<color name="red_primary">#C0392B</color>
<color name="red_accent">#E74C3C</color>
<color name="red_dark">#922B21</color>
<color name="white">#FFFFFF</color>
<color name="gray_light">#F5F5F5</color>
<color name="gray_medium">#9E9E9E</color>
<color name="gray_dark">#424242</color>
<color name="green_available">#27AE60</color>
<color name="urgency_critical">#C0392B</color>
<color name="urgency_urgent">#E67E22</color>
<color name="urgency_high">#F39C12</color>
<color name="text_primary">#212121</color>
<color name="text_secondary">#757575</color>
<color name="card_bg">#FFFFFF</color>
<color name="bottom_nav_bg">#FFFFFF</color>
```

### styles.xml — define:
- RedFilledButton: red background, white text, 8dp corners, 48dp height
- OutlinedRedButton: transparent bg, red border 1.5dp, red text, 8dp corners
- CardStyle: white bg, 8dp corners, 4dp elevation
- InputFieldStyle: outlined box style, red focus color

---

## LAYOUT FILES REQUIRED (provide complete XML for each):
1. activity_splash.xml — centered logo + text on red bg
2. activity_landing.xml — logo top, two buttons bottom
3. activity_login.xml — back arrow, phone input, send OTP btn, register link
4. activity_otp.xml — 4 OTP boxes, verify button, resend countdown
5. activity_register_step1.xml — scrollable form with radio group
6. activity_register_step2.xml — scrollable form with toggle + continue

---

Deliver: All Java files fully implemented + all XML layout files + colors.xml + styles.xml + build.gradle snippet. No placeholders. Every onClick, navigation, validation, Room insert, and SharedPreferences write must be complete and work