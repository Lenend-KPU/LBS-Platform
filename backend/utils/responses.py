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
createUserSucceed = {"success": True, "status": 200, "comment": "create user succeed"}
modifyUserSucceed = {"success": True, "status": 200, "comment": "modify user succeed"}
deleteUserSucceed = {"success": True, "status": 200, "comment": "delete user succeed"}

# Profile
noProfile = {"success": False, "status": 200, "comment": "there's no matched profile"}
noMyProfile = {"success": False, "status": 200, "comment": "My Profile is not exists"}
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

# Friend(follower)
noFriend = {"success": False, "status": 200, "comment": "there's no matched friend"}
friendExists = {
    "success": False,
    "status": 403,
    "comment": "Friend has already Exsited",
}
createFriendSucceed = {
    "success": True,
    "status": 200,
    "comment": "create friend succeed",
}
deleteFriendSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete friend succeed",
}

# Place
noPlace = {"success": False, "status": 200, "comment": "Place does not exists"}
noPlaceNum = {
    "success": False,
    "status": 200,
    "comment": "Place Field 'id' expected a number",
}
createPlaceSucceed = {
    "success": True,
    "status": 200,
    "comment": "create place succeed",
}
modifyPlaceSucceed = {
    "success": True,
    "status": 200,
    "comment": "modify place succeed",
}
deletePlaceSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete place succeed",
}

# Document
noDocument = {"success": False, "status": 200, "comment": "Document does not exists"}
createDocumentSucceed = {
    "success": True,
    "status": 200,
    "comment": "create Document succeed",
}
modifyDocumentSucceed = {
    "success": True,
    "status": 200,
    "comment": "modify Document succeed",
}
deleteDocumentSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete Document succeed",
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
createCommentSucceed = {
    "success": True,
    "status": 200,
    "comment": "create comment succeed",
}
modifyCommentSucceed = {
    "success": True,
    "status": 200,
    "comment": "modify comment succeed",
}
deleteCommentSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete comment succeed",
}

# Like
likeDoesNotExists = {
    "success": False,
    "status": 200,
    "comment": "like does not exists",
}
createLikeSucceed = {
    "success": True,
    "status": 200,
    "comment": "create like succeed",
}
modifyLikeSucceed = {
    "success": True,
    "status": 200,
    "comment": "modify like succeed",
}
deleteLikeSucceed = {
    "success": True,
    "status": 200,
    "comment": "delete like succeed",
}