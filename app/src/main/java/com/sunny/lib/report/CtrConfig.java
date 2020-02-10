package com.sunny.lib.report;

public class CtrConfig {

    private boolean isInitOn918;

    public boolean isInitOn918() {
        return isInitOn918;
    }

    private CtrConfig(Builder builder) {
        isInitOn918 = builder.isInitOn918;
    }

    public static class Builder {
        private boolean isInitOn918 = false;

        public Builder() {
        }

        public Builder isInitOn918(boolean isInitOn918) {
            this.isInitOn918 = isInitOn918;
            return this;
        }

        public CtrConfig build() {
            return new CtrConfig(this);
        }
    }
}