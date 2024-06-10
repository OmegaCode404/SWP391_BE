package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.Config.VNPayConfig;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.IRespository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IWatchService;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    @Autowired
    private IWatchService watchService;
    @PostMapping("/create-payment-url")
    public Map<String, String> createPaymentUrl(@RequestBody Map<String, Object> payload) throws UnknownHostException, UnsupportedEncodingException {
        String vnp_IpAddr = InetAddress.getLocalHost().getHostAddress();
        int amount = (int) payload.get("amount");
        String orderInfo = (String) payload.get("orderInfo");

        String vnp_Version = VNPayConfig.VNPAY_VERSION;
        String vnp_Command = VNPayConfig.VNPAY_COMMAND;
        String vnp_TmnCode = VNPayConfig.VNPAY_TMNCODE;
        String vnp_HashSecret = VNPayConfig.VNPAY_HASH_SECRET;
        String vnp_ReturnUrl = VNPayConfig.VNPAY_RETURNURL;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_OrderInfo = orderInfo;
        String orderType = "other";
        String vnp_Amount = String.valueOf(amount * 100);
        String vnp_Locale = "vn";
        String vnp_CreateDate = formatter.format(cal.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);


        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                hashData.append('&');
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append('&');
            }
        }
        hashData.setLength(hashData.length() - 1); // Remove the last '&'
        query.setLength(query.length() - 1); // Remove the last '&'

        String vnp_SecureHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, vnp_HashSecret).hmacHex(hashData.toString());
        query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.US_ASCII.toString()));
        String paymentUrl = VNPayConfig.VNPAY_URL + "?" + query.toString();

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        return response;
    }

    @PostMapping("/verify-payment")
    public Map<String, Boolean> verifyPayment(@RequestBody Map<String, String> payload) {
        String vnp_HashSecret = VNPayConfig.VNPAY_HASH_SECRET;
        String secureHash = payload.remove("vnp_SecureHash");

        List<String> fieldNames = new ArrayList<>(payload.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = payload.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                try {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    hashData.append('&');
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1); // Remove the last '&'
        }

        String calculatedHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, vnp_HashSecret).hmacHex(hashData.toString());

        // Log the details for debugging
        System.out.println("Received Payload: " + payload);
        System.out.println("Secure Hash: " + secureHash);
        System.out.println("Calculated Hash: " + calculatedHash);
        System.out.println("Hash Data: " + hashData.toString());

        Map<String, Boolean> response = new HashMap<>();
        boolean isSuccess = secureHash.equals(calculatedHash);
        response.put("success", isSuccess);

//        if (isSuccess) {
//            // Assuming the payload contains watchIds in a comma-separated string
//            String watchIdsString = payload.get("watchIds");
//            if (watchIdsString != null) {
//                String[] watchIdArray = watchIdsString.split(",");
//                List<Integer> watchIds = new ArrayList<>();
//                for (String idStr : watchIdArray) {
//                    watchIds.add(Integer.parseInt(idStr));
//                }
//                watchService.updateWatchStatus(watchIds, false);
//            }
//        }

        return response;
    }
}
