package com.juju.tistar.request;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WritePostRequest (
        @Nullable String tag,
        @Nullable String[] imageUrls,
        @NotNull List<Long> imageIds

)    {
}
