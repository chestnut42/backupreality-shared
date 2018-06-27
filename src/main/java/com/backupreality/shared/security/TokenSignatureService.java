package com.backupreality.shared.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacSigner;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;


public class TokenSignatureService
{
    public static TokenSignatureService of(
            KeyData hmacSecret
    )
    {
        MacSigner macSigner = new MacSigner(SignatureAlgorithm.HS256, hmacSecret.getData());
        return new TokenSignatureService(8, macSigner);
    }


    private final int tokenSignatureByteLength;
    private final MacSigner macSigner;


    public TokenSignatureService(
            int tokenSignatureByteLength,
            MacSigner macSigner
    )
    {
        if (tokenSignatureByteLength < 4)
        {
            throw new IllegalArgumentException("Token signature length is too small: " + tokenSignatureByteLength);
        }

        this.tokenSignatureByteLength = tokenSignatureByteLength;
        this.macSigner = macSigner;
    }


    public TokenSignature generateSignature()
    {
        byte[] secret = new byte[this.tokenSignatureByteLength];
        ThreadLocalRandom.current().nextBytes(secret);

        byte[] signature = macSigner.sign(secret);

        return new TokenSignature(
                KeyData.ofRawData(secret).toBase64String(),
                KeyData.ofRawData(signature).toBase64String()
        );
    }


    public boolean verifySignature(TokenSignature tokenSignature)
    {
        byte[] secret = KeyData.ofBase64String(tokenSignature.secret).getData();
        byte[] signature = KeyData.ofBase64String(tokenSignature.signature).getData();

        byte[] checkSignature = macSigner.sign(secret);

        return Arrays.equals(signature, checkSignature);
    }
}

