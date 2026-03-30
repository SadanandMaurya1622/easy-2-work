package com.easy2work.catalog;

import java.util.List;

/**
 * Rich service copy for the public detail page. Getters satisfy JSP/EL if needed.
 */
public record ServiceDetail(
        String code,
        String title,
        String imageUrl,
        String summary,
        String priceLabel,
        String priceDetail,
        List<String> weProvide,
        List<String> fromYou,
        List<String> notIncluded,
        List<String> visitSteps
) {

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public String getPriceDetail() {
        return priceDetail;
    }

    public List<String> getWeProvide() {
        return weProvide;
    }

    public List<String> getFromYou() {
        return fromYou;
    }

    public List<String> getNotIncluded() {
        return notIncluded;
    }

    public List<String> getVisitSteps() {
        return visitSteps;
    }
}
