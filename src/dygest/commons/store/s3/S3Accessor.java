/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.store.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.jclouds.aws.s3.S3Connection;
import org.jclouds.aws.s3.S3Context;
import org.jclouds.aws.s3.S3ContextFactory;

/**
 *
 * @author anand
 */
public class S3Accessor {

    S3Context context = null;
    S3Connection connection = null;
    String bucket = null;

    public S3Accessor(String bucket) throws Exception {
        this.bucket = bucket;
        context = S3ContextFactory.createS3Context("046T8W780QSWZNENHN82", "PFpfRBEvW1UityT1dFvnR+fNLGA2MVxLycgeskYL");
        connection = context.getConnection();
        Future<Boolean> successfullyCreated = connection.putBucketIfNotExists("mybucketname");
        if(!successfullyCreated.get(10, TimeUnit.SECONDS)) {
            throw new Exception("Error creating bucket");
        }
    }

    public InputStream get(String uri) {
        Map<String, InputStream> map = context.createInputStreamMap(this.bucket);
        if(map.containsKey(uri)) {
            return map.get(uri);
        }

        return null;
    }

    public boolean contains(String uri) {
        Map<String, InputStream> map = context.createInputStreamMap(this.bucket);
        return map.containsKey(uri);
    }

    public boolean put(String uri, File obj) {
        Map<String, InputStream> map = context.createInputStreamMap(this.bucket);
        try {
            map.put(uri, new FileInputStream(obj));
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void finalize() {
        if(context != null) {
            context.close();
        }
    }

}
