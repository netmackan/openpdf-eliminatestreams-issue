/*
 * Copyright (C) 2022 Keyfactor.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.primekey.signserver.openpdf.eliminatesharedstreamsissue;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Small sample app demonstrating PDF issue.
 *
 * @author Markus Kilås
 */
public class EliminateSharedStreamsIssue {

    public static void main(String[] args) throws Exception {
        
        try (PdfReader reader = new PdfReader("pdflib-duplicated-out.pdf");
                FileInputStream ksIn = new FileInputStream("dss10_keystore.p12")) {

            Security.addProvider(new BouncyCastleProvider());
            KeyStore ks = KeyStore.getInstance("pkcs12", "BC");
            ks.load(ksIn, "foo123".toCharArray());
            PrivateKey key = (PrivateKey) ks.getKey("signer00003", null);
            Certificate[] chain = ks.getCertificateChain("signer00003");
            
            FileOutputStream fout = new FileOutputStream(new File("target", "pdflib-duplicated-out-signed1.pdf"));
            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', new File("/tmp"), true);
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);

            // Make a modification so the page will be rewritten as it does not happen for this file otherwise for unclear resons
            stp.getUnderContent(2).rectangle(1, 1, 100, 100);

            sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 2, null);
            stp.close();
        }
        
    }
    
}
