# MQTT-Integrated Rule Processing System 

This repository contains a rules engine integrated with MQTT to process eligibility inputs for seasonal supplement eligibility. It connects to an MQTT broker, listens for messages on a specified topic, processes those messages using business rules, and then publishes the output to a designated output topic.

## Table of Contents
- [Overview](#overview)
- [Foreword](#foreword)  
- [Eligibility Rules](#eligibility-rules)  
  - [Winter Supplement Rules](#winter-supplement-rules)  
  - [Summer Supplement Rules](#summer-supplement-rules)  
- [Set Up the Development Environment](#set-up-the-development-environment)  
- [Configure Your Local Environment](#configure-your-local-environment)  
- [Install Dependencies](#install-dependencies)  
- [Unit Tests](#unit-tests)  
  - [Test Cases](#test-cases)  
- [Running the Application with Docker](#running-the-application-with-docker)  
  - [Managing the Container](#managing-the-container)  
- [Running the Application](#running-the-application)  
  - [Publishing a Message to the MQTT Topic](#publishing-a-message-to-the-mqtt-topic)  
  - [Checking the Output](#checking-the-output)
- [MQTT Topics](#mqtt-topics)
- [Data Formats](#data-formats)
  - [Input Data Format for Winter Supplement](#input-data-format-for-winter-supplement)
  - [Output Data Format for Winter Supplement](#output-data-format-for-winter-supplement)
  - [Input Data Format for Summer Supplement](#input-data-format-for-summer-supplement)
  - [Output Data Format for Summer Supplement](#output-data-format-for-summer-supplement)
- [Business Rules](#business-rules)
  - [Add a new rule](#add-a-new-rule)

## Overview  

The Supplement system is a benefit provided to help recipients cover extra expenses during holiday seasons. This application determines client eligibility for the Winter or Summer Supplement and calculates the eligible amount based on business rules.  

## Foreword  

The rules engine must work with the [Seasonal Supplement App](https://rules-engine-app.vercel.app/). For proper functionality, the project in this repository must be running.  

## Eligibility Rules  

### Winter Supplement Rules  

| Eligibility | Eligible for payment in December |
|----------------------|--------|
| Single person with no dependent children | $60 per calendar year |
| Childless couple | $120 per calendar year |
| Either a single- or two-parent family with dependent children | $120 per calendar year plus $20 for each dependent child |

### Summer Supplement Rules  

| Eligibility | Eligible for payment in July |
|----------------------|--------|
| A single person with no dependent children and a household income of less than or equal to $50,000 per year | $150 per calendar year |
| A childless couple with a household income of less than or equal to $80,000 per year | $250 per calendar year |
| A single-parent or two-parent family with dependent children and a household income of less than or equal to $90,000 per year | $100 per calendar year plus $50 for each dependent child |

## Set Up the Development Environment

The following technologies and tools were used during the development of this application:
* Java 17
* Maven
* Mosquitto 2.0.20
* Intellij IDEA Community Edition

## Configure Your Local Environment:

Update the configuration to point to your local MQTT broker. Edit the `src/main/resources/test.properties` file to specify the correct broker settings for local development:

```properties
mqtt.broker=tcp://localhost:1883
mqtt.topic.id=1234
mqtt.winter.input.topic=test/winterInput
mqtt.winter.output.topic=test/winterOutput
mqtt.summer.input.topic=test/summerInput
mqtt.summer.output.topic=test/summerOutput
rules.file.path=src/main/resources/rules/
```
For example, replace `test-topic-id` with the appropriate MQTT topic ID for your local setup.

Set `mqtt.broker` to your broker URL (e.g., `tcp://localhost:1883` for a local Mosquitto broker).

<b> `prod.properties` currently points to the `test.mosquitto.org` endpoint, when the application runs, it will interact with the production endpoints. </b>

## Install Dependencies:
This project uses Maven for dependency management. Before installing dependencies, ensure Mosquitto is running locally (as the tests require Mosquitto to pass). Start Mosquitto using:

```bash
mosquitto
```

To install the required dependencies, run the following command:

```bash
mvn clean install
```
## Unit Tests

The tests are divided into test and production tests. Therefore, before running the tests, Mosquitto must be running locally. You can start it by typing `mosquitto` in the terminal.

To ensure that the application works as expected, comprehensive unit tests were written. The tests cover various components, including the MQTT client and the rules engine.

### Run Tests:
Run the tests with Maven:
```bash
mvn test
```
### Test Cases:
The tests cover the following scenarios:

* Eligibility for Single Person: Verifies the eligibility and supplement amount for a single person without dependent children.
* Eligibility for Childless Couple: Checks eligibility for a childless couple.
* Eligibility for Parent Families: Validates the calculation for single-parent or two-parent families with children.
* Negative Cases: Ensures the engine handles invalid inputs (e.g., negative children) properly.
* MQTT Integration: Tests the interaction with the MQTT broker, verifying that the application correctly sends and receives messages.
* Base Amount Calculator and Children Amount Calculator for different scenarios.

## Running the Application with Docker

This project is available as a Docker image on DockerHub. Follow the steps below to set up and run the application using Docker.

### Prerequisites
Ensure that Docker is installed and running on your machine.

### Steps to Run
#### 1. Pull the Docker Image
Download the Docker image from DockerHub using the following command:
```bash
docker pull naydin43/wsrulesengine:1.0
```

#### 2. Run the container
Start the application by running the Docker container with this command:
```bash
docker run -p 8080:8080 --name mqtt-rules-engine naydin43/wsrulesengine:1.0
```

#### 3. Access the Application
The demo application is accessible at [this link](https://rules-engine-app.vercel.app). However, for the system to work as intended:
* Ensure that your Docker container is running on your machine.
* Open the demo application in your browser.
* Enter the required inputs on the website and click the Submit button.

The demo application will communicate with the Docker container running locally, and the output will be displayed on the website after processing.
You can access the source code of the demo application [here](https://github.com/neslihanaydin/RulesEngineApp).

### Managing the Container
#### Stop the container
To stop the running container, use the following command:
```bash
docker stop mqtt-rules-engine
```
#### Remove the container
To remove the container after stopping it, use this command:
```bash
docker rm mqtt-rules-engine
```

## Running the Application

### 1. Complete the Configuration
First, ensure that you have configured the `prod.properties` file correctly with the necessary settings. These steps have been mentioned earlier in the documentation.

### 2. Build the Project
After completing the configurations, build the application using the following command:
```bash
mvn clean install
```

### 3. Run the Application
```bash
java -jar target/WSRulesEngine-1.0-SNAPSHOT-jar-with-dependencies.jar
```

This command will start the application. Once the application is successfully started, you will see logs like the following, showing the MQTT topic the application is subscribed to:
```bash
2025-02-20 16:28:36 INFO  o.d.c.k.b.i.InternalKieModuleProvider - Creating KieModule for artifact org.apache.maven:maven-builder-support:3.8.6
2025-02-20 16:28:37 INFO  org.neslihantrpc.mqtt.MqttConfig - Configuration file loaded successfully.
2025-02-20 16:28:37 INFO  o.n.mqtt.MqttConnectionManager - Connected to MQTT broker.
2025-02-20 16:28:38 INFO  o.n.mqtt.MqttClientHandler - Subscribed to topics: RulesEngine/summerSupplementInput/72b88106039c
2025-02-20 16:28:38 INFO  o.n.mqtt.MqttClientHandler - Subscribed to topics: RulesEngine/winterSupplementInput/72b88106039c
```
### Publishing a Message to the MQTT Topic
The application listens for messages on a specific MQTT topic and processes them accordingly. You can publish a message either through the console or via the demo web application.
#### Using the Mosquitto
To send a message to the MQTT broker, you can use <b>Mosquitto</b>. Run the following command:
```bash
mosquitto_pub -h test.mosquitto.org -t "RulesEngine/winterSupplementInput/72b88106039c" -m '{"id":"a0c5365f","numberOfChildren":2,"familyComposition":"Single","familyUnitInPayForDecember":true}'
```
#### Using the Web App
You can also sent input via the demo web application, which is accessible at [this link](https://rules-engine-app.vercel.app). 
1. Open the demo application in your browser.
2. Enter the required inputs in the form fields and click the Submit button.

Once the message is sent, you will see the corresponding outputs displayed on the website.

### Checking the Output
Once the message is published, the application will process the message and output the result. You will see logs similar to the following:
```bash
2025-02-20 16:59:46 INFO  org.neslihantrpc.mqtt.MqttPublisher - Output published to topic: RulesEngine/winterSupplementOutput/72b88106039c : {"id":"72b88106039c","isEligible":true,"childrenAmount":20.0,"baseAmount":120.0,"supplementAmount":140.0,"supplementType":"WINTER"}
2025-02-20 16:59:46 INFO  o.n.mqtt.MqttClientHandler - Message delivered
```

## MQTT Topics

### MQTT topics for Winter Supplement:
Input: RulesEngine/winterSupplementInput/
Output: RulesEngine/winterSupplementOutput/

### MQTT topics for Summer Supplement:
Input: RulesEngine/summerSupplementInput/
Output: RulesEngine/summerSupplementOutput/

## Data Formats

The application processes input data and generates output data based on specific JSON schemas. Ensure that the data conforms to the formats described below for proper functionality.

### Input Data Format for Winter Supplement
The input data must adhere to the following JSON schema:
```json
{
  "id": "str",
  "numberOfChildren": "int",
  "familyComposition": "str",
  "familyUnitInPayForDecember": "bool"
}
```
Example Input:
```json
{
  "id": "a0c5365f",
  "numberOfChildren": 2,
  "familyComposition": "single",
  "familyUnitInPayForDecember": true
}
```
* id: A unique identifier that ensures traceability between input and output data.
* numberOfChildren: The number of children in the family.
* familyComposition: The type of family, either single or couple.
* familyUnitInPayForDecember: Must be true for eligibility.
  
### Output Data Format for Winter Supplement

The application generates output data in the following JSON schema:
```json
{
  "id": "str",
  "isEligible": "bool",
  "baseAmount": "float",
  "childrenAmount": "float",
  "supplementAmount": "float"
}
```
Example Output:
```json
{
  "id": "a0c5365f",
  "isEligible": true,
  "baseAmount": 120.0,
  "childrenAmount": 40.0,
  "supplementAmount": 160.0
}
```
* id: ID from input
* isEligible: Eligibility, equal to "familyUnitInPayForDecember"
* baseAmount: Base amount calculated from family composition.
* childrenAmount: Additional amount calculated based on the number of children.
* supplementAmount: The total supplement amount, summing baseAmount and childrenAmount.

### Input Data Format for Summer Supplement

The input data must adhere to the following JSON schema:
```json
{
   "id": "str",
  "numberOfChildren": "int",
  "familyComposition": "str",
  "householdIncome": "float",
  "familyUnitInPayForJuly": "bool"
}
```
Example Input:
```json
{
  "id": "a0c5365f",
  "numberOfChildren": 2,
  "familyComposition": "single",
  "householdIncome": "85000.0",
  "familyUnitInPayForJuly": true
}
```
* id: A unique identifier that ensures traceability between input and output data.
* numberOfChildren: The number of children in the family.
* familyComposition: The type of family, either single or couple.
* householdIncome: The amount of the family's annual income.
* familyUnitInPayForJuly: Must be true for eligibility.

### Output Data Format for Summer Supplement

The application generates output data in the following JSON schema:
```json
{
  "id": "str",
  "isEligible": "bool",
  "baseAmount": "float",
  "childrenAmount": "float",
  "supplementAmount": "float"
}
```
Example Output:
```json
{
  "id": "a0c5365f",
  "isEligible": true,
  "baseAmount": 100.0,
  "childrenAmount": 100.0,
  "supplementAmount": 200.0
}
```
* id: ID from input
* isEligible: Eligibility, equal to "familyUnitInPayForJuly"
* baseAmount: Base amount calculated from family composition.
* childrenAmount: Additional amount calculated based on the number of children.
* supplementAmount: The total supplement amount, summing baseAmount and childrenAmount.

## Business Rules
This project uses Drools to define and manage business rules. All rule definitions are located in the `src/main/resources/rules` directory.

### Add a new rule
1. Edit the existing rules files or create a new `.drl` file in the `rules` directory.
2. Add your new rule following the Drools syntax.
3. Save the file, and the changes will be automatically applied when the rules engine is executed.
For more information on Drools syntax and usage, refer to the [Drools Documentation](https://www.drools.org/).
