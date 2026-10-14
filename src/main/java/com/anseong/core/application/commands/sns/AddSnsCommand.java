package com.anseong.core.application.commands.sns;

public record AddSnsCommand(
     SnsCommand snsCommand
) {
     public AddSnsCommand(String type, String url) {
          this(new SnsCommand(type, url));
     }
}
