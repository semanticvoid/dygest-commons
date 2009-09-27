/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dygest.commons.data;

import dygest.commons.db.simple.IStorable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *
 * @author anand
 */
public class Tweet implements IStorable {

    private String uri;
    private String user;
    private String text;
    private String day;
    private String time;
    // links separated by ^A
    private String links = "";
    private Date date;
    private boolean hasLink = false;

    public Tweet(String uri, String user, String text, Date time, boolean resolve) {
        this.uri = uri;
        this.user = user;
        this.text = text;
        this.date = time;
        formatTime(time);

        if (text != null) {
            hasLink = text.matches(".*http://[a-zA-Z0-9]+.*");
            if (hasLink) {
                extractUrls(text, resolve);
            }
        }
    }

    private void extractUrls(String text, boolean resolve) {
        StringBuffer links = new StringBuffer();

        Pattern pattern = Pattern.compile("http://[a-zA-Z0-9\\/&?=#+-_%~]+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            if (resolve) {
                // @TODO resolve addresses
            }
            links.append(matcher.group());
            links.append('\001');
        }

        System.out.println(links.toString());
        this.links = links.toString();
    }

    private String resolveLink(String url) {
        String resolved = url;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                String redirectLocation;
                Header locationHeader = method.getResponseHeader("location");
                if(locationHeader != null) {
                    redirectLocation = locationHeader.getValue();
                    return resolveLink(redirectLocation);
                } else {
                    // The response is invalid and did not provide the new location for
                    // the resource.  Report an error or possibly handle the response
                    // like a 404 Not Found error.
                    return resolved;
                }
            }
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return resolved;
    }

    private void formatTime(Date t) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hhmm");

        day = dayFormat.format(t);
        time = timeFormat.format(t);
    }

    /**
     * @return the uri
     */
    public String getURI() {
        return uri;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the hasLink
     */
    public boolean hasLink() {
        return hasLink;
    }

    public String getID() {
        return uri;
    }

    public HashMap<String, String> toMap() {
        HashMap<String, String> objAsMap = new HashMap<String, String>();

        objAsMap.put("id", uri);
        objAsMap.put("uri", uri);
        objAsMap.put("user", user);
        objAsMap.put("text", text);
        objAsMap.put("links", links);
        objAsMap.put("time", time);
        objAsMap.put("day", day);
        objAsMap.put("hasLink", String.valueOf(hasLink));

        return objAsMap;
    }

    public String toJSON() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
