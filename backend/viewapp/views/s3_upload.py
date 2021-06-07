from rest_framework.views import APIView  # For swagger
from django.http import HttpResponse, HttpRequest
import sys
import os
import boto3

sys.path.append("..")
from utils import utils, responses, image_hash
from PIL import Image
import io


class FileView(APIView):
    def post(self, request: HttpRequest) -> HttpResponse:
        s3_client = boto3.client(
            "s3",
            aws_access_key_id=os.environ.get("aws_access_key_id"),
            aws_secret_access_key=os.environ.get("aws_secret_access_key"),
        )

        if not request:
            return utils.send_json(responses.fileDoesNotExists)
        file = request.FILES["file"]
        print(file)
        file_img = Image.open(file)
        file_name = str(image_hash.average_hash(file_img))

        s3_client.upload_fileobj(
            file, "lbs-bucket", file_name, ExtraArgs={"ContentType": "image/png"}
        )

        host_image_url = (
            f"https://lbs-bucket.s3.ap-northeast-2.amazonaws.com/images/{file_name}.png"
        )

        return HttpResponse(host_image_url, status=200)
