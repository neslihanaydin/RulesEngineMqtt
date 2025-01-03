# Winter Supplement Rules Engine

Winter Supplement is an additional benefit provided to recipients of certain assistance programs to help cover extra expenses during the winter holiday season. This application determines the eligibility of clients for the Winter Supplement and calculates the eligible amount based on business rules.

This repository contains a rules engine integrated with MQTT to process eligibility inputs for winter supplement eligibility. It connects to an MQTT broker, listens for messages on a specified topic, processes those messages using business rules, and then publishes the output to a designated output topic.

### Table below is the rule that is used to calculate the Winter Supplement:

| Eligibility | Eligible for payment in December |
|----------------------|--------|
| Single person with no dependent children | $60 per calendar year |
| Childless couple | $120 per calendar year |
| Either a single- or two-parent family with dependent children | $120 per calendar year plus $20 for each dependent child |

## Foreword

The rules engine must work with the provided Winter Supplement Calculator, which gives an MQTT topic ID for integrating the rule engine with the web application. While MQTT topic IDs and topics were used successfully for subscription and publishing operations on the console, interaction with the provided website could not be established. Due to unforeseen issues, it was not possible to discuss the details of this problem, and therefore, the test connection was performed on the console instead.

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
mqtt.topic.id=test-topic-id
mqtt.input.topic=input-topic/
mqtt.output.topic=output-topic/
rules.file.path=src/main/resources/rules/eligibility.drl
```
For example, replace `test-topic-id` with the appropriate MQTT topic ID for your local setup.

Set `mqtt.broker` to your broker URL (e.g., `tcp://localhost:1883` for a local Mosquitto broker).

### Additionally, `prod.properties` currently points to the `test.mosquitto.org` endpoint, so when the application runs, it will interact with the production endpoints.

## Changing the MQTT Topic ID

If you need to update the MQTT topic ID to run the engine on a local machine:
1. Open `src/main/resources/test.properties`.
2. Modify the `mqtt.topic.id` property to match the topic ID you want to use. For example:
   ```properties
   mqtt.topic.id=local-topic-id
   ```
4. After making this change, restart the application to apply the new topic ID.

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

### Prerequisites:
* Mosquitto Broker: Ensure that Mosquitto is installed and running locally. Start it with the following command:
```bash
mosquitto
```

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

## Running the Application
### 1. Complete the Configuration
First, ensure that you have configured the `prod.properties` file correctly with the necessary settings. These steps have been mentioned earlier in the documentation.

### 2. Run the Application
After completing the configurations, run the application using the following command:
```bash
mvn exec:java
```
This command will start the application. Once the application is successfully started, you will see logs like the following, showing the MQTT topic the application is subscribed to:
```bash
2024-12-02 16:56:13 INFO  o.n.mqtt.MqttClientHandler - Connected to MQTT broker.
2024-12-02 16:56:14 INFO  o.n.mqtt.MqttClientHandler - Subscribed to topic: BRE/calculateWinterSupplementInput/af3054e0-27ff-428e-9531-72b88106039c
2024-12-02 16:56:14 INFO  org.neslihantrpc.Main - Application started successfully.
```
### 3. Publish a Message to the MQTT Topic
The application will listen for messages on the subscribed MQTT topic and process them accordingly. To publish a message, use <b>Mosquitto</b>. Run the following command to send a message to the MQTT broker:
```bash
mosquitto_pub -h test.mosquitto.org -t "BRE/calculateWinterSupplementInput/af3054e0-27ff-428e-9531-72b88106039c" -m '{"id":"a0c5365f","numberOfChildren":2,"familyComposition":"Single","familyUnitInPayForDecember":true}'
```
### 4. Check the Output
Once the message is published, the application will process the message and output the result. You will see logs similar to the following:
```bash
2024-12-02 17:00:01 INFO  o.n.mqtt.MqttClientHandler - Processing input
2024-12-02 17:00:01 INFO  o.n.engine.RulesEngineFactory - Creating a new RulesEngine instance.
2024-12-02 17:00:01 INFO  org.neslihantrpc.engine.RulesEngine - WinterSupplementEligibilityOutput is -> WinterSupplementEligibilityOutput{id='a0c5365f', isEligible=true, baseAmount=120.0, childrenAmount=40.0, supplementAmount=160.0}
2024-12-02 17:00:02 INFO  o.n.mqtt.MqttClientHandler - Output published to topic: BRE/calculateWinterSupplementOutput/af3054e0-27ff-428e-9531-72b88106039c
2024-12-02 17:00:02 INFO  o.n.mqtt.MqttClientHandler - Message delivery complete: true
```
## Data Formats

The application processes input data and generates output data based on specific JSON schemas. Ensure that the data conforms to the formats described below for proper functionality.

### Input Data Format
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
### Output Data Format
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

## Business Rules
This project uses Drools to define and manage business rules. All rule definitions are located in the `src/main/resources/rules` directory.

### To add a new rule:
1. Open the `eligibility.drl` file in the `rules` directory.
2. Add your new rule following the Drools syntax.
3. Save the file, and the changes will be automatically applied when the rules engine is executed.
For more information on Drools syntax and usage, refer to the [Drools Documentation](https://www.drools.org/).
