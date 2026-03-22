package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatsResponse {
    private Long upTime;
    private Long requestCounter;
    private int mapSize;
}
