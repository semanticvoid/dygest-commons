/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.commons.store.s3;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 *
 * @author anand
 */
public class S3Accessor {

    private static S3Service s3Service = null;

    static {
        try {
            s3Service = new RestS3Service(new AWSCredentials("046T8W780QSWZNENHN82", "PFpfRBEvW1UityT1dFvnR+fNLGA2MVxLycgeskYL"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private String bucketName = null;
    private S3Bucket bucket = null;

    public S3Accessor(String bucket) throws Exception {
        this.bucketName = bucket;
        if(s3Service != null) {
            this.bucket = s3Service.getOrCreateBucket(bucketName);
        }
    }

    public boolean put(String uri, String data) {
        try {
            S3Object object = new S3Object(uri, data);
            s3Service.putObject(bucket, object);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        try {
            S3Accessor s3 = new S3Accessor("dygest-testbucket");
            s3.put("testdata", "this is a test");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
