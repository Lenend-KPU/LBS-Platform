from rest_framework.views import APIView  # For swagger
from django.http import HttpResponse, HttpRequest
from django.core.files.uploadedfile import InMemoryUploadedFile
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

        if not request.FILES:
            return utils.send_json(responses.fileDoesNotExists)
        file: InMemoryUploadedFile = request.FILES["file"]

        temp_file_name = "temp.png"

        with open(temp_file_name, "wb") as w:
            w.write(file.open().file.read())

        file_img = Image.open(temp_file_name)
        file_name = str(image_hash.average_hash(file_img)) + ".png"

        s3_client.upload_file(
            temp_file_name,
            "lbs-bucket",
            file_name,
            ExtraArgs={"ContentType": "image/png", "ACL": "public-read"},
        )

        os.remove("temp.png")

        host_image_url = (
            f"https://lbs-bucket.s3.ap-northeast-2.amazonaws.com/{file_name}"
        )

        return HttpResponse(host_image_url, status=200)
