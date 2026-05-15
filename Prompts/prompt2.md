You are an expert Android Java developer continuing the Rakta-Seva Connect app. Authentication, Room DB, SessionManager, seed data, and all models are already built (Prompt 1). Now build the entire main app: Home Dashboard, Donors list, Requests management, and Profile screen.

---

## MAIN NAVIGATION (MainActivity.java)
- BottomNavigationView with 4 tabs: Home (house icon), Donors (drop icon), Requests (bell icon), Profile (person icon)
- Hosts fragments via FragmentManager: HomeFragment, DonorsFragment, RequestsFragment, ProfileFragment
- Bottom nav background white, selected item red, unselected gray
- Red toolbar with "Rakta-Seva" logo text left, bell icon + profile icon right

---

## SCREEN: HOME FRAGMENT (HomeFragment.java + fragment_home.xml)

### Availability Card (top)
- White card, rounded corners
- Left: "You are Available for Donation" bold text, below: "Last donation: [from SharedPreferences]"
- Right: Material SwitchCompat toggle (green when ON)
- On toggle change → update SharedPreferences is_available + update DonorEntity in Room for logged-in user

### "Nearby Emergency Requests" section
- Title "Nearby Emergency Requests" + "View All" text button (red) right-aligned → navigates to RequestsFragment
- RecyclerView (vertical, no scroll — show top 3 only on home)
- Each card (RequestCardView):
  - Left red circle badge with blood group text (e.g. "O-")
  - Urgency chip top-right: "Critical"=red, "Urgent"=orange, "High Priority"=yellow, text white, 4dp corners
  - Time ago text (gray, small) top-right corner
  - Location pin icon + distance (e.g. "2.5 km") in gray
  - Hospital name in bold below distance
  - "Required: X units" small text
  - Two buttons: "Accept" (green filled) | "Reject" (gray outlined)
  - On Accept: update RequestEntity status="accepted", acceptedByPhone=logged-in user phone, show SuccessDialog
  - On Reject: update status="rejected", remove card from list

### Bottom Quick Actions (below RecyclerView)
- Two equal cards side by side:
  - Card 1: blood drop icon + "View Donors" text → switch to DonorsFragment
  - Card 2: bell icon + "My Requests" text → switch to RequestsFragment
- Full-width red button "⚠ Request Blood" → RequestBloodDialog

### RequestBloodDialog (DialogFragment)
- Title "Post Emergency Request"
- Spinner: Blood Group
- Spinner: Urgency (Critical/Urgent/High Priority)
- EditText: Hospital Name
- EditText: Location
- EditText: Units Required (numeric)
- "Submit" button → insert new RequestEntity into Room with current timestamp, timeAgo="Just now", distanceKm=random 1.0-9.9, status="pending" → dismiss dialog → refresh RecyclerView → show toast "Request posted successfully"

---

## SCREEN: DONORS FRAGMENT (DonorsFragment.java + fragment_donors.xml)

### Search & Filter Bar
- SearchView (full width) with hint "Search by name or blood group"
- Below: horizontal HorizontalScrollView with filter chips:
  - "All" (default selected, red filled), "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
  - Toggle "Available Only" chip
  - On chip select → filter Room query result accordingly

### Donors RecyclerView
- Each DonorCard:
  - Left: circular avatar with initials (first letter of name), red background
  - Name bold, blood group red badge beside name
  - Location pin icon + location text gray
  - "Verified" green chip if isVerified=true
  - Right: availability dot — green if available, gray if not
  - "Available" / "Unavailable" small text below dot
  - Tap card → DonorDetailDialog

### DonorDetailDialog
- Circular avatar large, name, blood group badge, verified status
- Info rows: Email, Phone (only show if donor is available), Location, Last Donation
- "Request This Donor" button (red, full width) → creates a RequestEntity pre-filled with this donor's blood group → toast "Request sent to [name]"
- Note at bottom: "Phone number visible only when donor accepts your request"

### Empty state: if no donors match filter, show blood drop icon + "No donors found" centered

---

## SCREEN: REQUESTS FRAGMENT (RequestsFragment.java + fragment_requests.xml)

### Tab Layout (2 tabs):
- Tab 1: "Nearby Requests" — shows all RequestEntity with status="pending", sorted by distanceKm ASC
- Tab 2: "My Requests" — shows RequestEntity where acceptedByPhone = logged-in user's phone OR all requests (mock: show accepted ones as "You responded")

### Each request card (same design as home but full list, no limit)
- Nearby tab cards: Accept + Reject buttons
- My Requests tab cards: show status badge ("Accepted"=green, "Rejected"=gray, "Pending"=orange) instead of buttons

### FAB (Floating Action Button) — red, "+" icon
- Opens same RequestBloodDialog from HomeFragment

---

