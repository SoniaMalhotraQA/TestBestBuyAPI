# TestBestBuyAPI
Rest Assured based TestNG suite for testing BestBuy API

## Prerequisites

Make sure you have Best Buy API(https://github.com/BestBuy/api-playground) running on your local. 
If the API is hosted on some external server then follow the configuration step to add the server details where the API is deployed.

Install Java 8
https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

Install Maven (Apache Maven 3.6.3 or above)
https://maven.apache.org/install.html

## Configuration Options

Configuration settings are managed using src/test/resources/configuration.properties file . 
The options that you may want to adjust, depending on where the API is hosted, are:
* `apiServerURL`  -  Server URL where API is hosted. Defaults to http://localhost.
* `apiServerPort` -  HTTP port where the API is listening. Defaults to 3030.

## Getting Started

```bash
git clone https://github.com/SoniaMalhotraQA/TestBestBuyAPI/
cd TestBestBuyAPI
mvn clean test
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running TestSuite
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.736 sec - in TestSuite

Results :

Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```
The test report will be generated in TestBestBuyAPI/target/surefire-reports/index.html


