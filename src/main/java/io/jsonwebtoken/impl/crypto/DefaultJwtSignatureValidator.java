/*
 * Copyright (C) 2014 jsonwebtoken.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.codec.Decoder;
import io.jsonwebtoken.lang.Assert;

import java.nio.charset.Charset;
import java.security.Key;

public class DefaultJwtSignatureValidator implements JwtSignatureValidator {

    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    private final SignatureValidator signatureValidator;
    private final Decoder<String, byte[]> base64UrlDecoder;

    @Deprecated
    public DefaultJwtSignatureValidator(SignatureAlgorithm alg, Key key) {
        this(DefaultSignatureValidatorFactory.INSTANCE, alg, key, Decoder.BASE64URL);
    }

    public DefaultJwtSignatureValidator(SignatureAlgorithm alg, Key key, Decoder<String, byte[]> base64UrlDecoder) {
        this(DefaultSignatureValidatorFactory.INSTANCE, alg, key, base64UrlDecoder);
    }

    @Deprecated
    public DefaultJwtSignatureValidator(SignatureValidatorFactory factory, SignatureAlgorithm alg, Key key) {
        this(factory, alg, key, Decoder.BASE64URL);
    }

    public DefaultJwtSignatureValidator(SignatureValidatorFactory factory, SignatureAlgorithm alg, Key key, Decoder<String, byte[]> base64UrlDecoder) {
        Assert.notNull(factory, "SignerFactory argument cannot be null.");
        Assert.notNull(base64UrlDecoder, "Base64Url decoder argument cannot be null.");
        this.signatureValidator = factory.createSignatureValidator(alg, key);
        this.base64UrlDecoder = base64UrlDecoder;
    }

    @Override
    public boolean isValid(String jwtWithoutSignature, String base64UrlEncodedSignature) {

        byte[] data = jwtWithoutSignature.getBytes(US_ASCII);

        byte[] signature = base64UrlDecoder.decode(base64UrlEncodedSignature);

        return this.signatureValidator.isValid(data, signature);
    }
}
