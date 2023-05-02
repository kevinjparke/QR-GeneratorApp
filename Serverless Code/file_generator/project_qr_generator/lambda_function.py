import json
import qrcode
import boto3
import base64

from io import BytesIO

s3 = boto3.client('s3')

def lambda_handler(event, context):
    deviceId = event['deviceId']
    string_to_generate = event['string_to_generate']
    filename = event['filename']
    detail = event['detail']
    
    if event['detail'] > 0 and event['detail'] < 41:
        version = detail
    else:
        return invalid_detail_resp(detail) 
    
    # Generate QR Code
    qr = qrcode.QRCode(
        version=version,
        error_correction=qrcode.constants.ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(string_to_generate)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    
    # Convert the PIL Image to a BytesIO object
    img_bytes = BytesIO()
    img.save(img_bytes, 'PNG')
    img_bytes.seek(0)
    
    
    s3.put_object(
        Body=img_bytes,
        Bucket='project-bucket-kv736815',
        Key=f'{deviceId}/{filename}.png'
    )
    
    return {
        'statusCode': 200,
        'body': json.dumps({
            'deviceId': deviceId,
            'image': base64.b64encode(img_bytes.getvalue()).decode('utf-8')
        })
    }


def invalid_detail_resp(detail):
    return {
        "statusCode": 400,
        "body": f'{detail} is not a valid value'
    }
