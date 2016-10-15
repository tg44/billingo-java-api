package hu.billingo;

import hu.billingo.exceptions.BillingoException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class RequestBuilder {

    public String requesterUrl = "cli";
    public String publicKey = "";
    public String privateKey = "";
    public int leeway = 60;
    public String baseUrl = "https://www.billingo.hu/api";

    public RequestBuilder(String publicKey, String privateKey, String requesterUrl) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.requesterUrl = requesterUrl;
    }

    public String get(String url) throws BillingoException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(baseUrl + url);

            get.setHeader("Authorization", "Bearer " + generateAuthHeader());

            HttpResponse response = client.execute(get);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(ClientProtocolException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(IOException e) {
            throw new BillingoException("Billingo request exception! ", e);
        }
    }

    public String post(String url, String body) throws BillingoException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(baseUrl + url);

            post.setHeader("Authorization", "Bearer " + generateAuthHeader());
            post.setEntity(new ByteArrayEntity(body.getBytes()));

            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(ClientProtocolException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(IOException e) {
            throw new BillingoException("Billingo request exception! ", e);
        }
    }

    public String put(String url, String body) throws BillingoException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPut put = new HttpPut(baseUrl + url);

            put.setHeader("Authorization", "Bearer " + generateAuthHeader());
            put.setEntity(new ByteArrayEntity(body.getBytes()));

            HttpResponse response = client.execute(put);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(ClientProtocolException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(IOException e) {
            throw new BillingoException("Billingo request exception! ", e);
        }
    }

    public String delete(String url) throws BillingoException {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpDelete delete = new HttpDelete(baseUrl + url);

            delete.setHeader("Authorization", "Bearer " + generateAuthHeader());

            HttpResponse response = client.execute(delete);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(ClientProtocolException e) {
            throw new BillingoException("Billingo request exception! ", e);
        } catch(IOException e) {
            throw new BillingoException("Billingo request exception! ", e);
        }
    }

    public String generateAuthHeader() throws NoSuchAlgorithmException {
        String secret = new String(Base64.encodeBase64(privateKey.getBytes() ));
        Long time = System.currentTimeMillis() / 1000;
        String iss = requesterUrl;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("sub", publicKey);
        map.put("iat", time.toString());
        map.put("exp", ((Long) (time + leeway)).toString());
        map.put("iss", iss);
        map.put("nbf", ((Long) (time - leeway)).toString());
        map.put("jti", MD5(publicKey + (time.toString())));
        return Jwts.builder().setHeaderParam("typ", "JWT")
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // http://stackoverflow.com/a/6565597
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch(java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}