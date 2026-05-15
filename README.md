# Rakta-Seva Connect

Rakta-Seva Connect is an Android application designed to connect blood donors and requesters quickly during medical emergencies. The app combines modern Android development with Firebase cloud services and Generative AI support to reduce the time required to find eligible donors and send urgent blood requests.[1]

## Overview

The project was developed as part of an internship on Android App Development using Generative AI. It focuses on solving a real-world problem: delays in finding blood donors through manual calls, social media posts, or unverified contact chains.[1]

The application allows users to register as donors, manage their availability, receive emergency requests, and respond in real time. It also enforces donor eligibility rules, such as the mandatory 90-day interval between donations, and protects donor privacy by revealing contact details only after request acceptance.[1]

## Features

- OTP-based login using Firebase Authentication.[1]
- Donor registration and profile management.[1]
- Blood group-based donor filtering.[1]
- Emergency blood request creation and broadcast.[1]
- Real-time donor updates using Firebase Firestore.[1]
- Push notifications using Firebase Cloud Messaging (FCM).[1]
- Donor eligibility validation based on the 90-day donation rule.[1]
- Privacy-first contact sharing using an accept-to-reveal flow.[1]
- Jetpack Compose UI with MVVM architecture.[1]
- Generative AI support for emergency message drafting and chatbot assistance.[1]

## Tech Stack

- **Language:** Kotlin.[1]
- **IDE:** Android Studio.[1]
- **UI Toolkit:** Jetpack Compose.[1]
- **Architecture:** MVVM (Model-View-ViewModel).[1]
- **Backend:** Firebase Authentication, Firestore, and Firebase Cloud Messaging.[1]
- **Design Tool:** Figma.[1]
- **AI Integration:** Cloud-based Generative AI API.[1]

## Architecture

The app follows an MVVM architecture to keep the codebase modular and maintainable. The View layer handles UI rendering, the ViewModel layer manages state and business logic, and the Repository layer communicates with Firebase services for authentication, cloud storage, and notifications.[1]

The data flow is event-driven. User actions move from the UI to the ViewModel, then to the repository and Firebase, while results flow back to the UI in real time.[1]

## Gen AI Integration

Generative AI is used to support users during high-stress medical emergencies. The AI layer can generate short and urgent emergency broadcast messages based on blood group and hospital location, reducing the time needed for manual typing.[1]

The application also includes a chatbot assistant restricted to blood donation guidance, eligibility rules, and app usage. Fallback logic is included so the app continues functioning even if the AI service times out.[1]

## Core Modules

- User Authentication and Profile Module.
- Eligibility and Filtering Engine.
- Emergency Broadcast and Geo-Filtering Module.
- Privacy Control and Contact Module.
- Generative AI Assistance Layer.[1]

## Project Goals

- Build a practical Android application for emergency blood donation support.[1]
- Learn modern Android development tools and workflows.[1]
- Apply Firebase for real-time backend operations.[1]
- Integrate Generative AI into a real-world mobile use case.[1]
- Improve debugging, testing, and system design skills through project implementation.[1]

## Future Enhancements

Planned enhancements include live donor tracking, multilingual support, blood bank inventory integration, wearable health device integration, gamification for repeat donors, and more advanced health analytics.[1]

## Status

This project is a functional internship prototype developed for academic and learning purposes. It demonstrates a complete end-to-end implementation of an AI-enhanced emergency blood donation support system.[1]

## Author

**Rohit K**  
B.E. Computer Science and Engineering  
Sri Sairam College of Engineering
