/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.util;

import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

/**
 * This class handles looking up service providers on the class path.
 * it implements the system described in:
 *
 * http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html
 * Under Service Provider. Note that this interface is very
 * similar to the one they describe which seems to be missing in the JDK.
 *
 * @author <a href="mailto:Thomas.DeWeeese@Kodak.com">Thomas DeWeese</a>
 * @version $Id$
 */
public class Service {

    static HashMap providerMap = new HashMap();

    public static synchronized Iterator providers(Class cls) {
        ClassLoader cl = cls.getClassLoader();
        String serviceFile = "META-INF/services/"+cls.getName();

        // System.out.println("File: " + serviceFile);

        Vector v = (Vector)providerMap.get(serviceFile);
        if (v != null)
            return v.iterator();

        v = new Vector();
        providerMap.put(serviceFile, v);

        Enumeration e;
        try {
            e = cl.getResources(serviceFile);
        } catch (IOException ioe) {
            return v.iterator();
        }

        while (e.hasMoreElements()) {
            try {
                URL u = (URL)e.nextElement();
                // System.out.println("URL: " + u);

                InputStream    is = u.openStream();
                Reader         r  = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(r);

                String line = br.readLine();
                while (line != null) {
                    try {
                        // First strip any comment...
                        int idx = line.indexOf('#');
                        if (idx != -1)
                            line = line.substring(0, idx);

                        // Trim whitespace.
                        line = line.trim();

                        // If nothing left then loop around...
                        if (line.length() == 0) {
                            line = br.readLine();
                            continue;
                        }
                        // System.out.println("Line: " + line);

                        // Try and load the class 
                        Object obj = cl.loadClass(line).newInstance();
                        // stick it into our vector...
                        v.add(obj);
                    } catch (Exception ex) {
                        // Just try the next line
                    }
                    line = br.readLine();
                }
            } catch (Exception ex) {
                // Just try the next file...
            }
        }
        return v.iterator();
    }
}