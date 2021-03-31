package com.qiniu.storage;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;

public class ApiResumableUploadV2UploadPart extends Api {

    public ApiResumableUploadV2UploadPart(Client client) {
        super(client);
    }

    public Response request(Request request) throws QiniuException {
        com.qiniu.http.Response response = client.post(request.getUrl().toString(), request.body, request.bodyOffset, request.bodySize,
                request.getHeader(), request.bodyContentType);
        return new Response(response);
    }

    /**
     * 请求信息
     */
    public static class Request extends Api.Request {
        String key;
        String uploadId;
        Integer partIndex;

        public Request(String host, String token, String uploadId, Integer partIndex) {
            super(host);
            setToken(token);
            this.uploadId = uploadId;
            this.partIndex = partIndex;
        }

        @Override
        public void buildPath() throws QiniuException {
            String bucket = getUploadToken().getBucket();
            addPathSegment("buckets");
            addPathSegment(bucket);
            addPathSegment("objects");
            addPathSegment(ApiUtils.resumeV2EncodeKey(key));
            addPathSegment("uploads");
            addPathSegment(uploadId);
            addPathSegment(partIndex + "");
            super.buildPath();
        }
    }

    /**
     * 响应信息
     */
    public static class Response extends Api.Response {

        public Response(com.qiniu.http.Response response) throws QiniuException {
            super(response);
        }

        public String getMd5() {
            return getStringValueFromDataMap("md5");
        }

        public String getEtag() {
            return getStringValueFromDataMap("etag");
        }
    }
}
