package com.rsuniverse.car_park.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedRes<T> {
    private List<T> items;
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private long totalCount;

}
