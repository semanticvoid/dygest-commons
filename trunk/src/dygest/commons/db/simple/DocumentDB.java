/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.db.simple;

import com.amazonaws.sdb.AmazonSimpleDB;
import com.amazonaws.sdb.AmazonSimpleDBClient;
import com.amazonaws.sdb.AmazonSimpleDBException;
import com.amazonaws.sdb.model.Attribute;
import com.amazonaws.sdb.model.CreateDomainRequest;
import com.amazonaws.sdb.model.CreateDomainResponse;
import com.amazonaws.sdb.model.GetAttributesRequest;
import com.amazonaws.sdb.model.GetAttributesResponse;
import com.amazonaws.sdb.model.GetAttributesResult;
import com.amazonaws.sdb.model.Item;
import com.amazonaws.sdb.model.PutAttributesRequest;
import com.amazonaws.sdb.model.PutAttributesResponse;
import com.amazonaws.sdb.model.ReplaceableAttribute;
import com.amazonaws.sdb.model.SelectRequest;
import com.amazonaws.sdb.model.SelectResponse;
import com.amazonaws.sdb.model.SelectResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author anand
 */
public class DocumentDB {

    // SimpleDB service
    AmazonSimpleDB service = new AmazonSimpleDBClient("046T8W780QSWZNENHN82", "PFpfRBEvW1UityT1dFvnR+fNLGA2MVxLycgeskYL");

    public boolean createDomain(String domain) {
        CreateDomainRequest request = new CreateDomainRequest();
        request.setDomainName(domain);
        
        try {
            CreateDomainResponse response = service.createDomain(request);
            return true;
        } catch (AmazonSimpleDBException ex) {
            return false;
        }
    }

    public boolean put(String domain, IStorable dbObj) {
        PutAttributesRequest request = new PutAttributesRequest();
        request.setDomainName(domain);
        request.setItemName(dbObj.getID());

        HashMap<String, String> attrs = dbObj.toMap();
        List<ReplaceableAttribute> attrList = new ArrayList();
        for(String key : attrs.keySet()) {
            String value = attrs.get(key);
            ReplaceableAttribute attr = new ReplaceableAttribute(key, value, true);
            attrList.add(attr);
        }

        request.setAttribute(attrList);

        try {
            PutAttributesResponse response = service.putAttributes(request);
        } catch(AmazonSimpleDBException ex) {
            return false;
        }

        return true;
    }

    public HashMap<String, String> get(String domain, String id) {
        GetAttributesRequest request = new GetAttributesRequest();
        HashMap<String, String> attrs = new HashMap<String, String>();
        request.setDomainName(domain);
        request.setItemName(id);

        try {
            GetAttributesResponse response = service.getAttributes(request);
            if(response.isSetGetAttributesResult()) {
                GetAttributesResult  getAttributesResult = response.getGetAttributesResult();
                List<Attribute> attributeList = getAttributesResult.getAttribute();
                for(Attribute attr : attributeList) {
                    attrs.put(attr.getName(), attr.getValue());
                }
            }
        } catch(AmazonSimpleDBException ex) {
            return null;
        }

        return attrs;
    }

    public List<HashMap<String, String>> select(String query) {
        List<HashMap<String, String>> queryResults = new ArrayList<HashMap<String, String>>();
        SelectRequest request = new SelectRequest();
        request.setSelectExpression(query);

        try {
            SelectResponse response = service.select(request);
            if(response.isSetSelectResult()) {
                SelectResult  selectResult = response.getSelectResult();
                List<Item> itemList = selectResult.getItem();
                for(Item item : itemList) {
                    HashMap<String, String> itemAttrs = new HashMap<String, String>();
                    List<Attribute> attributeList = item.getAttribute();
                    for(Attribute attribute : attributeList) {
                        itemAttrs.put(attribute.getName(), attribute.getValue());
                    }
                    queryResults.add(itemAttrs);
                }
            }
        } catch(AmazonSimpleDBException ex) {
            return null;
        }

        return queryResults;
    }

//    public static void main(String[] args) {
//        DocumentDB db = new DocumentDB();
//        if(db.createDomain("test")) {
//            System.out.println("created or present");
//        }
//
//
//        /*testObj t = new testObj();
//
//        if(db.put("test", t)) {
//            System.out.println("added");
//        }*/
//
//        HashMap<String, String> obj = db.get("test", "item-1");
//        System.out.println();
//    }
//
//    public static class testObj implements IStorable {
//
//        public String getID() {
//            return "item-1";
//        }
//
//        public HashMap<String, String> toMap() {
//            HashMap<String, String> obj = new HashMap<String, String>();
//            obj.put("attr1", "val1");
//            obj.put("attr2", "val2");
//            return obj;
//        }
//
//        public String toJSON() {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//    }
}
