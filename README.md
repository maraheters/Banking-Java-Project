# Banking System

## Overview

This repository contains a simple banking system developed as part of the first Object-Oriented Programming (OOP) lab work at BSUIR.

The project is a RESTful API written in Java using Spring Boot, with JDBC for database communication. It serves as an educational example rather than a real-world banking application.

The front-end part of this banking system can be found in the following repository:  
[Banking Client](https://github.com/maraheters/Banking-client)

## Features

The system allows users to register with different roles, each having specific permissions and responsibilities within the financial system.

### **Client**
- Can register (requires approval from a Manager).
- Can create accounts, deposit funds, withdraw funds, and transfer money to other accounts and financial entities.
- Can apply for loans (requires approval from a Manager).
- Can open deposit accounts.

### **Operator**
- Can approve or reject salary project applications.
- Can reverse certain transactions.

### **Manager**
- Has all the privileges of an Operator.
- Can approve or reject client registrations and loan applications.

### **Enterprise Specialist**
- Can apply for a salary project.
- Can process salary payments to employees.

### **Administrator**
- Has all the privileges of a Manager.
- Can access application logs.
