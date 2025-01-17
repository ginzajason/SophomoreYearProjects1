/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class DynamicTree extends JPanel {
	
	/* note that we set up three things to start: a root node of the tree,
	 * a tree model of the tree, and finally the Swing component that displays
	 * the tree.
	 * 
	 */
	
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public DynamicTree() {
        super(new GridLayout(1,0));
        
        rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        
        /* the JTree wants a model; the model in turn needs to be initialized with a root node
         * (that's from the DefaultTreeModel class; the TreeModel interface of course doesn't specify
         * a constructor schema, because it's just an interface!)
         * 
         */
        
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }
	
	    //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
       
        
        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
        	tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }
    
    //**//
    
    /**
     * This method creates a new node and inserts it as the parent of the currently selected
     * node. So it's kinda the opposite of the "add child" behavior of the previous method. It should 
     * leave the new parent node selected and visible.
     * 
     * @return A reference to the newly created parent node.
     */
    
    
    //Side Note before this. I never called addObject() because THE FUCKING METHOD REMOVES YOUR CHILD NODES WITH THIS LINE OF CODE:
    //DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
    //SO THIS WHOLE TIME WE WERE DOING IT WRITE ITS JUST THAT LINE FUCKING REMOVED IT SO WE JUST SPENT HOURS OF WORK FOR NOTHING BECAUSE OF ONE LINE OF CODE ERASING OUR DAMN NODES
    public DefaultMutableTreeNode newParent(String name) {
    	TreePath currentSelection = tree.getSelectionPath();
    	MutableTreeNode node = (MutableTreeNode) tree.getLastSelectedPathComponent();
    	
        if (currentSelection != null && node != rootNode) {
        	String temp = "New Parent " + name;
        	name = temp;
        	DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentSelection.getParentPath().getLastPathComponent();
        	node = (MutableTreeNode) tree.getLastSelectedPathComponent();
        	
        	//removed the node from the tree
        	removeCurrentNode();
        	
        	//creates a new node called name that we called for this
        	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
        	//we then add the node that was selected into this newNode that is the parent
        	newNode.add(node);

    	    //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        	//Then we insert the newNode into the parent
            treeModel.insertNodeInto(newNode, parent, parent.getChildCount());
           
            
            //Make sure the user can see the lovely new node.
            if (true) {
            	tree.scrollPathToVisible(new TreePath(newNode.getPath()));
            }
        	
        }
        
        if(node == rootNode) {
        	String temp = "New Root " + name;
        	name = temp;
        	
        	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
        	newNode.add(node);
        
        	rootNode = newNode;
        	
        	treeModel.setRoot(rootNode);
        	treeModel.reload();

        	
        	
        }
    	// this return is just to satisfy the compiler
    	return new DefaultMutableTreeNode();
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */

                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
