/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGLineElement;

/**
 * This class implements {@link SVGLineElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id$
 */
public class SVGOMLineElement
    extends    SVGGraphicsElement
    implements SVGLineElement {

    /**
     * Creates a new SVGOMLineElement object.
     */
    protected SVGOMLineElement() {
    }

    /**
     * Creates a new SVGOMLineElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMLineElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_LINE_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getX1()}.
     */
    public SVGAnimatedLength getX1() {
        return getAnimatedLengthAttribute
            (null, SVG_X1_ATTRIBUTE, SVG_LINE_X1_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    } 

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getY1()}.
     */
    public SVGAnimatedLength getY1() {
        return getAnimatedLengthAttribute
            (null, SVG_Y1_ATTRIBUTE, SVG_LINE_Y1_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getX2()}.
     */
    public SVGAnimatedLength getX2() {
        return getAnimatedLengthAttribute
            (null, SVG_X2_ATTRIBUTE, SVG_LINE_X2_DEFAULT_VALUE,
             SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    } 

    /**
     * <b>DOM</b>: Implements {@link SVGLineElement#getY2()}.
     */
    public SVGAnimatedLength getY2() {
        return getAnimatedLengthAttribute
            (null, SVG_Y2_ATTRIBUTE, SVG_LINE_Y2_DEFAULT_VALUE,
             SVGOMAnimatedLength.VERTICAL_LENGTH);
    } 

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMLineElement();
    }
}
