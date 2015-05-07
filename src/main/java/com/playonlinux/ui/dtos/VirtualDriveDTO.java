package com.playonlinux.ui.dtos;


public class VirtualDriveDTO {
    private String name;

    public VirtualDriveDTO(Builder builder) {
        this.name = builder.name;
    }

    public static class Builder {
        private String name;

        public VirtualDriveDTO.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public VirtualDriveDTO build() {
            return new VirtualDriveDTO(this);
        }

    }
}
