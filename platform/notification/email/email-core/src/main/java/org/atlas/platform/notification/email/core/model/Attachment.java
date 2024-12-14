package org.atlas.platform.notification.email.core.model;

import java.io.File;

public record Attachment(String fileName, File file) {
}
