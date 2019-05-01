package com.harishkannarao.ktor.dao.exception

class DbEntityConflictException(errorMessage: String) : RuntimeException(errorMessage)