package com.rocaweb.ui.exceptions

case class NotPDMLException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) {}