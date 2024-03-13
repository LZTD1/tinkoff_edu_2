package edu.java.clients.dto.sofDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class SofResponseDto {
    public ArrayList<ItemDto> items;

    @JsonProperty("has_more")
    public boolean hasMore;
    @JsonProperty("quota_max")
    public int quotaMax;
    @JsonProperty("quota_remaining")
    public int quotaRemaining;
}
