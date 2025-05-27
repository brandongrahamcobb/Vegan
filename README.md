# Vegan Store Application

This document provides a comprehensive overview of the Vegan Store application's architecture, focusing on its organization, key components, and design principles. It aims to serve as a guide for understanding the codebase and facilitating future development.

## Table of Contents

1.  [Project Structure](#1-project-structure)
2.  [Core Concepts & Design Principles](#2-core-concepts--design-principles)
    *   [Layered Architecture](#layered-architecture)
    *   [Separation of Concerns](#separation-of-concerns)
    *   [Vaadin UI Best Practices](#vaadin-ui-best-practices)
3.  [Key Packages and Their Responsibilities](#3-key-packages-and-their-responsibilities)
    *   [`com.brandongcobb.vegan.store.domain`](#combrandongcobbveganstoredomain)
    *   [`com.brandongcobb.vegan.store.repo`](#combrandongcobbveganstorerepo)
    *   [`com.brandongcobb.vegan.store.service`](#combrandongcobbveganstoreservice)
    *   [`com.brandongcobb.vegan.store.api.dto`](#combrandongcobbveganstoreapidto)
    *   [`com.brandongcobb.vegan.store.ui`](#combrandongcobbveganstoreui)
    *   [`com.brandongcobb.vegan.store.config`](#combrandongcobbveganstoreconfig)
    *   [`com.brandongcobb.vegan.store.utils`](#combrandongcobbveganstoreutils) (New/Implicit)
4.  [Application Flow (Bird's Eye View)](#4-application-flow-birds-eye-view)
    *   [User Authentication & Session Management](#user-authentication--session-management)
    *   [Product Browsing & Search](#product-browsing--search)
    *   [Shopping Cart Management](#shopping-cart-management)
    *   [Order Placement & History](#order-placement--history)
    *   [Admin Functionality](#admin-functionality)
5.  [Development Guidelines](#5-development-guidelines)
    *   [Adding New Features](#adding-new-features)
    *   [Troubleshooting](#troubleshooting)

---

## 1. Project Structure

The project follows a standard Spring Boot application structure, with a clear separation of concerns into distinct packages.

