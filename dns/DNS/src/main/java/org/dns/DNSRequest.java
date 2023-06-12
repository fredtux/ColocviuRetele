package org.dns;

import java.nio.charset.StandardCharsets;

public class DNSRequest {
    private String domain;

    public String getDomain(){
        return this.domain;
    }

    public DNSRequest(byte[] requestData, int requestDataLength){
        // Get domain out of requestData
        this.domain = this.parseDomain(requestData, requestDataLength);
    }

    private String parseDomain(byte[] requestData, int requestDataLength){
        int currPos = 12;

        StringBuilder domain = new StringBuilder();


        while(currPos < requestDataLength){
            int labelLength = requestData[currPos];
            if(labelLength == 0){
                break;
            }
            if ((labelLength & 0xC0) == 0xC0) { // Compression
                int pointer = ((labelLength & 0x3F) << 8) | (requestData[++currPos] & 0xFF);
                currPos = pointer;
            } else {
                byte[] labelBytes = new byte[labelLength];
                System.arraycopy(requestData, currPos + 1, labelBytes, 0, labelLength);
                String label = new String(labelBytes, StandardCharsets.UTF_8);

                domain.append(label).append(".");
                currPos += labelLength + 1;
            }
        }

        return domain.toString();
    }
}
