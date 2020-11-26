package com.innilabs.restboard.exception;

import lombok.Getter;

@Getter
public class BoardException extends Exception{
    private int  type;

    public BoardException(String message){
        super(message);
    }

    public BoardException(int type, String message){
        super(message);
        this.type = type;
    }
}
