/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.dom.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/**
 * The MutationEvent class provides specific contextual information
 * associated with Mutation events.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 */
public class DOMMutationEvent extends AbstractEvent implements MutationEvent {

    private Node relatedNode;
    private String prevValue;
    private String newValue;
    private String attrName;
    private short attrChange;

    /**
     * DOM: <code>relatedNode</code> is used to identify a secondary
     * node related to a mutation event. For example, if a mutation
     * event is dispatched to a node indicating that its parent has
     * changed, the <code>relatedNode</code> is the changed parent.
     * If an event is instead dispatch to a subtree indicating a node
     * was changed within it, the <code>relatedNode</code> is the
     * changed node.  
     */
    public Node getRelatedNode() {
	return relatedNode;
    }

    /**
     * DOM: <code>prevValue</code> indicates the previous value of the
     * <code>Attr</code> node in DOMAttrModified events, and of the
     * <code>CharacterData</code> node in DOMCharDataModified events.  
     */
    public String getPrevValue() {
	return prevValue;
    }

    /**
     * DOM: <code>newValue</code> indicates the new value of the
     * <code>Attr</code> node in DOMAttrModified events, and of the
     * <code>CharacterData</code> node in DOMCharDataModified events.  
     */
    public String getNewValue() {
	return newValue;
    }

    /**
     * DOM: <code>attrName</code> indicates the name of the changed
     * <code>Attr</code> node in a DOMAttrModified event.  
     */
    public String getAttrName() {
	return attrName;
    }

    /**
     * Implements {@link org.w3c.dom.events.MutationEvent#getAttrChange()}.
     */
    public short getAttrChange() {
        return attrChange;
    }

    /**
     * DOM: The <code>initMutationEvent</code> method is used to
     * initialize the value of a <code>MutationEvent</code> created
     * through the <code>DocumentEvent</code> interface.  This method
     * may only be called before the <code>MutationEvent</code> has
     * been dispatched via the <code>dispatchEvent</code> method,
     * though it may be called multiple times during that phase if
     * necessary.  If called multiple times, the final invocation
     * takes precedence.
     *
     * @param typeArg Specifies the event type.
     * @param canBubbleArg Specifies whether or not the event can bubble.
     * @param cancelableArg Specifies whether or not the event's default  
     *   action can be prevented.
     * @param relatedNodeArg Specifies the <code>Event</code>'s related Node
     * @param prevValueArg Specifies the <code>Event</code>'s 
     *   <code>prevValue</code> property
     * @param newValueArg Specifies the <code>Event</code>'s 
     *   <code>newValue</code> property
     * @param attrNameArg Specifies the <code>Event</code>'s 
     *   <code>attrName</code> property 
     */
    public void initMutationEvent(String typeArg, 
				  boolean canBubbleArg, 
				  boolean cancelableArg, 
				  Node relatedNodeArg, 
				  String prevValueArg, 
				  String newValueArg, 
				  String attrNameArg,
                                  short attrChangeArg) {
	initEvent(typeArg, canBubbleArg, cancelableArg);
	this.relatedNode = relatedNodeArg;
	this.prevValue = prevValueArg;
	this.newValue = newValueArg;
	this.attrName = attrNameArg;
        this.attrChange = attrChangeArg;
    }
}
