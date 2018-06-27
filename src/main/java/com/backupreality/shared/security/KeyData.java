package com.backupreality.shared.security;

import com.backupreality.shared.utils.StringUtils;
import java.util.Base64;


public class KeyData
{
    public static KeyData ofRawData(byte[] data)
    {
        return new KeyData(data);
    }


    public static KeyData ofBase64String(String base64String)
    {
        byte[] data = Base64.getDecoder().decode(base64String);
        return ofRawData(data);
    }


    public static KeyData ofHexString(String hexString)
    {
        byte[] data = StringUtils.parseHexBinary(hexString);
        return ofRawData(data);
    }


    private final byte[] data;


    private KeyData(byte[] data)
    {
        this.data = data;
    }


    public byte[] getData()
    {
        return data;
    }

    public String toBase64String()
    {
        return Base64.getEncoder().encodeToString(data);
    }

    public String toHEXString()
    {
        return StringUtils.printHexBinary(data);
    }
}

