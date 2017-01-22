/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

import org.junit.Test;

import static org.junit.Assert.*;

public class SignatureCheckerTest {
    private static final String SCRIPT = "#!/bin/bash\n" +
            "if [ \"$PLAYONLINUX\" = \"\" ]\n" +
            "then\n" +
            "exit 0\n" +
            "fi\n" +
            "source \"$PLAYONLINUX/lib/sources\"\n" +
            "\n" +
            "POL_SetupWindow_Init \n" +
            "POL_SetupWindow_free_presentation \"Anti-aliasing\" \"This script will enable anti-aliasing\"\n" +
            "POL_SetupWindow_games \"Choose an application\" \"Anti-aliasing\"\n" +
            "if [ \"$APP_ANSWER\" == \"\" ]\n" +
            "then\n" +
            "POL_SetupWindow_Close\n" +
            "exit\n" +
            "fi\n" +
            "PREFIX=$(detect_wineprefix \"$APP_ANSWER\")\n" +
            "select_prefix \"$PREFIX\"\n" +
            "fonts_to_prefix\n" +
            "POL_SetupWindow_wait_next_signal \"Processing\" \"Anti aliasing\"\n" +
            "REGEDIT4\n" +
            "\n" +
            "cat << EOF > \"$REPERTOIRE/tmp/fontsaa.reg\"\n" +
            "[HKEY_CURRENT_USER\\Control Panel\\Desktop]\n" +
            "\"FontSmoothing\"=\"2\"\n" +
            "\"FontSmoothingType\"=dword:00000002\n" +
            "\"FontSmoothingGamma\"=dword:00000578\n" +
            "\"FontSmoothingOrientation\"=dword:00000001\n" +
            "EOF\n" +
            "regedit \"$REPERTOIRE/tmp/fontsaa.reg\"\n" +
            "\n" +
            "POL_SetupWindow_detect_exit\n" +
            "POL_SetupWindow_message \"Anti-aliasing has been successfully enabled\" \"Anti-aliasing\"\n" +
            "POL_SetupWindow_Close\n" +
            "exit\n";

    private static final String SIGNATURE = "-----BEGIN PGP SIGNATURE-----\n" +
            "Version: GnuPG v1.4.9 (GNU/Linux)\n" +
            "\n" +
            "iEYEABECAAYFAk1cJE4ACgkQ5TH6yaoTykcNqQCdEECsTszINVdQRtPYZFU9Dat6\n" +
            "EoUAni+IJF9wAvjdYHviiiDNVflQKmYA\n" +
            "=v/ss\n" +
            "-----END PGP SIGNATURE-----";

