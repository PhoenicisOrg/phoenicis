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

package com.playonlinux.tools.gpg;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Iterator;

import static org.bouncycastle.openpgp.PGPUtil.getDecoderStream;

/**
 * Verifies the signature of a script
 */
public class SignatureChecker {
    private final Logger LOGGER = LoggerFactory.getLogger(SignatureChecker.class);

    private String publicKey;
    private String signedData;
    private String signature;

    /**
     * Define the signature
     * @param signature The signature
     * @return the same object
     */
    public SignatureChecker withSignature(String signature) {
        this.signature = signature;
        return this;
    }

    /**
     * Define the data
     * @param signedData The data to verify
     * @return the same object
     */
    public SignatureChecker withData(String signedData) {
        this.signedData = signedData;
        return this;
    }

    public SignatureChecker withPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public Boolean check() {
        final PGPPublicKey pgpSigningKey = readPublicKey(new ByteArrayInputStream(publicKey.getBytes()));

        final ArmoredInputStream armoredInputStream;
        try {
            armoredInputStream = new ArmoredInputStream(new ByteArrayInputStream(signature.getBytes()));
        } catch (IOException e) {
            throw new SignatureException("Failed to verify signature", e);
        }

        final PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(armoredInputStream);

        try {
            final Object nextObject = pgpObjectFactory.nextObject();
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

            initVerify(pgpSignature, pgpSigningKey);

            pgpSignature.update(signedData.getBytes());
            return pgpSignature.verify();
        } catch (IOException | PGPException | NoSuchProviderException | java.security.SignatureException e) {
            throw new SignatureException("Failed to verify signature", e);
        }
    }

    private void initVerify(PGPSignature pgpSignature, PGPPublicKey pgpSigningKey) throws PGPException, NoSuchProviderException {
        try {
            pgpSignature.initVerify(pgpSigningKey, "BC");
        } catch(NoSuchProviderException e) {
            LOGGER.debug("No security provider found. Adding bouncy castle. This message can be ignored", e);
            Security.addProvider(new BouncyCastleProvider());
            pgpSignature.initVerify(pgpSigningKey, "BC");
        }
    }


    private PGPPublicKey readPublicKey(InputStream publicKeyInputStream) {
        try(InputStream publicKeyDecoderStream = getDecoderStream(publicKeyInputStream)) {
            final PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(publicKeyDecoderStream);
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
        } catch (IOException | PGPException e) {
            throw new SignatureException("Failed to read public key", e);
        }
    }

    public static String getPublicKey() {
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(SignatureChecker.class.getResourceAsStream("playonlinux.gpg")));

        final StringBuilder readPublicKey = new StringBuilder();
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
