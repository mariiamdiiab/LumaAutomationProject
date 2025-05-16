# Luma E-Commerce Test Automation Framework

## Overview
A Selenium WebDriver framework for automating tests on the Luma demo e-commerce website. Built with Java, Maven, and TestNG, this framework implements industry best practices for reliable web automation.

## Key Features
✔ **Fluent Page Object Model** - Clean, maintainable test structure  
✔ **Cross-Browser Testing** - Chrome, Firefox, Edge (including headless)  
✔ **Data-Driven Approach** - Excel-based test data management  
✔ **Comprehensive Reporting** - Allure reports with screenshots and logs  
✔ **CI/CD Ready** - Jenkins pipeline integration  
✔ **Parallel Execution** - TestNG-powered parallel testing  

## Technical Stack
- **Language**: Java 11+
- **Test Runner**: TestNG
- **Build Tool**: Maven
- **Reporting**: Allure + Log4j2
- **Driver Management**: WebDriverManager
- **CI/CD**: Jenkins

## Project Structure
```text
Project Structure
src/
├── main/
│   ├── java/org/project/
│   │   ├── pages/            # Page classes (POM implementation)
│   │   ├── utilities/        # Helper classes (ExcelReader, ConfigManager)
│   │   └── managers/        # WebDriver management
│   └── resources/
│       ├── config.properties
│       └── testdata/        # Excel test files
└── test/
    ├── java/org/project/tests/
    └── resources/
        ├── suites/          # TestNG XML files
        └── log4j2.xml
```
## Getting Started

### Prerequisites
- JDK 11+
- Maven 3.6+
- IDE (IntelliJ IDEA recommended)
- Modern browsers (Chrome/Firefox/Edge)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/mariiamdiiab/LumaAutomationProject.git
2.Import as Maven project in your IDE

## Running Tests
### IDE Execution
1.Navigate to src/test/java/org/project/tests/

2.Right-click on test class → "Run"

## Command Line Options

| Command | Description |
|---------|-------------|
| `mvn clean test` | Run all tests (default Chrome) |
| `mvn test -Dbrowser=firefox` | Run tests with Firefox |
| `mvn test -Dbrowser=edge` | Run tests with Microsoft Edge |
| `mvn test -Dbrowser=headless` | Run tests in headless mode |
| `mvn test -DsuiteXmlFile=suites/parallel.xml` | Run specific test suite |
| `mvn allure:serve` | Generate and view Allure report |

## Viewing Reports
### Generate Allure report:
mvn allure:serve

## CI/CD Integration (Jenkins)
1. Create new Pipeline job

2. Configure Git repository

3. Add build step:
mvn clean test -Dbrowser=headless

4.Add post-build action for Allure reports

## Test Data
### Location: src/main/resources/testdata/

TestData.xlsx: Test data

GlobalVariable.java: For environment URLs, default credentials, and values that rarely change
