package exceptions

import exceptions.AuthException

class UserNotFoundException() : AuthException("No such user");
