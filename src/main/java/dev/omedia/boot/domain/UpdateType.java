package dev.omedia.boot.domain;

public enum UpdateType {
    TYPO,
    SUDO;

    public String toString() {
        if (this == TYPO) {
            return "TYPO";
        }
        if (this == SUDO) {
            return "SUDO";
        }
        return "";
    }
}
