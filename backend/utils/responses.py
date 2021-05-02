# JSON 응답 딕셔너리를 모아둔 코드

APIOnly = {"success": True, "status": 200, "comment": "API Only"}
postOnly = {"success": False, "status": 400, "comment": "only POST allowed"}

ok = {"success": True, "status": 200, "comment": "OK"}
no = {"success": False, "status": 400, "comment": "NO"}

illegalArgument = {"success": False, "status": 400, "comment": "Illegal argument"}
illegalModifyArgument = {
    "success": False,
    "status": 400,
    "comment": "Illegal modify argument",
}
# User
noUser = {"success": False, "status": 200, "comment": "there's no matched user"}
userAlreadyRegistered = {
    "success": False,
    "status": 403,
    "comment": "User has already registered",
}
registerSucceed = {"success": True, "status": 200, "comment": "register succeed"}
modifyUserSucceed = {"success": True, "status": 200, "comment": "modify user succeed"}
deleteUserSucceed = {"success": True, "status": 200, "comment": "delete user succeed"}

# Profile
noProfile = {"success": False, "status": 200, "comment": "there's no matched profile"}
profileAlreadyRegistered = {
    "success": False,
    "status": 403,
    "comment": "Profile has already registered",
}
modifyProfileSucceed = {
    "success": True,
    "status": 200,
    "comment": "modify profile succeed",
}
deleteProfileSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete profile succeed",
}

# Login
userAlreadyLogin = {
    "success": True,
    "status": 200,
    "comment": "User has already logged in",
    "is_login": True,
}
userLogin = {"success": True, "status": 200, "comment": "Login succeed"}
userDoesNotExist = {"success": False, "status": 403, "comment": "User does not exists"}
userDoesNotMatch = {
    "success": False,
    "status": 403,
    "comment": "Username and password does not match",
}

# Logout
userAlreadyLogout = {
    "success": False,
    "status": 403,
    "comment": "User has already logged out",
}
userLogout = {"success": True, "status": 200, "comment": "Logout succeed"}
loginRequired = {
    "success": True,
    "status": 200,
    "comment": "login required",
    "is_login": False,
}
postSucceed = {"success": True, "status": 200, "comment": "Post request succeed"}


def getSucceedFunc(field):
    return {"success": True, "status": 200, "comment": f"obtaining {field} succeed"}


# comment
commentDoesNotExists = {
    "success": False,
    "status": 200,
    "comment": "Comment does not exists",
}
