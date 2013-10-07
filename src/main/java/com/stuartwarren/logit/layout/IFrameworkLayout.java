/**
 * 
 */
package com.stuartwarren.logit.layout;

/**
 * @author Stuart Warren 
 * @date 7 Oct 2013
 *
 */
public interface IFrameworkLayout {
    
    /**
     * @return the layoutType
     */
    public String getLayoutType();

    /**
     * @param layoutType the layoutType to set
     */
    public void setLayoutType(String layoutType);

    /**
     * @return the detailThreshold
     */
    public String getDetailThreshold();

    /**
     * @param detailThreshold the detailThreshold to set
     */
    public void setDetailThreshold(String detailThreshold);
    
    /**
     * @return the fields
     */
    public String getFields();

    /**
     * @param fields the fields to set
     */
    public void setFields(String fields);

    /**
     * @return the tags
     */
    public String getTags();

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags);


}
