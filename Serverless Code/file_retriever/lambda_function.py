import boto3
import base64
import json

s3 = boto3.resource('s3')

def lambda_handler(event, context):
    bucket_name = 'project-bucket-kv736815'
    deviceId = event['deviceId']
    prefix = f'{deviceId}/'
    
    bucket = s3.Bucket(bucket_name)
    files = [obj.key for obj in bucket.objects.filter(Prefix=prefix)]
    
    images = []
    for file in files:
        if file.endswith('.png'):
            obj = bucket.Object(file)
            response = obj.get()
            image_bytes = response['Body'].read()
            encoded_image = base64.b64encode(image_bytes).decode('utf-8')
            images.append({"filename": file, "image": encoded_image})
    
    json_data = json.dumps({"images": images})
    return {"response": json_data}
