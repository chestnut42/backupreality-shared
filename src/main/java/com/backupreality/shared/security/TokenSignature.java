package com.backupreality.shared.security;


public class TokenSignature
{
    public final String secret;
    public final String signature;


    public TokenSignature(
            String secret,
            String signature
    )
    {
        this.secret = secret;
        this.signature = signature;
    }
}

