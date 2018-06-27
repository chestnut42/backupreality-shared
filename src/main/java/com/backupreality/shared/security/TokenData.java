package com.backupreality.shared.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TokenData
{
    public final Optional<String> userID;
    public final Optional<List<String>> authorities;
    public final Optional<String> secret;


    TokenData(
            Optional<String> userID,
            Optional<List<String>> authorities,
            Optional<String> secret
    )
    {
        this.userID = userID;
        this.authorities = authorities;
        this.secret = secret;
    }



    public static class Builder
    {
        private Optional<String> userID = Optional.empty();
        private Optional<List<String>> authorities = Optional.empty();
        private Optional<String> secret = Optional.empty();


        private Builder() {}


        public Builder setUserID(String userID)
        {
            return this.setUserID(Optional.of(userID));
        }

        public Builder setUserID(Optional<String> userID)
        {
            this.userID = userID;
            return this;
        }

        public Builder setAuthorities(List<GrantedAuthority> authorities)
        {
            return setAuthoritiesRaw(authorities
                    .stream()
                    .map((a) -> a.getAuthority())
                    .collect(Collectors.toList()));
        }

        public Builder setAuthoritiesRaw(List<String> authoritiesRaw)
        {
            this.authorities = Optional.of(authoritiesRaw);
            return this;
        }

        public Builder setSignature(TokenSignature tokenSignature)
        {
            return this.setSecret(tokenSignature.secret);
        }

        public Builder setSecret(String secret)
        {
            this.secret = Optional.of(secret);
            return this;
        }


        public TokenData build()
        {
            return new TokenData(
                    this.userID,
                    this.authorities,
                    this.secret
            );
        }
    }


    public static Builder builder()
    {
        return new Builder();
    }
}

