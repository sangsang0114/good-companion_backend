package org.sku.zero.dto.request;

public record ResetPasswordRequest(String email, String password, String uuid) {
}
