/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.bridge;

import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is responsible for creating a GVT tree using an SVG DOM tree.
 *
 * @author <a href="mailto:tkormann@apache.org">Thierry Kormann</a>
 * @version $Id$
 */
public class GVTBuilder implements SVGConstants {

    /**
     * Constructs a new builder.
     */
    public GVTBuilder() { }

    /**
     * Builds using the specified bridge context the specified SVG document.
     *
     * @param ctx the bridge context
     * @param document the SVG document to build
     * @exception BridgeException if an error occured while constructing
     * the GVT tree
     */
    public GraphicsNode build(BridgeContext ctx, Document document) {
        // the bridge context is now associated to one document
        ctx.setDocument(document);
        ctx.initializeDocument(document);

        // inform the bridge context the builder to use
        ctx.setGVTBuilder(this);

        // build the GVT tree
        RootGraphicsNode rootNode = new RootGraphicsNode();
        Element svgElement = document.getDocumentElement();
        GraphicsNode topNode = null;
        try {
            // get the appropriate bridge according to the specified element
            Bridge bridge = ctx.getBridge(svgElement);
            if (bridge == null || !(bridge instanceof GraphicsNodeBridge)) {
                return null;
            }
            // create the associated composite graphics node
            GraphicsNodeBridge gnBridge = (GraphicsNodeBridge)bridge;
            topNode = gnBridge.createGraphicsNode(ctx, svgElement);
            if (topNode == null) {
                return null;
            }
            rootNode.getChildren().add(topNode);

            buildComposite(ctx, svgElement, (CompositeGraphicsNode)topNode);
            gnBridge.buildGraphicsNode(ctx, svgElement, topNode);
        } catch (BridgeException ex) {
            // update the exception with the missing parameters
            ex.setGraphicsNode(rootNode);
            Element errElement = ex.getElement();
            ex.setLineNumber(ctx.getDocumentLoader().getLineNumber(errElement));
            //ex.printStackTrace();
            throw ex; // re-throw the udpated exception
        }

        // For cursor handling
        ctx.addUIEventListeners(document);

        // <!> FIXME: TO BE REMOVED
        if (ctx.isDynamic()) {
            // register GVT listeners for AWT event support
            BridgeEventSupport.addGVTListener(ctx, document);
            // register DOM listeners for dynamic support
            ctx.addDOMListeners();
        }
        return rootNode;
    }

    /**
     * Builds using the specified bridge context the specified Element.
     *
     * @param ctx the bridge context
     * @param e the element to build
     * @exception BridgeException if an error occured while constructing
     * the GVT tree
     */
    public GraphicsNode build(BridgeContext ctx, Element e) {
        // get the appropriate bridge according to the specified element
        Bridge bridge = ctx.getBridge(e);
        if (bridge == null || !(bridge instanceof GraphicsNodeBridge)) {
            return null;
        }
        // create the associated graphics node
        GraphicsNodeBridge gnBridge = (GraphicsNodeBridge)bridge;
        // check the display property
        if (!gnBridge.getDisplay(e)) {
            return null;
        }
        GraphicsNode gn = gnBridge.createGraphicsNode(ctx, e);
        if (gn != null) {
            if (gnBridge.isComposite()) {
                buildComposite(ctx, e, (CompositeGraphicsNode)gn);
            }
            gnBridge.buildGraphicsNode(ctx, e, gn);
        }
        // <!> FIXME: see build(BridgeContext, Element)
        // + may load the script twice (for example
        // outside 'use' is ok versus local 'use' maybe wrong).
        if (ctx.isDynamic()) {
            //BridgeEventSupport.loadScripts(ctx, e);
        }
        return gn;
    }

    /**
     * Builds a composite Element.
     *
     * @param ctx the bridge context
     * @param e the element to build
     * @param parent the composite graphics node, parent of the
     *               graphics node to build
     * @exception BridgeException if an error occured while constructing
     * the GVT tree
     */
    protected void buildComposite(BridgeContext ctx,
                                  Element e,
                                  CompositeGraphicsNode parentNode) {
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                buildGraphicsNode(ctx, (Element)n, parentNode);
            }
        }
    }

    /**
     * Builds a 'leaf' Element.
     *
     * @param ctx the bridge context
     * @param e the element to build
     * @param parent the composite graphics node, parent of the
     *               graphics node to build
     * @exception BridgeException if an error occured while constructing
     * the GVT tree
     */
    protected void buildGraphicsNode(BridgeContext ctx,
                                     Element e,
                                     CompositeGraphicsNode parentNode) {
        // Check for interruption.
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedBridgeException();
        }
        // get the appropriate bridge according to the specified element
        Bridge bridge = ctx.getBridge(e);
        if (bridge == null || !(bridge instanceof GraphicsNodeBridge)) {
            return;
        }
        // check the display property
        if (!CSSUtilities.convertDisplay(e)) {
            return;
        }
        GraphicsNodeBridge gnBridge = (GraphicsNodeBridge)bridge;
        try {
            // create the associated graphics node
            GraphicsNode gn = gnBridge.createGraphicsNode(ctx, e);
            if (gn != null) {
                // attach the graphics node to the GVT tree now !
                parentNode.getChildren().add(gn);
                // check if the element has children to build
                if (gnBridge.isComposite()) {
                    buildComposite(ctx, e, (CompositeGraphicsNode)gn);
                }
                gnBridge.buildGraphicsNode(ctx, e, gn);
            }
        } catch (BridgeException ex) {
            // some bridge may decide that the node in error can be
            // displayed (e.g. polyline, path...)
            // In this case, the exception contains the GraphicsNode
            GraphicsNode errNode = ex.getGraphicsNode();
            if (errNode != null) {
                parentNode.getChildren().add(errNode);
                gnBridge.buildGraphicsNode(ctx, e, errNode);
                ex.setGraphicsNode(null);
            }
            //ex.printStackTrace();
            throw ex;
        }
    }
}

