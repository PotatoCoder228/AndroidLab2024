package exceptions

import exceptions.AuthException

class IncorrectLoginOrPasswordException() : AuthException("Wrong login or password");
