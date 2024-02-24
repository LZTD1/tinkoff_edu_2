package edu.java.dto.sofDto;

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

    @Override public String toString() {
        return "SofResponse{"
            + "items=" + items
            + ", has_more=" + hasMore
            + ", quota_max=" + quotaMax
            + ", quota_remaining=" + quotaRemaining
            + '}';
    }
}
