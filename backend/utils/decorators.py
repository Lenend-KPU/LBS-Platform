from .utils import decode_jwt, send_json
from .responses import loginRequired, userDoesNotMatch
from django.contrib.auth.models import User


def login_required(func):
    def wrapper(self, request, *args, **kwargs):
        session = request.session
        if "JWT_TOKEN" not in session:
            return send_json(loginRequired)
        decoded = decode_jwt(session)
        if "userid" not in decoded:
            return send_json(loginRequired)
        userid = decoded["userid"]
        try:
            User.objects.get(id=userid)
        except User.DoesNotExist:
            return send_json(userDoesNotMatch)
        return func(self, request, *args, **kwargs)

    return wrapper