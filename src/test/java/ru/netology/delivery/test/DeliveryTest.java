package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

import static ru.netology.delivery.data.DataGenerator.*;
class DeliveryTest {

    @BeforeAll
    @DisplayName("Включение логера перед тестами")
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    @DisplayName("Открытие браузера перед выполнением теста")
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    @DisplayName("Выключение логера после выполнения тестов")
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replay meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification']  .notification__title").shouldBe(visible, ofSeconds(5)).shouldHave(exactText("Успешно!"));
        $("[data-test-id='success-notification']  .notification__content").shouldBe(visible, ofSeconds(5)).shouldHave(exactText("Встреча успешно Запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification']  .notification__title").shouldBe(visible, ofSeconds(5)).shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification']  .notification__content").shouldBe(visible, ofSeconds(5)).shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$("[data-test-id='replan-notification'] button").find(exactText("Перепланировать")).click();
        $("[data-test-id='success-notification']  .notification__title").shouldBe(visible, ofSeconds(5)).shouldHave(exactText("Успешно!"));
        $("[data-test-id='success-notification']  .notification__content").shouldBe(visible, ofSeconds(5)).shouldHave(exactText("Встреча успешно Запланирована на " + secondMeetingDate));
    }

    @Test
    @DisplayName("Should if you don't enter a date")
    public void ShouldIfYouDontEnterDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAdd = 3;
        var meetingDate = generateDate(daysToAdd);
        $("[data-test-id=city] input").setValue(validUser.getCity());

        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id='success-notification']").should(visible, ofSeconds(15))
                .shouldHave(text("Встреча успешно Запланирована на " + meetingDate));

//    @Test
//    @DisplayName("Should successful plan and replan meeting")
//    void shouldSuccessfulPlanAndReplanMeeting() {
//        var validUser = DataGenerator.Registration.generateUser("ru");
//        var daysToAddForFirstMeeting = 4;
//        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
//        var daysToAddForSecondMeeting = 7;
//        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
    }
}
