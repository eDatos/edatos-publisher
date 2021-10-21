package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.time.Instant;

public class ExternalOperationDto extends ExternalItemDto {

    private long subscribers;
    private long numberOfFilters;
    private ExternalCategoryDto category;
    private boolean enabled;
    private boolean notificationsEnabled;
    private Instant publicationDate;

    public long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }

    public long getNumberOfFilters() {
        return numberOfFilters;
    }

    public void setNumberOfFilters(long numberOfFilters) {
        this.numberOfFilters = numberOfFilters;
    }

    public ExternalCategoryDto getCategory() {
        return category;
    }

    public void setCategory(ExternalCategoryDto category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public Instant getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
    }
}
