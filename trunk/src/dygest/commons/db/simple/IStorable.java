/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.db.simple;

import java.util.HashMap;

/**
 * This interface is to be implemented by all data objects that
 * need to be storable in a document db
 * @author anand
 */
public interface IStorable {

    public String getID();

    /**
     * Map of attributes and values
     * @return
     */
    public HashMap<String, String> toMap();

    /**
     * JSON string representation of the object
     * @return
     */
    public String toJSON();

}
