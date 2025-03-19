package org.atlas.service.notification.port.outbound.email;

import java.io.File;

public record Attachment(String fileName, File file) {

}
