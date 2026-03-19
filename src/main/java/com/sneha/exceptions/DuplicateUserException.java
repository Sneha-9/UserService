package com.sneha.exceptions;

public class DuplicateUserException extends Exception{
  public DuplicateUserException(){
        super("User with provided emailId already exists");
    }
}
