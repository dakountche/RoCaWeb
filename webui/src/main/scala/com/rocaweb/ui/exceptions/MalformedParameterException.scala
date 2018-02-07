package com.rocaweb.ui.exceptions

case class MalformedParameterException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) {

}