## SCREEN: PROFILE FRAGMENT (ProfileFragment.java + fragment_profile.xml)

### Profile Header Card
- Large circular avatar (initials, red bg)
- Name bold, blood group red badge
- "Verified" green chip (if is_verified=true in SharedPreferences)
- Edit icon (pencil) top-right → EditProfileDialog

### Info Section (cards with icons)
- Email icon + email
- Phone icon + phone
- Location pin icon + location
- Person icon + "Age • Gender" (e.g. "28 years • Male")

### Donation History Card
- Calendar icon + "Last Donation" label
- Date value + "X months ago" calculated from stored date

### Settings Section
- "Availability Status" row with toggle (same as home card toggle, synced)
- "Change Blood Group" row → spinner dialog
- "Logout" row (red text) → AlertDialog confirm → clearSession() → finish all activities → start LandingActivity with FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK

### EditProfileDialog
- Editable fields: Name, Email, Location, Age, Gender (spinner)
- "Save" → update SharedPreferences + update DonorEntity in Room

---

## UTILITY CLASSES

### DistanceCalculator.java
```java
// Haversine formula
public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    // implement haversine — return distance in km
}
```
Logged-in user's dummy location: lat=28.630, lng=77.215 (Sector 12, New Delhi)
Use this to re-calculate distanceKm for all requests/donors on display.

### TimeAgoFormatter.java
```java
public static String format(long timestamp) {
    // return "X mins ago", "X hours ago", "Yesterday", etc.
}
```

### BloodGroupCompatibility.java
```java
// Returns list of compatible donor blood groups for a given recipient group
public static List<String> getCompatibleDonors(String recipientGroup) { ... }
```

### InitialsAvatarView.java (custom View or utility method)
- Takes name string → returns first letter uppercase
- Used in circular drawables for donor/profile avatars

---

## ADDITIONAL LAYOUTS REQUIRED:

1. fragment_home.xml — availability card + RecyclerView + quick action cards + request blood button
2. fragment_donors.xml — search + filter chips + RecyclerView
3. fragment_requests.xml — TabLayout + ViewPager2 (or manual tab switching) + FAB
4. fragment_profile.xml — scrollable profile with all sections
5. item_request_card.xml — request card layout for RecyclerView
6. item_donor_card.xml — donor card layout
7. dialog_request_blood.xml — post request dialog
8. dialog_donor_detail.xml — donor detail bottom sheet
9. dialog_edit_profile.xml — edit profile dialog
10. activity_main.xml — BottomNavigationView + fragment container + toolbar
11. menu/bottom_nav_menu.xml — 4 menu items with icons

---

## DRAWABLES NEEDED (describe as XML vector drawables):
- ic_blood_drop.xml — simple blood drop shape, red
- ic_home.xml, ic_donors.xml, ic_requests.xml, ic_profile.xml — bottom nav icons
- ic_location_pin.xml, ic_phone.xml, ic_email.xml, ic_calendar.xml — info row icons
- ic_verified.xml — checkmark circle
- blood_group_badge_bg.xml — red rounded rectangle shape
- card_background.xml — white with 8dp corners and shadow

---

## ADAPTERS:

### RequestsAdapter.java
- RecyclerView.Adapter with ViewHolder
- Bind RequestEntity to item_request_card.xml
- Blood group badge background tint by urgency
- Callback interface: onAccept(int requestId), onReject(int requestId)
- DiffUtil for smooth updates

### DonorsAdapter.java
- RecyclerView.Adapter with ViewHolder
- Bind DonorEntity to item_donor_card.xml
- Filter method: filterByBloodGroup(String group), filterAvailableOnly(boolean)
- Callback: onDonorClick(DonorEntity donor)

---

## VIEWMODELS:

### HomeViewModel.java
- LiveData<List<RequestEntity>> pendingRequests — from Room, top 3
- LiveData<Boolean> isAvailable
- fun acceptRequest(int id, String phone)
- fun rejectRequest(int id)
- fun postNewRequest(RequestEntity entity)

### DonorsViewModel.java
- LiveData<List<DonorEntity>> allDonors
- fun filterDonors(String bloodGroup, boolean availableOnly) → updates LiveData

### RequestsViewModel.java
- LiveData<List<RequestEntity>> nearbyRequests
- LiveData<List<RequestEntity>> myRequests(String phone)

### ProfileViewModel.java
- Reads/writes SharedPreferences via SessionManager
- fun updateAvailability(boolean available, AppDatabase db)

---

Deliver: All Java files fully implemented (no TODOs) + all XML layout and drawable files. Every RecyclerView must populate from Room DB seed data. All navigation, dialogs, toggles, filters, and Room operations must be complete and functional. The app must be fully runnable after combining Prompt 1 and Prompt 2 outputs.
