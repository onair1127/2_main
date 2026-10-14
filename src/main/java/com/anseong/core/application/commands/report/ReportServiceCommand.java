package com.anseong.core.application.commands.report;

public record ReportServiceCommand(
     String image,
     String phone,
     String content
) {}
