# Spectator Management System

### Developed by:
- **Salah Ben Sarar**
- **Edgar Hamilton**

[GitHub Repository Link](#)

---

## Table of Contents:
1. [Introduction](#1-introduction)
2. [Implemented Tasks](#2-implemented-tasks)
   - [Database Menu](#21-database-menu)
   - [SOAP Client](#22-soap-client)
   - [Parallel Programming](#23-parallel-programming)
   - [Forex Menu](#24-forex-menu)
3. [Technologies Used](#3-technologies-used)
4. [Folder Structure](#4-folder-structure)
5. [GitHub Usage](#5-github-usage)
6. [Conclusion](#6-conclusion)
7. [Future Improvements](#7-future-improvements)

---

## 1. Introduction

The **Spectator Management System** is a desktop application designed to handle data and processes efficiently. This application is tailored to meet academic requirements and demonstrates a range of skills, including CRUD operations, SOAP web services integration, parallel programming, and Forex API usage.

The project emphasizes:
- Database manipulation with multiple submenus.
- Integration with SOAP and REST APIs for financial data handling.
- Advanced programming techniques such as multithreading.
- Comprehensive version control using GitHub.

---

## 2. Implemented Tasks

### 2.1. Database Menu (5 Points)
CRUD operations were implemented using a database with at least three tables.

#### Submenus:
- **Read Submenu**: Displays database data in a table, showing data from at least three tables. For large datasets, partial data is displayed.
- **Read2 Submenu**: Includes a form with:
  - Text input field
  - Drop-down list
  - Radio button
  - Checkbox  
  This form filters data displayed in a table.
- **Write Submenu**: Adds a new record to one of the database tables via a form.
- **Change Submenu**: Updates an existing record selected via a drop-down list.
- **Delete Submenu**: Deletes a record from a table, with the record ID selectable via a drop-down list.

---

### 2.2. SOAP Client (5 Points)
Developed a SOAP client for the Hungarian National Bank's web service.

#### Submenus:
- **Download Submenu**: Downloads all data to a file (`Bank.txt`).
- **Download2 Submenu**: Displays a form with input fields (e.g., drop-down list, radio buttons). Downloads selected data to `Bank.txt`.
- **Graph Submenu**: Allows dataset selection and displays the data in a graph format.

#### Reference:
[Hungarian National Bank Exchange Rates](https://www.mnb.hu/arfolyam-lekerdezes)

---

### 2.3. Parallel Programming (5 Points)
Demonstrated parallel programming on a single page.

#### Submenu:
- A button initiates two parallel threads:
  - One updates a label every second.
  - The other updates a second label every two seconds.

---

### 2.4. Forex Menu (5 Points)
Integrated with the **Oanda API** to manage Forex data and operations.

#### Submenus:
- **Account Information**: Displays account details in a table.
- **Current Prices**: Allows users to select a currency pair from a drop-down list and displays the current price on button click.
- **Historical Prices**: Enables users to select a currency pair, start date, and end date from drop-down lists. Lists historical prices in a table and displays them on a graph.
- **Position Opening**: Allows selection of a currency pair, quantity, and direction (buy/sell) to open a position.
- **Position Closing**: Closes a position by entering its ID in an input field.
- **Opened Positions**: Displays all opened positions in a table.

---

## 3. Technologies Used
- **Frontend**: JavaFX
- **Backend**: Java (JDK 20)
- **Database**: sqlite
- **SOAP Integration**: Hungarian National Bank API
- **Forex API**: Oanda API
- **Version Control**: GitHub

---

## 4. Folder Structure
```plaintext
/src                 - Source code
/main/java           - Backend logic
/resources           - FXML files for the UI
/database            - SQL scripts for schema and data setup
/tests               - Unit tests
/docs                - Documentation
/out/artifacts       - Compiled JAR file
