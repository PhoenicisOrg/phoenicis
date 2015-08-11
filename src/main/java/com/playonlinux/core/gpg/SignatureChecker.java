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

package com.playonlinux.core.gpg;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Iterator;

import static org.bouncycastle.openpgp.PGPUtil.getDecoderStream;


public class SignatureChecker {
    private String publicKey;
    private String signedData;
    private String signature;
    private static final Logger LOGGER = Logger.getLogger(SignatureChecker.class);

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

    public Boolean check() throws IOException, PGPException, NoSuchProviderException, SignatureException {
        PGPPublicKey pgpSigningKey = readPublicKey(new ByteArrayInputStream(publicKey.getBytes()));

        ArmoredInputStream armoredInputStream = new ArmoredInputStream(new ByteArrayInputStream(signature.getBytes()));

        PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(armoredInputStream);

        Object nextObject = pgpObjectFactory.nextObject();
        PGPSignature pgpSignature = null;
        if (nextObject instanceof PGPSignatureList) {
            PGPSignatureList list = (PGPSignatureList) nextObject;
            if (!list.isEmpty()) {
                pgpSignature = list.get(0);
            }
        }

        if(pgpSignature == null) {
            return false;
        }

        try {
            pgpSignature.initVerify(pgpSigningKey, "BC");
        } catch(NoSuchProviderException e) {
            // LOGGER.info("No security provider found. Adding bouncy castle. This error can be ignored", e);
            Security.addProvider(new BouncyCastleProvider());
            pgpSignature.initVerify(pgpSigningKey, "BC");
        }

        pgpSignature.update(signedData.getBytes());
        return pgpSignature.verify();
    }


    private PGPPublicKey readPublicKey(InputStream publicKeyInputStream) throws IOException, PGPException {
        try(InputStream publicKeyDecoderStream = getDecoderStream(publicKeyInputStream)) {
            PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(publicKeyDecoderStream);
            PGPPublicKey key = null;

            Iterator rIt = pgpPub.getKeyRings();


            while (key == null && rIt.hasNext()) {
                PGPPublicKeyRing kRing = (PGPPublicKeyRing) rIt.next();
                Iterator kIt = kRing.getPublicKeys();
                while (key == null && kIt.hasNext()) {
                    key = (PGPPublicKey) kIt.next();
                }
            }

            if (key == null) {
                throw new IllegalArgumentException("Can't find encryption key in key ring.");
            }

            return key;
        }
    }

    public static String getPublicKey() {
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(SignatureChecker.class.getResourceAsStream("playonlinux.gpg")));

        StringBuilder readPublicKey = new StringBuilder();
        try {
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                readPublicKey.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return readPublicKey.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(SignatureChecker.class)
                .append(publicKey)
                .append(signedData)
                .append(signature)
                .toString();
    }
}
