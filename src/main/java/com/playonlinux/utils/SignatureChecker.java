/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.utils;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;


import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Iterator;

import static org.bouncycastle.openpgp.PGPUtil.getDecoderStream;


public class SignatureChecker {
    private String publicKey;
    private String signedData;
    private String signature;

    public SignatureChecker withSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public SignatureChecker withData(String signedData) {
        this.signedData = signedData;
        return this;
    }

    public SignatureChecker withPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public Boolean check() throws IOException, CMSException, PGPException, NoSuchProviderException, SignatureException {
        PGPPublicKey pgpSigningKey = readPublicKey(new ByteArrayInputStream(publicKey.getBytes()));

        ArmoredInputStream armoredInputStream = new ArmoredInputStream(new ByteArrayInputStream(signature.getBytes()));

        PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(armoredInputStream);

        Object nextObject = pgpObjectFactory.nextObject();
        PGPSignature pgpSignature = null;
        if (nextObject instanceof PGPSignatureList) {
            PGPSignatureList list = (PGPSignatureList) nextObject;
            if (list.size() > 0) {
                pgpSignature = list.get(0);
            }
        }

        if(pgpSignature == null) {
            return false;
        }

        try {
            pgpSignature.initVerify(pgpSigningKey, "BC");
        } catch(NoSuchProviderException e) {
            Security.addProvider(new BouncyCastleProvider());
            pgpSignature.initVerify(pgpSigningKey, "BC");
        }

        pgpSignature.update(signedData.getBytes());
        return pgpSignature.verify();
    }


    private PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
        in = getDecoderStream(in);
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in);

        PGPPublicKey key = null;

        Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();


        while (key == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (key == null && kIt.hasNext()) {
                PGPPublicKey k = kIt.next();
                key = k;
            }
        }

        if (key == null) {
            throw new IllegalArgumentException("Can't find encryption key in key ring.");
        }

        return key;
    }
}
