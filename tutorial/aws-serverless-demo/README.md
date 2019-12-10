# RESTful API with AWS Lambda &amp; API Gateway

A demo project which implements a RESTful API (specs [here](https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.1)) with AWS Lambda &amp; API Gateway 
Related technologies include:

1. **HikariCP** for connection pooling to AWS RDS
2. **Log4j2** for logging
3. **Kafka Streaming** for data analysis.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What you need to install and how to install them

1. Install AWS Command Line Interface ([AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html))
2. Install [Docker](https://docs.docker.com/install/) (Docker must always be running whenever you develop, test, analyze, or deploy serverless applications or functions)
3. Install AWS Serverless Application Model Command Line Interface ([AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html))
4. [AWS-Toolkit for Intellij](https://aws.amazon.com/intellij/)
    * Create an IAM user and group
    * Create an access key for the user
      

## Deployment on AWS

1. Create another EC2 instance to host zookeeper/ kafka broker
    1. Set up security settings to allow inbound connections on port 9092 from VPC and/or your machine
    
    2. Update jdk
        ``` 
        sudo yum update -y
        sudo yum install java-1.8.0
        sudo yum remove java-1.7.0-openjdk
        ```
    
    3. Download Kafka
        ```
        wget https://www-eu.apache.org/dist/kafka/2.3.1/kafka-2.3.1-src.tgz
        tar -xzf kafka-2.3.1-src.tgz
        cd kafka-2.3.1-src/
        ```
        
    4. Start Zookeeper
        ```
        bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
        ```
        
        Test that zookeeper is up and running: `echo "ruok" | nc localhost 2181`
    
    5. Set up advertised.listeners config param in **server.properties**
        
        `advertised.listeners=PLAINTEXT://<kafka-hostname>:9092`
        
    6. Start Kafka server
        ```
        bin/kafka-topics.sh –create –bootstrap-server localhost:9092 –replication-factor 1 –partitions 1 –topic lift-usage-input
        bin/kafka-topics.sh –create –bootstrap-server localhost:9092 –replication-factor 1 –partitions 1 –topic lift-usage-output
        ```

2. Grant **AWSLambdaVPCAccessExecutionRole** policy to AWS Lambda Functions
3. Enable Lambda Proxy Integration from API Gateway

## Run

1. Make sure you are using the correct AWS Credentials in the right region
2. Deploy AWS Lambda Functions onto a CloudFormation stack
3. Send GET/POST requests to API Gateway
4. Check if database gets updated correspondingly
5. Read messages from lift-usage-output topic on kafka broker hosted on EC2
    
    * Read from command line:
      ```
      bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic lift-usage-output \
      --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true \
      --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
      ```
      
    * Read from Kafka Consumer written in Java: `Run kafka.StreamConsumer.java`

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md]() for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning
 

## Authors

* **QA Hoang**

See also the list of [contributors]() who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments