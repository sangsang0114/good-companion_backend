package org.sku.zero.dto.request;

public record ChangeNotificationSettingRequest(Boolean emailFlag, Boolean fcmFlag) {
}
