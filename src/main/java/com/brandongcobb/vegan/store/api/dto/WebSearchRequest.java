package com.brandongcobb.vegan.store.api.dto;

import java.util.List;
import java.util.Map;

public class WebSearchRequest {
        private String type; // always "file_search"
        private List<String> vectorStoreIds;
        private Map<String, String> filters;
        private Integer maxNumResults;
        private Map<String, Object> rankingOptions;

        // Getters & setters

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<String> getVectorStoreIds() { return vectorStoreIds; }
        public void setVectorStoreIds(List<String> vectorStoreIds) { this.vectorStoreIds = vectorStoreIds; }

        public Map<String, String> getFilters() { return filters; }
        public void setFilters(Map<String, String> filters) { this.filters = filters; }

        public Integer getMaxNumResults() { return maxNumResults; }
        public void setMaxNumResults(Integer maxNumResults) { this.maxNumResults = maxNumResults; }

        public Map<String, Object> getRankingOptions() { return rankingOptions; }
        public void setRankingOptions(Map<String, Object> rankingOptions) { this.rankingOptions = rankingOptions; }
}
