import boto3

s3 = boto3.resource('s3')

def lambda_handler(event, context):
    bucket_name = 'project-bucket-kv736815'
    deviceId = event['deviceId']
    filename = event['filename']
    key_name = f'{deviceId}/{filename}.png'
    
    try:
        s3.Object(bucket_name, key_name).delete()
        
        return {
            "statusCode": 200, 
            "body": f'{filename} was deleted.'
        }
    except Exception as e:
        return {
            "statusCode": 400, 
            "body": f'{filename} could not be deleted. {e}'
        }
