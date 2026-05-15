# Rakta-Seva Connect

Rakta-Seva Connect is an Android application designed to connect blood donors and requesters quickly during medical emergencies. The app combines modern Android development with Firebase cloud services and Generative AI support to reduce the time required to find eligible donors and send urgent blood requests.

## Overview

The project was developed as part of an internship on Android App Development using Generative AI. It focuses on solving a real-world problem: delays in finding blood donors through manual calls, social media posts, or unverified contact chains.

The application allows users to register as donors, manage their availability, receive emergency requests, and respond in real time. It also enforces donor eligibility rules, such as the mandatory 90-day interval between donations, and protects donor privacy by revealing contact details only after request acceptance.

## Features

- OTP-based login using Firebase Authentication.
- Donor registration and profile management.
- Blood group-based donor filtering.
- Emergency blood request creation and broadcast.
- Real-time donor updates using Firebase Firestore.
- Push notifications using Firebase Cloud Messaging (FCM).
- Donor eligibility validation based on the 90-day donation rule.
- Privacy-first contact sharing using an accept-to-reveal flow.
- Jetpack Compose UI with MVVM architecture.
- Generative AI support for emergency message drafting and chatbot assistance.

## Tech Stack

- **Language:** Kotlin.
- **IDE:** Android Studio.
- **UI Toolkit:** Jetpack Compose.
- **Architecture:** MVVM (Model-View-ViewModel).
- **Backend:** Firebase Authentication, Firestore, and Firebase Cloud Messaging.
- **Design Tool:** Figma.
- **AI Integration:** Cloud-based Generative AI API.

## Architecture

The app follows an MVVM architecture to keep the codebase modular and maintainable. The View layer handles UI rendering, the ViewModel layer manages state and business logic, and the Repository layer communicates with Firebase services for authentication, cloud storage, and notifications.

The data flow is event-driven. User actions move from the UI to the ViewModel, then to the repository and Firebase, while results flow back to the UI in real time.

## Gen AI Integration

Generative AI is used to support users during high-stress medical emergencies. The AI layer can generate short and urgent emergency broadcast messages based on blood group and hospital location, reducing the time needed for manual typing.

The application also includes a chatbot assistant restricted to blood donation guidance, eligibility rules, and app usage. Fallback logic is included so the app continues functioning even if the AI service times out.

## Core Modules

- User Authentication and Profile Module.
- Eligibility and Filtering Engine.
- Emergency Broadcast and Geo-Filtering Module.
- Privacy Control and Contact Module.
- Generative AI Assistance Layer.

## Project Goals

- Build a practical Android application for emergency blood donation support.
- Learn modern Android development tools and workflows.
- Apply Firebase for real-time backend operations.
- Integrate Generative AI into a real-world mobile use case.
- Improve debugging, testing, and system design skills through project implementation.

## Future Enhancements

Planned enhancements include live donor tracking, multilingual support, blood bank inventory integration, wearable health device integration, gamification for repeat donors, and more advanced health analytics.

## Status

This project is a functional internship prototype developed for academic and learning purposes. It demonstrates a complete end-to-end implementation of an AI-enhanced emergency blood donation support system.

## Author

**Rohit K**  
B.E. Computer Science and Engineering  
Sri Sairam College of Engineering
