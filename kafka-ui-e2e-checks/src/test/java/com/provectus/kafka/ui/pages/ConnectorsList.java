package com.provectus.kafka.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.provectus.kafka.ui.base.TestConfiguration;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class ConnectorsList {
    private static final String path = "ui/clusters/%s/connectors";

    @Step
    public ConnectorsList goTo(String cluster) {
        Selenide.open(TestConfiguration.BASE_URL+path.formatted(cluster));
        return this;
    }

    @Step
    public ConnectorsList isOnPage() {
        $(By.xpath("//*[contains(text(),'Loading')]")).shouldBe(Condition.disappear);
        $(By.xpath("//span[text()='All Connectors']")).shouldBe(Condition.visible);
        return this;
    }

    @Step
    public void clickCreateConnectorButton() {
        $(By.xpath("//a[text()='Create Connector']")).click();
    }

    @SneakyThrows
    public ConnectorsList openConnector(String connectorName) {
        $(By.xpath("//*/tr/td[1]/a[text()='%s']".formatted(connectorName)))
                .click();
        return this;
    }
}
