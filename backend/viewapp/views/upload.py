from django.views import View
from rest_framework.views import APIView  # For swagger
import sys
import os
from datetime import datetime
from PIL import Image

sys.path.append("..")

from utils import utils, responses
from userapp.models import User
from profileapp.models import Profile


class ImageUploadView(APIView):
    def post(self, request):
        if not request.FILES:
            return utils.send_json(responses.fileDoesNotExists)

        file = request.FILES.popitem()
        if not file:
            return utils.send_json(responses.fileDoesNotExists)
        image = file[1][0]
        try:
            binary_image = image.file
            img = Image.open(binary_image)
        except:
            return utils.send_json(responses.imageDoesNotExists)

        num = datetime.now().strftime("%Y%m%d_%H%M%S")
        abspath = os.path.abspath("../images/{}.jpg".format(num))
        print(abspath)
        try:
            img.save(
                abspath,
                "JPEG",
            )
        except:
            return utils.send_json(responses.saveFailed)
        return utils.send_json(responses.saveSucceed)
