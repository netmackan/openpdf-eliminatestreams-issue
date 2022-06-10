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

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Small sample app demonstrating PDF issue.
 *
 * @author Markus Kilås
 */
public class EliminateSharedStreamsIssue {

    public static void main(String[] args) throws Exception {

        try (PdfReader reader = new PdfReader("pdflib-duplicated-out.pdf")) {

            FileOutputStream fout = new FileOutputStream(new File("target", "pdflib-duplicated-out-modified1.pdf"));
            PdfStamper stp = new PdfStamper(reader, fout, '\0', true);

            // Make a modification to page 2
            stp.getOverContent(2).rectangle(1, 1, 100, 100);

            stp.close();
        }

    }

}
