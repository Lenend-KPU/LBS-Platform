# For Swagger Documentation
from rest_framework import serializers


class UserBodySerializer(serializers.Serializer):
    username = serializers.CharField(help_text="사용자 이름")
    email = serializers.CharField(help_text="이메일, 유니크 값, 해당 컬럼으로 로그인")
    password = serializers.CharField(help_text="비밀번호, 해당 컬럼으로 로그인")
    address = serializers.CharField(help_text="주소")
