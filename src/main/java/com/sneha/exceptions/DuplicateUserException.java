package com.sneha.exceptions;

import com.sneha.Constants;

public class DuplicateUserException extends Exception{
  public DuplicateUserException(){
        super(Constants.DUPLICATE_USER_EXCEPTION_MESSAGE);
    }
}