    private static final String PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Comment: GPGTools - http://gpgtools.org\n" +
            "\n" +
            "mQGiBE0ozSYRBADdPem93uvIqrZGpkM8pSxKjyK5PmXhfBsCTRowU09b3OL1eqXP\n" +
            "s1k+waRy6YFK+jwA+wp8vPeGUUDeINMPayL+g+5hXitgoMWrna/64PPLaDf0cqSP\n" +
            "A/2kFxx3vdWaTHwqQRaSx4k68O/8yAJKK4K9FlpUSq3hvOIUYH3ze2XvvwCgszn3\n" +
            "awTWpcnuZZaeZn7E88CGTu8D/iHSLnkBvF8AGcnJUw8SyPSyKiGnBH6rssOpjy/0\n" +
            "Mkx7yfbjXrpiYWsEbvgmgPWGf1cnTELUFosQNR8rv4W7KS8KyYjDR6agc4ek1P6J\n" +
            "bQu2VGUoBhNm//LITwh4ZdOR9aZ1ewUQFksOPFnd/1peFu13MFduZCAHGCpCeoXI\n" +
            "ZE/VA/4/Akh2diRNb9DIAB3Mqxx1ZjhnLl/elb8wCWRo/YLPXiLQNdEHK9c8M+sn\n" +
            "pt/1zbRlg306n8N5C6s6z7HwbfZaA8fDL2UMkmuJrycO2YRWjx8GDhfoocmcQw6b\n" +
            "OXls6f2ZBeGAkj9l1ueDTL3IVClJftK53EjzPeSv84Onw46cK7RMUGxheU9uTGlu\n" +
            "dXggKFBsYXlPbkxpbnV4IGFuZCBQbGF5T25NYWMgc2NyaXB0aW5nIGtleXMpIDxn\n" +
            "cGdAcGxheW9ubGludXguY29tPohgBBMRAgAgBQJNKM0mAhsDBgsJCAcDAgQVAggD\n" +
            "BBYCAwECHgECF4AACgkQ5TH6yaoTykdOxQCfR8Y6HVG1FvNRuXDFKAN9O5eHrOEA\n" +
            "oKEt7Et07dALQnEA099qQOqCZxnQuQINBE0ozSYQCAC7JscaD6ompySGIRR3BmpY\n" +
            "mlwdJDbk0ptZPFi2PKK87oJc2r/fGwKZZw/hHBv46R0zDay8X1r/7a452scZ8SiP\n" +
            "Y2m02pXv1y+cS/c8jPUWNUG39BdzqD6SdgFjsuvgjeQGRpjVrh/K0SKrzLmmdV/9\n" +
            "85b8mbPjWpfn3iwaq/zmbLFZdGY/gcbihTfHNRDys4DX3UrOnQgq/X7psit1pDYT\n" +
            "qVX2gzZvwaXoCIAa0aw1/JAVi/H689bAAuASKkI57u4OdCfvdlMnA5DOcPRR8ttP\n" +
            "9z9wQC/Gsu1VkOsJ4Mfcu0Hh42e6Ta05TDIg+nZQ/iREJjHTnptOTNqeE4K6908D\n" +
            "AAMFB/9UlG6ab0kAXNKsmgeev3P5B1c9g/BgD9TuHF8fONQ04z2ar6n9iwJTxV8W\n" +
            "e7oTZkqS/Zq4dKdYTlhxck16g6CZHVDu4yWVIq7QSz8E5NLi0xhXLWciK3Tw3soQ\n" +
            "QZrfjoQnEuXCCXCWw/1B3odATjfpvRfRxbHZ5kUwwoiRUs3Y1pvRyHpvpBem72Lj\n" +
            "orYJSHh0B76atJG1o21LnsDQCILbANbqyF2BaT6mp7fH3v2f0fliLYTphd6T2i0/\n" +
            "5vDg5JymoIRJY7cjegprA1KIo1n5mvfv2zTARVNO3IyFVIaD7fdn3zW3Pzh75WKx\n" +
            "ghoOaMtjdexZKpJnEpkqbe8Qs2/qiEkEGBECAAkFAk0ozSYCGwwACgkQ5TH6yaoT\n" +
            "ykftZQCfTCroby2HAxhIFRO9+3ACr6bIDkYAnjS2zGBJ44bNFBYHet4DmI9JfEw1\n" +
            "=PAJ4\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    @Test
    public void testSignatureChecker_withValidSignature_returnTrue() {
        SignatureChecker signatureChecker = new SignatureChecker()
                .withSignature(SIGNATURE)
                .withData(SCRIPT)
                .withPublicKey(PUBLIC_KEY);

        assertTrue(signatureChecker.check());
    }

    @Test
    public void testSignatureChecker_withInvalidValidSignature_returnFalse() {
        SignatureChecker signatureChecker = new SignatureChecker()
                .withSignature(SIGNATURE)
                .withData(SCRIPT.replace("a","b"))
                .withPublicKey(PUBLIC_KEY);

        assertFalse(signatureChecker.check());
    }

    @Test
    public void testGetPublicKey() {
        assertEquals(PUBLIC_KEY, SignatureChecker.getPublicKey());
    }
}