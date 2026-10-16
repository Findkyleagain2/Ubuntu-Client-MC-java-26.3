package com.ubuntu.client.auth;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ubuntu.client.UbuntuClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PremiumVerifier {


    public static final String WALLET_ADDRESS = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh";
    private static final String API_URL = "https://blockchain.info/rawaddr/";
    private static final double MIN_PREMIUM_AMOUNT_BTC = 0.0005;

    /**
    * Verifies if a given transaction hash exists for the WALLET_ADDRESS and meets the minimum amount.
    *
    * @param txHash The Bitcoin transaction hash provided by the user.
    * @return true if the transaction is found and valid, false otherwise.
    */
    public static boolean verifyPayment(String txHash) {
        if (txHash == null || txHash.trim().isEmpty()) {
            UbuntuClient.LOGGER.warning("Provided transaction hash is empty or null. Cannot verify premium.");
            return false;
        }

        HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(API_URL + WALLET_ADDRESS))
        .timeout(Duration.ofSeconds(15))
        .header("Accept", "application/json")
        .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                UbuntuClient.LOGGER.severe("Failed to fetch blockchain data. Status code: " + response.statusCode() + ", Body: " + response.body());
                return false;
            }

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray transactions = json.getAsJsonArray("txs");

            if (transactions == null || transactions.isEmpty()) {
                UbuntuClient.LOGGER.info("No transactions found for wallet address: " + WALLET_ADDRESS);
                return false;
            }

            for (int i = 0; i < transactions.size(); i++) {
                JsonObject tx = transactions.get(i).getAsJsonObject();
                String currentTxHash = tx.get("hash").getAsString();

                if (currentTxHash.equalsIgnoreCase(txHash.trim())) {

                    long totalReceivedSatoshis = 0;
                    JsonArray out = tx.getAsJsonArray("out");
                    if (out != null) {
                        for (int j = 0; j < out.size(); j++) {
                            JsonObject output = out.get(j).getAsJsonObject();
                            JsonArray addrs = output.getAsJsonArray("addr");
                            if (addrs != null && addrs.contains(JsonParser.parseString("\"" + WALLET_ADDRESS + "\""))) {
                                totalReceivedSatoshis += output.get("value").getAsLong();
                            }
                        }
                    }

                    double receivedBtc = totalReceivedSatoshis / 100_000_000.0;

                    if (receivedBtc >= MIN_PREMIUM_AMOUNT_BTC) {
                        UbuntuClient.LOGGER.info(String.format("Transaction %s found and amount %.8f BTC meets minimum %.8f BTC. Premium verified!", txHash, receivedBtc, MIN_PREMIUM_AMOUNT_BTC));
                        return true;
                    } else {
                        UbuntuClient.LOGGER.warning(String.format("Transaction %s found, but amount %.8f BTC is less than required %.8f BTC.", txHash, receivedBtc, MIN_PREMIUM_AMOUNT_BTC));
                        return false;
                    }
                }
            }

            UbuntuClient.LOGGER.info("Transaction hash " + txHash + " not found for wallet address " + WALLET_ADDRESS);
            return false;

        } catch (IOException | InterruptedException e) {
            UbuntuClient.LOGGER.severe("Network or API communication error during premium verification: " + e.getMessage());
            return false;
        } catch (Exception e) {
            UbuntuClient.LOGGER.severe("Unexpected error during premium verification: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }
}