package com.ecell.odyssey;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Uploads payment screenshots to a Supabase Storage bucket using the Storage
 * REST API and returns a permanent public URL.
 *
 * Configuration (environment variables on Render):
 *   SUPABASE_URL                e.g. https://abcdxyz.supabase.co
 *   SUPABASE_SERVICE_ROLE_KEY   service role key from Supabase dashboard
 *   SUPABASE_BUCKET             bucket name (default: payments) - must be public
 *
 * When not configured (or on any failure) the service reports itself as
 * unconfigured and callers fall back to saving files on local disk.
 */
@Service
public class SupabaseStorageService {

    private static final Logger log = LoggerFactory.getLogger(SupabaseStorageService.class);

    private final String baseUrl;
    private final String serviceRoleKey;
    private final String bucket;
    private final HttpClient httpClient;

    public SupabaseStorageService(
            @Value("${supabase.storage.url:}") String baseUrl,
            @Value("${supabase.storage.key:}") String serviceRoleKey,
            @Value("${supabase.storage.bucket:payments}") String bucket) {
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim();
        this.serviceRoleKey = serviceRoleKey == null ? "" : serviceRoleKey.trim();
        this.bucket = (bucket == null || bucket.isBlank()) ? "payments" : bucket.trim();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public boolean isConfigured() {
        return !baseUrl.isBlank() && !serviceRoleKey.isBlank();
    }

    /**
     * Uploads the given bytes to {bucket}/{objectPath} and returns the public URL.
     * Returns null when Supabase is not configured or the upload fails.
     */
    public String upload(String objectPath, byte[] content, String contentType) {
        if (!isConfigured() || content == null || content.length == 0 || objectPath == null || objectPath.isBlank()) {
            return null;
        }
        try {
            String cleanType = (contentType == null || contentType.isBlank())
                    ? "application/octet-stream"
                    : contentType;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/storage/v1/object/" + bucket + "/" + objectPath))
                    .header("Authorization", "Bearer " + serviceRoleKey)
                    .header("apikey", serviceRoleKey)
                    .header("Content-Type", cleanType)
                    .header("x-upsert", "true")
                    .timeout(Duration.ofSeconds(30))
                    .PUT(HttpRequest.BodyPublishers.ofByteArray(content))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return baseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
            }
            log.warn("Supabase Storage upload failed (status {}): {}", response.statusCode(), response.body());
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Supabase Storage upload interrupted: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.warn("Supabase Storage upload error: {}", e.getMessage());
            return null;
        }
    }
}
