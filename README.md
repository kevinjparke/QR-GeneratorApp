# QR-GeneratorApp
## Introduction
QR-Generator is an API built to utilise serverless mechanisms and cloud storage to generate and store images all for an Android mobile device. QR-Generator utilises AWS services for its industry leading reliability. The services used to create this API include, AWS Lambdas, Step Functions, Simple Storage Service, API Gateway, AWS Back Up, and Cloud Formation.

This application was build with the Python programming language to implement the the lambda functions as it is easy to learn and has a vast collection of libraries that I could utilise to implement the required functionalities. For the front end of my application I chose to use Kotlin as the language of choice as it is the future of android development.

## Cloud Delivery Model & Deployment
This project utilises a private cloud deployment model, as AWS services were used to host the API. This model was chosen as it simplifies the process of provisioning just the resources I need to build and deploy my services, without having to manage the resources myself. Additionally, this deployment model offers the most advantages as it relates to scalability and high availability.

The delivery model used for this project was Function-as-a-Service (AWS Lambda, AWS Step Function). This delivery model was chosen because it freed me from the responsibility of having to deploy and manage my own servers. Additionally, I could take advantage of Amazons infrastructure and capacity to scale as demand for the application grows.  These advantages along with the low cost of operating an FaaS service, and the nature of the functionalities offered by this app made this decision a no-brainer.

## Final Architecture
### Serverless Mechanisms
AWS Lambda and Step Functions were used as the core serverless components for this project. Lambda functions were created to define the logic used throughout the API. The following section describes the role of each lambda function within the state machine:

QRGenerator: Using the deviceId, string to generate, filename and detail, this lambda creates an image with a specified level of detail and stores it in an S3 bucket. Once the image is successfully stored in the bucket, the image is decoded into a base64 string so that it may be returned within a response as a JSON object

QRRetriever: Takes as input, the deviceID and the filename, and combines this input to search the directory of the folder for images stored there. The image(s) are returned as an array containing relevant metadata about each image. 

QRDelete: Takes the same input as QRRetriever and deletes the image from S3, if it exists. 

The Step Function used in this project utilises the latest upgrade of this service implemented by Amazon. The express state machine, aptly named ‘project_state_machine_express_fn’, provides the only means of returning responses from the lambda functions synchronously. This is especially helpful as I was able to avoid the integration of other services such as SQS to poll responses from the lambda functions after they were initially invoked. 

This projects state machine uses a simple definition that check the input type, such as ‘GENERATE’, ‘RETRIEVE’, and ‘DELETE’. Based on the type passed into it, the state machine will invoke the appropriate lambda function. 

## Network
The fully managed service of AWS API Gateway was used to create, deploy and manage the API. By acting as the proverbial front door of the service, with one endpoint, I could get the inherent advantages of simplifying the integration of the step functions as well as the lambda functions. The singular endpoint of the API allows for the API to be secured in a simpler way with no other entry points to the back end. With real-time monitoring and logging capabilities API Gateway provides an easy way to detect and troubleshoot security issues. 

## Storage and Back Ups
All data for the API was stored using S3 buckets. The deployment packages for each of my lambda functions as well as the storage for all generated images were store on S3. This service was chose because of its reliability and and scalability 
Cloud Operations & Management
The entire system was provisioned and managed using Cloud Formation which provides a an easy, reliable and reproducible way to provision and manage all of the resources mentioned above.

## Monitoring
The service most likely to offer the best assurances by way of monitoring would be the Lambda functions. This is the function that will be utilised the most and as such it will be important to monitor each lambdas memory usage, duration and number of requests. 
