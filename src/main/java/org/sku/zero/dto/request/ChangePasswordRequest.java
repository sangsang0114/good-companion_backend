package org.sku.zero.dto.request;

public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
