package org.atlas.service.notification.adapter.email.ses.model;

import java.io.File;

public record Attachment(String fileName, File file) {

}
