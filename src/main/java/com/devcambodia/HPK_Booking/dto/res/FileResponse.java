package com.devcambodia.HPK_Booking.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileResponse {
    private String fileName;
    private String url;
    private String type;
    private long size;
    private String urlDownload;
}
