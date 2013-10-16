/*
 * Autopsy Forensic Browser
 * 
 * Copyright 2013 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.datamodel;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.datamodel.TagName;

/**
 * Instances of this class are the root nodes of tree that is a sub-tree of the 
 * Autopsy presentation of the SleuthKit data model. The sub-tree consists of 
 * content and blackboard artifact tags, grouped first by tag type, then by 
 * tag name. 
 */
public class TagsNode extends DisplayableItemNode {
    private static final String DISPLAY_NAME = "Tags";
    private static final String ICON_PATH = "org/sleuthkit/autopsy/images/tag-folder-blue-icon-16.png";
        
    public TagsNode() {
        super(Children.create(new TagNameNodeFactory(), true));
        super.setName(DISPLAY_NAME);
        super.setDisplayName(DISPLAY_NAME);
        this.setIconBaseWithExtension(ICON_PATH);
    }
    
    public static String getNodeName() {
        return DISPLAY_NAME;
    }
    
    @Override
    public boolean isLeafTypeNode() {
        return false;
    }
    
    @Override
    public <T> T accept(DisplayableItemNodeVisitor<T> v) {
        return v.visit(this);
    }

    @Override
    protected Sheet createSheet() {
        Sheet propertySheet = super.createSheet();
        Sheet.Set properties = propertySheet.get(Sheet.PROPERTIES);
        if (properties == null) {
            properties = Sheet.createPropertiesSet();
            propertySheet.put(properties);
        }

        properties.put(new NodeProperty("Name", "Name", "", getName()));

        return propertySheet;
    }
            
    private static class TagNameNodeFactory extends ChildFactory<TagName> {
        @Override
        protected boolean createKeys(List<TagName> keys) {
            Case.getCurrentCase().getServices().getTagsManager().getAllTagNames(keys);
            return true;
        }

        @Override
        protected Node createNodeForKey(TagName key) {
            return new TagNameNode(key);
        }
    }
}
